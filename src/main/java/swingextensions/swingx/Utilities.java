package swingextensions.swingx;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JTabbedPane;

/**
 * Various utility methods.
 * Based on Sun Swing PasswordStore demo.
 */
public final class Utilities {
    
    /**
     * Makes the specified component recursively visible. If the specified
     * component is contained in a tab pane, the tab is selected.
     *
     * @param c the component to make recursively visible
     * @throws NullPointerException if c is null.
     */
    public static void makeVisible(Component c) {
        if (!c.isShowing()) {
            Component parent = c;
            while (parent != null) {
                Container nextParent = parent.getParent();
                // Note, this should handle internal frames as well
                if (nextParent instanceof JTabbedPane) {
                    JTabbedPane tp = (JTabbedPane)nextParent;
                    int index = tp.indexOfComponent(parent);
                    if (index != -1 && tp.getSelectedIndex() != index) {
                        tp.setSelectedIndex(index);
                    }
                }
                parent = nextParent;
            }
        }
    }
}
