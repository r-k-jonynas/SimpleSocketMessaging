package com.encryp.io.sockettests;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    TextView ownIP, mReceivedMessageField;
    EditText eHost, ePort, eName, eMessage;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // For printing out the host IP
        ownIP = findViewById(R.id.ip_address_tview);
        CheckIPBackgroundTask cIPbt = new CheckIPBackgroundTask();
        cIPbt.execute();

        eHost = (EditText) findViewById(R.id.host_name);
        ePort = (EditText) findViewById(R.id.port_number);
        eName = (EditText) findViewById(R.id.user_name);
        eMessage = (EditText) findViewById(R.id.message);
        mButton = (Button) findViewById(R.id.send_message);
        // For printing out the messages received
        mReceivedMessageField = findViewById(R.id.received_mssg_tview);

        Thread myThread = new Thread(new MyServer());
        myThread.start();
    }

    
    class MyServer implements Runnable {
        ServerSocket ss;
        Socket mysocket;
        DataInputStream dis;
        String message;
//        // Defines a Handler object that's attached to the UI thread
//        Handler handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message inputMessage) {}
//        };
        Handler handler = new Handler();
        BufferedReader bufferedReader;

        @Override
        public void run() {
            try {
                ss = new ServerSocket(9700);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Waiting for client", Toast.LENGTH_LONG).show();
                    }
                });
                while(true) {
                    mysocket = ss.accept();
                    dis = new DataInputStream(mysocket.getInputStream());
                    message = dis.readUTF();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            mReceivedMessageField.setText(message);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public void sendMessage(View view) {
        MessageBackgroundTask mbt = new MessageBackgroundTask();
        mbt.execute(eHost.getText().toString(), ePort.getText().toString(),
                eName.getText().toString(), eMessage.getText().toString());
        Toast.makeText(getApplicationContext(),"Message sent", Toast.LENGTH_SHORT).show();
    }

    class CheckIPBackgroundTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try(final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ownIP.setText(socket.getLocalAddress().getHostAddress());
            } catch (SocketException e) {
                ownIP.setText("SocketException");
            } catch (UnknownHostException e) {
                ownIP.setText("UnknownHostException");
            }
            return null;
        }
    }


    class MessageBackgroundTask extends AsyncTask<String, Void, String> {
        Socket s;
        DataOutputStream dos;
        String ip, port, sender, message;

        @Override
        protected String doInBackground(String... params) {
            ip = params[0];
            port = params[1];
            sender = params[2];
            message = params[3];

            try {
                s = new Socket(ip, Integer.parseInt(port));
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(sender + ": " + message);

                dos.flush();
                dos.close();

                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
