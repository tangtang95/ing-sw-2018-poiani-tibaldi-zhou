package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.INetworkFakeObserver;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    private Map<String, INetworkFakeObserver> tokenINetworkFakeObserverMap;

    public NetworkManager() {
        tokenINetworkFakeObserverMap = new HashMap<>();
    }

    // GETTER

    public Map<String, INetworkFakeObserver> getTokenINetworkFakeObserverMap() {
        return tokenINetworkFakeObserverMap;
    }


    // MODIFIER

    public void attachNetworkObserver(String token, INetworkFakeObserver networkFakeObserver) {
        tokenINetworkFakeObserverMap.putIfAbsent(token, networkFakeObserver);
    }

    public void detachNetworkObserver(String token) {
        tokenINetworkFakeObserverMap.remove(token);
    }
}
