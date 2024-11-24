package org.poo.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

public class Card {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private int health;

    @JsonIgnore
    private boolean hasAttacked;

    @JsonIgnore
    private boolean frozen;

    /**
     * check if a card is frozen
     * @return
     */
    public boolean getFrozen() {
        return frozen;
    }

    /**
     * set the frozen status of a card
     * @param frozen
     */
    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * check if a card has attacked
     * @return
     */
    public boolean getHasAttacked() {
        return hasAttacked;
    }

    /**
     * set the status of a card if it has attacked
     * @param hasAttacked
     */
    public void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * reduce a minions health with attackers damage
     * @param minion
     */
    public void reduceHealth(final Minion minion) {
        setHealth(getHealth() - minion.getAttackDamage());
    }

    /**
     * @return
     */
    public int getMana() {
        return mana;
    }

    /**
     *
     * @param mana
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     *
     * @return
     */
    public int getHealth() {
        return health;
    }

    /**
     *
     * @param health
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     *
     * @param colors
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }


    public Card(final CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.health = cardInput.getHealth();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
    }

    public Card(final Card card) {
        this.mana = card.getMana();
        this.health = card.getHealth();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.name = card.getName();
    }
}
