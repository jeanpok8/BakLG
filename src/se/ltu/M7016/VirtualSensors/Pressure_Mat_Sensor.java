
package WFakeSensor;

import java.sql.*;
import java.util.Random;

public class PressureMatSensor {

	/*
	 * A Simple program to Push data in MySQL database to simulate Pressure mat sensor for bed Occupency
	 */
       public static void main(String[] args) {
               String url = "jdbc:mysql://localhost:3306/";
		String dbName = "openhab";
		String userName = "openhab";
		String password = "openhab";
		String PMS_Occupancy ="ON";
		String PMS_Occupancy1 = "OFF";
		int i = 1;

		while (true) {

			try {
				/*
				 * Connect to a database with different parameters used to
				 * connect to the database.
				 */

				Connection conn = DriverManager.getConnection(url + dbName,
						userName, password);

				/* Populate the database using PreparedStatemets. with a loop to simulate the slept time */

				String sql = "INSERT INTO Item21 (Time,value) values (?, ?)";

				if (i > 100) {
					java.sql.PreparedStatement statement = conn
							.prepareStatement(sql);
					statement.setString(2,PMS_Occupancy);
					statement.setTimestamp(1,
							new Timestamp(System.currentTimeMillis()));
					statement.executeUpdate();
					statement.clearBatch();
				}

				else {

					java.sql.PreparedStatement statement = conn
							.prepareStatement(sql);
					statement.setString(2, PMS_Occupancy1);
					statement.setTimestamp(1,
							new Timestamp(System.currentTimeMillis()));
					statement.executeUpdate();
					statement.clearBatch();

				}

				/* print the content of the table for just testing Purpose. */

				Statement st = conn.createStatement();
				ResultSet res = st.executeQuery("SELECT * FROM  Item21");
				while (res.next()) {
					int Time = res.getInt("Time");
					String Value = res.getString("Value");
					System.out.println(Time + "\t" + Value );
				}
				conn.close(); // close the database connection.

			} catch (SQLException e) {
				e.printStackTrace();
			}

			/*
			 * Make the thread to sleep for a specific time to persist data
			 * after 10 seconds.
			 */
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			i++;

		}

	}
}

