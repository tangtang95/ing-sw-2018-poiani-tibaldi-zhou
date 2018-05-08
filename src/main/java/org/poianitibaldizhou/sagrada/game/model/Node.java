package org.poianitibaldizhou.sagrada.game.model;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private List<Node<T>> children = new ArrayList<Node<T>>();
    private Node<T> parent = null;
    private T data = null;

    /**
     * Creates the node of the tree.
     *
     * @param data root element
     */
    public Node(T data) {
        this.data = data;
    }

    /**
     * Creates a node attaching as a child of parant
     *
     * @param data new element
     * @param parent new element's father
     */
    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public void setParent(Node<T> parent) {
        parent.addChild(this);
        this.parent = parent;
    }

    /**
     * Add an element as a child of this
     * @param data element to add
     */
    public void addChild(T data) {
        Node<T> child = new Node<T>(data);
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * Add a tree as a child of this
     * @param child tree to add
     */
    public void addChild(Node<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public void removeParent() {
        this.parent = null;
    }
}

