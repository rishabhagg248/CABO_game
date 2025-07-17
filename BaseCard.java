import processing.core.PApplet;
import java.util.*;
import java.io.File;

public class BaseCard {

  private static processing.core.PImage cardBack;
  private processing.core.PImage cardImage;
  protected boolean faceUp;
  private final int HEIGHT = 70;
  protected static processing.core.PApplet processing;
  protected int rank;
  protected String suit;
  private final int WIDTH = 50;
  private int x;
  private int y;

  public BaseCard(int rank, String suit) {

    if (processing == null) {
      throw new IllegalStateException(
          "Processing environment must be set before creating the card.");
    }

    this.rank = rank;
    this.suit = suit;
    this.faceUp = false;

    if (cardBack == null) {
      cardBack = processing.loadImage("images" + File.separator + "back.png");
    }

    String imagePath = "images" + File.separator + rank + "_of_" + suit.toLowerCase() + ".png";
    this.cardImage = processing.loadImage(imagePath);

  }

  public void draw(int xPosition, int yPosition) {

    this.x = xPosition;
    this.y = yPosition;

    if (faceUp) {

      processing.fill(255);
      processing.rect(xPosition, yPosition, WIDTH, HEIGHT);
      processing.image(cardImage, xPosition, yPosition, WIDTH, HEIGHT);

    } else {

      processing.fill(255);
      processing.rect(xPosition, yPosition, WIDTH, HEIGHT);
      processing.image(cardBack, xPosition, yPosition, WIDTH, HEIGHT);

    }

  }

  public int getRank() {

    if (this.suit.equals("Diamonds") && this.rank == 13) {
      return -1;
    } else {
      return this.rank;
    }

  }

  public boolean isMouseOver() {
    int left = this.x;
    int top = this.y;
    int right = this.x + this.WIDTH;
    int bottom = this.y + this.HEIGHT;

    if ((processing.mouseX >= left) && (processing.mouseX <= right) && (processing.mouseY <= bottom)
        && (processing.mouseY >= top)) {
      return true;
    } else {
      return false;
    }

  }

  public void setFaceUp(boolean faceUp) {

    this.faceUp = faceUp;

  }

  public static void setProcessing(processing.core.PApplet processing) {

    BaseCard.processing = processing;

  }

  public String toString() {

    return this.suit + " " + this.rank;

  }

}
