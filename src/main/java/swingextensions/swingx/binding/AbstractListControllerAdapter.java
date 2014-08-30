package swingextensions.swingx.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import swingextensions.beansx.ExtendedBean;
import swingextensions.collectionsx.ObservableList;
import swingextensions.collectionsx.ObservableListListener;

/**
 * AbstractListControllerAdapter provides the basis for classes displaying
 * the entries and selection from a ListController. AbstractListControllerAdapter
 * registers an ObservableListListener on the entries of the ListController and
 * invokes the various abstract methods of this class as the entries change.
 * Based on Sun Swing PassswordStore demo.
 */
public abstract class AbstractListControllerAdapter<T extends ExtendedBean> {
    protected ListController<T> listController;
    private PropertyChangeListener propertyChangeListener;
    private ObservableListListener<T> observableListListener;
    protected List<T> entries;
    protected boolean changingSelection;
    
    public AbstractListControllerAdapter(ListController<T> listController) {
        this.listController = listController;
        propertyChangeListener = new PropertyChangeHandler();
        listController.addPropertyChangeListener(propertyChangeListener);

        // Install property change listeners on entries, install ObservableListListener on list. 
        updateEntries();
    }
    
    public void dispose() {
        listController.removePropertyChangeListener(propertyChangeListener);

        // Set this to ensure an NPE is generated if dispose is invoked twice.
        listController = null;
    }
    
    // interface ObservableListListener<T>
    protected abstract void listElementsAdded(int index, int length);
    protected abstract void listElementsRemoved(int index, List<T> elements);
    protected abstract void listElementReplaced(int index, Object oldElement);
    protected abstract void listElementPropertyChanged(int index);
    // property changed from listController, key="entries"
    protected abstract void entriesChanged();
    // property changed from listController, key="selection"
    protected abstract void listControllerSelectionChanged();
    
    /** Install property change listeners on entries, install ObservableListListener on list. */
    protected void updateEntries() {
        if (entries != null) {
            if (entries instanceof ObservableList) {
                ((ObservableList<T>)entries).removeObservableListListener(observableListListener);
            }
            for (T entry : entries) {
                removePropertyChangeListener(entry);
            }
        }
        entries = listController.getEntries();
        for (T entry : entries) {
            installPropertyChangeListener(entry);
        }
        if (entries instanceof ObservableList) {
            if (observableListListener == null) {
                observableListListener = new ObservableListHandler();
            }
            ((ObservableList<T>)entries).addObservableListListener(observableListListener);
        }
    }
    
    protected void installPropertyChangeListener(T entry) {
        if (entry != null) {
            try {
                Class<?> klass = entry.getClass();
                Method pclMethod = klass.getMethod( "addPropertyChangeListener", PropertyChangeListener.class);
                pclMethod.invoke(entry, propertyChangeListener);
            } catch (SecurityException ex) {
            } catch (IllegalArgumentException ex) {
            } catch (IllegalAccessException ex) {
            } catch (NoSuchMethodException ex) {
            } catch (InvocationTargetException ex) {
            }
        }
    }
    
    protected void removePropertyChangeListener(T entry) {
        if (entry != null) {
            try {
                Class<?> klass = entry.getClass();
                Method pclMethod = klass.getMethod( "removePropertyChangeListener", PropertyChangeListener.class);
                pclMethod.invoke(entry, propertyChangeListener);
            } catch (SecurityException ex) {
            } catch (IllegalArgumentException ex) {
            } catch (IllegalAccessException ex) {
            } catch (NoSuchMethodException ex) {
            } catch (InvocationTargetException ex) {
            }
        }
    }
        
    protected class PropertyChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getSource() == listController) {
                String key = e.getPropertyName();
                if (key == "selection") {
                    if (!changingSelection) {
                        handleListControllerSelectionChanged();
                    }
                } else if (key == "entries") {
                    handleEntriesChanged();
                }
            } else {
                int index = entries.indexOf(e.getSource());
                if (index != -1) {
                    handleListElementPropertyChanged(index);
                }
            }
        }
    }

    protected void handleListControllerSelectionChanged() {
        listControllerSelectionChanged();
    }
    
    protected void handleEntriesChanged() {
        updateEntries();
        entriesChanged();
    }

    protected class ObservableListHandler implements ObservableListListener<T> {
        public void listElementsAdded(ObservableList<T> list, int index,int length) {
            handleListElementsAdded(index, length);
        }

        public void listElementsRemoved(ObservableList<T> list, int index, List<T> oldElements) {
            handleListElementsRemoved(index, oldElements);
        }

        public void listElementReplaced(ObservableList<T> list, int index, T oldElement) {
            handleListElementReplaced(index, oldElement);
        }

        public void listElementPropertyChanged(ObservableList<T> list, int index) {
            handleListElementPropertyChanged(index);
        }
    }
    
    private void handleListElementsAdded(int index, int length) {
        for (int i = 0; i < length; i++) {
            installPropertyChangeListener(entries.get(index + i));
        }
        listElementsAdded(index, length);
    }
    
    private void handleListElementsRemoved(int index, List<T> oldElements) {
        for (T o : oldElements) {
            removePropertyChangeListener( o );
        }
        listElementsRemoved(index, oldElements);
    }
    
    private void handleListElementReplaced(int index, T oldElement) {
        removePropertyChangeListener( oldElement);
        installPropertyChangeListener(entries.get(index));
        listElementReplaced(index, oldElement);
    }
    
    private void handleListElementPropertyChanged(int index) {
        listElementPropertyChanged(index);
    }
}