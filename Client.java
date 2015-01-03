import java.net.*;
import java.io.*;
import java.nio.channels.*;
import java.util.Date;

public class Client {
    public static void main(String[] args) throws Exception {
        Date date = new Date();
        System.out.println(date.toString());
		String logType = args[0];
		String secondParam = "";
		String secondParamString = "";
		URL url;
		int valuesPerLine = 4;
		String logFileName = "fast_log";
		if(logType.equals("n")) {
			valuesPerLine = 16;
			logFileName = "normal_log";
		}
		if(args.length > 1) {
			secondParam = args[1];
			if(logType.equals("f")) {
				secondParamString = "&seconds=";
			} else {
				secondParamString = "&solenoids=";
			}
		}
		url = new URL("http://169.254.101.102/?log=" + logType + secondParamString + secondParam + ";");
		
		BufferedReader in = null;
		PrintWriter out = null;
		URLConnection connection = null;
		
		try {
			connection = url.openConnection();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			out = new PrintWriter(logFileName);
			
			int hiByte, lowByte, value, count = 0;
			
			while ( ((hiByte = in.read()) != -1) && ((lowByte = in.read()) != -1) )  {
                value = ((hiByte << 8) | lowByte);
                out.print(Integer.toString(value) + ',');
                count++;
                if(count % valuesPerLine == 0) {
                    out.println();
					out.flush();
				}
            }
		} finally {
			if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            date = new Date();
            System.out.println(date.toString());
		}
    }
}