package ButtonComponent;
//Deleted
import model.TeamColor;

import javax.swing.*;
import java.awt.*;

public class RedoButton extends JComponent {
  
  private static final Font CHESS_FONT = new Font("Rockwell", Font.BOLD, 15);
  protected String name;
  
  public RedoButton(int width, int height) {
    setSize(width, height);
    setVisible(true);
    name = "重做";
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    System.out.printf("Flag!\n");
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setStroke(new BasicStroke(1f));
    
    g2.setColor(new Color(0, 191, 243));
    g2.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
    g2.setColor(new Color(0, 0, 0));
    g2.setFont(CHESS_FONT);
    g2.drawString(name, getWidth() / 4, getHeight() * 2 / 3);
  }
}

