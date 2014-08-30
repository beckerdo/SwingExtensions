package swingextensions.utils;


/**
 * A strongly typed holder of data.
 * @author <a href="mailto:dan@danbecker.info">Dan Becker</a>
 */
public class ValueHolder<Type> {

	private Type value;
	private Class<Type> cls;

	protected ValueHolder() {
	}

	public ValueHolder(Type value, Class<Type> cls) {
		this.value = value;
		this.cls = cls;
	}

	public Type getValue() {
		return value;
	}

	public void setValue(Type value) {
		this.value = value;
	}

	public Class<Type> getType() {
		return cls;
	}

	// ValueHolder<String> v = ValueHolder.create("hello world",String.class);
	public static <Type> ValueHolder<Type> create(Type value, Class<Type> cls) {
		return new ValueHolder<Type>(value, cls);
	}
}
