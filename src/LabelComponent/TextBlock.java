package LabelComponent;

import java.awt.*;

public class TextBlock {
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final Font DEFAULT_FONT = new Font("宋体", Font.PLAIN, 12);
    String text = "";
    Font font = DEFAULT_FONT;
    Color color = DEFAULT_COLOR;
    public TextBlock() {}
    public TextBlock(String text) {
        this.text = text;
    }
    public TextBlock(String text, int font_size) {
        this.text = text;
        font = new Font("宋体", Font.PLAIN, font_size);
    }
    public TextBlock(String text, Color color) {
        this.text = text;
        this.color = color;
    }
    public TextBlock(String text, Color color, int font_size) {
        this.text = text;
        font = new Font("宋体", Font.PLAIN, font_size);
        this.color = color;
    }
    public TextBlock(String text, Color color, Font font) {
        this.text = text;
        this.color = color;
        this.font = font;
    }
}
