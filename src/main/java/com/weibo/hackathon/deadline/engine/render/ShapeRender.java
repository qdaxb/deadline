package com.weibo.hackathon.deadline.engine.render;

import com.weibo.hackathon.deadline.engine.model.Block;
import com.weibo.hackathon.deadline.engine.model.Candy;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.GameObject;
import com.weibo.hackathon.deadline.engine.model.GameString;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Player;
import com.weibo.hackathon.deadline.engine.model.Size;
import com.weibo.hackathon.deadline.engine.utils.Util;

public class ShapeRender implements Render<char[][]> {

    @Override
    public char[][] render(GameObject obj) {
        Element element = obj.element;
        char[][] shape = null;
        if (element instanceof Block) {
            shape = renderingBlock((Block) element);
        } else if (element instanceof Candy) {
            shape = renderingCandy((Candy) element);
        } else if (element instanceof Player) {
            shape = renderingPlayer((Player) element);
        } else if (element instanceof GameString) {
            shape = renderingString((GameString) element);
        } else {
        }
        return shape;
    }

    private char[][] renderingBlock(Block block) {
        Size size = block.size;
        char[][] shape = new char[size.height][size.width];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                boolean isHeightMatch = i == 0 || i == shape.length - 1;
                boolean isWidthMatch = j == 0 || j == shape[0].length - 1;
                if (isHeightMatch || isWidthMatch) {
                    shape[i][j] = '*';
                } else {
                    shape[i][j] = ' ';
                }
            }
        }
        return shape;
    }

    private char[][] renderingString(GameString gameString) {
        Size size = gameString.size;
        char[] content = gameString.content.toCharArray();
        char[][] shape = new char[size.height][size.width];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                int positon = i * j + j;
                if (positon < content.length) {
                    shape[i][j] = content[positon];
                }else {
                    shape[i][j] = ' ';
                }
            }
        }
        return shape;
    }


    private char[][] renderingCandy(Candy candy) {
        Size size = candy.size;
        char[][] shape = new char[size.height][size.width];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                boolean isHeightMatch = i == 0 || i == shape.length - 1;
                boolean isWidthMatch = j == 0 || j == shape[0].length - 1;
                if (isHeightMatch || isWidthMatch) {
                    shape[i][j] = '*';
                } else {
                    shape[i][j] = ' ';
                }
            }
        }
        return shape;
    }

    private char[][] renderingPlayer(Player player) {
        char[][] shape = new char[9][8];
        shape[0] = "   ...  ".toCharArray();
        shape[1] = "   ...  ".toCharArray();
        shape[2] = "    .   ".toCharArray();
        shape[3] = "   ...  ".toCharArray();
        shape[4] = "  . . . ".toCharArray();
        shape[5] = "    .   ".toCharArray();
        shape[6] = "    .   ".toCharArray();
        shape[7] = "   . .  ".toCharArray();
        shape[8] = "  .   . ".toCharArray();
        return shape;
    }


    public static void main(String[] args) {
        GameObject root = new GameObject();
        Element e = new Block();
        Size s = new Size(30, 79);
        Location l = new Location(1, 1);
        e.size = s;
        e.loc = l;
        root.element = e;

        GameObject player = new GameObject();
        Player e1 = new Player();
        Location l1 = new Location(1, 10);
        e1.size = s;
        e1.loc = l1;
        player.element = e1;

        root.children.add(player);
        player.father = root;
        
        GameObject string = new GameObject();
        GameString e2 = new GameString();
        e2.content = "Fuck";
        e2.size = new Size(1, 9);
        e2.loc = new Location(28, 60);
        string.element = e2;
        
        root.children.add(player);
        root.children.add(string);
        string.father = root;
        player.father = root;


        // Util.printArray(new ShapeRender().render(root));
        System.out.println(new TextRender().render(root));
    }
}
