package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.observers.IDrawableCollectionObserver;

import java.rmi.RemoteException;
import java.util.*;

public class DrawableCollection<T> {
    private List<T> collection;
    private List<IDrawableCollectionObserver<T>> observerList;

    /**
     * Constructor.
     * Creates an empty DrawableCollection
     */
    public DrawableCollection() {
        collection = new ArrayList<>();
        observerList = new ArrayList<>();
    }

    public DrawableCollection(List<T> list) {
        collection = new ArrayList<>(list);
        observerList = new ArrayList<>();
    }

    // GETTER
    /**
     * Returns the list of the observers. A new list is created, but the single elements
     * are not deep copied.
     * @return list of observers
     */
    public List<IDrawableCollectionObserver<T>> getObserverList() {
        return new ArrayList<>(observerList);
    }

    /**
     * Returns the number of elements in DrawableCollection
     *
     * @return number of elements in the collection
     */
    @Contract(pure = true)
    public int size() {
        return collection.size();
    }

    /**
     * Returns an array containing the elements present in DrawableCollection
     * The single elements are not deep copied.
     *
     * @return a generic collection array
     */
    public Object[] toArray() {
        return collection.toArray();
    }

    /**
     * Returns the copy of the list present in the DrawableCollection.
     * The single elements are not deep copied.
     *
     * @return list of elements present in this
     */
    public List<T> getCollection() {
        return new ArrayList<>(collection);
    }

    // MODIFIERS
    public void attachObserver(IDrawableCollectionObserver<T> observer) {
        observerList.add(observer);
    }

    /**
     * Adds an element to the collection
     * It notifies the observers that an element's been added.
     *
     * @param elem elements that needs to be added
     * @throws NullPointerException if elem is null
     */
    public void addElement(@NotNull T elem) throws RemoteException {
        collection.add(elem);
        for(IDrawableCollectionObserver<T> obs : observerList) obs.onElementAdd(elem);
    }

    /**
     * Draws a random element from DrawableCollection.
     * This removes the element from DrawableCollection.
     * It notifies the observers that an element's been added.
     *
     * @return the drawn element
     * @throws EmptyCollectionException if DrawableCollection is empty
     */
    public T draw() throws EmptyCollectionException, RemoteException {
        Random rand = new Random();
        if (collection.isEmpty()) {
            throw new EmptyCollectionException();
        }
        int pos = Math.abs(rand.nextInt(collection.size()));
        T elem = collection.get(pos);
        collection.remove(pos);
        for(IDrawableCollectionObserver<T> obs : observerList) obs.onElementDraw(elem);
        return elem;
    }

    /**
     * Adds a list of elements to DrawableCollection.
     *
     * @param list list of elements that need to be added
     */
    public void addElements(@NotNull List<T> list) throws RemoteException {
        collection.addAll(list);
        for(IDrawableCollectionObserver<T> obs : observerList) obs.onElementsAdd(list);
    }

    @Override
    public String toString() {
        return collection.toString();
    }

    /**
     * Two DrawableCollection are equals if they contains the same elements in the same number in any order.
     *
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

        fillPositionAndCounterArray(param, counter1, positions);

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

    /**
     * Fills counter and positions array based on the param list.
     * In the positions array, position[i] indicates the position of the i-th element in param in the counter array.
     * Counter array simply counts the occurrences of the elements in param
     *
     * @param param     fill the counter and position array based on this list
     * @param counter   counter array to fill
     * @param positions position array to fill
     */
    private void fillPositionAndCounterArray(List param, int[] counter, int[] positions) {
        for (int i = 0; i < param.size(); i++) {
            for (int j = 0; j < param.size(); j++) {
                if (param.get(i).equals(param.get(j))) {
                    counter[j]++;
                    positions[i] = j;
                    break;
                } else if (counter[j] == 0) {
                    counter[j]++;
                    positions[i] = j;
                    break;
                }
            }
        }
    }
}
