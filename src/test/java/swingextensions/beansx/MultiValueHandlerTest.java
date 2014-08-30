package swingextensions.beansx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import swingextensions.utils.DumbBean;

/**
 * Unit tests to validate this class.  
 * @author <a href="mailto:dan@danbecker.info">Dan Becker</a>
 */
public class MultiValueHandlerTest 
{
	public static final Calendar satchDay = new GregorianCalendar(2006, Calendar.NOVEMBER, 2);
	
    @Test
    public void testBasicBean() 
    {
    	DumbBean dog = new DumbBean( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" );
    	
    	assertEquals( "Bean getName", "Satch", dog.getName() );
    	assertEquals( "Bean getSsn", new Integer( 12345 ), dog.getSsn() );
    	assertEquals( "Bean getBirthday", satchDay.getTime(), dog.getBirthday() );    	
    }    

    @Test
    public void testMultiValueHandler() 
    {
    	MultiValue dog = new MultiValueHandlerImpl( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" );
    	
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


    /** Extends a basic bean with support multi value. Handler support */
    public class MultiValueHandlerImpl implements MultiValue {
        protected final MultiValueHandler handler;
        
        @SuppressWarnings("serial")
    	// Can be static in top level class.
		public final Map<String,Class<?>> memberTypes = new HashMap<String,Class<?>>() {{
           put("Name", String.class);
           put("Age", Integer.class);
           put("Birthday", Date.class);
           put("Ssn", Integer.class);
           put("Image", BufferedImage.class);
        }};
        
    	public MultiValueHandlerImpl( String name, Date birthday, Integer ssn, String imageLocation ) {
    	    handler = new MultiValueHandler(new DumbBean(), memberTypes );
    	    setMember( "Name", name );
    	    setMember( "Birthday", birthday );
    	    setMember( "Ssn", ssn );
    	    setMember( "Image", imageLocation );
    	}

    	public MultiValueHandlerImpl( String name, int year, int month, int day, Integer ssn, String imageLocation ) {
    		DumbBean bean = new DumbBean();
    		bean.setName( name );
    		bean.setSsn( ssn );
    		bean.setBirthday( year, month, day );
    		bean.setImage( imageLocation );
    	    handler = new MultiValueHandler(bean, memberTypes );
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
        	// Object oldValue = getMember( memberName );
        	handler.setMember(memberName, value);
            // firePropertyChange( new PropertyChangeEvent(this,memberName, oldValue, value ));
         }
         
        public void setMember(int memberIndex,Object value) {
        	handler.setMember(memberIndex, value);
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

        public String getMemberName( int memberIndex ) {
        	return handler.getMemberName( memberIndex );
        }
   }   
}