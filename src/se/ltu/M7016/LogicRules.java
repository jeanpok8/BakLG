package testing;
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

		OpenhabClient state_tv = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
		OpenhabClient state_cooker = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
		OpenhabClient state_kettle = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
		OpenhabClient state_morning = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
		OpenhabClient state_presence = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
		OpenhabClient state_Switch = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
		
		OpenhabClient state_LK = new OpenhabClient("localhost", 8080);
		OpenhabClient state_LR = new OpenhabClient("localhost", 8080);
		PushbulletClient RulePush = new PushbulletClient();
		OpenhabClient pull= new OpenhabClient("localhost", 8080);
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "openhab";
		String userName = "openhab";
		String password = "openhab";
		java.sql.Statement st= null;
		java.sql.Statement st2=null;
		java.sql.Statement st3=null;
		java.sql.Statement st5=null;
		java.sql.Statement st6=null;
		java.sql.Statement st7=null;
		java.sql.Statement st9=null;
		java.sql.Statement st10=null;
		java.sql.Statement st11=null;
		java.sql.Statement st12=null;
		java.sql.Statement st13=null;
		
		
	      
		Connection conn = DriverManager.getConnection(url + dbName, userName,
				password);
		st = conn.createStatement();
		
		/*
		 * Switch the light ON in the morning after checking LUX and the
		 * occupancy at a given time.
		 */
    
		st3 = conn.createStatement();
		ResultSet res3 = st3
				.executeQuery("SELECT * From Item15 ORDER BY Time DESC LIMIT 1");
		while (res3.next()) {
			String Value = res3.getString("Value");
			if (Value.equals("ON")) {
				try {
					
					st5 = conn.createStatement();
					ResultSet res5 = st5
							.executeQuery("SELECT * From Item18 ORDER BY Time DESC LIMIT 1");
					while (res5.next()) {
						int Value1 = res5.getInt("Value");
						if (Value1 <= 200) {
							 state_morning.pushItemValue("Meter_Switch", "ON");
							 state_kettle.pushItemValue("Kettle_Switch", "ON");
							}
					}
				} catch (IOException e) {

					e.printStackTrace();
				}

			} else if (Value.equals("OFF")) {
               {
				try {
                    state_Switch.pushItemValue("Meter_Switch","OFF");
					 state_kettle.pushItemValue("Kettle_Switch", "OFF");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
                       }}}
                    
		
		

		/* Rule for blood pressure compare to 120/80mmHg*/
		
	  ResultSet res = st.executeQuery("SELECT * From Item2 ORDER BY Time DESC LIMIT 1");
        while (res.next()) {
			float Value = res.getFloat("Value");
			Float value=new Float(1.5f);
		
			if (Value!=value){
				try {
					RulePush.sendNote("Blood pressure"," abnormal blood pressure");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}}
		}

		/* Rule for calculating the average of HeartPulse per minute in a period of 1 day*/

		st2 = conn.createStatement();
		ResultSet res2 = st2
				.executeQuery("SELECT AVG(Value) AS value_Average FROM Item1 WHERE `Time` BETWEEN '2015-08-20' AND '2015-08-20'");
		while (res2.next()) {

			int Value = res2.getInt("value_Average");
            
			if ( Value<80 && Value >136) {
				try {
					RulePush.sendNote("Blood Pressure"," Abnormal Heart Pulse");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
			}
		/* Turn ON/OFF light and/or the TV */
		
		st10 = conn.createStatement();
		ResultSet res10 = st10.executeQuery("SELECT * From Item3 ORDER BY Time DESC LIMIT 1");
		while (res10.next()) {
			Double Value = res10.getDouble("Value");
			if (Value < 8.0) {
				try { 
					state_LR.pushItemValue("LivingRoom_Light", "ON");
					
					if (Value<5.0){
						state_tv.pushItemValue("Tv_Switch", "ON");
					} else {
					 state_tv.pushItemValue("Tv_Switch", "OFF");
						    }

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}}
			
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
				.executeQuery("SELECT * From Item4 ORDER BY Time DESC LIMIT 1");
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
				.executeQuery("SELECT AVG(Value) AS value_Average FROM Item22 WHERE `Time` BETWEEN '2015-08-19' AND '2015-08-19'");
		while (res7.next()) {

			  int Value = res7.getInt("value_Average");
              if (Value >70) {
				try {
					RulePush.sendNote("Weight check",
							"the weight average increased");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			else if (Value < 0) {
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
		
		/*Rule to switch ON the light*/
		st11= conn.createStatement();
		ResultSet res11 = st11
				.executeQuery("SELECT * From Item21 ORDER BY Time DESC LIMIT 1");
		while (res11.next()) {
			String Value = res11.getString("Value");
		 if (Value.equals("OPEN")) {
			try {
				state_presence.pushItemValue("Light_Switch","ON");
				} catch (ClientProtocolException e)  {
				e.printStackTrace();
			}
		}
		 else {
			 try {
					
	               state_presence.pushItemValue("Light_Switch",
							"OFF");
				} catch (ClientProtocolException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			 
		 }
	}
		
		/*Rule to not combine two dangerous actions*/
		
		st12 = conn.createStatement();
		ResultSet res12 = st12
				.executeQuery("SELECT * From Item15 ORDER BY Time DESC LIMIT 1");
		while (res12.next()) {
			
			String Value = res12.getString("Value");
			if (Value.equals("ON")) {
				try {
					st13 = conn.createStatement();
					ResultSet res13 = st13
							.executeQuery("SELECT * From Item16 ORDER BY Time DESC LIMIT 1");
					while (res13.next()) {
						String Value1 = res13.getString("Value");
						if (Value1.equals("ON")) {
							RulePush.sendNote("Warning",
									"Please don't sleep and cook");
							}
					}
				} catch (IOException e) {

					e.printStackTrace();
				}
		
	}}}}

