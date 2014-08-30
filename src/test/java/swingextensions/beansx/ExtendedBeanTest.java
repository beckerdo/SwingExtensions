package swingextensions.beansx;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import swingextensions.utils.DumbBean;

/**
 * Unit tests to validate this class.
 * Uses DumbBean as a basic bean with mixin functionality.
 * The DIYSingleHandlerImpl is the preferred method to mix in PropertyChange
 * and MultiValue while preserving the original bean interface.  
 * @author <a href="mailto:dan@danbecker.info">Dan Becker</a>
 */
public class ExtendedBeanTest 
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
    public void testMultiValueName() 
    {
    	MultiValue dog = new DIYMultiHandlerImpl( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" );
    	
    	assertTrue( "Member count", 5 == dog.getMemberCount() );
    	assertEquals( "Member class 0", String.class, dog.getMemberClass( "Name" ) );
    	assertEquals( "Member class 1", Integer.class, dog.getMemberClass( "Age" ) );
    	assertEquals( "Member class 2", Date.class, dog.getMemberClass( "Birthday" ) );
    	assertEquals( "Member class 3", Integer.class, dog.getMemberClass( "Ssn" ) );
    	assertEquals( "Member class 4", BufferedImage.class, dog.getMemberClass( "Image" ) );

    	assertEquals( "Member getName", "Satch", dog.getMember( "Name" ) );
    	assertEquals( "Member getBirthday", satchDay.getTime(), dog.getMember( "Birthday" ) );
    	assertEquals( "Member getSsn", 12345, dog.getMember( "Ssn" ) );

    	dog.setMember( "Name", "Boots" );
    	assertEquals( "Member getName", "Boots", dog.getMember( "Name" ) );
    }    

    @Test
    public void testMultiValueIndex() 
    {
    	MultiValue dog = new DIYMultiHandlerImpl( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" );
    	
    	assertTrue( "Member count", 5 == dog.getMemberCount() );
    	String [] expectedKeys = new String[] { "Name", "Age", "Birthday", "Ssn", "Image" };
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
    public void testMultiValueListener() 
    {
    	DIYMultiHandlerImpl dog = new DIYMultiHandlerImpl( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" );
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
    public void testSingleHandlerMultiValueName() 
    {
    	ExtendedBean dog = new DIYSingleHandlerImpl( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" );
    	
    	// Note that set/getImage disappears as the field type != setter param type.
    	// System.out.println("Member count=" + dog.getMemberCount() );
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
    public void testSingleHandlerMultiValueIndex() 
    {
    	ExtendedBean dog = new DIYSingleHandlerImpl( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" );
    	
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
    public void testSingleHandlerMultiValueListener() 
    {
    	ExtendedBean dog = new DIYSingleHandlerImpl( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" );
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
    public void testMultiValueHandler() 
    {
    	ExtendedBean dog = new ExtendedBeanImpl(
    	   new DumbBean( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" ));
    	
    	// Note that set/getImage disappears as the field type != setter param type.
    	// System.out.println("Member count=" + dog.getMemberCount() );
    	String [] memberNames = dog.getMemberNames();
    	for ( String memberName : memberNames ) {
        	System.out.println( "   member=" + memberName + ", type=" + dog.getMemberClass( memberName ) );    		
    	}
    	
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

    /** Extends a basic bean with support for property change and multi value. 
     *  Preserves DumbBean interface. Mixes in new support to DumbBean. */
    public class DIYMultiHandlerImpl implements PropertyChange, MultiValue {
        protected final MultiValueHandler handler;
        protected final PropertyChangeSupport support;

        @SuppressWarnings("serial")
    	// Can be static in top level class.
		public final Map<String,Class<?>> memberTypes = new LinkedHashMap<String,Class<?>>() {{
           put("Name", String.class);
           put("Age", Integer.class);
           put("Birthday", Date.class);
           put("Ssn", Integer.class);
           put("Image", BufferedImage.class);
        }};
        
    	public DIYMultiHandlerImpl( String name, Date birthday, Integer ssn, String imageLocation ) {
    	    handler = new MultiValueHandler(new DumbBean(), memberTypes );
    	    setMember( "Name", name );
    	    setMember( "Birthday", birthday );
    	    setMember( "Ssn", ssn );
    	    setMember( "Image", imageLocation );
            support = new PropertyChangeSupport(this);
    	}

    	public DIYMultiHandlerImpl( String name, int year, int month, int day, Integer ssn, String imageLocation ) {
    		DumbBean bean = new DumbBean();
    		bean.setName( name );
    		bean.setSsn( ssn );
    		bean.setBirthday( year, month, day );
    		bean.setImage( imageLocation );
    	    handler = new MultiValueHandler(bean, memberTypes );
            support = new PropertyChangeSupport(this);
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
            support.firePropertyChange(event);
        }
    	
    	// MultiValue interface
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

        public final String [] getMemberNames() {
        	return handler.getMemberNames();
        }

        public String getMemberName( int memberIndex ) {
        	return handler.getMemberName( memberIndex );
        }
   }
   
    /** Extends a basic bean with support for ExtendedBean.
    *  Preserves DumbBean interface. Mixes in new support to DumbBean. */
    public class DIYSingleHandlerImpl implements ExtendedBean {
        protected final ExtendedBeanHandler handler;

    	public DIYSingleHandlerImpl( String name, Date birthday, Integer ssn, String imageLocation ) {
            DumbBean dumbBean = new DumbBean( name, birthday, ssn, imageLocation );
    	    handler = new ExtendedBeanHandler( dumbBean  );
    	}

    	public DIYSingleHandlerImpl( String name, int year, int month, int day, Integer ssn, String imageLocation ) {
    		DumbBean bean = new DumbBean();
    		bean.setName( name );
    		bean.setSsn( ssn );
    		bean.setBirthday( year, month, day );
    		bean.setImage( imageLocation );
    	    handler = new ExtendedBeanHandler( bean  );
    	}

        // Property support
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            handler.addPropertyChangeListener(listener);
        }
        
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            handler.removePropertyChangeListener(listener);
        }
        
        public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
            handler.addPropertyChangeListener(property, listener);
        }
        
        public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
            handler.removePropertyChangeListener(property, listener);
        }
        
        public void firePropertyChange(PropertyChangeEvent event) {
            handler.firePropertyChange(event);
        }
    	
    	// MultiValue interface
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

        public final String [] getMemberNames() {
        	return handler.getMemberNames();
        }

        public String getMemberName( int memberIndex ) {
        	return handler.getMemberName( memberIndex );
        }
   }
   
    /** Extends a basic bean with support for property change and multi value. Adapter handler. */
    public class ExtendedBeanImpl extends ExtendedBeanHandler {
    	public ExtendedBeanImpl( Object dumbBean ) {
    		super( dumbBean );
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