package swingextensions.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;

/**
 * Implements a basic bean with no property changes or multi value handler.
 * Used in a number of ExtendedBean, PropertyChange, MultiValue tests.
 * @author <a href="mailto:dan@danbecker.info">Dan Becker</a>
 */
public class DumbBean 
{
    public DumbBean() {}
    	
    public DumbBean( String name, Date birthday, Integer ssn, String imageLocation ) {
    	setName( name );
    	setSsn( ssn );
    	setBirthday( birthday );
    	setImage( imageLocation );
    }
    
    public DumbBean( String name, int year, int month, int day, Integer ssn, String imageLocation ) {
    	setName( name );
    	setSsn( ssn );
    	setBirthday( year, month, day );
    	setImage( imageLocation );
    }
    	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;			
		this.age = calcAge(this.birthday);
	}
	/** Use calendar values, e.g. 1998, Calendar.DECEMBER, 25. */
	public void setBirthday(int year, int month, int day) {
		Calendar whatday = new GregorianCalendar(year, month, day);
		setBirthday( whatday.getTime());
	}
		protected int calcAge(final Date birthday) {
			// Create a calendar object with today's date
			Calendar today = Calendar.getInstance();
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTime( birthday );
			// Get age based on year
			int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
			// Add the tentative age to the date of birth to get this year's birthday
			birthDate.add(Calendar.YEAR, this.age);
			// If this year's birthday has not happened yet, subtract one from age
			if (today.before(birthDate)) {
			    this.age--;
			}
			return age;
		}
		public Integer getSsn() {
			return ssn;
		}
		public void setSsn(Integer ssn) {
			this.ssn = ssn;
		}
		public BufferedImage getImage() {
			return image;
		}
		public void setImage(String imageLocation) {
		    File file = new File( imageLocation );
		    if ( !file.canRead() )
		    	throw new IllegalArgumentException( "Cannot read file at \"" + imageLocation + "\"" );
		    try {
		    image = ImageIO.read( file );
		    } catch (IOException e) {
		    	throw new IllegalArgumentException( e );
		    }
		}

	protected String name;
    protected int age;
    protected Date birthday;
    protected Integer ssn;
    protected BufferedImage image;
}