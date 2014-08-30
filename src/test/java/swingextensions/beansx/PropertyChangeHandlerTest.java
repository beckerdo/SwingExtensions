package swingextensions.beansx;

import static org.junit.Assert.assertEquals;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import swingextensions.utils.DumbBean;

/**
 * Unit tests to validate this class.  
 * @author <a href="mailto:dan@danbecker.info">Dan Becker</a>
 */
public class PropertyChangeHandlerTest 
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
    public void testMultiValueListener() 
    {
    	PropertyChangeImpl dog = new PropertyChangeImpl( "Satch", 2006, Calendar.NOVEMBER, 2, 12345, "src/test/resources/SnowSatch.jpg" );
    	BeanListener listener = new BeanListener();
    	
    	dog.addPropertyChangeListener(listener);
    	dog.setName( "Boots" );
    	PropertyChangeEvent change = listener.getChange();
    	
    	assertEquals( "Bean change old value", "Satch",  change.getOldValue() );
    	assertEquals( "Bean change new value", "Boots",  change.getNewValue() );
    	assertEquals( "Bean change name", "Name",  change.getPropertyName() );
    }    

    /** Extends a basic bean with support for property change. */
    public class PropertyChangeImpl extends DumbBean implements PropertyChange {
        protected final PropertyChangeSupport support;

    	public PropertyChangeImpl( String name, Date birthday, Integer ssn, String imageLocation ) {
    		super( name, birthday, ssn, imageLocation );
            support = new PropertyChangeSupport(this);
    	}

    	public PropertyChangeImpl( String name, int year, int month, int day, Integer ssn, String imageLocation ) {
    		super( name, year, month, day, ssn, imageLocation );
            support = new PropertyChangeSupport(this);
    	}

    	// Catch property changes
		public void setName(String name) {
			if (( null == this.name) || !this.name.equals( name )) {
				Object old = this.name;
				super.setName(name);
				firePropertyChange( new PropertyChangeEvent( this, "Name", old, this.name ));
			}
		}
		public void setAge(int age) {
			if ( this.age != age ) {
				int old = this.age;
				super.setAge(age);
				firePropertyChange( new PropertyChangeEvent( this, "Age", old, this.age ));
			}
		}
		public void setBirthday(Date birthday) {
			if (( null == this.birthday ) || !this.birthday.equals(birthday)) {
				Date old = this.birthday;
				super.setBirthday(birthday);
				firePropertyChange( new PropertyChangeEvent( this, "Borthday", old, this.birthday ));
			}
		}
		public void setSsn(int ssn) {
			if ( this.ssn != ssn ) {
				int old = this.ssn;
				super.setSsn(ssn);
				firePropertyChange( new PropertyChangeEvent( this, "Ssn", old, this.ssn ));
			}
		}
		public void setImage(String imageLocation) {
			Object old = super.getImage();
			super.setImage( imageLocation );
			firePropertyChange( new PropertyChangeEvent( this, "Image", old, super.getImage() ));
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