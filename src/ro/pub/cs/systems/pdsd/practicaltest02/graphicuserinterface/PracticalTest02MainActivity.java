package ro.pub.cs.systems.pdsd.practicaltest02.graphicuserinterface;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ro.pub.cs.systems.pdsd.practicaltest02.R;
import ro.pub.cs.systems.pdsd.practicaltest02.General.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02.General.Network.ClientThread;
import ro.pub.cs.systems.pdsd.practicaltest02.General.Network.ServerThread;

public class PracticalTest02MainActivity extends Activity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    
    private EditText getEditText = null;
    private EditText putEditText = null;
    private Button getButton = null;
    private Button putButton = null;
    
    private TextView getTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }

    private GetButtonClickListener getClickListener = new GetButtonClickListener();
    
    
    private class GetButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort    = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            String get_key = getEditText.getText().toString();
            
            if (get_key == null || get_key.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Error get request",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            getTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress,
                    Integer.parseInt(clientPort),
                    get_key,
                    getTextView);
            clientThread.start();
        }
    }
    
    private PutButtonClickListener putClickListener = new PutButtonClickListener();
    
    
    private class PutButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort    = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            String put_value = getEditText.getText().toString();
            
            if (put_value == null || put_value.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Error get request",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            getTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress,
                    Integer.parseInt(clientPort),
                    put_value,
                    getTextView);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        getEditText = (EditText)findViewById(R.id.get_edit_text);
        putEditText = (EditText)findViewById(R.id.put_edit_text);
        getButton = (Button)findViewById(R.id.get_key);
        getButton.setOnClickListener(getClickListener);
        putButton = (Button)findViewById(R.id.put_value);
        putButton.setOnClickListener(putClickListener);
        
        getTextView = (TextView)findViewById(R.id.get_text_view);
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}