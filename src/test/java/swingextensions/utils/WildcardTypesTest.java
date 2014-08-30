package swingextensions.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

/**
 * I need to do a concrete example to understand generics wild card types!
 *   
 * @author <a href="mailto:dan@danbecker.info">Dan Becker</a>
 */
public class WildcardTypesTest 
{
    @Test
    public void testGenSuper() 
    {
        Stack<Number> numberStack = new Stack<Number>();
        Iterable<Integer> integers = Arrays.asList(3, 1, 4, 1, 5, 9);
        numberStack.pushAll(integers);
        System.out.println( "Stack<Number> with Integers=" + numberStack );

        Collection<Object> objects = new ArrayList<Object>();
        numberStack.popAll(objects);
        System.out.println( "Collection<Object> with Integers=" + objects);
    }    

    // Stack with Generics. Bulk methods use wildcard types.
    // Taken from Joshua Block Effective Java 2 - Chapter 5 Generics, Item 28 Bounded Wildcards.
    // toString added by Dan Becker
    public class Stack<E> {
        private E[] elements;
        private int size = 0;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;

        // The elements array will contain only E instances from push(E).
        // This is sufficient to ensure type safety, but the runtime
        // type of the array won't be E[]; it will always be Object[]!
        @SuppressWarnings("unchecked") 
        public Stack() {
            elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
        }

        public void push(E e) {
            ensureCapacity();
            elements[size++] = e;
        }

        public E pop() {
            if (size==0)
                throw new EmptyStackException();
            E result = elements[--size];
            elements[size] = null; // Eliminate obsolete reference
            return result;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        private void ensureCapacity() {
            if (elements.length == size)
                elements = Arrays.copyOf(elements, 2 * size + 1);
        }

        // pushAll method without wildcard type - deficient!
        //  public void pushAll(Iterable<E> src) {
        //          for (E e : src)
        //              push(e);
        //  }

         // Wildcard type for parameter that serves as an E producer
        // Mnemonic - PECS producer-extends, consumer-super.
        public void pushAll(Iterable<? extends E> src) {
            for (E e : src) {
            	// Illustration of casts. Either works.
               	// if ( Number.class.isAssignableFrom( e.getClass() ) ) {
            	// if ( e instanceof Number ) {
            	//	Number number = (Number) e;
            	//	System.out.println( "Popping number=" + number);
            	// }
                push(e);
            }
        }

        // popAll method without wildcard type - deficient!
        //  public void popAll(Collection<E> dst) {
        //          while (!isEmpty())
        //              dst.add(pop());
        //  }

        // Wildcard type for parameter that serves as an E consumer
        public void popAll(Collection<? super E> dst) {
            while (!isEmpty())
                dst.add(pop());
        }

        @Override
        /** String of contents, trying to match Collection.toString(). */
        public synchronized String toString() {
        	StringBuilder sb = new StringBuilder( "[" );
            for ( int i = 0; i < size; i++ ) {
            	E e = elements[ i ];
            	sb.append( e );
            	if ( i + 1 < size )
                	sb.append( ", " );
            }
        	sb.append( "]");
        	return sb.toString();
        }
    }
    
    public class EmptyStackException extends RuntimeException {
		private static final long serialVersionUID = 1L;
    }
}