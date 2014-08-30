package swingextensions.swingx.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import swingextensions.beansx.ExtendedBean;
import swingextensions.beansx.PropertyChange;
import swingextensions.collectionsx.CollectionsX;
import swingextensions.collectionsx.ObservableList;
import swingextensions.collectionsx.ObservableListListener;

/**
 * ListController manages a List of entries, a selection of the entries,
 * and a filter. 
 * PropertyChangeListeners are notified as any of these properties change.
 * <p>
 * This class is abstract, subclasses must override the includeEntry to
 * determine if a value should be included in the current filter string.
 */
public abstract class ListController<T extends ExtendedBean> implements PropertyChange {
    private String filter;
    private List<T> prefilteredEntries;
    private List<T> entries;
    private List<T> selection;
    protected final PropertyChangeSupport support;

    public ListController() {
        selection = Collections.emptyList();
        selection = Collections.unmodifiableList(selection);
        entries = new ArrayList<T>(1);
        entries = CollectionsX.observableList(entries);
        support = new PropertyChangeSupport(this);
    }
    
    public final void setEntries(List<T> entries) {
        List<T> oldEntries = this.entries;
        this.entries = entries;
        this.selection = Collections.emptyList();
        support.firePropertyChange( new PropertyChangeEvent( this, "entries", oldEntries, this.entries ));
    }
    
    public final List<T> getEntries() {
        return entries;
    }
    
    public int getEntryCount() {
        return entries.size();
    }
    
    // Selection APIs
    public final void setSelection(final List<T> selection) {
        List<T> oldSelection = this.selection;
        this.selection = Collections.unmodifiableList(selection);
        // firePropertyChange("selection", oldSelection, null);
        support.firePropertyChange(new PropertyChangeEvent( this, "selection", oldSelection, this.selection));
    }
    
    public final List<T> getSelection() {
        return selection;
    }
    
    public void deleteSelection() {
    	System.out.println( "ListController.deleteSelection");    	
    }
    
    public void deleteAll() {
    	System.out.println( "ListController.deleteAll");

    	entries.clear();
        List<T> oldSelection = this.selection;
        selection = Collections.emptyList();
        selection = Collections.unmodifiableList(selection);
        // firePropertyChange("selection", oldSelection, null);
        support.firePropertyChange(new PropertyChangeEvent( this, "deleteAll", oldSelection, this.selection));
    }
    
	// Property support
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
    
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        support.addPropertyChangeListener(property, listener);
    }
    
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        support.removePropertyChangeListener(property, listener);
    }
    
    public void firePropertyChange(PropertyChangeEvent event) {
    	if ( null != support ) // true during constructor
           support.firePropertyChange(event);
    }
    
    public final void setSelectedEntry(T entry) {
        if (entry == null) {
            setSelection(null);
        } else {
            List<T> selection = new ArrayList<T>(1);
            selection.add(entry);
            setSelection(selection);
        }
    }
    
    public final T getSelectedEntry() {
        if (selection.size() > 0) {
            return selection.get(0);
        }
        return null;
    }
    
    // Member API
    public Class<? extends ExtendedBean> getEntryType() {
    	if ( entries.size() == 0 ) {
    	   return ExtendedBean.class;
        } else {
        	ExtendedBean bean = entries.get( 0 );
        	return bean.getClass();
        }
    }
    
    public int getMemberCount() {
    	if ( entries.size() == 0 ) {
     	   return 0;
         } else {
         	ExtendedBean bean = entries.get( 0 );
         	return bean.getMemberCount();
         }
    }
    
    // Filter API
    public void setFilter(String filter) {
        String oldFilter = this.filter;
        this.filter = filter;
        filterChanged(filter);
        // firePropertyChange("filter", oldFilter, filter);
        support.firePropertyChange(new PropertyChangeEvent( this, "filter", oldFilter, this.filter));
    }
    
    public String getFilter() {
        return filter;
    }
    
    public boolean isFiltered() {
        return (prefilteredEntries != null);
    }
    
    /** Maintain prefilteredEntries and selection list. Listen to filtered collection. */
    private void filterChanged(String filter) {
        if (filter == null || filter.length() == 0) {
            if (prefilteredEntries != null) {
                setEntries(prefilteredEntries);
                prefilteredEntries = null;
            }
        } else {
            filter = filter.toLowerCase();
            List<T> entries;
            if (prefilteredEntries == null) {
                prefilteredEntries = this.entries;
                entries = this.entries;
            } else {
                entries = prefilteredEntries;
            }
            List<T> filteredEntries = new ArrayList<T>(entries.size());
            for (T entry : entries) {
                if (includeEntry(entry, filter)) {
                    filteredEntries.add(entry);
                }
            }
            ObservableList<T> oEntries = CollectionsX.observableList(filteredEntries);
            filteredEntries = oEntries;
            oEntries.addObservableListListener(new FilteredListListener());
            setEntries(filteredEntries);
        }
        List<T> entries = getEntries();
        if (entries.size() > 0) {
            setSelectedEntry(entries.get(0));
        } else {
            setSelection(null);
        }
    }

    protected abstract boolean includeEntry(T entry, String filter);
    
    protected class FilteredListListener implements ObservableListListener<T> {
        public void listElementsAdded(ObservableList<T> list, int index, int length) {
            for (int i = 0; i < length; i++) {
                prefilteredEntries.add(list.get(index + i));
            }
        }

        public void listElementsRemoved(ObservableList<T> list, int index, List<T> oldElements) {
            for (T elem : oldElements) {
                prefilteredEntries.remove(elem);
            }
        }

        public void listElementReplaced(ObservableList<T> list, int index, T oldElement) {
            prefilteredEntries.set(prefilteredEntries.indexOf(oldElement),list.get(index));
        }

        public void listElementPropertyChanged(ObservableList<T> list, int index) {
        }
    }
}