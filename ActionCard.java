import processing.core.*;
import java.util.*;
import java.io.File;

public class ActionCard extends BaseCard {

  private String actionType;

  public ActionCard(int rank, String suit, String actionType) {

    super(rank, suit);
    this.actionType = actionType;

  }
  
  public String getActionType() {
    
    return this.actionType;
    
  }

}
