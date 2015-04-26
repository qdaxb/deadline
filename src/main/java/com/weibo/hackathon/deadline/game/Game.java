package com.weibo.hackathon.deadline.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.weibo.hackathon.deadline.engine.model.GameObject;
import com.weibo.hackathon.deadline.engine.model.ModelAdapter;
import com.weibo.hackathon.deadline.game.EventDispatcher.Event;
import com.weibo.hackathon.deadline.game.EventDispatcher.EventListener;
import com.weibo.hackathon.deadline.game.EventDispatcher.MeetCandyEvent;
import com.weibo.hackathon.deadline.game.EventDispatcher.ResultEvent;
import com.weibo.hackathon.deadline.game.EventDispatcher.ResultEvent.Result;

/**
 * A standalone game with multiple players.
 * 
 * @author xnslong
 *
 */
public class Game {

	private class RateControl {
		int interval;
		int ticks = 0;

		public RateControl(int interval) {
			setInterval(interval);
		}

		public void tick() {
			ticks = (ticks + 1) % interval;
		}

		public boolean ready() {
			return ticks == 0;
		}

		public void setInterval(int i) {
			if (i <= 0) {
				interval = 1;
			} else {
				interval = i;
			}
		}
	}

	public class OneWinGameFinishProcessor implements EventListener {

		@Override
		public void onEvent(Event e) {
			if (!over && (e instanceof ResultEvent)) {
				over = true;

				ResultEvent re = (ResultEvent) e;
				String cancel = "cancelled by " + re.source;
				String result = re.source + " has "
						+ (re.result == Result.WIN ? "won" : "failed");
				for (String id : playings.keySet()) {
					Ground play = playings.get(id);
					if (re.result == Result.CANCEL) {
						play.cancel(cancel);
					} else {
						if (isFriend(id, re.source)) {
							if (re.result == Result.WIN) {
								play.win(result);
							} else {
								play.fail(result);
							}
						} else {
							if (re.result == Result.WIN) {
								play.fail(result);
							} else {
								play.win(result);
							}
						}
					}
				}
			}
		}
	}

	private static final int THING_MOVE_WINDOW = 2;
	private static final int THING_GEN_WINDOW = 2;
	private static final int PLAYER_MOVE_WINDOW = 1;
	private static final int BASE_WINDOW = 1;

	private static final int GROUND_HEIGHT = 28;
	private static final int GROUND_WIDTH = 78;

	private RateControl thingMove, playerYMove, playerXMove, thingGen, base;
	private long timeTicks = 0;
	private Map<String, Ground> playings;
	private final EventDispatcher eventDispatcher;
	private final EventListener resultProcessor;
	private final Map<String, Boolean> changed;
	private boolean over = false;

	public Game() {
		playings = new HashMap<String, Ground>();
		changed = new ConcurrentHashMap<String, Boolean>();
		eventDispatcher = new EventDispatcher();
		resultProcessor = new OneWinGameFinishProcessor();
		eventDispatcher.addListener(ResultEvent.class, resultProcessor);
		thingMove = new RateControl(THING_MOVE_WINDOW);
		playerYMove = new RateControl(PLAYER_MOVE_WINDOW);
		playerXMove = thingMove;
		thingGen = new RateControl(THING_GEN_WINDOW);
		base = new RateControl(BASE_WINDOW);
	}

	/**
	 * To tell if player <code>id1</code> and <code>id2</code> is friend.
	 * <p>
	 * NOTE:
	 * <ol>
	 * <li>any player is a friend of itself. i.e.
	 * <code>isFriend(id0, id0)</code> is true.
	 * <li><code>isFriend(id1, id2)</code> and <code>isFriend(id2, id1)</code>
	 * will return the same value.
	 * </ol>
	 * 
	 * @param id1
	 * @param id2
	 * @return
	 */
	public boolean isFriend(String id1, String id2) {
		return Objects.equals(id1, id2);
	}

	public int getPlayerNumber() {
		return playings.size();
	}

	public Set<String> playerIds() {
		return Collections.unmodifiableSet(playings.keySet());
	}

	public synchronized void setPlayerNumber(int playerNumber) {
		if (getPlayerNumber() <= 0 && playerNumber > 0) {
			for (int i = 0; i < playerNumber; i++) {
				String id = "player " + i;
				Ground p = new Ground(id, GROUND_HEIGHT, GROUND_WIDTH, this);
				eventDispatcher.addListener(MeetCandyEvent.class,
						p.getEventListener());
				playings.put(id, p);
			}
		}
	}

	public long getTimeTicks() {
		return timeTicks;
	}

	public boolean canPlayerYMove() {
		return playerYMove.ready();
	}

	public boolean canPlayerXMove() {
		return playerXMove.ready();
	}

	public boolean canThingMove() {
		return thingMove.ready();
	}

	public boolean canGenThing() {
		return thingGen.ready();
	}

	public void dispatch(Event e) {
		eventDispatcher.dispatch(e);
	}

	public void oneStep() {
		timeAdvances();
		if (canGameRun()) {
			gameTick();
			doStep();
			collectChanges();
		}
	}

	private void collectChanges() {
		Map<String, Ground> changes = new HashMap<String, Ground>();
		synchronized (this) {
			for (String key : playings.keySet()) {
				Ground play = playings.get(key);
				if (play.lastUpdate() == now()) {
					changes.put(key, play);
				}
			}
		}
		for (String key : changes.keySet()) {
			changed.put(key, true);
		}
	}

	private void timeAdvances() {
		timeTicks++;
		base.tick();
	}

	private void gameTick() {
		for (RateControl rc : Arrays.asList(playerYMove, thingMove, thingGen)) {
			rc.tick();
		}
	}

	public GameObject getUpdate(String id) {
		Boolean exist = changed.remove(id);
		if (exist != null && exist == true) {
			return ModelAdapter.toGameObject(getPlaying(id));
		}
		return null;
	}

	public boolean hasChanged(String id) {
		Ground play = getPlaying(id);
		if (play == null) {
			return false;
		} else {
			return now() == play.lastUpdate();
		}
	}

	private boolean canGameRun() {
		return base.ready();
	}

	private Set<Ground> allPlays() {
		Set<Ground> set = com.weibo.hackathon.deadline.engine.utils.Util
				.identitySet();
		for (Ground g : playings.values()) {
			set.add(g);
		}
		return set;
	}

	private void doStep() {
		Set<Ground> set = allPlays();
		for (Ground g : set) {
			g.oneStep();
		}
	}

	public long now() {
		return timeTicks;
	}

	public Ground getPlaying(String ground) {
		return playings.get(ground);
	}

	public boolean isOver() {
		return over;
	}

}
