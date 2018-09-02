package gtu.system;

import java.util.Enumeration;

public class SystemProps {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(Enumeration it = System.getProperties().keys(); it.hasMoreElements() ;){
			String key = (String)it.nextElement();
			Object value = System.getProperties().get(key);
			System.out.println(key +"\t\t" + value);
		}
	}
}
