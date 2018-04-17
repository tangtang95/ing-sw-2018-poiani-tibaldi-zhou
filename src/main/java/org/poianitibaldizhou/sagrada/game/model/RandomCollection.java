package org.poianitibaldizhou.sagrada.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomCollection<T> {
    List<T> collection;

    public RandomCollection() {
        collection = new ArrayList<T>();
    }

    public RandomCollection(ArrayList<T> collection) {
        this.collection = collection;
    }

    public void addElement(T elem) {
        collection.add(elem);
    }

    public void draw() {
        Random rand = new Random();
        collection.remove(rand.nextInt(collection.size()));
    }

    public void addElemenets(ArrayList<T> list) {
        collection.addAll(list);
    }

}
