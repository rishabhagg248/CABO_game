import java.util.ArrayList;
import java.util.Collections;
import processing.core.PApplet;

public class Button {

  private boolean active;
  private int height;
  private String label;
  protected static processing.core.PApplet processing;
  private int width;
  private int x;
  private int y;

  public Button(String label, int x, int y, int width, int height) {

    if (processing==null) {
      throw new IllegalStateException();
    }
    this.height = height;
    this.width = width;
    this.label = label;
    this.x = x;
    this.y = y;

  }

  public void draw() {

    if (active) {
      if (this.isMouseOver()) {
        processing.fill(150);
      } else {
        processing.fill(200);
      }
    } else {
      processing.fill(255, 51, 51);
    }

    processing.rect(x, y, width, height, 5);
    processing.fill(0);
    processing.textSize(14);
    processing.textAlign(PApplet.CENTER, PApplet.CENTER);
    processing.text(label, (x + (width / 2)), (y + (height / 2)));

  }

  public String getLabel() {
    return this.label;
  }

  public boolean isActive() {
    return active;
  }

  public boolean isMouseOver() {
    int left = x;
    int top = y;
    int right = x + width;
    int bottom = y + height;
    int mouseX = processing.mouseX;
    int mouseY = processing.mouseY;

    // System.out.println(mouseX + " " + mouseY);

    if (mouseX >= left && mouseX <= right && mouseY <= bottom && mouseY >= top) {
      return true;
    } else {
      return false;
    }
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public static void setProcessing(processing.core.PApplet processing) {
    Button.processing = processing;
  }

}
