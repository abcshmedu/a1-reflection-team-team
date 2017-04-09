package edu.hm;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Yo Havlik
 *
 */
public class Renderer {

    private Object obj;

	/**
	 * Create new Renderer for specified object.
	 * @param obj Object to render fields from
	 */
	public Renderer(Object obj) {
		this.obj = obj;
	}

	/**
	 * @return String representation of annotated fields
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public String render() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		Class< ? > c = obj.getClass();
		Field[] fields = c.getDeclaredFields();
		Method[] methods = c.getDeclaredMethods();
		String output = "Instance of " + c.getName() + ":\n";

		//Render annotated fields
		for (Field f : fields) {
			f.setAccessible(true);
			if (f.getDeclaredAnnotation(RenderMe.class) instanceof RenderMe) {
				Class< ? > renderClass = Class.forName(f.getDeclaredAnnotation(RenderMe.class).with());
				output += f.getName() + " (Type " + f.getType().getSimpleName() + "): ";
				
				if (renderClass == this.getClass()) {
					Object value = f.get(obj);
					output += value + "\n";
				} else {
					try {
						Method m = renderClass.getDeclaredMethod("render", Object[].class);
						Object[] uarr = unpackArray(f.get(obj));
						output += m.invoke(renderClass, new Object[]{uarr}) + "\n";
					} catch (NoSuchMethodException | SecurityException | InvocationTargetException e) {
						e.printStackTrace();
					}	
				}
			}
		}
		
		//Invoke annotated methods
		for (Method m : methods) {
			m.setAccessible(true);
			if (m.getDeclaredAnnotation(RenderMe.class) instanceof RenderMe) {
				try {
					output += m.getName() + " returns: " + m.invoke(c) + "\n";
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		return output;
	}
	
	/**
	 * @param arr Array Object to unpack
	 * @return unpacked Object[] 
	 */
	private static Object[] unpackArray(Object arr) {
		Object[] arrOut = new Object[Array.getLength(arr)];
		for (int i = 0; i < arrOut.length; i++) {
			arrOut[i] = Array.get(arr, i);
		}
		return arrOut;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SomeClass sc = new SomeClass(5);
		Renderer r = new Renderer(sc);
		try {
			System.out.println(r.render());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
