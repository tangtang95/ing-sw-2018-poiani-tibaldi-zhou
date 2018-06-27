package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONObject;

/**
 * OVERVIEW: Represents an object that can be converted to JSON in order to be transmitted
 * over the network.
 */
public interface JSONable {

    /**
     * Return the json object of the concrete object
     * @return json object of the concrete object
     */
    JSONObject toJSON();
}
