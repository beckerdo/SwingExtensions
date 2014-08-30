package swingextensions.swingx.undo;

import javax.swing.undo.CompoundEdit;

/**
 * A CompoundEdit that maintains a reference to an ExtendedUndoManager. When
 * end is invoked, the undo managers actions are updated appropriately.
 * Based on the Sun Swing PasswordStore demo.
 */
public class ExtendedCompoundEdit extends CompoundEdit {
	private static final long serialVersionUID = 1L;
	private ExtendedUndoManager undoManager;
    
    public ExtendedCompoundEdit(ExtendedUndoManager undoManager) {
        this.undoManager = undoManager;
    }

    public void end() {
        if (isInProgress()) {
            super.end();
            undoManager.updateActions();
            undoManager = null;
        }
    }
}
