package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Node<T> {
    private Node<T> rightChild = null;
    private Node<T> leftChild = null;
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

    public Node<T> getParent() {
        return parent;
    }

    public Node<T> getRightChild() {
        return rightChild;
    }

    public Node<T> getLeftChild() { return leftChild;}

    public void setRightChild(Node<T> rightChild) {
        this.rightChild = rightChild;
        rightChild.setParent(parent);
    }

    public void setLeftChild(Node<T> leftChild) {
        this.leftChild = leftChild;
        leftChild.setParent(parent);
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
        return rightChild == null && leftChild == null;
    }

    public void removeParent() {
        this.parent = null;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    /**
     * Adds an element following the structure for which the right child is at current position * 2,
     * while left child is at current position * 2 + 1.
     *
     * @param elem element to add
     * @param index index of the element
     * @throws IllegalArgumentException if current structure doesn't allow index as number
     */
    public void addAtIndex(T elem, int index) throws IllegalArgumentException {
        Stack<Integer> stack = new Stack<>();
        Node<T> currentNode = this;
        Node<T> elemParent = this;
        int currPos;

        while(index > 0 ) {
            stack.push(index);
            index = index / 2;
        }

        stack.pop();

        while(!stack.empty()) {
            currPos = stack.pop();
            if(stack.size() == 0)
                elemParent = currentNode;

            if (currPos % 2 == 0) {
                if(stack.size() == 0) {
                    elemParent.setLeftChild(new Node(elem));
                } else {
                    currentNode = currentNode.getLeftChild();
                }
            } else {
                if (stack.size() == 0) {
                    elemParent.setRightChild(new Node(elem));
                } else {
                    currentNode = currentNode.getRightChild();
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        if(!(o instanceof Node))
            return false;
        Node<T> node = (Node) o;

        boolean flagRight, flagLeft;
        if(this.getRightChild() != null)
            flagRight = this.getRightChild().equals(node.getRightChild());
        else
            flagRight = node.getRightChild() == null;
        if(this.getLeftChild() != null)
            flagLeft = this.getLeftChild().equals(node.getLeftChild());
        else
            flagLeft = node.getLeftChild() == null;

        return flagLeft && flagRight && this.getData().equals(node.getData());
    }

    @Override
    public String toString() {
        String s;
        s = this.getData().toString();

        if(this.getRightChild() != null)
            s = s+this.getRightChild().toString();
        if(this.getLeftChild() != null)
            s = s+this.getLeftChild().toString();

        return s;
    }
}

