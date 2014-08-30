package swingextensions.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Simple JPanel with tiled image background.
 */
public class ImageBackgroundPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected String backgroundLocation;
	protected BufferedImage bufferedImage;
	protected TexturePaint texture;

	public ImageBackgroundPanel() {
	}

	public ImageBackgroundPanel( String backgroundLocation ) {
		setBackgroundLocation( backgroundLocation );
	}

	public String getBackgroundLocation() {
		return backgroundLocation;
	}

	public void setBackgroundLocation(String backgroundLocation) {
        File test = new File( backgroundLocation );
        if (!test.canRead()) {
        	System.out.println( "About background \"" + backgroundLocation + "\" is not readable.");
        	System.out.println( "   absolutePath is " + test.getAbsolutePath() );
        	return;
        }
        try {
			bufferedImage = ImageIO.read( test );
			texture = new TexturePaint(bufferedImage, 
					new Rectangle(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight()));
	        this.backgroundLocation = backgroundLocation;
		} catch (IOException e) {
		}
	}

	// The class should override this method.
	public void paintComponent(Graphics g) {
		if ( null != texture ) {
		   Graphics2D g2d = (Graphics2D) g;
		   g2d.setPaint(texture);
		   g2d.fillRect(0, 0, getSize().width, getSize().height);
		}
	}
}