package com.weibo.hackathon.deadline.game;

public abstract class Thing {

	public class XMove {
		int d = 0, steps = 0;

		public int move(int shift) {
			if (steps > 0) {
				if (!isNonStop()) {
					steps--;
				}
				if (steps < 0) {
					steps = 0;
				}
				return shift + d;
			} else {
				return shift;
			}
		}

		public void setDelta(int delta) {
			this.d = delta;
			if (delta != 0 && steps <= 0) {
				setSteps(1);
			}
		}

		public int getDelta() {
			return d;
		}

		public void setSteps(int steps) {
			this.steps = steps;
			if (steps < 0) {
				steps = 0;
			}
		}

		public void setNonStop() {
			steps = Integer.MAX_VALUE;
		}

		public boolean isNonStop() {
			return Integer.MAX_VALUE == steps;
		}

	}

	public class Movement {
		XMove mx = new XMove(), my = new XMove();

		public boolean xMove() {
			Thing t = Thing.this;
			int x = mx.move(t.x);
			if (x != t.x) {
				t.x = x;
				return true;
			} else {
				return false;
			}
		}

		public boolean yMove() {
			Thing t = Thing.this;
			int y = my.move(t.y);
			if (y != t.y) {
				t.y = y;
				return true;
			} else {
				return false;
			}
		}

		public boolean move() {
			boolean moved = false;
			moved |= xMove();
			moved |= yMove();
			return moved;
		}

		public void setMovement(int dx, int dy) {
			setDx(dx);
			setDy(dy);
		}

		public void setDx(int dx) {
			mx.setDelta(dx);
		}

		public void setDy(int dy) {
			my.setDelta(dy);
		}

		public int getDx() {
			return mx.getDelta();
		}

		public int getDy() {
			return my.getDelta();
		}

		public void setXSteps(int steps) {
			mx.setSteps(steps);
		}

		public void setYSteps(int steps) {
			my.setSteps(steps);
		}

		public void setXNonStop() {
			mx.setNonStop();
		}

		public void setYNonStop() {
			my.setNonStop();
		}

	}

	public static class Player extends Thing {
		public Player() {
			this.move.setXSteps(0);
			this.move.setYSteps(0);
			this.move.setDx(0);
			this.move.setDy(0);
		}

		public boolean doYMove() {
			return this.move.yMove();
		}

		public boolean doXMove() {
			return this.move.xMove();
		}
	}

	public static class Candy extends Thing {
		/**
		 * if helpful, it will help the player and all its friends, and harm its
		 * enemies. if not it will harm all its friends.
		 */
		public boolean helpful;
	}

	public static class Wall extends Thing {

		@Override
		public void setTTL(int ttl) {
			if (ttl > this.ttl) {
				return;
			}
			super.setTTL(ttl);
		}

		@Override
		public void setLongLive() {
			super.setTTL(Integer.MAX_VALUE);
		}

	}

	/**
	 * data of the location and size state.
	 */
	public int x, y, h, w;
	/**
	 * the movement to perform on each step.
	 */
	public final Movement move;
	/**
	 * time to live. when it is zero, exists will be false.
	 */
	protected int ttl = Integer.MAX_VALUE;

	public Thing() {
		move = new Movement();
		setLongLive();
	}

	public boolean doMove() {
		boolean moved = false;
		moved |= move.xMove();
		moved |= move.yMove();
		setTTL(ttl - 1);
		return moved;
	}

	public boolean isExisting() {
		return ttl > 0;
	}

	public boolean isLongLive() {
		return ttl == Integer.MAX_VALUE;
	}

	public void setTTL(int ttl) {
		this.ttl = ttl;
		if (ttl < 0) {
			this.ttl = 0;
		}
	}

	public void setLongLive() {
		setTTL(Integer.MAX_VALUE);
	}

	public void setDisappear() {
		setTTL(0);
	}

}
