package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.jetbrains.annotations.Contract;

import java.util.*;

public class Node<T> {
    private Node<T> rightChild = null;
    private Node<T> leftChild = null;
    private Node<T> parent = null;
    private T data;

    /**
     * Creates the node of the tree.
     *
     * @param data root element
     */
    public Node(T data) {
        this.data = data;
    }

    // GETTER
    public Node<T> getParent() {
        return parent;
    }

    public Node<T> getRightChild() {
        return rightChild;
    }

    public Node<T> getLeftChild() { return leftChild;}

    public T getData() {
        return this.data;
    }

    @Contract(pure = true)
    public boolean isRoot() {
        return (this.parent == null);
    }

    @Contract(pure = true)
    public boolean isLeaf() {
        return rightChild == null && leftChild == null;
    }

    // MODIFIER
    public void setRightChild(Node<T> rightChild) {
        this.rightChild = rightChild;
        rightChild.setParent(parent);
    }

    public void setLeftChild(Node<T> leftChild) {
        this.leftChild = leftChild;
        leftChild.setParent(parent);
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
     * @throws NullPointerException if current structure doesn't allow index as number
     */
    public void addAtIndex(T elem, int index) {
        Deque<Integer> stack = new ArrayDeque<>();
        Node<T> currentNode = this;
        Node<T> elemParent = this;
        int currPos;

        while(index > 0 ) {
            stack.push(index);
            index = index / 2;
        }

        stack.pop();

        while(!stack.isEmpty()) {
            currPos = stack.pop();
            if(stack.isEmpty())
                elemParent = currentNode;

            if (currPos % 2 == 0) {
                if(stack.isEmpty()) {
                    elemParent.setLeftChild(new Node<>(elem));
                } else {
                    currentNode = currentNode.getLeftChild();
                }
            } else {
                if (stack.isEmpty()) {
                    elemParent.setRightChild(new Node<>(elem));
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
        if(!(o instanceof Node<?>))
            return false;
        Node<?> node = (Node<?>) o;

        boolean flagRight;
        boolean flagLeft;
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
    public int hashCode() {
        return Objects.hash(rightChild, leftChild, data);
    }
}

