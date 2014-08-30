package swingextensions.utils;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests to validate this class.  
 * @author <a href="mailto:dan@danbecker.info">Dan Becker</a>
 */
public class IntrospectionUtilsTest 
{
    @Test
    public void testGenSuper() 
    {
        List<String> list = new LinkedList<String>();
        IntrospectionUtils.printGenericSuperType(list.getClass());
    }    

    @Test
    public void testGenTypeParams()
    {
        List<String> list = new LinkedList<String>();
        IntrospectionUtils.printTypeParams(list.getClass());
    }    

    @Test
    public void testValueHolder()
    {
        ValueHolder<String> holder = ValueHolder.create("hello world", String.class);
        IntrospectionUtils.printTypeParams(holder.getClass());
    }    

    @Test
    public void testPrintClass()
    {
        ValueHolder<String> holder = ValueHolder.create("hello world", String.class);
        IntrospectionUtils.printClass(holder.getClass());
    }    

    @Test
    public void testPrintClassExtends()
    {
        // ValueExtension holder = (ValueExtension) ValueHolder.create("hello world", String.class);
        // IntrospectionUtils.printClass(ValueExtension.class);
        // IntrospectionUtils.printClass(holder.getClass());
        IntrospectionUtils.printClass(ValueHolder.create("hello world", String.class).getClass());
    }    
    
    @Test
    public void testPrintClassArray()
    {
    	// ValueHolder<String> [] holder = new ValueHolder<String>[2]
    	// {
    	// 	ValueHolder.create("One", String.class),
    	// 	ValueHolder.create("Two", String.class)
    	// };

    	// String [] test = new String[]{ "fred",  "dummy" };
        // ValueExtension holder = (ValueExtension) ValueHolder.create("hello world", String.class);
        // IntrospectionUtils.printClass(ValueExtension.class);
        // IntrospectionUtils.printClass(holder.getClass());
        IntrospectionUtils.printClass(ValueHolder.create("hello world", String.class).getClass());
    }    
    
    public class ValueExtension extends ValueHolder<String> {
    	
    }
}