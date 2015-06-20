import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.http.client.ClientProtocolException;

/**
 * This class represents Logic rules context reasoning decision model.
 * 
 * @author Bambanza.
 *
 */
public class LogicRules {
	protected void connect() throws SQLException, InterruptedException,
			ClientProtocolException, IOException {

		OpenhabClient state_tv = new OpenhabClient("localhost", 8080);
		OpenhabClient state_cooker = new OpenhabClient("localhost", 8080);
		OpenhabClient state_kettle = new OpenhabClient("localhost", 8080);
		OpenhabClient state_morning = new OpenhabClient("localhost", 8080);
		OpenhabClient state_presence = new OpenhabClient("localhost", 8080);
		OpenhabClient state_LK = new OpenhabClient("localhost", 8080);
		OpenhabClient state_LR = new OpenhabClient("localhost", 8080);
		PushbulletClient RulePush = new PushbulletClient();
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "openhab";
		String userName = "openhab";
		String password = "openhab";
		java.sql.Statement st = null;
		java.sql.Statement st2 = null;
		java.sql.Statement st3 = null;
		java.sql.Statement st5 = null;
		java.sql.Statement st6 = null;
		java.sql.Statement st7 = null;
		java.sql.Statement st9 = null;
		java.sql.Statement st10 = null;

		Connection conn = DriverManager.getConnection(url + dbName, userName,
				password);
		st = conn.createStatement();

		/* Rule for heart Beat rate */

		ResultSet res = st
				.executeQuery("SELECT * From Item7 ORDER BY Time DESC LIMIT 1");

		while (res.next()) {
			int Value = res.getInt("Value");
			if (Value >= 100||Value <= 60) {
				try {
					RulePush.sendNote("Heart Beat rate",
							"Abnomal heart beat rate");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		/* Rule for calculating the average of Blood Pressure for a period of time */

		st2 = conn.createStatement();
		ResultSet res2 = st2
				.executeQuery("SELECT AVG(Value) AS value_Average FROM Item8 WHERE `Time` BETWEEN '2015-select th05-20' AND '2015-06-07'");
		while (res2.next()) {

			int Value = res2.getInt("value_Average");

			if (Value >20) {
				try {
					RulePush.sendNote("Blood Pressure","The blood pressure increased");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
		/* Turn ON/OFF light and/or the TV */
		
		st10 = conn.createStatement();
		ResultSet res10 = st10
				.executeQuery("SELECT * From Item21 ORDER BY Time DESC LIMIT 1");
		while (res10.next()) {
			Double Value = res10.getDouble("Value");
			if (Value < 8.0) {
				try {
					state_LR.pushItemValue("LivingRoom_Light", "ON");
					if (Value<5.0) {
						state_tv.pushItemValue("Tv_Switch", "ON");
					} else {
						state_tv.pushItemValue("Tv_Switch", "OFF");
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
               else {
				
            	   try {
					state_LR.pushItemValue("LivingRoom_Light", "OFF");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/* Switch ON/OFF appliances in the kitchen */
		st9 = conn.createStatement();
		ResultSet res9 = st9
				.executeQuery("SELECT * From Item20 ORDER BY Time DESC LIMIT 1");
		while (res9.next()) {
			Double Value = res9.getDouble("Value");

			if (Value < 7.0) {
                 try {
					state_LK.pushItemValue("Kitchen_Light", "ON");

					if (Value < 2.3 && Value > 1.5) {
						try {
							state_kettle.pushItemValue("Kettle_Switch", "ON");
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					else {
						try {
							state_kettle.pushItemValue("Kettle_Switch", "OFF");
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (Value <= 1.2) {
						state_cooker.pushItemValue("Cooker_Switch", "ON");
					} else {
						state_cooker.pushItemValue("Cooker_Switch", "OFF");
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
                    e.printStackTrace();
				}
			} else {
				state_LK.pushItemValue("Kitchen_Light", "OFF");
			}
		}

		st7 = conn.createStatement();
		ResultSet res7 = st7
				.executeQuery("SELECT AVG(Value) AS value_Average FROM Item19 WHERE `Time` BETWEEN '2015-select th05-20' AND '2015-06-07'");
		while (res7.next()) {

			int Value = res7.getInt("value_Average");

			if (Value > 1000) {
				try {
					RulePush.sendNote("Weight check",
							"the weight average increased");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			else if (Value < 1) {
				try {
					RulePush.sendNote("Weight check",
							"the weight average decreased");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		/*
		 * Switch the light ON in the morning after checking and LUX and the
		 * occupancy.
		 */

		st3 = conn.createStatement();
		ResultSet res3 = st3
				.executeQuery("SELECT * From Item10 ORDER BY Time DESC LIMIT 1");
		while (res3.next()) {
			String Value = res3.getString("Value");
			if (Value.equals("ON")) {
				try {
					st5 = conn.createStatement();
					ResultSet res5 = st5
							.executeQuery("SELECT * From Item2 ORDER BY Time DESC LIMIT 1");
					while (res5.next()) {
						int Value1 = res5.getInt("Value");

						if (Value1 <= 100) {
							state_morning.pushItemValue("Light_Switch", "ON");
							 state_kettle.pushItemValue("Kettle_Switch", "ON");
							}
					}
				} catch (IOException e) {

					e.printStackTrace();
				}

			} else if (Value.equals("OFF")) {

				{
					st6 = conn.createStatement();
					ResultSet res6 = st6
							.executeQuery("SELECT * From Item9 ORDER BY Time DESC LIMIT 1");
					while (res6.next()) {
						String Value1 = res6.getString("Value");

						if (Value1.equals("CLOSED")) {
							try {

								 state_presence.pushItemValue("Light_Switch","OFF");
								 state_kettle.pushItemValue("Kettle_Switch", "OFF");
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						else if (Value1.equals("OPEN")) {
							try {
                               state_presence.pushItemValue("Light_Switch",
										"ON");
							} catch (ClientProtocolException e) {
								
								e.printStackTrace();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
						}
					}
				}

			}

		}
  }
}
