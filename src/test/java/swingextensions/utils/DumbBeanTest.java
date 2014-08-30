package swingextensions.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests to validate this class.
 * @author <a href="mailto:dan@danbecker.info">Dan Becker</a>
 */
public class DumbBeanTest 
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
}