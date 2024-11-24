package org.poo.rungame;

import org.poo.cards.Minion;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commands.Actions;
import org.poo.commands.ActionsDebug;
import org.poo.commands.ActionsHero;
import org.poo.fileio.Input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Game {

    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private int suffleseed;
    private int startingPlayer;

    private ArrayList<Player> players = new ArrayList<>(2);

    private final int rows = 4;
    private final int columns = 5;
    private ArrayList<ArrayList<Minion>> table = new ArrayList<>(rows);
    private int rounds;
    private Statistics statistics = new Statistics();
    private boolean gameEnded = false;

    public Statistics getStatistics() {
        return statistics;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    /**
     * check that a player won and game ended
     * @return
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     *
     * @param gameEnded
     */
    public void setGameEnded(final boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public ArrayList<ArrayList<Minion>> getTable() {
        return table;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(final int rounds) {
        this.rounds = rounds;
    }

    /**
     * increment number of rounds
     */
    public void incrementRounds() {
        setRounds(getRounds() + 1);
    }

    /**
     * change players turn each round
     */
    public void changePlayerTurn() {
        if (getStartingPlayer() == 1) {
            setStartingPlayer(2);
        } else {
            setStartingPlayer(1);
        }
    }

    /**
     *
     * @return
     */
    public Player getPlayer(final int idx) {
        return players.get(idx);
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(final int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public int getPlayerOneDeckIdx() {
        return playerOneDeckIdx;
    }

    public void setPlayerOneDeckIdx(final int playerOneDeckIdx) {
        this.playerOneDeckIdx = playerOneDeckIdx;
    }

    public int getPlayerTwoDeckIdx() {
        return playerTwoDeckIdx;
    }

    public void setPlayerTwoDeckIdx(final int playerTwoDeckIdx) {
        this.playerTwoDeckIdx = playerTwoDeckIdx;
    }

    public int getSuffleseed() {
        return suffleseed;
    }

    public void setSuffleseed(final int suffleseed) {
        this.suffleseed = suffleseed;
    }

    /**
     * reset attack variable each round
     * @param player
     */
    public void resetAttack(final Game game, final Player player) {
       for (int i = 0; i < getRows(); i++) {
           for (int j = 0; j < getColumns(); j++) {
               if (game.getTable().get(i).size() > j) {
                   game.getTable().get(i).get(j).setHasAttacked(false);
               }
           }
       }
        player.getPlayerHero().setHasAttacked(false);
    }

    /**
     * reset frozen variable each round
     * @param game
     */
    public void resetFrozen(final Game game) {
        if (game.getStartingPlayer() == 1) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < getColumns(); j++) {
                    if (game.getTable().get(i).size() > j) {
                        game.getTable().get(i).get(j).setFrozen(false);
                    }
                }
            }
        } else {
            for (int i = 2; i < rows; i++) {
                for (int j = 0; j < getColumns(); j++) {
                    if (game.getTable().get(i).size() > j) {
                        game.getTable().get(i).get(j).setFrozen(false);
                    }
                }
            }
        }
    }

    /**
     * prepare and run all games
     * @param input
     * @param output
     */
    public void startGames(final Input input, final ArrayNode output) {
        for (int i = 0; i < input.getGames().size(); i++) {
            this.players.clear();
            this.table = new ArrayList<>(rows);
            setGameEnded(false);
            setStartingPlayer(input.getGames().get(i).getStartGame().getStartingPlayer());
            setPlayerOneDeckIdx(input.getGames().get(i).getStartGame().getPlayerOneDeckIdx());
            setPlayerTwoDeckIdx(input.getGames().get(i).getStartGame().getPlayerTwoDeckIdx());
            setSuffleseed(input.getGames().get(i).getStartGame().getShuffleSeed());
            setRounds(2);

            for (int k = 0; k < rows; k++) {
                ArrayList<Minion> row = new ArrayList<>(columns);
                table.add(row);
            }

            ArrayList<Minion> deck1 = new ArrayList<>();
            ArrayList<Minion> deck2 = new ArrayList<>();

            for (int k = 0; k < input.getPlayerOneDecks().getDecks()
                    .get(getPlayerOneDeckIdx()).size(); k++) {
                Minion card1 = new Minion(input.getPlayerOneDecks().getDecks()
                        .get(getPlayerOneDeckIdx()).get(k));
                deck1.add(card1);
            }

            for (int k = 0; k < input.getPlayerTwoDecks().getDecks()
                    .get(getPlayerTwoDeckIdx()).size(); k++) {
                Minion card2 = new Minion(input.getPlayerTwoDecks().getDecks()
                        .get(getPlayerTwoDeckIdx()).get(k));
                deck2.add(card2);
            }

            Collections.shuffle(deck1,
                    new Random(getSuffleseed()));
            Collections.shuffle(deck2,
                    new Random(getSuffleseed()));

            this.players.add(0, new Player(input.getGames().get(i)
                    .getStartGame().getPlayerOneHero(),
                    deck1));
            this.players.add(1, new Player(input.getGames().get(i)
                    .getStartGame().getPlayerTwoHero(),
                    deck2));

            for (int j = 0; j < input.getGames().get(i).getActions().size(); j++) {
                Actions actions = new Actions(input.getGames().get(i).getActions().get(j));
                ActionsDebug actionsDebug = new ActionsDebug(input.getGames().get(i)
                        .getActions().get(j));
                ActionsHero actionsHero = new ActionsHero(input.getGames().get(i)
                        .getActions().get(j));
                if (!gameEnded) {
                    actions.executeGame(input.getGames().get(i).getActions()
                            .get(j), output, this);
                    actionsHero.executeGameHero(input.getGames().get(i).getActions()
                            .get(j), output, this);
                }
                actionsDebug.executeDebug(input.getGames().get(i).getActions()
                        .get(j), output, this);
            }
        }
    }
}
