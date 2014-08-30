package swingextensions.swingx.binding;

import javax.swing.JTable;

import swingextensions.beansx.ExtendedBean;

/**
 * Adapts a ListController to a Swing JTable.
 */
public final class JTableListControllerAdapter<T extends ExtendedBean> 
   extends AbstractJTableListControllerAdapter<T> {

	public JTableListControllerAdapter(ListController<T> controller, JTable table) {
        super(controller, table);
    }
    
	public void dispose(){		
	}
	
    @SuppressWarnings("unchecked")
	protected void setValueAt(Object value, int rowIndex, int columnIndex) {
        ExtendedBean entry = entries.get(rowIndex);
        entry.setMember( columnIndex, value);
        entries.set( rowIndex, (T) entry );
    }
    
    protected Object getValueAt(int rowIndex, int elementIndex) {
        ExtendedBean entry = entries.get(rowIndex);
        return entry.getMember( elementIndex );
    }
}