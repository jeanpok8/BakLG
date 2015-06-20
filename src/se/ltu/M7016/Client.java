import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;

public class test {

	public static void main(String[] args) throws InterruptedException, ClientProtocolException, IOException {
		while (true) {

			OpenhabClient heartBrPush = new OpenhabClient("localhost", 8080);
			OpenhabClient weightPush = new OpenhabClient("localhost", 8080);
			OpenhabClient blPressurePush = new OpenhabClient("localhost", 8080);
			OpenhabClient motionSensor = new OpenhabClient("localhost", 8080);
			OpenhabClient motionS = new OpenhabClient("localhost", 8080);
			
			Statements tst = new Statements();
			int Brate = 0;
			double WT_BloodPressure = 0;
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
					//motion=i;
					motion = rand3.nextInt(8) + 0.01;
				}
        heartBrPush.pushItemValue("WT_HBrate", Integer.toString(Brate));
				weightPush.pushItemValue("WT_Weight", "30");
				blPressurePush.pushItemValue("WT_BloodPressure","12");
				motionSensor.pushItemValue("LivingRoom_Motion",Double.toString(motion));
				motionS.pushItemValue("Kitchen_Motion","4");
		
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				tst.connect();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Thread.sleep(24 * 1000);
		}

	}
}
