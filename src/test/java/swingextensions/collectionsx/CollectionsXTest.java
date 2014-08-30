package swingextensions.collectionsx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import swingextensions.collectionsx.CollectionsX;

/**
 * Unit tests to validate this class.  
 * @author <a href="mailto:dan@danbecker.info">Dan Becker</a>
 */
public class CollectionsXTest 
{
    @Test
    public void testObservable() 
    {
    	List<String> stringList = new LinkedList<String>();
    	ObservableList<String> observable = CollectionsX.observableList( stringList );

    	observable.add( "Foo" );
    	observable.add( "Bar" );
    	observable.add( "Baz" );
    	observable.remove( "Bar" );
    	
    	assertTrue( "List length", observable.size() == 2 );
    	assertEquals( "List element 0", "Foo",  observable.get( 0 ) );
    	assertEquals( "List element 1", "Baz",  observable.get( 1 ) );
    }    

    @Test
    public void testObserver() 
    {
    	List<String> stringList = new LinkedList<String>();
    	ObservableList<String> observable = CollectionsX.observableList( stringList );
   	
    	ListListener<String> listener = new ListListener<String>();
    	observable.addObservableListListener(listener);
    	
    	observable.add( "Foo" );
    	observable.add( "Bar" );
    	observable.add( "Baz" );
    	observable.remove( "Bar" );
    	observable.set( 1, "NewBaz");
    	
    	final List<String> resultList = listener.getList();
    	
    	assertTrue( "List length", resultList.size() == 2 );
    	assertEquals( "List element 0", "Foo",  resultList.get( 0 ) );
    	assertEquals( "List element 1", "NewBaz",  resultList.get( 1 ) );
    }    

    public class ListListener<T> implements ObservableListListener<T> {
    	protected List<T> localList = new LinkedList<T>();
    	
    	// ObservableListListener
        public void listElementsAdded(ObservableList<T> list, int index, int length) {
        	for ( int i = index; i < index + length; i++ ) {
        		T element = list.get( i );
        		localList.add( element );
        	}
        }
        public void listElementsRemoved(ObservableList<T> list, int index, List<T> oldElements){
            for( T element : oldElements ) {
            	localList.remove( element );
            }
        }
        public void listElementReplaced(ObservableList<T> list, int index, T oldElement){
        	localList.set( index, list.get( index ));

        }
        public void listElementPropertyChanged(ObservableList<T> list, int index){
        	localList.set( index, list.get( index ));
        }

        public final List<T> getList() {
        	return localList;
        }
    }
}