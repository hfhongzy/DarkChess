package labelComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GameLabel extends JComponent {
    ArrayList<TextBlock> textBlocks;
    int baseline;
    public GameLabel(TextBlock... textBlocks) {
        this.textBlocks = new ArrayList<>(Arrays.asList(textBlocks));
        autoResize();
    }
    void setText(TextBlock... textBlocks) {
        this.textBlocks = new ArrayList<>(Arrays.asList(textBlocks));
        autoResize();
        repaint();
    }
    void autoResize() {
        int width = 0, maxAscent = 0, maxDescent = 0;
        for (TextBlock textBlock : textBlocks) {
            FontMetrics fontMetrics = getFontMetrics(textBlock.font);
            maxAscent = Math.max(maxAscent, fontMetrics.getAscent());
            maxDescent = Math.max(maxDescent, fontMetrics.getDescent());
            width += fontMetrics.stringWidth(textBlock.text);
        }
        int height = maxAscent + maxDescent;
        baseline = maxAscent;
        setSize(width, height);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GREEN);
        g2.drawRect(0, 0, getWidth(), getHeight());
        int cur = 0;
        for (TextBlock textBlock : textBlocks) {
            g2.setFont(textBlock.font);
            g2.setColor(textBlock.color);
            g2.drawString(textBlock.text, cur, baseline);
            cur += getFontMetrics(textBlock.font).stringWidth(textBlock.text);
        }
    }
}
