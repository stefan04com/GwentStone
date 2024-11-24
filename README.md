
# GwentStone - Project for POO

## Structure of the project
- src/
  - cards/ - contains the classes for the cards
    - Card - abstract class for the cards
    - Hero - class for the hero cards
    - Minion - class for the minion cards
  - rungame/ - contains the classes for the game
    - Game - class for the game
    - Player - class for the player 
    - Statistics - class for the statistics of the game
  - commands/
    - Actions - class for the actions of the game
    - ActionsHero - class for the actions of the hero cards
    - ActionsDebug - class for the debug actions
## Program Flow
  * Execution starts with startGames in the Game class, which sets up each game by initializing
players, shuffling decks, and preparing the game board. This setup ensures that the game state
is fresh and ready for players to start their turns.
  * The Game class manages the core game state, including player details, board configuration,
  and turn order. Each game’s progress is driven by the Actions class, where executeGame interprets
  player commands like "endPlayerTurn," "placeCard," and various attack actions, and calls the 
  corresponding method for each command.
  * Within executeGame, each command type directs a different game action. For instance, ending
a player’s turn increases mana and resets attack status, while placing a card checks board limits
and mana costs. When a card attacks, specific rules are enforced, such as targeting only enemy
minions and verifying tank protection, to maintain the game’s strategic flow.
  * If a hero is defeated, the game sets a "game over" flag, stopping further interactive actions
and logging the results. This ensures that once the game is over, only status-check commands
proceed, and game stats are ready for immediate display.