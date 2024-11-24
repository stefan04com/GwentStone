package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.cards.Minion;
import org.poo.fileio.ActionsInput;
import org.poo.rungame.Game;

public final class ActionsHero extends Actions {
    public ActionsHero(final ActionsInput command) {
        super(command);
    }
    /**
     * attack on a hero
     * @param input
     * @param output
     * @param game
     */
    public void useAttackHero(final ActionsInput input,
                              final ArrayNode output, final Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (input.getCardAttacker().getX() < game.getTable().size()
                && input.getCardAttacker().getY() < game.getTable()
                .get(input.getCardAttacker().getX()).size()) {
            Minion attacker = new Minion(game.getTable()
                    .get(input.getCardAttacker().getX())
                    .get(input.getCardAttacker().getY()));
            if (attacker.getFrozen()) {
                node.put("command", "useAttackHero");
                node.putPOJO("cardAttacker", input.getCardAttacker());
                node.put("error", "Attacker card is frozen.");
                output.addPOJO(node);
                return;
            }
            if (attacker.getHasAttacked()) {
                node.put("command", "useAttackHero");
                node.putPOJO("cardAttacker", input.getCardAttacker());
                node.put("error", "Attacker card has already attacked this turn.");
                output.addPOJO(node);
                return;
            }
            if (game.getStartingPlayer() == 1) {
                boolean tankFound = false;
                for (int i = 0; i < game.getTable().get(1).size(); i++) {
                    if (game.getTable().get(1).get(i).getTank()) {
                        tankFound = true;
                        break;
                    }
                }
                if (tankFound) {
                    node.put("command", "useAttackHero");
                    node.putPOJO("cardAttacker", input.getCardAttacker());
                    node.put("error", "Attacked card is not of type 'Tank'.");
                    output.addPOJO(node);
                    return;
                } else {
                    game.getPlayer(1).getPlayerHero().setHealth(game.getPlayer(1).
                            getPlayerHero().getHealth() - attacker.getAttackDamage());
                    game.getTable().get(input.getCardAttacker().getX())
                            .get(input.getCardAttacker().getY()).setHasAttacked(true);
                    if (game.getPlayer(1).getPlayerHero().getHealth() <= 0) {
                        node.put("gameEnded", "Player one killed the enemy hero.");
                        output.addPOJO(node);
                        game.getStatistics().incrementPlayerOneWins();
                        game.setGameEnded(true);
                    }
                }
            } else {
                boolean foundTank = false;
                for (int i = 0; i < game.getTable().get(2).size(); i++) {
                    if (game.getTable().get(2).get(i).getTank()) {
                        foundTank = true;
                        break;
                    }
                }
                if (foundTank) {
                    node.put("command", "useAttackHero");
                    node.putPOJO("cardAttacker", input.getCardAttacker());
                    node.put("error", "Attacked card is not of type 'Tank'.");
                    output.addPOJO(node);
                    return;
                } else {
                    game.getPlayer(0).getPlayerHero().setHealth(game.getPlayer(0)
                            .getPlayerHero().getHealth() - attacker.getAttackDamage());
                    game.getTable().get(input.getCardAttacker().getX())
                            .get(input.getCardAttacker()
                                    .getY()).setHasAttacked(true);
                    if (game.getPlayer(0).getPlayerHero().getHealth() <= 0) {
                        node.put("gameEnded", "Player two killed the enemy hero.");
                        output.addPOJO(node);
                        game.getStatistics().incrementPlayerTwoWins();
                        game.setGameEnded(true);
                    }
                }
            }
        }
    }

    /**
     * use hero ability
     * @param input
     * @param output
     * @param game
     */
    public void useHeroAbility(final ActionsInput input,
                               final ArrayNode output, final Game game) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (game.getPlayer(game.getStartingPlayer() - 1)
                .getMana() < game.getPlayer(game.getStartingPlayer() - 1)
                .getPlayerHero().getMana()) {
            node.put("command", "useHeroAbility");
            node.put("affectedRow", input.getAffectedRow());
            node.put("error", "Not enough mana to use hero's ability.");
            output.addPOJO(node);
            return;
        }
        if (game.getPlayer(game.getStartingPlayer() - 1)
                .getPlayerHero().getHasAttacked()) {
            node.put("command", "useHeroAbility");
            node.put("affectedRow", input.getAffectedRow());
            node.put("error", "Hero has already attacked this turn.");
            output.addPOJO(node);
            return;
        }
        if (game.getPlayer(game.getStartingPlayer() - 1).getPlayerHero().getName()
                .equals("Lord Royce") || game.getPlayer(game.getStartingPlayer() - 1)
                .getPlayerHero().getName().equals("Empress Thorina")) {
            if (!checkEnemyRow(input, game)) {
                node.put("command", "useHeroAbility");
                node.put("affectedRow", input.getAffectedRow());
                node.put("error", "Selected row does not belong to the enemy.");
                output.addPOJO(node);
                return;
            } else {
                game.getPlayer(game.getStartingPlayer() - 1).getPlayerHero()
                        .useAbilityHero(input.getAffectedRow(), game);
                game.getPlayer(game.getStartingPlayer() - 1).getPlayerHero()
                        .setHasAttacked(true);
                game.getPlayer(game.getStartingPlayer() - 1).setMana(game.getPlayer(game
                        .getStartingPlayer() - 1).getMana() - game.getPlayer(game
                        .getStartingPlayer() - 1).getPlayerHero().getMana());
            }
        }
        if (game.getPlayer(game.getStartingPlayer() - 1).getPlayerHero().getName()
                .equals("General Kocioraw") || game.getPlayer(game
                        .getStartingPlayer() - 1)
                .getPlayerHero().getName().equals("King Mudface")) {
            if (!checkPlayerRow(input, game)) {
                node.put("command", "useHeroAbility");
                node.put("affectedRow", input.getAffectedRow());
                node.put("error",
                        "Selected row does not belong to the current player.");
                output.addPOJO(node);
                return;
            } else {
                game.getPlayer(game.getStartingPlayer() - 1).getPlayerHero()
                        .useAbilityHero(input.getAffectedRow(), game);
                game.getPlayer(game.getStartingPlayer() - 1).getPlayerHero()
                        .setHasAttacked(true);
                game.getPlayer(game.getStartingPlayer() - 1).setMana(game.getPlayer(game
                        .getStartingPlayer() - 1).getMana() - game.getPlayer(game
                        .getStartingPlayer() - 1).getPlayerHero().getMana());
            }
        }
    }

    /**
     * execute game commands for hero
     * @param input
     * @param output
     * @param game
     */
    public void executeGameHero(final ActionsInput input,
                                final ArrayNode output, final Game game) {
        switch (super.getCommand()) {
            case "useAttackHero":
                useAttackHero(input, output, game);
                break;
            case "useHeroAbility" :
                useHeroAbility(input, output, game);
                break;
            default:
                break;
        }
    }

}
