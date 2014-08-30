package swingextensions.beansx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/** 
 * Implements property change support, multi-value interface for a basic bean. 
 * A basic bean is a Java object with fields that have public getters and setter.
 * <p>
 * Fields and methods are introspected with {@link getPublicFieldTypes}.
 * The name of the field member must match the getter and setter names. 
 * The type of the field member must match the getter and setter param types. 
 */
public class ExtendedBeanHandler implements ExtendedBean {
    protected PropertyChangeSupport support;
    protected MultiValueHandler handler;
    
    public ExtendedBeanHandler() {   	
    }
    
    public ExtendedBeanHandler( Object basicBean ) {
        support = new PropertyChangeSupport( basicBean );
	    handler = new MultiValueHandler(basicBean, getPublicFieldTypes( basicBean.getClass() ));
    }
    
    /** Constructs a map of public field names to class. */
    protected final Map<String,Class<?>> getPublicFieldTypes( Class<? extends Object> beanClass ) {
		Map<String,Class<?>> memberTypes = new LinkedHashMap<String,Class<?>>();
		try {
		   // Field [] fields = beanClass.getFields(); // only gets public fields.
		   Field [] fields = beanClass.getDeclaredFields(); // only gets this class fields, public, protected, defa.ult
		   for ( Field field: fields) {
			   Class<?> fieldClass = field.getType();
			   String fieldName = initCap( field.getName() ); 

				try {
					Method getterMethod = beanClass.getMethod("get" + fieldName);
					if (null != getterMethod) {
						// if (getterMethod.isAccessible()) {
						Method setterMethod = beanClass.getMethod("set"	+ fieldName, fieldClass);
						// if ((null != setterMethod) && (setterMethod.isAccessible())) {
						if (null != setterMethod) {
							memberTypes.put(fieldName, fieldClass);
						}
					    // }
					}
				} catch (NoSuchMethodException e) {
				}
		   }
		} catch (SecurityException e) {			
		}
		return memberTypes;
    }

    /** Capitalizes the first char of the String. */
    public static String initCap(String nonNorm) {
    	char[] charArray = nonNorm.toCharArray();
    	charArray[0] = Character.toUpperCase(charArray[0]);
    	return new String(charArray);
   	}
    
    // PropertyChange interface
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
        support.firePropertyChange(event);
    }
    // Supports event based API to allow listening to Collection beans via IndexedPropertyChangeEvent
    // public void firePropertyChange(String key, Object oldValue,Object newValue) {
    //     support.firePropertyChange(key, oldValue, newValue);
    // }
    
    public int getMemberCount() {
    	return handler.getMemberCount();
    }
    
    public Object getMember(String memberName) {
    	return handler.getMember( memberName );
    }
    
    public Object getMember(int memberIndex) {
    	return handler.getMember( memberIndex );
    }
    
    public void setMember(String memberName,Object value) {
    	Object oldValue = getMember( memberName );
    	handler.setMember(memberName, value);
        firePropertyChange( new PropertyChangeEvent(this,memberName, oldValue, value ));
     }
     
    public void setMember(int memberIndex,Object value) {
    	setMember( getMemberNames()[ memberIndex ], value ); // returns name in property change
     }
     
    public Class<?> getMemberClass(String memberName) {
    	return handler.getMemberClass( memberName );
    }

    public Class<?> getMemberClass(int memberIndex) {
    	return handler.getMemberClass( memberIndex );
    }

    public final String[] getMemberNames() {
    	return handler.getMemberNames();
    }

    public String getMemberName(int memberIndex) {
    	return handler.getMemberName( memberIndex );
    }
}