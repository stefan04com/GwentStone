package org.poo.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

@JsonPropertyOrder({"mana", "attackDamage", "health", "description", "colors", "name"})
public final class Minion extends Card {

    private int attackDamage;
    @JsonIgnore
    private String placingRow;

    @JsonIgnore
    private boolean tank;

    public boolean getTank() {
        return tank;
    }

    public String getPlacingRow() {
        return placingRow;
    }

    /**
     * decide on which row to place the minion
     */
    public void setPlacingRow() {
        final String names = "Goliath, Warden, The Ripper,Miraj";
        if (names.contains(this.getName())) {
            this.placingRow = "front";
        } else {
            this.placingRow = "back";
        }
    }

    /**
     * set if a card it is a tank or not
     */
    public void setTank() {
        final ArrayList<String> tankNames = new ArrayList<>();
        tankNames.add("Goliath");
        tankNames.add("Warden");
        if (tankNames.contains(this.getName())) {
            this.tank = true;
        } else {
            this.tank = false;
        }
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attacDamage) {
        this.attackDamage = attacDamage;
    }

    /**
     *  use the ability for one of the 4 minions
     * @param minion
     */
    public void useAbility(final Minion minion) {
        switch (this.getName()) {
            case "Disciple":
                minion.setHealth(minion.getHealth() + 2);
                break;
            case "The Cursed One":
                final int aux = minion.getHealth();
                minion.setHealth(minion.getAttackDamage());
                minion.setAttackDamage(aux);
                break;
            case "The Ripper":
                minion.setAttackDamage(minion.getAttackDamage() - 2);
                if (minion.getAttackDamage() < 0) {
                    minion.setAttackDamage(0);
                }
                break;
            case "Miraj":
                final int health = minion.getHealth();
                minion.setHealth(this.getHealth());
                this.setHealth(health);
                break;
            default:
                break;
        }
    }

    public Minion(final CardInput cardInput) {
        super(cardInput);
        this.attackDamage = cardInput.getAttackDamage();
        setPlacingRow();
        setTank();
    }

    public Minion(final Minion minion) {
        super(minion);
        this.attackDamage = minion.getAttackDamage();
        setTank();
        setPlacingRow();
        this.setHasAttacked(minion.getHasAttacked());
        this.setFrozen(minion.getFrozen());
    }
}
