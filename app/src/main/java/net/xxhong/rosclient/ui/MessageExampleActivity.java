package net.xxhong.rosclient.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.hp.ros.MessageHandler;
import com.android.hp.ros.entity.Angular;
import com.android.hp.ros.entity.Linear;
import com.android.hp.ros.entity.Movement;
import com.android.hp.ros.entity.Msg;
import com.android.hp.ros.rosbridge.ROSBridgeClient;
import com.google.gson.Gson;

import net.xxhong.rosclient.R;
import net.xxhong.rosclient.RCApplication;
import net.xxhong.rosclient.packets.NaviPacket;
import net.xxhong.rosclient.packets.Params;

import java.util.Date;

public class MessageExampleActivity extends Activity {

    private ROSBridgeClient client;

    private com.android.hp.ros.Topic<Msg> messageTopic;
//    private com.jilk.ros.Topic<Movement> movementTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_example);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // "std_msgs/String"
        client = ((RCApplication)getApplication()).getRosClient();
        messageTopic = new com.android.hp.ros.Topic<>("/angel/navi", Msg.class, client);
        messageTopic.subscribe(new MessageHandler<Msg>() {
            @Override
            public void onMessage(Msg message) {
                Log.d("TAG", "onMessage: " + message.data);
            }
        });

//        movementTopic = new com.jilk.ros.Topic<>("/cmd_vel", Movement.class, client);
//        movementTopic.subscribe(new MessageHandler<Movement>() {
//            @Override
//            public void onMessage(Movement message) {
//                Log.d("TAG", "onMessage: " + message.toString());
//            }
//        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        messageTopic.unsubscribe();
//        movementTopic.unsubscribe();
    }

    public void sendMessage(View view) {
        Msg msg = new Msg();

        Params params   = new Params();
        params.angular  = 133.2f;
        params.linear   = 434.5f;
        params.how_long = 100;

        NaviPacket naviPacket = new NaviPacket();
        naviPacket.id     = new Date().getTime();
        naviPacket.intent = "navi";
        naviPacket.ts     = 2313.545f;
        naviPacket.params = params;

        Gson gson = new Gson();
        String json = gson.toJson(naviPacket, NaviPacket.class);

        msg.data = json;

        messageTopic.publish(msg);
    }

    public void sendMovement(View view) {
//        float linearX = 0.0f;
//        float angularZ = 1.0f;
//        float howLong = 5f;
        Angular angular = new Angular();
        angular.x = 0f;
        angular.y = 0f;
        angular.z = 0f;

        Linear linear = new Linear();
        linear.x = 0f;
        linear.y = 0f;
        linear.z = 1f;

        Movement movement = new Movement();
        movement.angular = angular;
        movement.linear = linear;

//        movementTopic.publish(movement);
//        or
//        client.send("{\"op\":\"publish\",\"topic\":\"/cmd_vel\",\"msg\":{\"linear\":{\"x\":" + linearX + ",\"y\":0,\"z\":0},\"angular\":{\"x\":0,\"y\":0,\"z\":" + angularZ + "}},\"x\":" + howLong + "}");
    }
}