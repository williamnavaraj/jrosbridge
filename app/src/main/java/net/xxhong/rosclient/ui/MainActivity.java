package net.xxhong.rosclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.hp.ros.ROSClient;
import com.android.hp.ros.rosbridge.ROSBridgeClient;

import net.xxhong.rosclient.R;
import net.xxhong.rosclient.RCApplication;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private Button mBtnConnect;
    private EditText etIP;
    private EditText etPort;

    private ROSBridgeClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void connect(String ip, String port) {
        client = new ROSBridgeClient("ws://" + ip + ":" + port);
        client.connect(new ROSClient.ConnectionStatusListener() {
            @Override
            public void onConnect() {
                client.setDebug(true);
                ((RCApplication)getApplication()).setRosClient(client);
                showTip("Connect ROS success");
                Log.d(TAG,"Connect ROS success");
                startActivity(new Intent(MainActivity.this, VideoActivity.class));
                //startActivity(new Intent(MainActivity.this, MessageExampleActivity.class));
            }

            @Override
            public void onDisconnect(boolean normal, String reason, int code) {
                showTip("ROS disconnect");
                Log.d(TAG,"ROS disconnect" + reason);
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
                showTip("ROS communication error");
                Log.d(TAG,"ROS communication error");
            }
        });
    }
    @Override
    public void onResume() {

        super.onResume();
        setContentView(R.layout.activity_main);
        mBtnConnect = (Button) findViewById(R.id.btn_connect);
        etIP=(EditText) findViewById(R.id.et_ip);
        etPort=(EditText) findViewById(R.id.et_port);
        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"Hai",Toast.LENGTH_LONG);

                showTip("Hai");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, tip,Toast.LENGTH_SHORT).show();
                        String ip = etIP.getText().toString();
                        String port = etPort.getText().toString();
                        connect(ip, port);
                    }
                });

            }
        });

    }
    private void showTip(final String tip) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, tip,Toast.LENGTH_SHORT).show();
            }
        });
    }


}