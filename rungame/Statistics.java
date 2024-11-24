package org.poo.rungame;

public final class Statistics {
    private int playerOneWins;
    private int playerTwoWins;

    public int getPlayerOneWins() {
        return playerOneWins;
    }

    public int getPlayerTwoWins() {
        return playerTwoWins;
    }

    /**
     * increment player one wins
     * @return
     */
    public void incrementPlayerOneWins() {
        this.playerOneWins++;
    }

    /**
     * increment player two wins
     * @return
     */
    public void incrementPlayerTwoWins() {
        this.playerTwoWins++;
    }
}
