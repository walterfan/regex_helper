package com.github.walterfan.swing;

import java.awt.*;

public class StatusBar extends Component {
    private String text = "";

    public StatusBar() {
        setForeground(Color.BLACK);
        setBackground(Color.LIGHT_GRAY);
    }

    public void setText(String text) {
        if (text != null) {
            this.text = text;
            repaint();
        }
    }

    public String getText() {
        return text;
    }

    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        int width = fm.stringWidth(text) + 4;
        int height = fm.getMaxAscent() + fm.getMaxDescent() + 2;
        return new Dimension(width, height);
    }

    public void paint(Graphics g) {
        FontMetrics fm = getFontMetrics(getFont());
        Dimension d = getSize();
        int baseline = d.height - fm.getMaxDescent() - 1;

        g.setColor(getBackground());
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(getForeground());
        g.drawString(text, 2, baseline);

        g.setColor(Color.black);
        g.drawRect(0, 0, d.width - 1, d.height - 1);
    }
}