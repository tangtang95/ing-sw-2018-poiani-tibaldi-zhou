package org.poianitibaldizhou.sagrada.cli;

/**
 * OVERVIEW: Represents a screen in a CLI client.
 */
public interface IScreen {

    /**
     * Start the CLI screen.
     *
     * @throws InterruptedException when the method is interrupted.
     */
    void startCLI() throws InterruptedException;
}
