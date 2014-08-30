package swingextensions.swingx.binding;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import swingextensions.beansx.ExtendedBean;
import swingextensions.beansx.ExtendedBeanHandler;
import swingextensions.swingx.binding.ListController;
import swingextensions.utils.DumbBean;

/**
 * Unit tests to validate this class.
 * The DIYSingleHandlerImpl is the preferred method to mix in PropertyChange
 * and MultiValue while preserving the original bean interface.  
 * @author <a href="mailto:dan@danbecker.info">Dan Becker</a>
 */
public class ListControllerTest 
{
	public static final Calendar satchDay = new GregorianCalendar(2006, Calendar.NOVEMBER, 2);
	
    @Test
    public void testDumbBean() 
    {
    	DumbBean dog = new DumbBean( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" );
    	
    	assertEquals( "Bean getName", "Satch", dog.getName() );
    	assertEquals( "Bean getSsn", new Integer( 12345 ), dog.getSsn() );
    	assertEquals( "Bean getBirthday", satchDay.getTime(), dog.getBirthday() );
    }    

    @Test
    public void testMultiValueHandler() 
    {
    	ExtendedBean dog = new ExtendedBeanImpl(
    	   new DumbBean( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" ));
    	
    	// Note that set/getImage disappears as the field type != setter param type.
    	// System.out.println("Member count=" + dog.getMemberCount() );
    	// String [] memberNames = dog.getMemberNames();
    	// for ( String memberName : memberNames ) {
        // 	System.out.println( "   member=" + memberName + ", type=" + dog.getMemberClass( memberName ) );    		
    	// }
    	
    	assertTrue( "Member count", 4 == dog.getMemberCount() );
    	assertEquals( "Member class 0", String.class, dog.getMemberClass( "Name" ) );
    	assertEquals( "Member class 1", int.class, dog.getMemberClass( "Age" ) );
    	assertEquals( "Member class 2", Date.class, dog.getMemberClass( "Birthday" ) );
    	assertEquals( "Member class 3", Integer.class, dog.getMemberClass( "Ssn" ) );
    	// assertEquals( "Member class 4", BufferedImage.class, dog.getMemberClass( "Image" ) );

    	assertEquals( "Member getName", "Satch", dog.getMember( "Name" ) );
    	assertEquals( "Member getBirthday", satchDay.getTime(), dog.getMember( "Birthday" ) );
    	assertEquals( "Member getSsn", 12345, dog.getMember( "Ssn" ) );

    	dog.setMember( "Name", "Boots" );
    	assertEquals( "Member getName", "Boots", dog.getMember( "Name" ) );
    }    

    @Test
    public void testMultiValueHandlerIndex() 
    {
    	ExtendedBean dog = new ExtendedBeanImpl(
    	    	   new DumbBean( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" ));
    	
    	// Note that set/getImage disappears as the field type != setter param type.
    	// System.out.println("Member count=" + dog.getMemberCount() );
    	assertTrue( "Member count", 4 == dog.getMemberCount() );
    	String [] expectedKeys = new String[] { "Name", "Age", "Birthday", "Ssn" };
    	assertArrayEquals( "Member names", expectedKeys, dog.getMemberNames() );

    	assertEquals( "Member getName", "Satch", dog.getMember( "Name" ) );
    	int index = 0;
    	for ( String key : expectedKeys ) {
        	assertEquals( "Member get key=\"" + key + "\" index " + index, dog.getMember( key ), dog.getMember( index ) );
        	index += 1;    		
    	}

    	dog.setMember( 0, "Boots" );
    	assertEquals( "Member getName", "Boots", dog.getMember( 0 ) );
    	dog.setMember( 3, 67890 );
    	assertEquals( "Member getName", 67890, dog.getMember( 3 ) );
    }    

    @Test
    public void testMultiValueListenerHandler() 
    {
    	ExtendedBean dog = new ExtendedBeanImpl( 
           new DumbBean( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" ));
    	BeanListener listener = new BeanListener();
    	
    	dog.addPropertyChangeListener(listener);
    	dog.setMember( "Name", "Boots" );
    	PropertyChangeEvent change = listener.getChange();
    	
    	assertEquals( "Bean change old value", "Satch",  change.getOldValue() );
    	assertEquals( "Bean change new value", "Boots",  change.getNewValue() );
    	assertEquals( "Bean change name", "Name",  change.getPropertyName() );

    	dog.setMember( 0, "Harriet" );
    	change = listener.getChange();
    	
    	assertEquals( "Bean change old value", "Boots",  change.getOldValue() );
    	assertEquals( "Bean change new value", "Harriet",  change.getNewValue() );
    	assertEquals( "Bean change name", "Name",  change.getPropertyName() );
    }    

    @Test
    public void testListController() 
    {
    	ExtendedBean dog1 = new ExtendedBeanImpl( 
           new DumbBean( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" ));
    	ExtendedBean dog2 = new ExtendedBeanImpl( 
    	           new DumbBean( "Mook", 2001, Calendar.MAY, 13, 12345, "src/test/resources/SnowSatch.jpg" ));

    	ListController<ExtendedBean> list = new ListControllerImpl<ExtendedBean>();

    	assertTrue( "List type empty", ExtendedBean.class.isAssignableFrom( list.getEntryType() ) );
    	System.out.println( "ListController empty type=" + list.getEntryType());
    	assertEquals( "List members empty", 0, list.getMemberCount() );

    	list.setEntries( Arrays.asList( new ExtendedBean[] { dog1, dog2 }));

    	assertTrue( "List type", ExtendedBean.class.isAssignableFrom( list.getEntryType() ) );
    	System.out.println( "ListController non-empty type=" + list.getEntryType());
    	assertEquals( "List members", 4, list.getMemberCount() );
    	
    }    

    /** Extends a basic bean with support for property change and multi value. Adapter handler. */
    public class ExtendedBeanImpl extends ExtendedBeanHandler {
    	public ExtendedBeanImpl( Object dumbBean ) {
    		super( dumbBean );
    	}
   }
   
    /** Extends a basic bean with support for property change and multi value. Adapter handler. */
    public class ListControllerImpl<T extends ExtendedBean> extends ListController<T> {
        protected final PropertyChangeSupport support;
        
        public ListControllerImpl() {
            support = new PropertyChangeSupport(this);        	
        }
        
        protected boolean includeEntry(T entry, String filter) {
        	return true;
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
   }
   
   public class BeanListener implements PropertyChangeListener {   
	   // PropertyChangeListener
		public void propertyChange(PropertyChangeEvent evt) {
            this.change = evt;
		}
		
        public PropertyChangeEvent getChange(){
        	return this.change;
        }
        protected PropertyChangeEvent change;
   }
}