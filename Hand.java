import processing.core.*;
import java.util.*;
import java.io.File;

public class Hand extends Deck {

  private final int HAND_SIZE = 4;

  public Hand() {
    super(new ArrayList<>());

  }

  public void addCard(BaseCard card) {

    if (this.size() < HAND_SIZE) {
      super.addCard(card);
    } else {
      throw new IllegalStateException("The player cannot be dealt more cards");
    }

  }

  public int calcHand() {

    int total = 0;

    for (int i = 0; i < 4; i++) {
      total += this.cardList.get(i).getRank();
    }

    return total;

  }

  public void draw(int y) {

    for (int i = 0; i < 4; i++) {
      this.cardList.get(i).draw((50 + (60 * i)), y);
    }

  }

  public int getRankAtIndex(int index) {

    return this.cardList.get(index).getRank();

  }

  public int indexOfMouseOver() {

    for (int i = 0; i < 4; i++) {

      if (this.cardList.get(i).isMouseOver()) {
        return i;
      }

    }

    return -1;

  }
  
  public void setFaceUp(int index, boolean faceUp) {
    
    this.cardList.get(index).setFaceUp(faceUp);
    
  }
  
  public BaseCard swap(BaseCard newCard, int index) {
    
    BaseCard oldCard = super.cardList.get(index);
    this.cardList.set(index, newCard);
    return oldCard;
    
  }
  
  public void switchCards(int myIndex, Hand otherHand, int otherIndex) {
    
    BaseCard myCard = this.cardList.get(myIndex);
    BaseCard otherCard = otherHand.cardList.get(otherIndex);
    this.cardList.set(myIndex, otherCard);
    otherHand.cardList.set(otherIndex, myCard);
    
  }

}
