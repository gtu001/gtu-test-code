package gtu.swing.util;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalToolTipUI;
import javax.swing.text.StyleContext;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MultiLineToolTip extends JToolTip {

    int cMaxWidth = -1;
    int cMaxHeight = -1;
    
    public MultiLineToolTip() {
        setUI(new MultiLineToolTipUI());
    }

    public MultiLineToolTip(int cMaxWidth, int cMaxHeight) {
        this.cMaxWidth = cMaxWidth;
        this.cMaxHeight = cMaxHeight;
        setUI(new MultiLineToolTipUI());
    }

    private class MultiLineToolTipUI extends MetalToolTipUI {
        private String[] tooltipLines;

        MultiLineToolTipUI() {
            tooltipLines = new String[0];
        }

        public Dimension getPreferredSize(final JComponent c) {
            final FontMetrics metrics = StyleContext.getDefaultStyleContext().getFontMetrics(c.getFont());
            String tipText = ((JToolTip) c).getTipText();
            if (tipText == null) {
                tipText = "";
            }
            final BufferedReader br = new BufferedReader(new StringReader(tipText));
            String line;
            int maxWidth = 0;
            final List<String> list = new ArrayList<String>();
            try {
                while ((line = br.readLine()) != null) {
                    final int width = SwingUtilities.computeStringWidth(metrics, line);
                    maxWidth = (maxWidth < width) ? width : maxWidth;
                    list.add(line);
                }
            } catch (IOException e) {
                // Log.get(this).logp(LogLevel.ERROR, getClass().getName(),
                // "getPreferredSize",
                // "Unexpected exception from a StringReader", e);
            }

            tooltipLines = list.toArray(new String[list.size()]);

            int width = maxWidth + 6;
            int height = (metrics.getHeight() * tooltipLines.length) + 4;
            if (cMaxWidth != -1 && (cMaxWidth < width)) {
                width = cMaxWidth;
            }
            if (cMaxHeight != -1 && (cMaxHeight < height)) {
                height = cMaxHeight;
            }
            return new Dimension(width, height);
        }

        public void paint(final Graphics g, final JComponent c) {
            final FontMetrics metrics = StyleContext.getDefaultStyleContext().getFontMetrics(c.getFont());
            final Dimension size = c.getSize();
            g.setColor(c.getBackground());
            g.fillRect(0, 0, size.width, size.height);
            g.setColor(c.getForeground());
            for (int i = 0; i < tooltipLines.length; i++) {
                g.drawString(tooltipLines[i], 3, (metrics.getHeight()) * (i + 1));
            }
        }
    }
}