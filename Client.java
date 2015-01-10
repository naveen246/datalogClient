import java.net.*;
import java.io.*;
import java.nio.channels.*;
import java.util.Date;

public class Client {

	private static final String BASE_URL = "http://169.254.101.102/";
	private static final String FAST_LOG_FILE_NAME = "fast_log.csv";
	private static final String NORMAL_LOG_FILE_NAME = "normal_log.csv";

	//Example Urls
	//Fast Log - http://169.254.101.102/?log=f&seconds=3&channels=3,4,6,7;
	//Normal Log - http://169.254.101.102/?log=n&solenoids=1,3,5;
	public static String getUrlString(String[] args) {
		String firstParamName = "?log=";
		String fastLogSecondParamName = "&seconds=";
		String normalLogSecondParamName = "&solenoids=";
		String secondParamValue = "";
		String thirdParamName = "channels";
		String thirdParamValue = "";
		String urlEndIdentifier = ";";
		if(args.length > 1) {
			secondParamValue = args[1];
		}
		if(args.length > 2) {
			thirdParamValue = args[2];
		}
		String logType = "n";
		String secondParamName = normalLogSecondParamName;
		if(args[0].equals("f")) {
			logType = "f";
			secondParamName = fastLogSecondParamName;
		}
		String url = BASE_URL + firstParamName + logType + secondParamName + secondParamValue + urlEndIdentifier;
		if(logType == "f") {
			url += thirdParamName + thirdParamValue;
		}
		return url;
	}

    public static void main(String[] args) {
    	if(args.length == 0) {
    		return;
    	}
        Date date = new Date();
        System.out.println(date.toString());
		String logType = args[0];
		
		int valuesPerLine = 16;
		String logFileName = NORMAL_LOG_FILE_NAME;
		if(logType.equals("f")) {
			if(args.length < 2) return;
			valuesPerLine = 4;
			logFileName = FAST_LOG_FILE_NAME;
		}

		URL url = null;
		URLConnection connection = null;
		BufferedReader in = null;
		PrintWriter out = null;
		
		try {
			url = new URL(getUrlString(args));
			connection = url.openConnection();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			out = new PrintWriter(logFileName);
			
			int hiByte, lowByte, value, count = 0;
			
			while ( ((hiByte = in.read()) != -1) && ((lowByte = in.read()) != -1) )  {
                value = ((hiByte << 8) | lowByte);
                out.print(Integer.toString(value) + ',');
                count++;
                if(count % valuesPerLine == 0) {
                    out.println("\t" + System.currentTimeMillis() % 10000);
					out.flush();
				}
            }

            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
		} catch(Exception e) {
			System.out.println("Connection Error : Disconnect the DataLogger, restart the DataLogger by powering off and on, connect the DataLogger");
		} finally {
            date = new Date();
            System.out.println(date.toString());
		}
    }
}