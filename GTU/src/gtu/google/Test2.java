package gtu.google;

import java.io.IOException;
import java.sql.SQLException;

import com.google.common.base.Throwables;

public class Test2 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException  {
		try {
		} catch (Throwable t) {
			Throwables.propagateIfInstanceOf(t, IOException.class);
			Throwables.propagateIfInstanceOf(t, SQLException.class);
			throw Throwables.propagate(t);
		}
	}

}
