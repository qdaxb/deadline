package com.weibo.hackathon.deadline.game;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

import org.omg.PortableInterceptor.SUCCESSFUL;

import com.weibo.hackathon.deadline.game.EventDispatcher.Event;
import com.weibo.hackathon.deadline.game.EventDispatcher.EventListener;
import com.weibo.hackathon.deadline.game.EventDispatcher.MeetCandyEvent;
import com.weibo.hackathon.deadline.game.EventDispatcher.ResultEvent;
import com.weibo.hackathon.deadline.game.EventDispatcher.ResultEvent.Result;
import com.weibo.hackathon.deadline.game.Thing.Candy;
import com.weibo.hackathon.deadline.game.Thing.Player;
import com.weibo.hackathon.deadline.game.Thing.Wall;

public class Ground {

	public class EventProcessor implements EventListener {

		@Override
		public void onEvent(Event e) {
			if (e instanceof MeetCandyEvent) {
				boolean helpful = ((MeetCandyEvent) e).helpful;
				if (Objects.equals(getId(), e.source)) {
					return;
				} else {
					if (!(controller.isFriend(getId(), e.source))) {
						helpful = !helpful;
					}
					doHelp(helpful);
				}
			}
		}

		private void doHelp(boolean helpful) {
			Random rand = new Random();
			for (Iterator<Thing> it = things.iterator(); it.hasNext();) {
				Thing thing = it.next();
				boolean wall = thing instanceof Wall;
				boolean harmfulCandy = thing instanceof Candy
						&& !((Candy) thing).helpful;
				boolean helpfulCandy = thing instanceof Candy
						&& ((Candy) thing).helpful;
				if (helpful && (wall || harmfulCandy)) {
					if (rand.nextGaussian() > 0.5) {
						thing.setDisappear();
						break;
					}
				} else if ((!helpful) && helpfulCandy) {
					if (rand.nextGaussian() > 0.5) {
						thing.setDisappear();
						break;
					}
				}
			}
		}
	}

	public class ThingGenerator {
		private final Random rand = new Random();
		private int num = 500;
		private boolean finished = false;

		public Thing gen() {
			if (finished) {
				return null;
			}
			int type = rand.nextInt(10);
			Thing t;
			if (type < RATIO_CANDY) {
				Candy c = new Candy();
				if (rand.nextInt(10) < RATIO_HARMFUL_CANDY) {
					c.helpful = false;
				} else {
					c.helpful = true;
				}
				t = c;
				t.h = 2;
				t.w = 3;
			} else {
				t = new Wall();
				t.h = rand.nextInt(MAX_THING_HEIGHT - 1) + 1;
				t.w = rand.nextInt(MAX_THING_WIDTH - 1) + 1;
			}

			t.y = rand.nextInt(h - t.h - 1) + 1;
			t.x = w - t.w;

			num--;
			if (num < 0) {
				finished = true;
			}

			return t;
		}
	}

	private static final int RATIO_HARMFUL_CANDY = 3;
	private static final int RATIO_CANDY = 2;
	private static final int MAX_THING_WIDTH = 6;
	private static final int MAX_THING_HEIGHT = 9;
	private static final int WALL_TTL = 5;
	private static final int CANDY_BONOUS = 5;
	private static final int UP = 1;
	private static final int DOWN = -1;
	private static final int STOP = 0;
	private static final int FORWARD = 1;
	private static final int BACKWARD = -1;
	private final Game controller;
	private final String id;
	private final int w, h;
	private final Collection<Thing> things;
	private final EventProcessor processor;
	private final ThingGenerator generator;
	private long update;
	private long survive;
	private Result result;
	private String text;
	private Player player;

	public Ground(String id, int h, int w, Game controller) {
		super();
		this.id = id;
		this.h = h;
		this.w = w;
		this.controller = controller;

		things = com.weibo.hackathon.deadline.engine.utils.Util.identitySet();
		processor = new EventProcessor();
		generator = new ThingGenerator();

		player = new Player();
		player.setLongLive();
		player.x = (w - player.w) / 2;
		player.y = (h - player.h) / 2;
		player.w = 3;
		player.h = 3;
		things.add(player);
	}

	public Collection<Thing> getThings() {
		return Collections.unmodifiableCollection(things);
	}

	public String getId() {
		return id;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public void oneStep() {
		if (!isOver()) {
			addIn();
			checkMeet();
			doMove();
			checkExist();
			checkResult();
			if (!isOver()) {
				survive++;
			}
		}
	}

	private void addIn() {
		if (controller.canGenThing()) {
			if (addThing(generator.gen())) {
				setUpdated();
			}
		}
	}

	private void checkMeet() {
		Collection<Thing> collision = getThingsTouchingArea(player.x, player.y,
				player.w, player.h);

		// meet front things
		Collection<Thing> front = getThingsTouchingArea(player.x, player.y,
				player.w + 1, player.h);
		front.removeAll(collision);
		for (Thing t : front) {
			if (!t.isExisting()) {
				continue;
			}
			if (t instanceof Candy) {
				meetCandy((Candy) t);
			} else {
				player.move.setDx(t.move.getDx());

				if (t instanceof Wall) {
					Wall wall = (Wall) t;
					wall.setTTL(WALL_TTL);
				}
			}
		}

		// meet up or down things
		if (player.move.getDy() != STOP) {
			Collection<Thing> vertical = Collections.emptySet();
			if (player.move.getDy() == UP) {
				vertical = getThingsTouchingArea(player.x, player.y, player.w,
						player.h + 1);
				vertical.removeAll(collision);
			} else if (player.move.getDy() == DOWN) {
				vertical = getThingsTouchingArea(player.x, player.y - 1,
						player.w, player.h);
				vertical.removeAll(collision);
			}
			for (Thing t : vertical) {
				if (!t.isExisting()) {
					continue;
				}
				if (t instanceof Candy) {
					meetCandy((Candy) t);
				} else {
					player.move.setDy(STOP);
					if (t instanceof Wall) {
						Wall wall = (Wall) t;
						wall.setTTL(WALL_TTL);
					}
				}
			}
		}
	}

	private void meetCandy(Candy candy) {
		// CANDY EATEN, NO LONGER EXISTS.
		candy.setDisappear();

		// DO MOVEMENT
		boolean helpful = candy.helpful;
		if (helpful) {
			player.move.setDx(FORWARD);
			player.move.setXSteps(CANDY_BONOUS);
		} else {
			player.move.setDx(BACKWARD);
			player.move.setXSteps(CANDY_BONOUS);
		}

		// BROADCAST, AFFECT FRIENDS AND ENEMIES.
		MeetCandyEvent mce = new MeetCandyEvent();
		mce.source = getId();
		mce.helpful = helpful;
		broadcast(mce);
	}

	private void doMove() {
		if (controller.canThingMove()) {
			Iterator<Thing> it = things.iterator();
			while (it.hasNext()) {
				Thing t = it.next();
				if (!(t instanceof Player)) {
					if (t.doMove()) {
						adjust(t);
						setUpdated();
					}
				}
			}
		}
		if (controller.canPlayerYMove()) {
			if (player.doYMove()) {
				setUpdated();
			}
		}
		if (controller.canPlayerXMove()) {
			if (player.doXMove()) {
				setUpdated();
			}
		}
		adjust(player);
	}

	private void checkExist() {
		for (Iterator<Thing> it = things.iterator(); it.hasNext();) {
			Thing thing = it.next();
			if (!thing.isExisting()) {
				it.remove();
				setUpdated();
			}
		}
	}

	private void checkResult() {
		if (generator.finished
				&& (things.isEmpty() || things.size() == 1
						&& things.contains(player))) {
			win(String.format("%s has overcome all obstacles!", getId()));
		} else if (player.x <= 1) {
			fail(String.format("%s is killed!", getId()));
		}
	}

	protected Collection<Thing> getThingsTouchingArea(int x, int y, int w, int h) {
		Collection<Thing> set = com.weibo.hackathon.deadline.engine.utils.Util
				.identitySet();
		for (Thing t : things) {
			if (x >= t.x + t.w || x + w <= t.x) {
				continue;
			}
			if (y >= t.y + t.h || y + h <= t.y) {
				continue;
			}
			set.add(t);
		}
		return set;
	}

	protected void adjust(Thing t) {
		if (t != null) {
			if (t.x < 1) {
				setUpdated();
				t.x = 1;
			}
			if (t.x + t.w + 1 > w) {
				setUpdated();
				t.x = w - 1 - t.w;
			}
			if (t.y < 1) {
				setUpdated();
				t.y = 1;
			}
			if (t.y + t.h + 1 > h) {
				setUpdated();
				t.y = h - 1 - t.h;
			}
			// post adjust
			if (t.x == 1) {
				if (t instanceof Player) {
					// fail("killed!");
				} else {
					t.setDisappear();
				}
			}
		}
	}

	protected void setUpdated() {
		update = controller.now();
	}

	public long lastUpdate() {
		return update;
	}

	public Game getController() {
		return controller;
	}

	private boolean addThing(Thing t) {
		if (t instanceof Wall || t instanceof Candy) {
			if (!getThingsTouchingArea(t.x, t.y, t.w, t.h).isEmpty()) {
				// failed to add in.
				return false;
			}
			t.move.setXNonStop();
			t.move.setDx(BACKWARD);
			things.add(t);
			setUpdated();
			return true;
		} else {
			return false;
		}
	}

	public void playerUp() {
		player.move.setDy(UP);
	}

	public void playerDown() {
		player.move.setDy(DOWN);
	}

	public void win(String text) {
		setResult(text, Result.WIN);
	}

	public void fail(String text) {
		setResult(text, Result.FAIL);
	}

	public void cancel(String text) {
		setResult(text, Result.CANCEL);
	}

	private void setResult(String text, Result result) {
		if (!isOver() && result != null) {
			this.result = result;
			this.text = text;
			setUpdated();

			// broadcast.
			ResultEvent re = new ResultEvent();
			re.source = getId();
			re.result = result;
			broadcast(re);
		}
	}

	private void broadcast(Event e) {
		controller.dispatch(e);
	}

	public Result getResult() {
		return result;
	}

	public boolean isOver() {
		return result != null;
	}

	public String getText() {
		return text;
	}

	public long getSurvive() {
		return survive;
	}

	public EventProcessor getEventListener() {
		return processor;
	}

}
