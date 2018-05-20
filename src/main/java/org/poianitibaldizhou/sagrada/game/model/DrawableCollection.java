package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;

import java.util.*;

public class DrawableCollection<T> {
    private List<T> collection;

    /**
     * Constructor.
     * Creates an empty DrawableCollection
     */
    public DrawableCollection() {
        collection = new ArrayList<>();
    }

    /**
     * Constructor
     * Creates a DrawableCollection initialized with collection
     *
     * @param collection list of elements with which initialize DrawableCollection
     */
    public DrawableCollection(List<T> collection) {
        this.collection = new ArrayList<>();
        this.collection.addAll(collection);
    }

    /**
     * Adds an element to the collection
     *
     * @param elem elements that needs to be added
     */
    public void addElement(T elem) {
        collection.add(elem);
    }

    /**
     * Draws a random element from DrawableCollection.
     * This removes the element from DrawableCollection.
     *
     * @return the drawn element
     * @throws EmptyCollectionException if DrawableCollection is empty
     */
    public T draw() throws EmptyCollectionException {
        Random rand = new Random();
        if (collection.isEmpty()) {
            throw new EmptyCollectionException();
        }
        int pos = Math.abs(rand.nextInt(collection.size()));
        T elem = collection.get(pos);
        collection.remove(pos);

        return elem;
    }

    /**
     * Adds a list of elements to DrawableCollection.
     *
     * @param list list of elements that need to be added
     */
    public void addElements(List<T> list) {
        collection.addAll(list);
    }

    /**
     * Returns the number of elements in DrawableCollection
     *
     * @return number of elements in the collection
     */
    public int size() {
        return collection.size();
    }

    /**
     * Returns an array containing the elements present in DrawableCollection
     *
     * @return a generic collection array
     */
    //TODO deep copy
    public T[] toArray() {
        return (T[]) collection.toArray();
    }

    @Override
    public String toString() {
        return collection.toString();
    }

    //TODO deep copy
    public List<T> getCollection() {
        return collection;
    }

    public static DrawableCollection newInstance(DrawableCollection drawableCollection) {
        if (drawableCollection == null)
            return null;
        return new DrawableCollection(drawableCollection.getCollection());
    }

    /**
     * Two DrawableCollection are equals if they contains the same elements in the same number in any order.
     * @param o object to compare
     * @return true if equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DrawableCollection))
            return false;

        List param = ((DrawableCollection) o).getCollection();

        if (param.size() != this.size())
            return false;

        int[] counter1 = new int[param.size()];
        int[] positions = new int[param.size()];

        for (int i = 0; i < param.size(); i++) {
            for (int j = 0; j < param.size(); j++) {
                if (param.get(i).equals(param.get(j))) {
                    counter1[j]++;
                    positions[i] = j;
                    break;
                } else if (counter1[j] == 0) {
                    counter1[j]++;
                    positions[i] = j;
                    break;
                }
            }
        }

        for (int j = 0; j < param.size(); j++) {
            for (int i = 0; i < param.size(); i++) {
                if (collection.get(j).equals(param.get(i))) {
                    counter1[positions[i]] -= 1;
                    break;
                }
            }
        }

        for (int i = 0; i < param.size(); i++) {
            if (counter1[i] != 0)
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        ArrayList<T> list = new ArrayList<>(this.getCollection());
        list.sort(Comparator.comparingInt(Object::hashCode));

        for (T elem : list) {
            hashCode = 31 * hashCode + (elem == null ? 0 : elem.hashCode());
        }

        return hashCode;
    }
}
