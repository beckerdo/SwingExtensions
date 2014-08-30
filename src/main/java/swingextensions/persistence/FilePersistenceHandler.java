package swingextensions.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstracts away the ability to save files. This is needed to distinguish
 * between running from the command line, and running from web start.
 */
public class FilePersistenceHandler extends PersistenceHandler {
	
	public final String storeName;
	
	public FilePersistenceHandler( String storeName ) {
		this.storeName = storeName;
	}
	
	// Returns the path to save the password file.
	// In a real app the user would be prompted for the location.
	private String getContentsPath() {
		return storeName;
	}

	public boolean exists() {
		return new File(getContentsPath()).exists();
	}

	public OutputStream getOutputStream() throws IOException {
		return new BufferedOutputStream(new FileOutputStream(getContentsPath()));
	}

	public InputStream getInputStream() throws IOException {
		return new BufferedInputStream(new FileInputStream(getContentsPath()));
	}
}
