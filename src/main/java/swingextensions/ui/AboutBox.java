package swingextensions.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import swingextensions.swingx.app.Application;

/**
 * Simple about box implementation.
 * This about box supports:
 *    -dialog box title,
 *    -info title, 
 *    -sub title 1..3 
 *       - 1 is recommended build and version, 
 *       - 2 is recommended copyright,
 *       - 3 is recommended web site, email.
 *    -a background icon, image icon
 */
public final class AboutBox {
    protected static AboutBox INSTANCE;
    
    
    public static AboutBox getInstance() {
        return INSTANCE;
    }
    
    protected String dialogTitle;
    protected String infoTitle; 
    protected String infoSub1;
    protected String infoSub2;
    protected String infoSub3;
    protected String iconLocation;
    protected String backgroundLocation;

    protected AboutBox() {}
    
    /** Populate from application resources with code such as 
     *     Application.getResourceAsString("aboutBox.title");
     *     Application.getResourceAsString("aboutBox.loc");
     * @param dialogTitle Dialog box title
     * @param infoTitle Information area title
     * @param infoSub1 Information area subtitle (recommend version and build)
     * @param infoSub2 Information area subtitle (recommend copyright statement)
     * @param infoSub3 Information area subtitle (recommend website or email)
     * @param iconLocation dialog box icon  
     * @param backgroundLocation background for dialog box (recommend texture)
     */
    public AboutBox(String dialogTitle, String infoTitle, String infoSub1, String infoSub2, String infoSub3,
    		String iconLocation, String backgroundLocation ) {
        this.dialogTitle = dialogTitle;
        this.infoTitle = infoTitle; 
        this.infoSub1 = infoSub1;
        this.infoSub2 = infoSub2;
        this.infoSub3 = infoSub3;
        this.iconLocation = Application.APP_RESOURCES_PREFIX + iconLocation;
        this.backgroundLocation = Application.APP_RESOURCES_PREFIX + backgroundLocation;
        
        File test = new File( this.iconLocation );
        if (!test.canRead()) {
        	System.out.println( "About icon \"" + this.iconLocation + "\" is not readable.");
        	System.out.println( "   absolutePath is " + test.getAbsolutePath() );
        } 
        test = new File( this.backgroundLocation );
        if (!test.canRead()) {
        	System.out.println( "About background \"" + this.backgroundLocation + "\" is not readable.");
        	System.out.println( "   absolutePath is " + test.getAbsolutePath() );
        }
    }
    
    public void show(Window parent) {
        JDialog dialog;
        if (parent == null) {
            dialog = new JDialog();
        } else {
            dialog = new JDialog(parent);
        }

        JPanel content = new ImageBackgroundPanel( this.backgroundLocation );
        dialog.setContentPane(content);
        content.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        content.setAlignmentX( Component.CENTER_ALIGNMENT );
        
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        titleLabel.setForeground( Color.BLACK );
        if ( null != infoTitle )
           titleLabel.setText( infoTitle );
        else
           titleLabel.setText( "Title null" );

        Font subFont = new Font("Serif", Font.PLAIN, 14);
        JLabel sub1Label = new JLabel();
        sub1Label.setFont(subFont);
        sub1Label.setForeground( Color.BLACK );
        if ( null != infoSub1 )
           sub1Label.setText( infoSub1 );
        else
           sub1Label.setText( "Sub Title 1 null" );
        JLabel sub2Label = new JLabel();
        sub2Label.setFont(subFont);
        sub2Label.setForeground( Color.BLACK );
        if ( null != infoSub2 )
           sub2Label.setText( infoSub2 );
        else
           sub2Label.setText( "Sub Title 2 null" );
        JLabel sub3Label = new JLabel();
        sub3Label.setFont(subFont);
        sub3Label.setForeground( Color.BLACK );
        if ( null != infoSub3 )
           sub3Label.setText( infoSub3 );
        else
           sub3Label.setText( "Sub Title 3 null" );
        
        BoxLayout layout = new BoxLayout(content, BoxLayout.Y_AXIS);
        content.setLayout(layout);
        content.add( titleLabel );
        content.add( sub1Label );
        content.add( sub2Label );
        content.add( sub3Label );
        content.add( Box.createRigidArea(new Dimension(0,10) ));

        // JLabel iconLabel = new JLabel( new ImageIcon( iconLocation ), SwingConstants.CENTER );
        JLabel iconLabel = new JLabel( new ImageIcon( iconLocation ) );
        iconLabel.setBorder( new BevelBorder( BevelBorder.RAISED ));
        content.add( iconLabel );
        
        if ( null != dialogTitle )
            dialog.setTitle( dialogTitle );
        else
        	dialog.setTitle( "About" );
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        // dialog.show();
        dialog.setVisible(true);
        dialog.dispose();
    }
}