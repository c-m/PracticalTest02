package ro.pub.cs.systems.pdsd.practicaltest02.General.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ro.pub.cs.systems.pdsd.practicaltest02.General.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02.General.Utilities;

public class CommunicationThread extends Thread{

	private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }
    
    @Override
    public void run() {
    	if (socket != null) {
    		try {
    			BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                
                if (bufferedReader != null && printWriter != null) {
                	
                	Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (get / put)!");
                    String request = bufferedReader.readLine();
                    HashMap<String, String> data = serverThread.getData();
                    
                    if (request.startsWith("put")){
                    	String minute = null;
                    	
                    	HttpClient httpClient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet("http://www.timeapi.org/utc/now");
                    	
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        String pageSourceCode = httpClient.execute(httpGet, responseHandler);
                         
                        String[] tokens = pageSourceCode.split(":");
                        String current_minute = tokens[1];
                         
                    	String[] tokens1 = request.split(",");
                    	String key = tokens1[1];
                    	String value = tokens1[2];
                    	if (current_minute.compareTo(minute) > 0) {
                        	serverThread.removeData(key);
                        }
                    	
                    	if (!data.containsKey(key)){
                    		serverThread.setData(key, value);
                    		minute = current_minute;
                    	}
                    	else {
                    		Log.e(Constants.TAG, "Key already exists.");
                    	}
                    }
                    
                    if (request.startsWith("get")){
                    	// treat get 
                    	
                    }
                	
                } else {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
                }
                
    			socket.close();
    		} catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
    		}
    	} else {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
        }
    }
}
