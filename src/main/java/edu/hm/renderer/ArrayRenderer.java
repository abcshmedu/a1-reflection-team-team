package edu.hm.renderer;

/**
 * @author Yo Havlik
 *
 */
public class ArrayRenderer {
	/**
	 * @param arr Array of objects
	 * @return String representation of the Array
	 */
	public static String render(Object[] arr) {
		String output = "[";
		for (Object o : arr) {
			output += " " + o.toString() + ",";
		}
		return output + "]";
	}
}
