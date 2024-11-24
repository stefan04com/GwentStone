package org.poo.rungame;

import org.poo.cards.Hero;
import org.poo.cards.Minion;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

public final class Player {
    private Hero playerHero;
    private ArrayList<Minion> hand = new ArrayList<Minion>();
    private ArrayList<Minion> deck = new ArrayList<Minion>();
    private int mana;
    private final int maxMana = 10;

    public Hero getPlayerHero() {
        return playerHero;
    }

    public ArrayList<Minion> getDeck() {
        return deck;
    }

    public ArrayList<Minion> getHand() {
        return hand;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * increase mana for each round
     * @param mana
     */
    public void increaseMana(final int mana) {
        if (mana > maxMana) {
            setMana(getMana() + maxMana);
        } else {
            setMana(getMana() + mana);
        }
    }

    public Player(final CardInput cardInput, final ArrayList<Minion> deck) {
        setMana(1);
        this.playerHero = new Hero(cardInput);
        Minion firstCardHand = new Minion(deck.get(0));
        this.hand.add(firstCardHand);
        for (int k = 1; k < deck.size(); k++) {
            Minion cardDeck = new Minion(deck.get(k));
            this.deck.add(cardDeck);
        }
    }
}
