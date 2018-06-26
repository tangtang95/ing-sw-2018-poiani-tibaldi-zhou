package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONObject;

public interface JSONable {

    /**
     * Return the json object of the concrete object
     * @return json object of the concrete object
     */
    JSONObject toJSON();
}