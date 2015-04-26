package com.weibo.hackathon.deadline.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventDispatcher {

	public static abstract class Event {
		public String source;
	}

	public static class ResultEvent extends Event {
		public enum Result {
			WIN("success"), FAIL("gameover"), CANCEL("cancelled");
			
			private Result(String text) {
				this.text = text;
			}
			
			private String text;
			public String getText() {
				return text;
			}
		}

		public Result result;
	}

	public static class MeetCandyEvent extends Event {

		public boolean helpful;

	}

	public static interface EventListener {
		public void onEvent(Event e);
	}

	private final Map<Class<? extends Event>, Set<EventListener>> listen;

	public EventDispatcher() {
		listen = new HashMap<Class<? extends Event>, Set<EventListener>>();
	}

	public synchronized void addListener(Class<? extends Event> e,
			EventListener el) {
		Set<EventListener> els = listen.get(e);
		if (els == null) {
			els = buildEventListenerSet();
			listen.put(e, els);
		}
		els.add(el);
	}

	private Set<EventListener> buildEventListenerSet() {
		return com.weibo.hackathon.deadline.engine.utils.Util.identitySet();
	}

	public synchronized void removeListener(Class<? extends Event> e,
			EventListener el) {
		Set<EventListener> els = listen.get(e);
		if (els != null) {
			els.remove(el);
			if (els.isEmpty()) {
				listen.remove(e);
			}
		}
	}

	public void dispatch(Event e) {
		if (e != null) {
			Set<EventListener> els = buildEventListenerSet();
			Class<?> clz = e.getClass();
			synchronized (this) {
				while (clz != Object.class && Event.class.isAssignableFrom(clz)) {
					Set<EventListener> listeners = listen.get(clz);
					if (listeners != null) {
						els.addAll(listeners);
					}
					clz = clz.getSuperclass();
				}
			}
			for (EventListener el : els) {
				try {
					el.onEvent(e);
				} catch (Throwable t) {
					// silent
				}
			}
		}
	}

}
