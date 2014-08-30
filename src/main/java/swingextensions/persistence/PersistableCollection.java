package swingextensions.persistence;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents the collection that can be loaded/saved as XML.
 * The load/save streams can be provided by a PersistenceHandler.
 */
public class PersistableCollection<T> {
	protected Collection<T> collection;
    
    public PersistableCollection() {
        setCollection( new ArrayList<T>(1) );
    }
    
    public PersistableCollection( Collection<T> collection) {
        setCollection( collection );
    }
    
    public void load(String file) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        try {
            load(inputStream);
        } finally {
            inputStream.close();
        }
    }
    
    @SuppressWarnings("unchecked")
    public void load(InputStream stream) {
        XMLDecoder decoder = null;
        decoder = new XMLDecoder(stream);
        decoder.setExceptionListener(new ExceptionHandler());
        List<T> entries = (List<T>)decoder.readObject();
        decoder.close();
        decoder = null;
        collection.clear();
        collection.addAll(entries);
    }
    
	public void save(String file) throws IOException {
		BufferedOutputStream outputStream = 
			new BufferedOutputStream( new FileOutputStream(file) );
		try {
			save(outputStream);
		} finally {
			outputStream.close();
		}
	}
    
    public void save(OutputStream stream) throws IOException {
        XMLEncoder encoder = null;
        encoder = new XMLEncoder(stream);
        encoder.setExceptionListener(new ExceptionHandler());
        // XMLDecoder doesn't handle a private inner class.
        // By creating a new ArrayList we ensure it's saved correctly.
        // encoder.writeObject(new ArrayList<T>(collection));
        encoder.writeObject(collection);
        encoder.close();
        encoder = null;
    }
    
    public void setCollection( Collection<T> collection ) {
        this.collection = collection;
    }
    
    public final Collection<T> getCollection() {
        return collection;
    }
    
    public final List<T> getList() {
        return new ArrayList<T>( collection );
    }
    
    private static class ExceptionHandler implements ExceptionListener {
        public void exceptionThrown(Exception e) {
            if (e instanceof IOException) {
                throw new RuntimeException("IOException", e);
            }
        }
    }
}