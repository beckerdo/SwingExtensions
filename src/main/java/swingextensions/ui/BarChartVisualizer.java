package swingextensions.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Provides a graphic bar chart visualization of multi-column data.
 * The number of columns is controlled by the column data array size.
 * The column data is in the range of 0 to max value;
 * The number of segments in each column is controlled by max segment.
 * each measurement. For example columns = { 0, 0, 4, 5, 10, 0 }
 */
public class BarChartVisualizer extends JPanel {
	private static final long serialVersionUID = 1L;

	private int [] columns;	
    private int [] targetColumns; // internal setting for animation. 
    
    private int maxValue; // calculates columns percentage of column height.    
    private int maxSegments; // calculates number of segments
    
    private int indicatorHeight;
    private int indicatorWidth;
    private int indicatorXSpacing;
    private int indicatorYSpacing;

	protected Color [] barColors;
    protected Color backgroundColor;
    
    public static Color COLOR_DEFAULT_ON = new Color(   96,   0,   0 );
    public static Color COLOR_DEFAULT_OFF = new Color( 232, 232, 232 ); // light gray
    
    private boolean animatesTransitions;
    private Timer timer;

    public BarChartVisualizer() {
        indicatorXSpacing = 2;
        indicatorYSpacing = 2;
        indicatorWidth = 28;
        indicatorHeight = 4;
        maxSegments = 10;
        maxValue = 0;
    }

    public int [] getColumns() {
		return columns;
	}

    public int getColumn( int i ) {
		return columns[ i ];
	}

	public void setColumns( final int [] columns ) {
        // animation updates columns until equal with targetColumns
		this.targetColumns = columns;
		if (( null == this.columns ) || ( this.columns.length != columns.length )) {
			// Init columns with 0 values.
			this.columns = new int [columns.length ];
			for( int columni = 0; columni < columns.length; columni++ )
		       this.columns[ columni ] = 0;
		}
        update();
        // firePropertyChange("columns", oldColumns, columns);
	}

    public Color getBarColor( int i ) {
		return barColors[ i ];
	}

    public Color [] getBarColors() {
		return barColors;
	}

	public void setBarColors(Color[] barColors) {
		this.barColors = barColors;
        update();
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
        update();
	}
    
    public void setIndicatorHeight(int height) {
        this.indicatorHeight = height;
        update();
    }
    
    public void setIndicatorWidth(int width) {
        indicatorWidth = width;
        update();
    }
    
    public void setIndicatorXSpacing(int spacing) {
        indicatorXSpacing = spacing;
        update();
    }
    
    public void setIndicatorYSpacing(int spacing) {
        indicatorYSpacing = spacing;
        update();
    }

    public int getSegments() {
    	return maxSegments;
    }

    public void setSegments(int segments) {
        if (segments < 1) {
            throw new IllegalArgumentException();
        }
        this.maxSegments = segments;
        update();
    }

    public int getMaxValue() {
    	return maxValue;    	
    }
    
    public void setMaxValue(int maxValue) {
        if (maxValue < 0) {
            throw new IllegalArgumentException();
        }
        this.maxValue = maxValue;
        update();
    }
    
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        return calcSize();
    }
    
    public Dimension getMinimumSize() {
        if (isMinimumSizeSet()) {
            return super.getMinimumSize();
        }
        return calcSize();
    }

    private Dimension calcSize() {
        Insets insets = getInsets();
        int maxColumns = ( null == columns ) ? 0 : columns.length;
        return new Dimension(
           (indicatorWidth + indicatorXSpacing) * maxColumns - indicatorXSpacing + insets.left + insets.right,
           (indicatorHeight + indicatorYSpacing) * maxSegments - indicatorYSpacing + insets.top + insets.bottom);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getForeground());
        Insets insets = getInsets();
        int x = insets.left;
        int startY = getHeight() - insets.bottom - indicatorHeight;
		if (null != columns) {
			// int columnTotalHeight = maxValue * (indicatorHeight + indicatorYSpacing);
			// int columnTotalHeight = getHeight() - insets.top - insets.bottom;
			for (int columni = 0; columni < columns.length; columni++) {
				int y = startY;
				// Fill in ON segments.
				if (null != barColors)
					g.setColor(getBarColor(columni));
				else
					g.setColor( COLOR_DEFAULT_ON );
				int segments = 0;
				if ( maxValue != 0 )
					segments = columns[ columni ] * maxSegments / maxValue;
				for (int j = 0; j < segments; j++) {
					g.fillRect(x, y, indicatorWidth, indicatorHeight);
					y -= (indicatorHeight + indicatorYSpacing);
				}
				// Fill in OFF segments.
				g.setColor( COLOR_DEFAULT_OFF );
				for (int j = segments; j < maxSegments; j++) {
					g.fillRect(x, y, indicatorWidth, indicatorHeight);
					y -= (indicatorHeight + indicatorYSpacing);
				}
				x += (indicatorWidth + indicatorXSpacing);
			}
		}
    }
    
    private void updateHeightsFromTarget() {
        boolean differ = false;
        if ( null != columns ) {
			for (int columni = 0; columni < columns.length; columni++) {
				if (columns[columni] < targetColumns[columni]) {
					columns[columni] += maxValue / 10 + 1;
					// Check for overshoot.
					if ( columns[ columni ] > targetColumns[ columni] ) columns[ columni] = targetColumns[ columni ];
					differ = true;
				}
				if (columns[columni] > targetColumns[columni]) {
					columns[columni] -= maxValue / 10 + 1;
					// Check for overshoot.
					if ( columns[ columni ] < targetColumns[ columni] ) columns[ columni] = targetColumns[ columni ];
					differ = true;
				}
			}
        }
        if (!differ) {
            stopTimer();
        } else {
            // PENDING: optimize repaint region
            repaint();
        }
    }

    private void update() {
        boolean differ = false;
		if (null != columns) {
			for (int columni = 0; columni < columns.length; columni++) {
				if (columns[columni] != targetColumns[columni]) {
					differ = true;
					break;
				}
			}
			if (animatesTransitions) {
				if (differ) {
					startTimer();
				} else {
					stopTimer();
				}
			} else {
				for (int columni = 0; columni < columns.length; columni++) {
				   columns[columni] = targetColumns[columni];				
				}
				stopTimer();
			}
		}
		revalidate();
		repaint();
    }
    
    private void startTimer() {
        if (timer == null) {
            timer = new Timer( 10, new ActionHandler());
            timer.setRepeats(true);
            timer.start();
        }
    }
    
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    public void setAnimatesTransitions(boolean b) {
        animatesTransitions = b;
    }    
    
    private class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            updateHeightsFromTarget();
        }
    }    
}