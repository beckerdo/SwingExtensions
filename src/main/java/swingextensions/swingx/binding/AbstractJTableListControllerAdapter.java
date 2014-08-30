package swingextensions.swingx.binding;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import swingextensions.beansx.ExtendedBean;

/**
 * Adapts the ListController to the JTable TableModel.
 * Subclasses must override the various TableModel methods in 
 * this class to report the column count, column name, etc.
 */
public abstract class AbstractJTableListControllerAdapter<T extends ExtendedBean> 
   extends AbstractListControllerAdapter<T> {

	private JTable table;
    private TableModelImpl tableModel;
    private ListSelectionListener listSelectionListener;
    
    public AbstractJTableListControllerAdapter(ListController<T> controller, JTable table) {
        super(controller);

        this.table = table;

        listSelectionListener = new ListSelectionHandler();
        tableModel = new TableModelImpl();
        table.setModel(tableModel);
        table.getSelectionModel().addListSelectionListener(listSelectionListener);
        
        // Update the selection
        listControllerSelectionChanged();
    }
    
    public void dispose() {
        super.dispose();
        table.getSelectionModel().removeListSelectionListener(listSelectionListener);
        table.setModel(new DefaultTableModel());
    }
    
    // Invoked when the Controller's selection has changed, and we didn't
    // initiate it. Selection needs to be applied to JTable.
    protected void listControllerSelectionChanged() {
        // Set changingSelection to true. This will avoid us trying to
        // apply the selection back to the Controller
        changingSelection = true;

        // Map the selection to the JTable.
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setValueIsAdjusting(true);
        selectionModel.clearSelection();
        for (T entry : listController.getSelection()) {
            int index = entries.indexOf(entry);
            if (( index >= 0 ) && (index < table.getRowCount() )) {
               index = table.convertRowIndexToView(index);
               selectionModel.addSelectionInterval(index, index);
            }
        }
        selectionModel.setValueIsAdjusting(false);
        
        // Selection done changing. Set changingSelection to false to indicate
        // any changes in the JTable (or Controller) need to be propagated.
        changingSelection = false;
    }

    // Invoked when the selection in the JTable changes and we didn't initiate
    // it. The selection needs to be applied to the Controller.
    private void tableSelectionChanged() {
        // Set changingSelection to true. This will avoid us trying to
        // apply the selection back to the Controller
        changingSelection = true;

        // Map the selection to the JTable.
        int[] indices = table.getSelectedRows();
        List<T> selection = new ArrayList<T>(indices.length);
        for (int i = 0; i < indices.length; i++) {
            int index = table.convertRowIndexToModel(indices[i]);
            selection.add(entries.get(index));
        }
        listController.setSelection(selection);
        
        // Selection done changing. Set changingSelection to false to indicate
        // any changes in the JTable (or Controller) need to be propagated.
        changingSelection = false;
    }

    protected void entriesChanged() {
        tableModel.fireTableDataChanged();
    }

    protected void listElementsAdded(int index, int length) {
        // RowSorter sorter = table.getRowSorter();
        tableModel.fireTableRowsInserted(index, index + length - 1);
    }

    protected void listElementsRemoved(int index, List<T> elements) {
        tableModel.fireTableRowsDeleted(index, index + elements.size() - 1);
    }

    protected void listElementReplaced(int index, Object oldElement) {
        tableModel.fireTableRowsUpdated(index, index);
    }

    protected void listElementPropertyChanged(int index) {
        tableModel.fireTableRowsUpdated(index, index);
    }
        
    private class ListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (!changingSelection && !e.getValueIsAdjusting()) {
                tableSelectionChanged();
            }
        }
    }

    // List support 
    protected abstract Object getValueAt(int rowIndex, int elementIndex);
    protected abstract void setValueAt(Object aValue, int rowIndex,int elementIndex);

    // The AbstractTableModel provides default implementations for most of the 
    // methods in the TableModel interface. 
    // To create a concrete <code>TableModel</code> as a subclass of
    // <code>AbstractTableModel</code> you need only provide implementations 
    // for the following three methods:
    //    public int getRowCount();
    //    public int getColumnCount();
    //    public Object getValueAt(int row, int column);    
    private class TableModelImpl extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		public int getRowCount() {
            return entries.size();
        }
        
        public int getColumnCount() {
            return listController.getMemberCount();
        }

        public Object getValueAt(int rowIndex, int elementIndex) {
        	ExtendedBean bean = listController.getEntries().get( rowIndex );
        	// String memberName = bean.getMemberName( elementIndex );
        	Object member =  bean.getMember(elementIndex);
            return member;
        }

        public void setValueAt(Object aValue, int rowIndex, int elementIndex) {
        	ExtendedBean bean = listController.getEntries().get( rowIndex );
            bean.setMember(elementIndex, aValue);
        }

        public Class<?> getColumnClass(int elementIndex) {
        	Class<? extends ExtendedBean> contentType = listController.getEntryType();
			try {
				ExtendedBean bean = contentType.newInstance();
	            return bean.getMemberClass( elementIndex );
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return Object.class;
        }
        
        public String getColumnName(int elementIndex) {
        	Class<? extends ExtendedBean> contentType = listController.getEntryType();
			try {
				ExtendedBean bean = contentType.newInstance();
	            return bean.getMemberName( elementIndex );
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return "no column name";
        }
    }
}