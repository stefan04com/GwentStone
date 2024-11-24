package org.poo.commands;

import org.poo.cards.Minion;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.rungame.Game;

public class Actions {
    private final int row3 = 3;

    private String command;

    public Actions(final ActionsInput input) {
        this.command = input.getCommand();
    }

    /**
     * get the command
     * @return
     */
    public String getCommand() {
        return command;
    }

    /**
     * set the command
     * @param command
     */
    public void setCommand(final String command) {
        this.command = command;
    }

    /**
     * Check if the card is an enemy player card.
     * @param input
     * @param game
     * @return
     */
    public boolean checkEnemyCard(final ActionsInput input, final Game game) {
        if (game.getStartingPlayer() == 1) {
            if (input.getCardAttacked().getX() == 1 || input.getCardAttacked().getX() == 0) {
                return true;
            }
        }
        if (game.getStartingPlayer() == 2) {
            if (input.getCardAttacked().getX() == 2 || input.getCardAttacked().getX() == row3) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the card is a player card.
     * @param input
     * @param game
     * @return
     */
    public boolean checkPlayerCard(final ActionsInput input, final  Game game) {
        if (game.getStartingPlayer() == 1) {
            if (input.getCardAttacked().getX() == 2 || input.getCardAttacked().getX() == row3) {
                return true;
            }
        }
        if (game.getStartingPlayer() == 2) {
            if (input.getCardAttacked().getX() == 1 || input.getCardAttacked().getX() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if there are tanks on the table.
     * @param input
     * @param game
     * @return
     */
    public boolean checkForTanks(final ActionsInput input, final  Game game) {
        if (game.getStartingPlayer() == 1) {
            for (int i = 0; i < game.getTable().get(1).size(); i++) {
                if (game.getTable().get(1).get(i).getTank()) {
                    return true;
                }
            }
        }
        if (game.getStartingPlayer() == 2) {
            for (int i = 0; i < game.getTable().get(2).size(); i++) {
                if (game.getTable().get(2).get(i).getTank()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the row is a player row.
     * @param input
     * @param game
     * @return
     */
    public boolean checkPlayerRow(final ActionsInput input, final  Game game) {
        if (game.getStartingPlayer() == 1) {
            if (input.getAffectedRow() == 2 || input.getAffectedRow() == row3) {
                return true;
            }
        }
        if (game.getStartingPlayer() == 2) {
            if (input.getAffectedRow() == 1 || input.getAffectedRow() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the row is an enemy row.
     * @param input
     * @param game
     * @return
     */
    public boolean checkEnemyRow(final ActionsInput input, final  Game game) {
        if (game.getStartingPlayer() == 1) {
            if (input.getAffectedRow() == 1 || input.getAffectedRow() == 0) {
                return true;
            }
        }
        if (game.getStartingPlayer() == 2) {
            if (input.getAffectedRow() == 2 || input.getAffectedRow() == row3) {
                return true;
            }
        }
        return false;
    }

    /**
     * end a players turn adding a new card and atribuing mana
     * @param input
     * @param output
     * @param game
     */
    public void endPlayerTurn(final ActionsInput input,
                              final ArrayNode output, final Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "endPlayerTurn");

        game.incrementRounds();
        game.changePlayerTurn();

        game.resetAttack(game, game.getPlayer(0));
        game.resetAttack(game, game.getPlayer(1));
        game.resetFrozen(game);
        if (game.getRounds() % 2 == 0) {

            int manaRound = game.getRounds() / 2;
            game.getPlayer(0).increaseMana(manaRound);
            game.getPlayer(1).increaseMana(manaRound);

            if (game.getPlayer(0).getDeck().size() > 0) {
                game.getPlayer(0).getHand().add(game.getPlayer(0).getDeck().get(0));
                game.getPlayer(0).getDeck().remove(0);
            }

            if (game.getPlayer(1).getDeck().size() > 0) {
                game.getPlayer(1).getHand().add(game.getPlayer(1).getDeck().get(0));
                game.getPlayer(1).getDeck().remove(0);
            }
        }
    }

    /**
     * place a card on the table
     * @param input
     * @param output
     * @param game
     */
    public void placeCard(final ActionsInput input,
                          final ArrayNode output, final Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (game.getPlayer(game.getStartingPlayer() - 1).getMana() < game.getPlayer(game
                .getStartingPlayer() - 1).getHand().get(input.getHandIdx()).getMana()) {
            node.put("command", "placeCard");
            node.put("handIdx", input.getHandIdx());
            node.put("error", "Not enough mana to place card on table.");
            output.addPOJO(node);
            return;
        } else {
            Minion minion = new Minion(game.getPlayer(game.getStartingPlayer() - 1)
                    .getHand().get(input.getHandIdx()));
            if (game.getStartingPlayer() == 1 && minion.getPlacingRow().equals("front")) {
                if (game.getTable().get(2).size() < game.getColumns()) {
                    game.getTable().get(2).add(minion);
                    game.getPlayer(game.getStartingPlayer() - 1).getHand()
                            .remove(input.getHandIdx());
                    game.getPlayer(game.getStartingPlayer() - 1).setMana(game
                            .getPlayer(game.getStartingPlayer() - 1)
                            .getMana() - minion.getMana());
                    return;
                } else {
                    node.put("command", "placeCard");
                    node.put("handIdx", input.getHandIdx());
                    node.put("error", "Cannot place card on table since row is full.");
                    output.addPOJO(node);
                    return;
                }
            }
            if (game.getStartingPlayer() == 1 && minion.getPlacingRow().equals("back")) {
                if (game.getTable().get(row3).size() < game.getColumns()) {
                    game.getTable().get(row3).add(minion);
                    game.getPlayer(game.getStartingPlayer() - 1).getHand()
                            .remove(input.getHandIdx());
                    game.getPlayer(game.getStartingPlayer() - 1).setMana(game
                            .getPlayer(game.getStartingPlayer() - 1)
                            .getMana() - minion.getMana());
                    return;
                } else {
                    node.put("command", "placeCard");
                    node.put("handIdx", input.getHandIdx());
                    node.put("error", "Cannot place card on table since row is full.");
                    output.addPOJO(node);
                    return;
                }
            }
            if (game.getStartingPlayer() == 2 && minion.getPlacingRow().equals("front")) {
                if (game.getTable().get(1).size() < game.getColumns()) {
                    game.getTable().get(1).add(minion);
                    game.getPlayer(game.getStartingPlayer() - 1).getHand()
                            .remove(input.getHandIdx());
                    game.getPlayer(game.getStartingPlayer() - 1).setMana(game
                            .getPlayer(game.getStartingPlayer() - 1)
                            .getMana() - minion.getMana());
                    return;
                } else {
                    node.put("command", "placeCard");
                    node.put("handIdx", input.getHandIdx());
                    node.put("error", "Cannot place card on table since row is full.");
                    output.addPOJO(node);
                    return;
                }
            }
            if (game.getStartingPlayer() == 2 && minion.getPlacingRow().equals("back")) {
                if (game.getTable().get(0).size() < game.getColumns()) {
                    game.getTable().get(0).add(minion);
                    game.getPlayer(game.getStartingPlayer() - 1).getHand()
                            .remove(input.getHandIdx());
                    game.getPlayer(game.getStartingPlayer() - 1).setMana(game.
                            getPlayer(game.getStartingPlayer() - 1)
                            .getMana() - minion.getMana());
                } else {
                    node.put("command", "placeCard");
                    node.put("handIdx", input.getHandIdx());
                    node.put("error", "Cannot place card on table since row is full.");
                    output.addPOJO(node);
                    return;
                }
            }
        }
    }

    /**
     * card uses attack
     * @param input
     * @param output
     * @param game
     */
    public void cardUsesAttack(final ActionsInput input,
                               final ArrayNode output, final Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (!checkEnemyCard(input, game)) {
            node.put("command", "cardUsesAttack");
            node.putPOJO("cardAttacker", input.getCardAttacker());
            node.putPOJO("cardAttacked", input.getCardAttacked());
            node.put("error", "Attacked card does not belong to the enemy.");
            output.addPOJO(node);
            return;
        }
        Minion attacker = new Minion(game.getTable().get(input.getCardAttacker().getX())
                .get(input.getCardAttacker().getY()));
        if (attacker.getHasAttacked()) {
            node.put("command", "cardUsesAttack");
            node.putPOJO("cardAttacker", input.getCardAttacker());
            node.putPOJO("cardAttacked", input.getCardAttacked());
            node.put("error", "Attacker card has already attacked this turn.");
            output.addPOJO(node);
            return;
        }
        if (attacker.getFrozen()) {
            node.put("command", "cardUsesAttack");
            node.putPOJO("cardAttacker", input.getCardAttacker());
            node.putPOJO("cardAttacked", input.getCardAttacked());
            node.put("error", "Attacker card is frozen.");
            output.addPOJO(node);
            return;
        }
        if (game.getStartingPlayer() == 1) {
            boolean tankFound = false;
            for (int k = 0; k < game.getTable().get(1).size(); k++) {
                if (game.getTable().get(1).get(k).getTank()) {
                    tankFound = true;
                    break;
                }
            }
            if (tankFound && !game.getTable().get(input.getCardAttacked().getX())
                    .get(input.getCardAttacked().getY()).getTank()) {
                node.put("command", "cardUsesAttack");
                node.putPOJO("cardAttacker", input.getCardAttacker());
                node.putPOJO("cardAttacked", input.getCardAttacked());
                node.put("error", "Attacked card is not of type 'Tank'.");
                output.addPOJO(node);
                return;
            } else {
                if (input.getCardAttacked().getX() >= 0 && input.getCardAttacked().getX() < game.getTable().size() &&
                        input.getCardAttacked().getY() >= 0 && input.getCardAttacked().getY() < game.getTable().get(input.getCardAttacked().getX()).size()) {
                    game.getTable().get(input.getCardAttacked().getX())
                            .get(input.getCardAttacked().getY()).reduceHealth(attacker);

                    game.getTable().get(input.getCardAttacker().getX())
                            .get(input.getCardAttacker().getY()).setHasAttacked(true);
                    if (game.getTable().get(input.getCardAttacked().getX())
                            .get(input.getCardAttacked().getY()).getHealth() <= 0) {
                        game.getTable().get(input.getCardAttacked().getX())
                                .remove(input.getCardAttacked().getY());
                    }
                }
            }
        } else {
            boolean tankFound = false;
            for (int k = 0; k < game.getTable().get(2).size(); k++) {
                if (game.getTable().get(2).get(k).getTank()) {
                    tankFound = true;
                    break;
                }
            }
            if (tankFound && !game.getTable().get(input.getCardAttacked().getX())
                    .get(input.getCardAttacked().getY()).getTank()) {
                node.put("command", "cardUsesAttack");
                node.putPOJO("cardAttacker", input.getCardAttacker());
                node.putPOJO("cardAttacked", input.getCardAttacked());
                node.put("error", "Attacked card is not of type 'Tank'.");
                output.addPOJO(node);
                return;
            } else {
                game.getTable().get(input.getCardAttacked().getX())
                        .get(input.getCardAttacked().getY()).reduceHealth(attacker);
                game.getTable().get(input.getCardAttacker().getX())
                        .get(input.getCardAttacker().getY()).setHasAttacked(true);
                if (game.getTable().get(input.getCardAttacked().getX())
                        .get(input.getCardAttacked().getY()).getHealth() <= 0) {
                    game.getTable().get(input.getCardAttacked().getX())
                            .remove(input.getCardAttacked().getY());
                }
            }
        }
    }

    /**
     * card uses ability
     * @param input
     * @param output
     * @param game
     */
    public void cardUsesAbility(final ActionsInput input,
                                final ArrayNode output, final Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (input.getCardAttacker().getX() < game.getTable().size() &&
                input.getCardAttacker().getY() < game.getTable().get(input.getCardAttacker().getX()).size()) {
            Minion specialMinion = new Minion(game.getTable().get(input.getCardAttacker().getX())
                    .get(input.getCardAttacker().getY()));
            if (specialMinion.getFrozen()) {
                node.put("command", "cardUsesAbility");
                node.putPOJO("cardAttacker", input.getCardAttacker());
                node.putPOJO("cardAttacked", input.getCardAttacked());
                node.put("error", "Attacker card is frozen.");
                output.addPOJO(node);
                return;
            }
            if (specialMinion.getHasAttacked()) {
                node.put("command", "cardUsesAbility");
                node.putPOJO("cardAttacker", input.getCardAttacker());
                node.putPOJO("cardAttacked", input.getCardAttacked());
                node.put("error",
                        "Attacker card has already attacked this turn.");
                output.addPOJO(node);
                return;
            }
            if (specialMinion.getName().equals("Disciple")) {
                if (checkPlayerCard(input, game)) {
                    specialMinion.useAbility(game.getTable()
                            .get(input.getCardAttacked().getX())
                            .get(input.getCardAttacked().getY()));
                    game.getTable().get(input.getCardAttacker().getX())
                            .get(input.getCardAttacker().getY()).setHasAttacked(true);
                } else {
                    node.put("command", "cardUsesAbility");
                    node.putPOJO("cardAttacker", input.getCardAttacker());
                    node.putPOJO("cardAttacked", input.getCardAttacked());
                    node.put("error",
                            "Attacked card does not belong to the current player.");
                    output.addPOJO(node);
                    return;
                }
            } else  {
                if (!checkEnemyCard(input, game)) {
                    node.put("command", "cardUsesAbility");
                    node.putPOJO("cardAttacker", input.getCardAttacker());
                    node.putPOJO("cardAttacked", input.getCardAttacked());
                    node.put("error", "Attacked card does not belong to the enemy.");
                    output.addPOJO(node);
                    return;
                } else {
                    if (checkForTanks(input, game) && !game.getTable()
                            .get(input.getCardAttacked().getX())
                            .get(input.getCardAttacked().getY()).getTank()) {
                        node.put("command", "cardUsesAbility");
                        node.putPOJO("cardAttacker", input.getCardAttacker());
                        node.putPOJO("cardAttacked", input.getCardAttacked());
                        node.put("error", "Attacked card is not of type 'Tank'.");
                        output.addPOJO(node);
                        return;
                    } else {
                        if (input.getCardAttacker().getX() >= 0 && input.getCardAttacker().getX() < game.getTable().size() &&
                                input.getCardAttacker().getY() >= 0 && input.getCardAttacker().getY() < game.getTable().get(input.getCardAttacker().getX()).size() &&
                                input.getCardAttacked().getX() >= 0 && input.getCardAttacked().getX() < game.getTable().size() &&
                                input.getCardAttacked().getY() >= 0 && input.getCardAttacked().getY() < game.getTable().get(input.getCardAttacked().getX()).size()) {
                            game.getTable().get(input.getCardAttacker().getX())
                                    .get(input.getCardAttacker().getY()).useAbility(game
                                            .getTable().get(input.getCardAttacked().getX())
                                            .get(input.getCardAttacked().getY()));
                            game.getTable().get(input.getCardAttacker().getX())
                                    .get(input.getCardAttacker().getY())
                                    .setHasAttacked(true);

                            if (game.getTable().get(input.getCardAttacked().getX())
                                    .get(input.getCardAttacked().getY())
                                    .getHealth() <= 0) {
                                game.getTable().get(input.getCardAttacked().getX())
                                        .remove(input.getCardAttacked().getY());
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * execute game commands
     * @param input
     * @param output
     * @param game
     */
    public void executeGame(final ActionsInput input,
                        final ArrayNode output, final Game game) {
        switch (command) {
            case "endPlayerTurn":
                endPlayerTurn(input, output, game);
                break;
            case "placeCard":
                placeCard(input, output, game);
                break;
            case "cardUsesAttack":
                cardUsesAttack(input, output, game);
                break;
            case "cardUsesAbility":
                cardUsesAbility(input, output, game);
                break;
            default:
                break;
        }
    }
}

