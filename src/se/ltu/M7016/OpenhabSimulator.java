import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
/*This class is used to generate data and serves as a client binding to OpenHAB*/
public class OpenhabSimulator {

	public static void main(String[] args) throws InterruptedException, ClientProtocolException, IOException {
		
		while (true) {
            OpenhabClient heartBrPush = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
			OpenhabClient weightPush = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
			OpenhabClient blPressurePush = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
			OpenhabClient motionSensor = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
			OpenhabClient motionS = new OpenhabClient("OpenHAB_Address", OpenHAB_Port);
			
			LogicRules tst = new LogicRules();
			int Brate = 0;
			double WT_BloodPressure = 0;
			int weight = 0;
			
			double motion=0.1;
			try {
				Random rand1 = new Random();
				for (int i = 0; i < 2; i++) {
					Brate = rand1.nextInt(41) + 60;
				}
				
				Random rand2 = new Random();
				for (int i = 0; i < 1; i++) {
					WT_BloodPressure = rand2.nextInt(2) + 0.5;
				}
				Random rand3 = new Random();
				for (int i = 0; i < 1; i++) {
					motion = rand3.nextInt(8) + 0.01;
				}
				
				Random rand4 = new Random();
				for (int i = 0; i < 1; i++) {
					weight = rand4.nextInt(41) + 30;
				}
                                heartBrPush.pushItemValue("WT_HBrate", Integer.toString(Brate));
                                weightPush.pushItemValue("WT_Weight", "80");
				blPressurePush.pushItemValue("WT_BloodPressure","1.5");
				motionSensor.pushItemValue("LivingRoom_Motion",Double.toString(motion));
		                motionS.pushItemValue("Kitchen_Motion","10");
			
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				tst.connect();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
              Thread.sleep(10 * 1000);
		}

	}
}

