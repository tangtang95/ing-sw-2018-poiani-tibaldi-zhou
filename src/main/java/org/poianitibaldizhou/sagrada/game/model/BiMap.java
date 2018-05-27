package org.poianitibaldizhou.sagrada.game.model;

import java.util.HashMap;
import java.util.Set;

public class BiMap<K, V> {
    private HashMap<K, V> directMap;
    private HashMap<V, K> reverseMap;

    public BiMap() {
        directMap = new HashMap<>();
        reverseMap = new HashMap<>();
    }

    public void insert(K directKey, V reverseKey) {
        if(directMap.containsKey(directKey) || reverseMap.containsKey(reverseKey))
            return;
        directMap.putIfAbsent(directKey, reverseKey);
        reverseMap.putIfAbsent(reverseKey, directKey);
    }

    public void removeByDirectKey(K directKey) {
        // todo handle null pointer exception
        reverseMap.remove(directMap.remove(directKey));
    }

    public K removeByReverseKey(V reverseKey) {
        // todo handle null pointer exception
        K elem = reverseMap.remove(reverseKey);
        directMap.remove(elem);
        return elem;
    }

    public V getElementByDirectKey(K directKey) {
        return directMap.get(directKey);
    }

    public K getElementByReverseKey(V reverseKey) {
        return reverseMap.get(reverseKey);
    }

    public Set<V> getReverseKeyList() {
        return reverseMap.keySet();
    }
}
