package swingextensions.beansx;

/** 
 * Identify and set/get values in a bean, container, or collection. 
 * Unlike a collection, this interface supports different object types.
 */
public interface MultiValue {
    public Object getMember(String memberName); 
    public Object getMember(int memberIndex); 
    public void setMember(String memberName, Object value);
    public void setMember(int memberIndex, Object value);
    public Class<?> getMemberClass(String memberName);
    public Class<?> getMemberClass(int memberIndex);

    public int getMemberCount(); 
    public String [] getMemberNames();    
    public String getMemberName(int memberIndex);    
}