package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.cards.Hero;
import org.poo.cards.Minion;
import org.poo.fileio.ActionsInput;
import org.poo.rungame.Game;

import java.util.ArrayList;

public final class ActionsDebug extends Actions {
    public ActionsDebug(final ActionsInput command) {
        super(command);
    }

    /**
     * print card at certain position
     * @param input
     * @param output
     * @param game
     */
    public void getCardAtPosition(final ActionsInput input,
                                  final ArrayNode output, final Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardAtPosition");
        node.put("x", input.getX());
        node.put("y", input.getY());
        if (game.getTable().get(input.getX()).size() < input.getY()) {
            node.put("output", "No card available at that position.");
        } else  if (input.getX() >= 0 && input.getX() < game.getTable().size() &&
                input.getY() >= 0 && input.getY() < game.getTable().get(input.getX()).size()) {
            Minion minion = new Minion(game.getTable().get(input.getX())
                    .get(input.getY()));
            node.putPOJO("output", minion);
        }
        output.addPOJO(node);
    }

    /**
     * print cards in hand
     * @param input
     * @param output
     * @param game
     */
    public void getCardsInHand(final ActionsInput input,
                               final ArrayNode output, final  Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardsInHand");
        node.put("playerIdx", input.getPlayerIdx());
        ArrayList<Minion> hand = new ArrayList<Minion>();
        for (int i = 0; i < game.getPlayer(input
                .getPlayerIdx() - 1).getHand().size(); i++) {
            hand.add(game.getPlayer(input.getPlayerIdx() - 1).getHand().get(i));
        }
        node.putPOJO("output", hand);
        output.addPOJO(node);
    }

    /**
     * print player deck
     * @param input
     * @param output
     * @param game
     */
    public void getPlayerDeck(final ActionsInput input,
                              final ArrayNode output, final  Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerDeck");
        node.put("playerIdx", input.getPlayerIdx());
        ArrayList<Minion> deck = new ArrayList<Minion>();
        for (int i = 0; i < game.getPlayer(input
                .getPlayerIdx() - 1).getDeck().size(); i++) {
            deck.add(game.getPlayer(input.getPlayerIdx() - 1).getDeck().get(i));
        }
        node.putPOJO("output", deck);
        output.addPOJO(node);
    }

    /**
     * print cards on table
     * @param input
     * @param output
     * @param game
     */
    public void getCardsOnTable(final ActionsInput input,
                                final ArrayNode output, final  Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardsOnTable");

        ArrayNode rowsArray = JsonNodeFactory.instance.arrayNode();
        for (int k = 0; k < game.getRows(); k++) {
            ArrayNode row = JsonNodeFactory.instance.arrayNode();
            for (Minion minion : game.getTable().get(k)) {
                row.addPOJO(minion);
            }
            rowsArray.add(row);
        }
        node.set("output", rowsArray);
        output.addPOJO(node);
    }

    /**
     *
     * @param input
     * @param output
     * @param game
     */
    public void getPlayerHero(final ActionsInput input,
                              final ArrayNode output, final  Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerHero");
        node.put("playerIdx", input.getPlayerIdx());
        Hero hero = new Hero(game.getPlayer(input.getPlayerIdx() - 1).getPlayerHero());
        node.putPOJO("output", hero);
        output.addPOJO(node);
    }

    /**
     * print current player
     * @param input
     * @param output
     * @param game
     */
    public void getPlayerTurn(final ActionsInput input,
                              final ArrayNode output, final  Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerTurn");
        node.put("output", game.getStartingPlayer());
        output.addPOJO(node);
    }

    /**
     * print player mana
     * @param input
     * @param output
     * @param game
     */
    public void getPlayerMana(final ActionsInput input,
                              final ArrayNode output, final  Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerMana");
        node.put("playerIdx", input.getPlayerIdx());
        node.put("output", game.getPlayer(input.getPlayerIdx() - 1).getMana());
        output.addPOJO(node);
    }

    /**
     * print frozen cards on table
     * @param input
     * @param output
     * @param game
     */
    public void getFrozenCardsOnTable(final ActionsInput input,
                                      final ArrayNode output, final  Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getFrozenCardsOnTable");
        ArrayNode frozenCards = JsonNodeFactory.instance.arrayNode();
        for (int k = 0; k < game.getRows(); k++) {
            for (Minion minion : game.getTable().get(k)) {
                if (minion.getFrozen()) {
                    Minion aux = new Minion(minion);
                    frozenCards.addPOJO(aux);
                }
            }
        }
        node.put("output", frozenCards);
        output.addPOJO(node);
    }

    /**
     * print player one wins
     * @param input
     * @param output
     * @param game
     */
    public void getPlayerOneWins(final ActionsInput input,
                                 final ArrayNode output, final  Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerOneWins");
        node.put("output", game.getStatistics().getPlayerOneWins());
        output.addPOJO(node);
    }

    /**
     * print player two wins
     * @param input
     * @param output
     * @param game
     */
    public void getPlayerTwoWins(final ActionsInput input,
                                 final ArrayNode output, final  Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerTwoWins");
        node.put("output", game.getStatistics().getPlayerTwoWins());
        output.addPOJO(node);
    }

    /**
     * sum players wins to get total games played
     * @param input
     * @param output
     * @param game
     */
    public void getTotalGamesPlayed(final ActionsInput input,
                                    final ArrayNode output, final  Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getTotalGamesPlayed");
        node.put("output", game.getStatistics().getPlayerOneWins() + game
                .getStatistics().getPlayerTwoWins());
        output.addPOJO(node);
    }

    /**
     * execute debug commands
     * @param input
     * @param output
     * @param game
     */
    public void executeDebug(final ActionsInput input,
                             final ArrayNode output, final  Game game) {
        switch (super.getCommand()) {
            case "getCardAtPosition":
                getCardAtPosition(input, output, game);
                break;
            case "getCardsInHand":
                getCardsInHand(input, output, game);
                break;
            case "getPlayerDeck":
                getPlayerDeck(input, output, game);
                break;
            case "getCardsOnTable":
                getCardsOnTable(input, output, game);
                break;
            case "getPlayerHero":
                getPlayerHero(input, output, game);
                break;
            case "getPlayerTurn":
                getPlayerTurn(input, output, game);
                break;
            case "getPlayerMana":
                getPlayerMana(input, output, game);
                break;
            case "getFrozenCardsOnTable":
                getFrozenCardsOnTable(input, output, game);
                break;
            case "getPlayerOneWins" :
                getPlayerOneWins(input, output, game);
                break;
            case "getPlayerTwoWins" :
                getPlayerTwoWins(input, output, game);
                break;
            case "getTotalGamesPlayed" :
                getTotalGamesPlayed(input, output, game);
                break;
            default:
                break;
        }
    }
}
