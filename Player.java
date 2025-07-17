import processing.core.*;
import java.util.*;
import java.io.File;

public class Player {
  
  private Hand hand;
  private boolean isComputer;
  private int label;
  private String name;
  
  public Player(String name, int label, boolean isComputer) {
    
    this.label = label;
    this.name = name;
    this.isComputer = isComputer;
    this.hand = new Hand();
    
  }
  
  public void addCardToHand(BaseCard card) {
    hand.addCard(card);
  }
  
  public Hand getHand() {
    return this.hand;
  }
  
  public int getLabel() {
    return this.label;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean isComputer() {
    return this.isComputer;
  }
  
}
