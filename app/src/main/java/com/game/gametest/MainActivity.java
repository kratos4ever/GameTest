package com.game.gametest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {


    public void doLogin(View view){

        EditText userId = (EditText) this.findViewById(R.id.userIdField);
        EditText password = (EditText) this.findViewById(R.id.passwordField);

        Log.i("INFO","\n\n ****************** WebSocketClient - ready state: " + mWebSocketClient.getReadyState());

        mWebSocketClient.send(userId.getText().toString());
        Log.i("INFO","USER:" + userId.getText() + ", Password:" + password.getText());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            //initialize the jetty websocket client
            this.connectWebSocket();

        }catch(Exception e){
            e.printStackTrace();
            Log.e("ERROR",e.getMessage());
        }

        setContentView(R.layout.activity_main);
    }
    private WebSocketClient mWebSocketClient;


    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://35.184.71.197:8990/game/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        Map<String,String> map = new HashMap<String,String>();
         mWebSocketClient = new WebSocketClient(uri, new Draft_17()) {
             public CountDownLatch latch = new CountDownLatch(1);
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                latch.countDown();
                mWebSocketClient.send("Testing");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                Log.i("INFO", "MEssage from the server:" + message);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        mWebSocketClient.connect();
        try{
            Thread.sleep(2000);
        }catch(Exception e){

        }
    }
}
