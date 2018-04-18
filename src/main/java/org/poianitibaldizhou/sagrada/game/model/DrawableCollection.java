package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawableCollection<T> {
    List<T> collection;

    public DrawableCollection() {
        collection = new ArrayList<T>();
    }

    public DrawableCollection(List<T> collection) {
        this.collection = new ArrayList<T>();
        this.collection.addAll(collection);
    }

    public void addElement(T elem) {
        collection.add(elem);
    }

    public T draw() throws EmptyCollectionException {
        Random rand = new Random();
        if(collection.size() < 1) {
            throw new EmptyCollectionException();
        }
        int pos = Math.abs(rand.nextInt(collection.size()));
        T elem = (T) collection.get(pos);
        collection.remove(pos);

        return elem;
    }

    public void addElements(List<T> list) {
        collection.addAll(list);
    }

    public int size() {
        return collection.size();
    }

    public T[] toArray() {
        return (T[]) collection.toArray();
    }

    @Override
    public String toString() {
        return collection.toString();
    }
}
