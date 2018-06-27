package org.poianitibaldizhou.sagrada.utilities;

/**
 * Utilities static class for encrypting the player's username.
 */
public final class NetworkUtility {

    /**
     * Private constructor.
     */
    private NetworkUtility() {
    }

    /**
     * Convert a username in string.
     *
     * @param username to convert in string.
     * @return String.valueOf
     */
    public static String encryptUsername(String username) {
        return String.valueOf(username.hashCode());
    }
}
