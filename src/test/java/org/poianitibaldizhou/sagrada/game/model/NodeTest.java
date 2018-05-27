package org.poianitibaldizhou.sagrada.game.model;

import org.junit.After;
import org.junit.Test;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.Node;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    public static Node<String> root;


    @After
    public void tearDown() {
        root = null;
    }

    @Test
    public void testAddAtIndex() {
        root = new Node<>("1");
        root.setRightChild(new Node("3"));
        root.setLeftChild(new Node("2"));
        root.getRightChild().setRightChild(new Node("7"));
        root.getRightChild().getRightChild().setLeftChild(new Node("14"));

        Node<String> root2 = new Node<>("1");
        root2.addAtIndex("3", 3);
        root2.addAtIndex("2", 2);
        root2.addAtIndex("7", 7);
        root2.addAtIndex("14", 14);

        assertEquals(root, root2);

        try {
            root2.addAtIndex("try", 100);
            fail("Exception expected");
        } catch(Exception e) {
            assertTrue(root.equals(root2));
        }
    }

    @Test
    public void testCreationAndBasicStuff() {
        String temp = "root";
        root = new Node<>(temp);
        assertEquals(temp, root.getData());
        assertEquals(null, root.getLeftChild());
        assertEquals(null, root.getRightChild());
        assertEquals(null, root.getParent());
        assertEquals(true, root.isLeaf());
        assertEquals(true, root.isRoot());

        root.setLeftChild(new Node("rootleft"));
        root.setRightChild(new Node("rootright"));
        assertEquals(new Node<>("rootright"), root.getRightChild());
        assertEquals(new Node<>("rootleft"), root.getLeftChild());
        assertEquals(false, root.isLeaf());
    }

    @Test
    public void testTreeWithOnlyLeftChild() {
        root = new Node<>("1");
        root.setLeftChild(new Node("2"));
        root.getLeftChild().setLeftChild(new Node("4"));
        root.getLeftChild().getLeftChild().setLeftChild(new Node("8"));
        root.getLeftChild().getLeftChild().getLeftChild().setLeftChild(new Node("16"));

        Node<String> root2 = new Node("1");
        root2.addAtIndex("2", 2);
        root2.addAtIndex("4", 4);
        root2.addAtIndex("8", 8);
        root2.addAtIndex("16", 16);

        assertEquals(root, root2);
    }
}