import java.util.ArrayList;
import processing.core.PApplet;

/**
 * The CaboGame class implements the main game logic for the card game CABO. It manages the deck,
 * discard pile, players, game state, and user interactions.
 */
public class CaboGame extends processing.core.PApplet {

  private Button[] buttons;
  private int caboPlayer;
  private int currentPlayer;
  private Deck deck;
  private Deck discard;
  private BaseCard drawnCard;
  private boolean gameOver;
  private Player[] players;
  private int selectedCardFromCurrentPlayer;

  /**
   * Enum representing the different action states in the game (e.g., swapping cards, peeking,
   * spying, switching).
   * 
   * This allows us to easily restrict the possible values of a variable.
   */
  private enum ActionState {
    NONE, SWAPPING, PEEKING, SPYING, SWITCHING
  }

  private ActionState actionState = ActionState.NONE;

  // provided data fields for tracking the players' moves through the game
  private ArrayList<String> gameMessages = new ArrayList<>();

  /**
   * Launch the game window; PROVIDED. Note: the argument to PApplet.main() must match the name of
   * this class, or it won't run!
   * 
   * @param args unused
   */
  public static void main(String[] args) {
    PApplet.main("CaboGame");
  }

  /**
   * Sets up the initial window size for the game; PROVIDED.
   */
  @Override
  public void settings() {
    size(1000, 800);
  }

  /**
   * Sets up the game environment, including the font, game state, and game elements.
   */
  @Override
  public void setup() {

    textFont(createFont("Arial", 16));

    // TODO: setProcessing for the classes which require it
    BaseCard.setProcessing(this);
    Deck.setProcessing(this);
    Button.setProcessing(this);

    deckCheck();

    // TODO: set up deck and discard pile
    deck = new Deck(Deck.createDeck());
    discard = new Deck(new ArrayList<BaseCard>());
    drawnCard = null;

    // TODO: set up players array and deal their cards
    players = new Player[4];
    players[0] = new Player("Cyntra", 0, false);
    players[1] = new AIPlayer("Avalon", 1, true);
    players[2] = new AIPlayer("Balthor", 2, true);
    players[3] = new AIPlayer("Ophira", 3, true);
    currentPlayer = 0;
    caboPlayer = -1;
    selectedCardFromCurrentPlayer = -1;
    setGameStatus("Turn for " + players[currentPlayer].getName());
    deal();

    // TODO: set up buttons and update their states for the beginning of the game
    buttons = new Button[5];
    buttons[0] = new Button("Draw from Deck", 50, 700, 150, 40);
    buttons[1] = new Button("Swap a Card", 220, 700, 150, 40);
    buttons[2] = new Button("Declare Cabo", 390, 700, 150, 40);
    buttons[3] = new Button("Use Action", 390 + 170, 700, 150, 40);
    buttons[4] = new Button("End Turn", 390 + 170 + 170, 700, 150, 40);

    updateButtonStates();

    // TODO: update the gameMessages log: "Turn for "+currentPlayer.name

  }

  private void deal() {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        BaseCard card = deck.drawCard();
        if (j == 0) {
          if (i < 2) {
            card.setFaceUp(true);
          }
        }
        players[j].addCardToHand(card);
      }
    }
  }

  /**
   * Console-only output for verifying the setup of the card objects and the deck containing them
   */
  public void deckCheck() {

    ArrayList<BaseCard> deck = Deck.createDeck();

    int cardCounter = 0;
    int peekCounter = 0;
    int spyCounter = 0;
    int switchCounter = 0;
    int clubCounter = 0;
    int diamondCounter = 0;
    int heartCounter = 0;
    int spadeCounter = 0;
    boolean kod = false;

    for (int i = 0; i < 52; i++) {

      BaseCard card = deck.get(i);

      cardCounter++;

      if (card.toString().equals("Diamonds 13")) {
        if (card.getRank() == -1) {
          kod = true;
        }
      }

      if (card.getRank() >= 7 && card.getRank() <= 12) {
        if (((ActionCard) card).getActionType().equals("peek")) {
          peekCounter++;
        } else if (((ActionCard) card).getActionType().equals("spy")) {
          spyCounter++;
        } else if (((ActionCard) card).getActionType().equals("switch")) {
          switchCounter++;
        }
      }

      if (card.toString().startsWith("Clubs")) {
        clubCounter++;
      } else if (card.toString().startsWith("Diamonds")) {
        diamondCounter++;
      } else if (card.toString().startsWith("Hearts")) {
        heartCounter++;
      } else if (card.toString().startsWith("Spades")) {
        spadeCounter++;
      }

    }

    // TODO: verify that there are 52 cards in the deck
    if (cardCounter != 52) {
      System.out.println("Deck size is 52: false");
    } else {
      System.out.println("Deck size is 52: true");
    }

    // TODO: verify that there are 8 of each type of ActionCard
    if (spyCounter == 8 && peekCounter == 8 && switchCounter == 8) {
      System.out.println("Found correct numbers of action cards: true");
    } else {
      System.out.println("Found correct numbers of action cards: false");
    }

    // TODO: verify that there are 13 of each suit
    if (clubCounter == 13 && diamondCounter == 13 && heartCounter == 13 && spadeCounter == 13) {
      System.out.println("Found correct numbers of each suit: true");
    } else {
      System.out.println("Found correct numbers of each suit: false");
    }

    // TODO: verify that the king of diamonds' getRank() returns -1
    if (kod) {
      System.out.println("King of diamonds found!");
    }

  }

  /**
   * Updates the state of the action buttons based on the current game state. Activates or
   * deactivates buttons depending on whether it's the start of a player's turn, a card has been
   * drawn, or the player is an AI.
   */
  public void updateButtonStates() {
    // TODO: if the current player is a computer, deactivate all buttons
    if (players[currentPlayer].isComputer()) {

      for (Button button : buttons) {
        button.setActive(false);
      }

    } else {
      // TODO: otherwise, if no card has been drawn, activate accordingly (see writeup)
      if (drawnCard == null) {

        buttons[0].setActive(true);
        buttons[1].setActive(false);
        buttons[2].setActive(true);
        buttons[3].setActive(false);
        buttons[4].setActive(false);

      }
      // TODO: otherwise, if a card has been drawn, activate accordingly (see writeup)
      else {
        buttons[0].setActive(false);
        buttons[1].setActive(true);
        buttons[2].setActive(false);
        buttons[4].setActive(true);

        if (drawnCard.getRank() >= 7 && drawnCard.getRank() <= 12) {
          buttons[3].setActive(true);
          buttons[3].setLabel(((ActionCard) drawnCard).getActionType().toUpperCase());
        } else {
          buttons[3].setActive(false);
        }
      }
    }

  }

  /**
   * Renders the graphical user interface; also handles some game logic for the computer players.
   */
  @Override
  public void draw() {
    background(0, 128, 0);

    // TODO: draw the deck and discard pile
    textSize(16);
    fill(255);
    text("Deck:", 520, 60);
    text("Discard Pile:", 644, 60);
    deck.draw(500, 80, false);
    discard.draw(600, 80, true);

    // TODO: draw the players' hands
    for (int i = 0; i < 4; i++) {
      text(players[i].getName(), 50, 45 + (150 * i));
      players[i].getHand().draw(60 + (150 * i));
    }

    // TODO: draw the buttons
    for (Button button : buttons) {
      button.draw();
    }

    // TODO: show the drawn card, if there is one
    if (drawnCard != null) {
      drawnCard.setFaceUp(true);
      drawnCard.draw(500, 500);
    }

    // Display game messages with different colors based on the content
    int y = 200; // Starting y-position for messages
    for (String message : gameMessages) {
      textSize(16);
      if (message.contains("CABO")) {
        fill(255, 128, 0);
      } else if (message.contains("switched")) {
        fill(255, 204, 153);
      } else if (message.contains("spied")) {
        fill(255, 229, 204);
      } else {
        fill(255);
      }
      text(message, width - 300, y); // Adjust x-position as needed
      y += 20; // Spacing between messages
    }

    // TODO: if the game is over, display the game over status
    if (gameOver) {
      displayGameOver();
    }

    // TODO: handle the computer players' turns
    if (players[currentPlayer].isComputer() && !gameOver) {
      performAITurn();
    }

  }

  /**
   * Handles mouse press events during the game. It manages user interactions with buttons (that is,
   * drawing a card, declaring CABO, swapping cards, using action cards) and updates the game state
   * accordingly.
   */
  @Override
  public void mousePressed() {

    // handle additional action states (TODO: complete these methods)
    switch (actionState) {
      case SWAPPING -> handleCardSwap();
      case PEEKING -> handlePeek();
      case SPYING -> handleSpy();
      case SWITCHING -> handleSwitch();
      default -> {
        /* No action to be taken */ }
    }

    // TODO: if game is over or it's the computer's turn, do nothing
    if (!gameOver && !players[currentPlayer].isComputer()) {
      // TODO: handle button clicks
      for (int i = 0; i < 5; i++) {

        if (buttons[i].isMouseOver()) {

          switch (i) {

            case 0: {
              drawFromDeck();
              break;
            }
            case 1: {
              handleCardSwap();
              break;
            }
            case 2: {
              declareCabo();
              break;
            }
            case 3: {
              String action = buttons[3].getLabel();
              if (action.toLowerCase().equals("swap")) {
                actionState = ActionState.SWAPPING;
              } else if (action.toLowerCase().equals("peek")) {
                actionState = ActionState.PEEKING;
              } else if (action.toLowerCase().equals("spy")) {
                actionState = ActionState.SPYING;
              } else if (action.toLowerCase().equals("switch")) {
                actionState = ActionState.SWITCHING;
              }
              break;
            }
            case 4: {
              nextTurn();
              break;
            }
            default: {
              /* No action to be taken */ }

          }

        }

      }

    }

  }

  ///////////////////////////////////// BUTTON CLICK HANDLERS /////////////////////////////////////

  /**
   * Handles the action of drawing a card from the deck. If the deck is empty, the game ends.
   * Otherwise, the drawn card is displayed in the middle of the table. The game status and button
   * states are updated accordingly.
   */
  public void drawFromDeck() {

    // TODO: if the deck is empty, game over
    if (deck.isEmpty()) {
      gameOver = true;
    } else {
      // TODO: otherwise, draw the next card from the deck
      drawnCard = deck.drawCard();
      // TODO: update the gameMessages log: player.name+" drew a card."
      setGameStatus(players[currentPlayer].getName() + " drew a card.");
      // TODO: update the button states
      updateButtonStates();
    }

  }

  /**
   * Handles the action of declaring CABO. Updates the game status to show that the player has
   * declared CABO.
   */
  public void declareCabo() {

    // TODO: update the gameMessages log: player.name+" declares CABO!"
    setGameStatus(players[currentPlayer].getName() + " declares CABO!");
    // TODO: set the caboPlayer to the current player's index
    caboPlayer = currentPlayer;
    // TODO: end this player's turn
    nextTurn();

  }

  ///////////////////////////////////// ACTION STATE HANDLERS /////////////////////////////////////

  /**
   * This method runs when the human player has chosen to SWAP the drawn card with one from their
   * hand. Detect if the mouse is over a card from the currentPlayer's hand and, if it is, swap the
   * drawn card with that card.
   * 
   * If the mouse is not currently over a card from the currentPlayer's hand, this method does
   * nothing.
   */
  public void handleCardSwap() {

    actionState = ActionState.SWAPPING;
    setGameStatus("Click a card in your hand to swap it with the drawn card.");

    // TODO: find a card from the current player's hand that the mouse is currently over
    Hand hand = players[currentPlayer].getHand();

    int i = -1;
    i = hand.indexOfMouseOver();
    if (i != -1) {
      // TODO: swap that card with the drawnCard
      BaseCard card = hand.cardList.get(i);
      hand.swap(drawnCard, i);
      // TODO: add the swapped-out card from the player's hand to the discard pile
      discard.addCard(card);
      // TODO: update the gameMessages log: "Swapped the drawn card with card "+(index+1)+" in the
      // hand."
      setGameStatus("Swapped the drawn card with card " + (i + 1) + " in the hand.");

      // TODO: set the drawnCard to null and the actionState to NONE
      drawnCard = null;
      actionState = ActionState.NONE;

      // TODO: set all buttons except End Turn to inactive
      buttons[0].setActive(false);
      buttons[1].setActive(false);
      buttons[2].setActive(false);
      buttons[3].setActive(false);
      buttons[4].setActive(true);

      // TODO: uncomment this code to erase all knowledge of the card at that index from the AI
      // (you may need to adjust its indentation and/or change some variables)
      AIPlayer AI;
      for (int j = 1; j < players.length; ++j) {
        AI = (AIPlayer) players[j];
        AI.setCardKnowledge(players[currentPlayer].getLabel(), i, false);
      }
    }

  }

  /**
   * Handles the action of peeking at one of your cards. The player selects a card from their own
   * hand, which is then revealed (set face-up).
   * 
   * If the mouse is not currently over a card from the currentPlayer's hand, this method does
   * nothing.
   */
  public void handlePeek() {

    setGameStatus("Click a card in your hand to peek at it.");

    Hand hand = players[currentPlayer].getHand();

    // TODO: find a card from the current player's hand that the mouse is currently over
    for (int i = 0; i < 4; i++) {

      BaseCard card = hand.cardList.get(i);

      if (card.isMouseOver()) {

        // TODO: set that card to be face-up
        card.setFaceUp(true);
        // TODO: update the gameMessages log: "Revealed card "+(index+1)+" in the hand."
        setGameStatus("Revealed card " + (i + 1) + " in the hand.");

        // TODO: add the drawnCard to the discard, set drawnCard to null and actionState to NONE
        discard.addCard(drawnCard);
        actionState = ActionState.NONE;

        // TODO: set all buttons except End Turn to inactive
        buttons[0].setActive(false);
        buttons[1].setActive(false);
        buttons[2].setActive(false);
        buttons[3].setActive(false);
        buttons[4].setActive(true);

      }
    }
  }

  /**
   * Handles the spy action, allowing the current player to reveal one of another player's cards.
   * The current player selects a card from another player's hand, which is temporarily revealed.
   * 
   * If the mouse is not currently over a card from another player's hand, this method does nothing.
   */
  public void handleSpy() {

    setGameStatus("Click a card in another player's hand to spy on it.");

    // TODO: find a card from any player's hand that the mouse is currently over
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {

        if (i != currentPlayer) {

          if (players[i].getHand().cardList.get(j).isMouseOver()) {

            // TODO: if it is not one of their own cards, set it to be face-up
            players[i].getHand().cardList.get(j).setFaceUp(true);
            // TODO: update the gameMessages log: "Spied on "+player.name+"'s card.";
            setGameStatus("Spied on " + players[i].getName() + "'s card.");

            // TODO: add the drawnCard to the discard, set drawnCard to null and actionState to NONE
            discard.addCard(drawnCard);
            actionState = ActionState.NONE;

            // TODO: set all buttons except End Turn to inactive
            buttons[0].setActive(false);
            buttons[1].setActive(false);
            buttons[2].setActive(false);
            buttons[3].setActive(false);
            buttons[4].setActive(true);

          }
        }
      }
    }
  }


  /**
   * Handles the switch action, allowing the current player to switch one of their cards with a card
   * from another player's hand.
   * 
   * This action is performed in 2 steps, in this order: (1) select a card from the current player's
   * hand (2) select a card from another player's hand
   * 
   * If the mouse is not currently over a card, this method does nothing.
   */
  public void handleSwitch() {

    setGameStatus(
        "Click a card from your hand, then a card from another Kingdom's hand to switch.");

    // TODO: add CaboGame instance variable to store the index of the card from the currentPlayer's
    // hand

    // TODO: check if the player has selected a card from their own hand yet
    if (selectedCardFromCurrentPlayer == -1) {

      // TODO: if they haven't: determine which card in their own hand the mouse is over & store it
      // and do nothing else
      for (int i = 0; i < 4; i++) {

        if (players[currentPlayer].getHand().cardList.get(i).isMouseOver()) {
          selectedCardFromCurrentPlayer = i;
        }

      }
    }
    // TODO: if they have selected a card from their own hand already:
    else {

      // TODO: find a card from any OTHER player's hand that the mouse is currently over
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {

          if (i != currentPlayer) {

            if (players[i].getHand().cardList.get(j).isMouseOver()) {

              // TODO: swap the selected card with the card from the currentPlayer's hand
              players[currentPlayer].getHand().switchCards(selectedCardFromCurrentPlayer,
                  players[i].getHand(), j);
              // TODO: update the gameMessages log: "Switched a card with "+player.name
              setGameStatus("Switched a card with " + players[i].getName());

              // TODO: add the drawnCard to the discard, set drawnCard to null and actionState to
              // NONE
              discard.addCard(drawnCard);
              actionState = ActionState.NONE;

              // TODO: set all buttons except End Turn to inactive
              buttons[0].setActive(false);
              buttons[1].setActive(false);
              buttons[2].setActive(false);
              buttons[3].setActive(false);
              buttons[4].setActive(true);

              // TODO: uncomment this code to update the knowledge of the swapped card for the other
              // player
              // (you may need to adjust its indentation and variables)
              boolean knowledge = ((AIPlayer) players[i]).getCardKnowledge(
                  players[currentPlayer].getLabel(), selectedCardFromCurrentPlayer);
              ((AIPlayer) players[i]).setCardKnowledge(players[currentPlayer].getLabel(),
                  selectedCardFromCurrentPlayer,
                  ((AIPlayer) players[i]).getCardKnowledge(players[i].getLabel(), j));
              ((AIPlayer) players[i]).setCardKnowledge(players[i].getLabel(), j, knowledge);

              // TODO: reset the selected card instance variable to -1
              selectedCardFromCurrentPlayer = -1;

            }
          }
        }
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Advances the game to the next player's turn. Hides all players' cards, updates the current
   * player, checks for game-over conditions, resets action states, and updates the UI button states
   * for the new player's turn.
   */
  public void nextTurn() {

    // TODO: hide all players' cards
    for (int i = 0; i < 4; i++) {
      Hand hand = players[i].getHand();
      for (int j = 0; j < 4; j++) {
        hand.cardList.get(j).setFaceUp(false);
      }
    }

    // TODO: if there is still an active drawnCard, discard it and set drawnCard to null
    if (drawnCard != null) {

      discard.addCard(drawnCard);
      drawnCard = null;

    }

    // TODO: advance the current player to the next one in the list
    currentPlayer++;
    if (currentPlayer >= 4) {
      currentPlayer = 0;
    }

    // TODO: check if the new player is the one who declared CABO (and end the game if so)
    if (caboPlayer == currentPlayer) {
      gameOver = true;
    }

    // TODO: update the gameMessages log: "Turn for "+player.name
    setGameStatus("Turn for " + players[currentPlayer].getName());

    // TODO: reset the action state to NONE
    actionState = ActionState.NONE;

    // TODO: update the button states
    updateButtonStates();

  }

  /**
   * Displays the game-over screen and reveals all players' cards. The method calculates each
   * player's score, identifies the winner, and displays a message about the game's result,
   * including cases where there is no winner.
   * 
   * We've provided the code for the GUI parts, but the logic behind this method is still TODO
   */
  public void displayGameOver() {

    // Create a dimmed background overlay
    fill(0, 0, 0, 200);
    rect(0, 0, width, height);
    fill(255);
    textSize(32);
    textAlign(CENTER, CENTER);
    text("Game Over!", (float) width / 2, (float) height / 2 - 150);

    int[] playerScores = new int[4];
    int counter = 0;
    int yPosition = height / 2 - 100;
    textSize(24);

    // TODO: reveal all players' cards
    // TODO: calculate and display each player's score
    for (Player player : players) {
      Hand hand = player.getHand();
      for (BaseCard card : hand.cardList) {
        card.setFaceUp(true);
      }
      playerScores[counter] = hand.calcHand();
      text(player.getName() + "'s score: " + hand.calcHand(), (float) width / 2, yPosition);
      counter++;
      yPosition += 30;
    }

    // TODO: check if there is a tie or a specific CABO winner (lowest score wins)
    String winner = null;
    int winnerIndex = 0;
    int winnerScore = 0;
    boolean isTie = false;
    for (int i = 0; i < 3; i++) {
      if (playerScores[i + 1] > playerScores[i]) {
        winnerIndex = i + 1;
        winnerScore = players[i + 1].getHand().calcHand();
      } else if (playerScores[i + 1] == winnerScore) {
        isTie = true;
      }
    }

    if (isTie) {
      // TODO: display this message if there is no winner
      text("No Winner. The war starts.", (float) width / 2, yPosition + 30);
    } else {
      // TODO: display this message if there is a winner
      winner = players[winnerIndex].getName();
      text("Winner: " + winner, (float) width / 2, yPosition + 30);
    }

  }

  /**
   * PROVIDED: Sets the current game status message and updates the message log. If the message log
   * exceeds a maximum number of messages, the oldest message is removed.
   *
   * @param message the message to set as the current game status.
   */
  private void setGameStatus(String message) {
    gameMessages.add(message);
    int MAX_MESSAGES = 15;
    if (gameMessages.size() > MAX_MESSAGES) {
      gameMessages.remove(0); // Remove the oldest message
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  // The 2 methods below this line are PROVIDED in their entirety to run the AIPlayer interactions
  // with the CABO game. Uncomment them once you are ready to add AIPlayer actions to your game!
  /////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Performs the AI player's turn by drawing a card and deciding whether to swap, discard, or use
   * an action card. If the AI player draws a card that is better than their highest card, they swap
   * it; otherwise, they discard it. If the drawn card is an action card, the AI player performs the
   * corresponding action. If the AI player's hand value is low enough, they may declare CABO.
   */

  private void performAITurn() {
    AIPlayer aiPlayer = (AIPlayer) players[currentPlayer];
    String gameStatus = aiPlayer.getName() + " is taking their turn.";
    setGameStatus(gameStatus);

    // Draw a card from the deck
    drawnCard = deck.drawCard();
    if (drawnCard == null) {
      gameOver = true;
      return;
    }

    gameStatus = aiPlayer.getName() + " drew a card.";
    setGameStatus(gameStatus);

    // Determine if AI should swap or discard
    int drawnCardValue = drawnCard.getRank();
    int highestCardIndex = aiPlayer.getHighestIndex();
    if (highestCardIndex == -1) {
      highestCardIndex = 0;
    }
    int highestCardValue = aiPlayer.getHand().getRankAtIndex(highestCardIndex);

    // Swap if the drawn card has a lower value than the highest card in hand
    if (drawnCardValue < highestCardValue) {
      BaseCard cardInHand = aiPlayer.getHand().swap(drawnCard, highestCardIndex);
      aiPlayer.setCardKnowledge(aiPlayer.getLabel(), highestCardIndex, true);
      discard.addCard(cardInHand);
      gameStatus = aiPlayer.getName() + " swapped the drawn card with card "
          + (highestCardIndex + 1) + " in their hand.";
      setGameStatus(gameStatus);
    } else if (drawnCard instanceof ActionCard) { // Use the action card
      String actionType = ((ActionCard) drawnCard).getActionType();
      gameStatus = aiPlayer.getName() + " uses an action card: " + actionType;
      setGameStatus(gameStatus);
      performAIAction(aiPlayer, actionType);
      discard.addCard(drawnCard);
    } else { // Discard the drawn card
      discard.addCard(drawnCard);
      gameStatus = aiPlayer.getName() + " discarded the drawn card: " + drawnCard;
      setGameStatus(gameStatus);
    }

    // AI may declare Cabo if hand value is low enough
    int handValue = aiPlayer.calcHandBlind();
    if (handValue <= random(13, 21) && caboPlayer == -1) {
      declareCabo();
    }

    // Prepare for the next turn
    drawnCard = null;
    nextTurn();
  }


  /**
   * Performs the specified action for the AI player based on the drawn action card. Actions include
   * peeking at their own cards, spying on another player's card, or switching cards with another
   * player.
   *
   * @param aiPlayer   the AI player performing the action.
   * @param actionType the type of action to perform ("peek", "spy", or "switch").
   */
  private void performAIAction(AIPlayer aiPlayer, String actionType) {
    Player otherPlayer = players[0];
    // Assuming Player 1 is the human player
    String gameStatus = "";
    switch (actionType) {
      case "peek" -> { // AI peeks at one of its own cards
        int unknownCardIndex = aiPlayer.getUnknownCardIndex();
        if (unknownCardIndex != -1) {
          aiPlayer.setCardKnowledge(aiPlayer.getLabel(), unknownCardIndex, true);
          gameStatus = aiPlayer.getName() + " peeked at their card " + (unknownCardIndex + 1);
          setGameStatus(gameStatus);
        }
      }
      case "spy" -> { // AI spies on one of the human player's cards
        int spyIndex = aiPlayer.getSpyIndex();
        if (spyIndex != -1) {
          aiPlayer.setCardKnowledge(0, spyIndex, true);
          gameStatus = aiPlayer.getName() + " spied on Player 1's card " + (spyIndex + 1);
          setGameStatus(gameStatus);
        }
      }
      case "switch" -> { // AI switches one of its cards with one of the human player's cards
        int aiCardIndex = aiPlayer.getHighestIndex();
        if (aiCardIndex == -1) {
          aiCardIndex = (int) random(aiPlayer.getHand().size());
        }
        int otherCardIndex = aiPlayer.getLowestIndex(otherPlayer);
        if (otherCardIndex == -1)
          otherCardIndex = (int) random(otherPlayer.getHand().size());
        // Swap the cards between AI and the human player
        aiPlayer.getHand().switchCards(aiCardIndex, otherPlayer.getHand(), otherCardIndex);
        boolean preCardKnowledge = aiPlayer.getCardKnowledge(aiPlayer.getLabel(), aiCardIndex);
        aiPlayer.setCardKnowledge(aiPlayer.getLabel(), aiCardIndex,
            aiPlayer.getCardKnowledge(0, otherCardIndex));
        aiPlayer.setCardKnowledge(0, otherCardIndex, preCardKnowledge);

        gameStatus = aiPlayer.getName() + " switched card " + (aiCardIndex + 1) + " with "
            + otherPlayer.getName() + "'s " + (otherCardIndex + 1) + ".";
        setGameStatus(gameStatus);
      }
    }
  }

}
