package swingextensions.beansx;

import java.lang.reflect.Method;
import java.util.Map;

/** 
 * Implements the {@link MultiValue} interface via a description of the elements. 
 * Note that in this implementation, the index order is determined from the keys of
 * element type map. If ordering is important, use the {@link java.util.LinkedHashMap} to implement. */
public class MultiValueHandler implements MultiValue {
	protected Object container;
	protected final Map<String,Class<?>> elementTypes;
	protected final String [] elementNames;
	
    public MultiValueHandler(Object container, final Map<String,Class<?>> elementTypes ) {
    	this.container = container;
    	this.elementTypes = elementTypes;
    	this.elementNames = elementTypes.keySet().toArray( new String[elementTypes.size()]);
    }
    
	// MultiValue interface
	public Object getMember(String memberName) {
		try {
			Class<?> containerClass = container.getClass();
			Method pclMethod = containerClass.getMethod("get" + memberName);
			Object value = pclMethod.invoke(container);
			return value;
		// } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
		} catch (Exception e) {
			throw new IllegalArgumentException("Illegal member \"" + memberName	+ "\", exception=" + e);
		}
	}

	public Object getMember(int memberIndex) {
		return getMember( elementNames[ memberIndex ] );
	}

    public void setMember(String memberName, Object value) {
    	// Object oldValue = getMember( memberName );
		try {
			Class<?> containerClass = container.getClass();
			Method pclMethod = containerClass.getMethod("set" + memberName, elementTypes.get(memberName));
			pclMethod.invoke(container, value);
		// } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
		} catch (Exception e) {
			throw new IllegalArgumentException("Illegal member \"" + memberName	+ "\", exception=" + e);
		}
     }

    public void setMember(int memberIndex, Object value) {
		setMember( elementNames[ memberIndex ], value );
    }

    public int getMemberCount() {
    	return elementTypes.size();
    }
    
    public Class<?> getMemberClass(String memberName) {
    	return elementTypes.get(memberName);
    }

    public Class<?> getMemberClass(int memberIndex) {
    	return elementTypes.get( elementNames[ memberIndex ] );
    }

    public final String[] getMemberNames() {
    	return elementNames;
    }

    public String getMemberName(int memberIndex ) {
    	return elementNames[ memberIndex ];
    }
}