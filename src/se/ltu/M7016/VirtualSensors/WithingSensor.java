import java.sql.*;
import java.util.Random;
  
  /*
	 * A Simple program to Push data in MySQL database to simulate the Withings
	 * sensor with Heart beat rate and Blood pressure parameters.
	 */

public class WithingsSensor {
public static void main(String[] args) {
                String url = "jdbc:mysql://localhost:3306/";
		String dbName = "openhab";
		String userName = "openhab";
		String password = "openhab";
		int WT_HBrate = 0;
		double WT_BloodPressure = 0;
		int WT_Weight=0;

		while (true) {

			try {   /*
				 * Connect to a database with different parameters.
				 * 
				 */

				Connection conn = DriverManager.getConnection(url + dbName,
						userName, password);

				/*
				 * create random numbers for WT_HBrate,WT_Weight and WT_Bloodpressure
				 * parameters.
				 */

				Random rand1 = new Random();
				for (int i = 0; i < 2; i++) {
					WT_HBrate = rand1.nextInt(41) + 60;
				}

				Random rand2 = new Random();
				for (int i = 0; i < 1; i++) {
					WT_BloodPressure = rand2.nextInt(2) + 0.5;
				}
				
				Random rand3 = new Random();
				for (int i = 0; i < 1; i++) {
					WT_Weight=rand3.nextInt(4)+60;
				}

				/* Populate the database using PreparedStatemets. */

				String sql = "INSERT INTO Item8 (Time,value) values (?, ?)";
				String sql2 = "INSERT INTO Item7 (Time,value) values (?, ?)";
				String sql3 = "INSERT INTO Item19 (Time,value) values (?, ?)";
				java.sql.PreparedStatement statement = conn
						.prepareStatement(sql);
				java.sql.PreparedStatement statement2 = conn
						.prepareStatement(sql2);
				java.sql.PreparedStatement statement3 = conn
						.prepareStatement(sql3);
				statement.setDouble(2, WT_BloodPressure);
				statement.setTimestamp(1,
						new Timestamp(System.currentTimeMillis()));
				statement.executeUpdate();
				statement.clearBatch();
				statement2.setInt(2, WT_HBrate);
				statement2.setTimestamp(1,
						new Timestamp(System.currentTimeMillis()));
				statement3.setInt(2, WT_Weight);
				statement3.setTimestamp(1,
						new Timestamp(System.currentTimeMillis()));
				statement2.executeUpdate();
				statement3.executeUpdate();
				

				/* print the content of Item7 for just testing. */
				
				Statement st = conn.createStatement();
				ResultSet res = st.executeQuery("SELECT * FROM  Item19");
				while (res.next()) {
					int Time = res.getInt("Time");
					String Value = res.getString("Value");
					System.out.println(Time + "\t" + Value);
				}
				conn.close(); //close the database connection.
				

			} catch (SQLException e) {
				e.printStackTrace();
			}

			/*
			 * Make the thread to sleep for a specific time to persist data after 10
			 * seconds.
			 */
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
