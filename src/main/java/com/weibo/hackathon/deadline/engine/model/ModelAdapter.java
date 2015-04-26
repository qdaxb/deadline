package com.weibo.hackathon.deadline.engine.model;

import com.weibo.hackathon.deadline.engine.storage.GameDataStorage;
import com.weibo.hackathon.deadline.game.Play;
import com.weibo.hackathon.deadline.game.Thing;
import com.weibo.hackathon.deadline.game.Thing.Candy;
import com.weibo.hackathon.deadline.game.Thing.Player;
import com.weibo.hackathon.deadline.game.Thing.Wall;

public class ModelAdapter {

	private static final class EmptyElement extends Element {
		// public EmptyElement() {
		// loc = new Location(0, 0);
		// }
	}

	// public static GameObject translate(final Scene scene) {
	// GameObject go = new GameObject();
	// go.father = null;
	// go.element = new Block();
	// go.element.size = scene.size;
	// go.element.loc = new Location(1, 1);
	// go.children = new ArrayList<GameObject>();
	// for (Element elem : scene.elements) {
	// GameObject ge = new GameObject();
	// ge.father = go;
	// go.children.add(ge);
	// if (elem instanceof InfoElement) {
	// AscIIImage asc = new AscIIImage();
	// ge.element = asc;
	// asc.name = ((InfoElement) elem).info.getText();
	// asc.loc = new Location(10, 20);
	// GameObject obj = new GameObject();
	// GameString gameString = new GameString();
	// // gameString.content = "alive time:"+((InfoElement)
	// // elem).info.getTime()+",max:"+
	// // GameDataStorage.getInstance().getMax();
	// System.out.println(gameString.content);
	// gameString.loc = new Location(8, 28);
	// gameString.size = new Size(1, 34);
	// obj.element = gameString;
	// obj.father = go;
	// go.children.add(obj);
	// } else {
	// ge.element = elem;
	// }
	// }
	// GameObject root = new GameObject();
	// root.element = new EmptyElement();
	// GameObject deadline = new GameObject();
	// deadline.father = root;
	// AscIIImage ascIIImage = new AscIIImage();
	// ascIIImage.name = "deadline";
	// deadline.element = ascIIImage;
	// deadline.element.loc = new Location(4, 1);
	// root.children.add(deadline);
	// go.father = root;
	// root.children.add(go);
	// go.element.loc = new Location(1, 8);
	// return root;
	// }

	public static GameObject toGameObject(Play play) {
		if (play == null)
			return null;

		GameObject go = new GameObject();
		go.father = null;
		if (!play.isOver()) {
			go.element = new Block();
			go.element.size = new Size(play.getHeight(), play.getWidth());
			go.element.loc = new Location(0, 0);

			for (Thing t : play.getThings()) {
				GameObject to = new GameObject();
				if (t instanceof Candy) {
					com.weibo.hackathon.deadline.engine.model.Candy candyElement = new com.weibo.hackathon.deadline.engine.model.Candy();
					to.element = candyElement;
					candyElement.helpful = ((Candy) t).helpful;
				} else if (t instanceof Wall) {
					to.element = new Block();
				} else if (t instanceof Player) {
					to.element = new com.weibo.hackathon.deadline.engine.model.Player();
				}
				if (to.element != null) {
					to.element.size = new Size(t.h, t.w);
					to.element.loc = new Location(t.y, t.x);
					to.father = go;
					go.children.add(to);
				}
			}
		} else {
			if (play.getResult() != null) {
				AscIIImage img = new AscIIImage();
				img.name = play.getResult().getText();
				img.loc = new Location(10, 20);
				go.element = img;

				GameObject obj = new GameObject();
				GameString gameString = new GameString();
				GameDataStorage storage = GameDataStorage.getInstance();
				String text = play.getText();
				gameString.content = (text != null ? text + ": alive time:"
						: "alive time:")
						+ play.getSurvive()
						+ ",max:"
						+ storage.getMax();
				System.out.println(gameString.content);
				gameString.loc = new Location(8, 28);
				gameString.size = new Size(1, 34);
				obj.element = gameString;
				obj.father = go;
				go.children.add(obj);

			}
		}
		GameObject root = new GameObject();
		root.element = new EmptyElement();
		GameObject deadline = new GameObject();
		deadline.father = root;
		AscIIImage ascIIImage = new AscIIImage();
		ascIIImage.name = "deadline";
		deadline.element = ascIIImage;
		deadline.element.loc = new Location(4, 1);
		root.children.add(deadline);
		go.father = root;
		root.children.add(go);
		go.element.loc = new Location(1, 8);
		return root;
	}

}
