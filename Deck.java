import java.util.ArrayList;
import java.util.Collections;
import processing.core.PApplet;

/**
 * The Deck class represents a deck of playing cards for the game Cabo. It manages a collection of
 * cards, including shuffling, drawing, and adding cards.
 */
public class Deck {

  // TODO: add everything else
  protected ArrayList<BaseCard> cardList;
  protected static processing.core.PApplet processing;

  public Deck(ArrayList<BaseCard> deck) {

    if (processing==null) {
      throw new IllegalStateException();
    }
    this.cardList = deck;

  }

  public void addCard(BaseCard card) {

    cardList.add(card);

  }

  public void draw(int x, int y, boolean isDiscard) {

    if (isEmpty()) {
      
      processing.stroke(0);
      processing.fill(0);
      processing.rect(x, y, 50, 70, 7);
      processing.fill(255);
      processing.textSize(12);
      processing.textAlign(processing.CENTER, processing.CENTER);
      processing.text("Empty", x + 25, y + 35);
      
    } else {
      
      cardList.get(cardList.size()-1).setFaceUp(isDiscard);
      cardList.get(cardList.size()-1).draw(x, y);
      
    }
    
  }

  public BaseCard drawCard() {

    if (cardList == null) {

      return null;

    } else {
      
      return cardList.remove(cardList.size()-1);

    }

  }

  public boolean isEmpty() {

    return cardList.isEmpty();

  }

  public static void setProcessing(processing.core.PApplet processing) {

    Deck.processing = processing;

  }

  public int size() {

    return cardList.size();

  }

  /**
   * Sets up the deck with CABO cards, including action cards. Initializes the deck with all
   * necessary cards and shuffles them.
   *
   * @return the completed ArrayList of CABO cards
   */
  public static ArrayList<BaseCard> createDeck() {
    ArrayList<BaseCard> cardList = new ArrayList<>();

    // Define the suits
    String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};

    // Cards from 1 (Ace) to 13 (King)
    for (int rank = 1; rank <= 13; ++rank) {
      // Loop through each suit
      for (String suit : suits) {
        if (rank >= 7 && rank <= 12) {
          // Special action cards
          String actionType = "";
          if (rank == 7 || rank == 8) {
            actionType = "peek";
          } else if (rank == 9 || rank == 10) {
            actionType = "spy";
          } else {
            actionType = "switch";
          }
          cardList.add(new ActionCard(rank, suit, actionType)); // Add ActionCard to deck
        } else {
          cardList.add(new BaseCard(rank, suit)); // Add NumberCard to deck
        }
      }
    }
    Collections.shuffle(cardList);
    return cardList;
  }

}
