package ro.pub.cs.systems.pdsd.practicaltest02.General.Network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.pdsd.practicaltest02.General.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02.General.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String getKey;
    private String putValue;
    private TextView getTextView;

    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String str,
            TextView getTextView) {
        this.address = address;
        this.port = port;
        if (str.startsWith("put")) {
        	this.putValue = str;
        	this.getKey = null;
        } else if (str.startsWith("get")) {
        	this.getKey = str;
        	this.putValue = null;
        }
        this.getTextView = getTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                if (this.getKey != null) {
                	printWriter.println(this.getKey);
                }
                else{
                	printWriter.println(this.putValue);
                }
                printWriter.flush();
                String getInformation;
                while ((getInformation = bufferedReader.readLine()) != null) {
                    final String finalizedgetInformation = getInformation;
                    getTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            getTextView.append(finalizedgetInformation + "\n");
                        }
                    });
                }
            } else {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}