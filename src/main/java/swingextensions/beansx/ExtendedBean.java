package swingextensions.beansx;

/** 
 * Implements property change support, multi-value interface for a basic bean. 
 * Beans should have a public no arg constructor to facilitate introspection.
 */
public interface ExtendedBean extends PropertyChange, MultiValue {
    
}