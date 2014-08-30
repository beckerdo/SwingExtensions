package swingextensions.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;

/**
 * Provides a vertical graphical view of a percentage.
 * Handles only one value, a percentile in the range of 0.0 to 1.0.
 * <p>
 * Optionally, the control allows an array of thresholds that choose
 * a set of colors to draw the bar. A set of N thresholds requires
 * N+1 color sets. For example, 2 thresholds requires 3 color sets.
 */
public class VerticalPercentileBar extends JComponent {
	private static final long serialVersionUID = 1L;
	
    private float percentile;

    public static float [] thresholds = new float[] { 0.5f, 0.8f };
    
	private static final int START = 218;
    private static final int MID = 255;
    private static final int END = 130;
    private static final Color[] RED_COLORS = new Color[] {
            new Color(START, 0, 0),
            new Color(MID, 0, 0),
            new Color(END, 0, 0) };
    private static final Color[] YELLOW_COLORS = new Color[] {
            new Color(START, START, 0),
            new Color(MID, MID, 0),
            new Color(END, END, 0) };
    private static final Color[] GREEN_COLORS = new Color[] {
            new Color(0, START, 0),
            new Color(0, MID, 0),
            new Color(0, END, 0) };
    private static final Color [][] colorSets = 
    		new Color[][] { RED_COLORS, YELLOW_COLORS, GREEN_COLORS };

    /** Controls the fill level from 0.0 to 1.0. */
    public void setPercentile(float percentile) {
        float oldStrength = this.percentile;
        this.percentile = percentile;
        firePropertyChange("strength", oldStrength, percentile);
        repaint();
    }
    
    /** Returns the current fill level from 0.0 to 1.0. */
    public float getPercentile() {
        return percentile;
    }

    /** Draw a fancy gradient color bar from one of the colorSets. */
    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        Insets insets = getInsets();
        Graphics2D g2 = (Graphics2D)g.create();
        g2.translate(insets.left, insets.top);
        float strength = getPercentile();
        int w = getWidth() - insets.left - insets.right;
        int h = getHeight() - insets.top - insets.bottom;
        int barHeight = (int)(strength * (float)h);

        // Select a basic color for the bar based on the numbre of thresholds.
        Color [] colors = colorSets[ colorSets.length - 1 ];
        for ( int i = 0; i < thresholds.length; i++ ) {
        	float threshold = thresholds[ i ];
        	if ( strength < threshold )
        		colors = colorSets[ i ];
        }
        
        // Draw a number of gradients equal to the number of colors in the set.
        float sliceWidth = w / (colors.length - 1);
        for( int i = 0; i < colors.length - 1; i++ ) {
            g2.setPaint( new GradientPaint( sliceWidth, 0, colors[ i ],  w, 0, colors[ i + 1 ]));
            g2.fillRect( (int) sliceWidth * i,   h - barHeight,  (int) sliceWidth * (i + 1 ), barHeight);        	
        }
        g2.dispose();
    }
}