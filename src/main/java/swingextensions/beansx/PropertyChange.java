package swingextensions.beansx;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/** 
 * Implements property change notifications.
 * Implementors of this class should use {@link java.beans.PropertyChangeSupport}
 * to implement this interface.
 * Listeners of this class should implement {@link java.beans.PropertyChangeListener} and
 * the method void propertyChange({@link java.beans.PropertyChangeEvent} evt) or 
 * {@link java.beans.propertyIndexedPropertyChangeEvent}.
 */
public interface PropertyChange {
    public void addPropertyChangeListener(PropertyChangeListener listener);
    
    public void removePropertyChangeListener(PropertyChangeListener listener);
    
    public void addPropertyChangeListener(String property, PropertyChangeListener listener);
    
    public void removePropertyChangeListener(String property, PropertyChangeListener listener);
    
    /** 
     * Use {@link java.beans.PropertyChangeEvent} for simple containers,
     * use {@link java.beans.IndexedPropertyChangeEvent} for collections.
     */
    public void firePropertyChange(PropertyChangeEvent event);
    // Supports event based API to allow listening to Collection beans via IndexedPropertyChangeEvent
    // public void firePropertyChange(String key, Object oldValue, Object newValue);
}