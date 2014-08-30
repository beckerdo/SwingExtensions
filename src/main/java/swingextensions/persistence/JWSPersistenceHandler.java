/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package swingextensions.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.jnlp.BasicService;
import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

/**
 * PersistenceHandler implementation targeting JavaWebStart.
 */
public class JWSPersistenceHandler extends PersistenceHandler {
    private final PersistenceService persistenceService; 
    private final BasicService basicService; 
    private final String storeName;

    public JWSPersistenceHandler( String storeName ) {
        PersistenceService persistenceService;
        BasicService basicService;
        try {
            persistenceService = 
            	(PersistenceService)ServiceManager.lookup("javax.jnlp.PersistenceService");
            basicService = 
            	(BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
        } catch (UnavailableServiceException e) {
            persistenceService = null;
            basicService = null;
            throw new RuntimeException(e);
        }
        this.persistenceService = persistenceService;
        this.basicService = basicService;
        this.storeName = storeName;
    }
    
    private URL getXMLURL() {
        URL codebase = basicService.getCodeBase();
        try {
            URL url = new URL(codebase.toString() + storeName );
            return url;
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public boolean exists() {
        // Ideally ask for the muffins (getNames), but that appears to throw an NPE.
        try {
            persistenceService.get(getXMLURL());
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public InputStream getInputStream() throws IOException {
        // find all the muffins for our URL
        URL xmlURL = getXMLURL();
        FileContents contents;
        try {
            contents = persistenceService.get(xmlURL);
        } catch (MalformedURLException ex) {
            throw new IOException();
        }
        return contents.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        URL xmlURL = getXMLURL();
        FileContents contents = null;
        try {
            contents = persistenceService.get(xmlURL);
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
        if (contents == null) {
            persistenceService.create(xmlURL, 10000);
            try {
                contents = persistenceService.get(xmlURL);
            } catch (MalformedURLException ex) {
            } catch (IOException ex) {
            }
            if (contents == null) {
                throw new IOException();
            }
        }
        return contents.getOutputStream(true);
    }
}