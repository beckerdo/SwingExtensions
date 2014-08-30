package swingextensions.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

/**
 * Describes the methods to persist to various services and media.
 * Distinguishes between running from application and running from web start.
 */
public abstract class PersistenceHandler {
    private static final boolean inWebStart;
    
    static {
        boolean inWS;
        try {
            ServiceManager.lookup("javax.jnlp.BasicService");
            inWS = true;
        } catch (UnavailableServiceException ex) {
            inWS = false;
        }
        inWebStart = inWS; // one assignment for final var.
    }
    
    public static PersistenceHandler getHandler( String storeName ) {
        if (inWebStart) {
            return new JWSPersistenceHandler( storeName );
        }
        return new FilePersistenceHandler( storeName );
    }
    
    PersistenceHandler() {
    }
    
    public abstract boolean exists();
    
    public abstract InputStream getInputStream() throws IOException;
    
    public abstract OutputStream getOutputStream() throws IOException;
    
}