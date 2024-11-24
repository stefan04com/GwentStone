package org.poo.cards;

import org.poo.fileio.CardInput;
import org.poo.rungame.Game;

public final class Hero extends Card {
    private final int hp = 30;

    public Hero(final CardInput cardInput) {
        super(cardInput);
        super.setHealth(hp);
    }

    public Hero(final Hero hero) {
        super(hero);
    }

    /**
     * use the ability of the hero
     * @param rowIdx
     * @param game
     */
    public void useAbilityHero(final int rowIdx, final Game game) {
        switch (this.getName()) {
            case "Lord Royce":
                for (int i = 0; i < game.getTable().get(rowIdx).size(); i++) {
                    game.getTable().get(rowIdx).get(i).setFrozen(true);
                }
                break;
            case "Empress Thorina":
                int maxhp = 0;
                int maxidx = 0;
                for (int i = 0; i < game.getTable().get(rowIdx).size(); i++) {
                    if (game.getTable().get(rowIdx).get(i).getHealth() > maxhp) {
                        maxhp = game.getTable().get(rowIdx).get(i).getHealth();
                        maxidx = i;
                    }
                }
                game.getTable().get(rowIdx).remove(maxidx);
                break;
            case "King Mudface":
                for (int i = 0; i < game.getTable().get(rowIdx).size(); i++) {
                    game.getTable().get(rowIdx).get(i).setHealth(game.getTable()
                            .get(rowIdx).get(i).getHealth() + 1);
                }
                break;
            case "General Kocioraw":
                for (int i = 0; i < game.getTable().get(rowIdx).size(); i++) {
                    game.getTable().get(rowIdx).get(i).setAttackDamage(game.getTable()
                            .get(rowIdx).get(i).getAttackDamage() + 1);
                }
                break;
            default:
                break;
        }
    }
}
