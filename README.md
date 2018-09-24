### RosClient: Android app for ROS
Android communicate with ROS(Robot Operating System),based on [rosbridge protocol](https://github.com/RobotWebTools/rosbridge_suite/blob/groovy-devel/ROSBRIDGE_PROTOCOL.md)

Updated Saulo Garcia (2018)

### Features
1. Enum all ROS nodes,service,topics
2. Show params in service or topics
3. Subscribe or publish ros topic
4. Call ros service
5. Process topic "/cmd_vel", control the movement of robot
6. Process topic "/map", show the map of SLAM,like rviz

## Installing driver
In order to run this project succesfull besides Ros kinect being installed we need to install inside Ubuntu the usb_cam driver.
```sh
$ mkdir -p ~/catkin-ws/src
$ cd ~/catkin-ws/src
$ git clone https://github.com/bosch-ros-pkg/usb_cam.git
$ cd ..
$ catkin_make
$ source ~/catkin-ws/devel/setup.bash
```
If you just want to install the driver without building:
```sh
$ sudo apt-get install ros-groovy-usb-cam
```
after the above you can verify using ..
```sh
$ roscd usb_cam
```

### Video configuration
This is the best configuration found, even after 2 hours of continuous streaming the video won't have a delay.
Under ~/catkin-ws/src/usb_cam/launch
Update usb_cam-test.launch to reduce the default image width and height to half their size, to improve and set the topic queue size to 1 (The queue size default valuer is 0).
```xml
<launch>
  <node name="usb_cam" pkg="usb_cam" type="usb_cam_node" output="screen" >
    <param name="video_device" value="/dev/video0" />
    <param name="image_width" value="320" />
    <param name="image_height" value="240" />
    <param name="pixel_format" value="mjpeg" />
    <param name="camera_frame_id" value="usb_cam" />
    <param name="io_method" value="mmap"/>
  </node>
  <node name="image_view" pkg="image_view" type="image_view" respawn="false" output="screen">
    <remap from="image" to="/usb_cam/image_raw"/>
    <param name="autosize" value="true" />
  </node>
</launch>
```

We can also half the framerate (from 30fps which is the default value to 15fps) and we can leave the default size of the streamed image.
```xml
<launch>
  <node name="usb_cam" pkg="usb_cam" type="usb_cam_node" output="screen" >
    <param name="video_device" value="/dev/video0" />
    <param name="image_width" value="640" />
    <param name="image_height" value="480" />
    <param name="framerate" value="15" />
    <param name="pixel_format" value="mjpeg" />
    <param name="camera_frame_id" value="usb_cam" />
    <param name="io_method" value="mmap"/>
  </node>
  <node name="image_view" pkg="image_view" type="image_view" respawn="false" output="screen">
    <remap from="image" to="/usb_cam/image_raw"/>
    <param name="autosize" value="true" />
  </node>
</launch>
```

### Connecting everything
1. Connect the Android device to the laptop where Android studio is installed through usb and execute in a terminal:
```sh
$ adb tcpip 5555
```
2. Disconnect the Android device and connect it to the Ubuntu machine via Usb and execute in a terminal: 
```sh
$ adb reverse tcp:9090 tcp:9090
```
3. In the same terminal execute:
```sh
$ roslaunch rosbridge_server rosbridge_websocket.launch
```
4. Running the app from a separate machine wireless:
```sh
$ abd connect android_device_ip
```

## Launching the camera from Ubuntu
In a new terminal execute (If you close this terminal, and you want to publish the video with the setup 
from "usb_cam-test.launch", you will need to repeat the next steps):
```sh
$ cd ~/catkin-ws/src/usb_cam/launch
$ source ~/catkin-ws/devel/setup.bash
$ roslaunch usb_cam-test.launch
```

If you stop the last command (in the same terminal)
```sh
$ roslaunch usb_cam-test.launch
```

or you can execute (still being in the same terminal):
```sh
$ rosrun usb_cam usb_cam_node 
```

### Application class example
```java
import android.app.Application;

import com.android.hp.ros.rosbridge.ROSBridgeClient;

public class RCApplication extends Application {
    private ROSBridgeClient client;

    public ROSBridgeClient getRosClient() {
        return client;
    }

    public void setRosClient(ROSBridgeClient client) {
        this.client = client;
    }
}
```

Manifest:
```xml
    <application
        android:name=".RCApplication"
```

### Working with a publisher, sending messages example

Check MessageExampleActivity
```java
import com.android.hp.ros.MessageHandler;
import com.android.hp.ros.rosbridge.ROSBridgeClient;

// Custom string std_msgs
import com.android.hp.ros.entity.Angular;
import com.android.hp.ros.entity.Linear;
import com.android.hp.ros.entity.Movement;
import com.android.hp.ros.entity.Msg;
import com.android.hp.ros.packets.NaviPacket;
import com.android.hp.ros.packets.Params;
```

Variables needed:
```java
    private ROSBridgeClient client; // RosBridge client

    private com.android.hp.ros.Topic<Msg> messageTopic; // Default Msg topic
    private com.android.hp.ros.Topic<Movement> movementTopic; // Default movement topic
```

Getting a connection, subscribing to a custom topic and subscribing to it.
```java
    @Override
    protected void onResume() {
        super.onResume();
        // "std_msgs/String"
        client = ((RCApplication)getApplication()).getRosClient();
        
        // Instantiating a topic
        messageTopic = new com.android.hp.ros.Topic<>("/angel/navi", Msg.class, client);
        // We can optionally subscribe to a topic if we want
        messageTopic.subscribe(new MessageHandler<Msg>() {
            @Override
            public void onMessage(Msg message) {
                Log.d("TAG", "onMessage: " + message.data);
            }
        });

        movementTopic = new com.android.hp.ros.Topic<>("/cmd_vel", Movement.class, client);
        // We can optionally subscribe to a topic if we want
        movementTopic.subscribe(new MessageHandler<Movement>() {
            @Override
            public void onMessage(Movement message) {
                Log.d("TAG", "onMessage: " + message.toString());
            }
        });
    }
```
If the activity needs to be stopped.
```java
    @Override
    protected void onStop() {
        super.onStop();
        messageTopic.unsubscribe();
        movementTopic.unsubscribe();
    }
```

Publishing a regular string message from a java object change into a json string:
```java
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
```

Publishing a Movement example:
```java
    public void sendMovement(View view) {
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

        movementTopic.publish(movement);
    }
```

Another way to publish a message example:
```java
    float linearX = 0.0f;
    float angularZ = 1.0f;
    float howLong = 5f;
    client.send("{\"op\":\"publish\",\"topic\":\"/cmd_vel\",\"msg\":{\"linear\":{\"x\":" + linearX + ",\"y\":0,\"z\":0},\"angular\":{\"x\":0,\"y\":0,\"z\":" + angularZ + "}},\"x\":" + howLong + "}");
```


### Working with subscribers example, Streaming images example
Check VideoActivity

Imports needed:
```java
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.android.hp.ros.MessageHandler;
import com.android.hp.ros.rosbridge.ROSBridgeClient;

import com.android.hp.ros.message.BitmapFromCompressedImage;
import com.android.hp.ros.message.CompressedImage;

import net.xxhong.rosclient.R;
import net.xxhong.rosclient.RCApplication;
```

Variables needed:
```java
    private ROSBridgeClient client; // RosBridge client

    private com.android.hp.ros..Topic<CompressedImage> messageTopic; // Compressed image topic

    private Panel panel; // Our custom surfaceview where we will show the video
    private BitmapFromCompressedImage bmp = new BitmapFromCompressedImage(); // The class that will generate the bmp from the compressed image
```

Basic subscribing to a topic and receiving the CompressedImageMessage, generating the bmp from the message and drawing it in the canvas of the custom surface view.
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        panel = findViewById(R.id.surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // Do it here so if there is a configuration change, the subscription can be resumed
        bmp    = new BitmapFromCompressedImage();
        
        // Getting the RosClient connection from the Application class
        client = ((RCApplication)getApplication()).getRosClient();
        
        // Instantiating a specific topic of an specific type and setting its queue size to 1
        messageTopic = new com.android.hp.ros.Topic<>("/usb_cam/image_raw/compressed", CompressedImage.class, client);
        messageTopic.queue_length = 1;

        // Subscribing to the topic and asynchronously waiting for the response
        messageTopic.subscribe(new MessageHandler<CompressedImage>() {
            @Override
            public void onMessage(CompressedImage message) {
                // Transforming the message into a bitmap
                Bitmap bitmap = bmp.call(message);
                if (bitmap != null) {
                    // Pass the bitmap to the customsurface, so it gets painted
                    panel.bmpIcon = bitmap;
                }
            }
        });
    }
```

If the activity needs to be stopped, we unsubscribe.
```java
    @Override
    protected void onStop() {
        super.onStop();
        messageTopic.unsubscribe();
    }
```

### Use Library
- [EventBus](https://github.com/greenrobot/EventBus)
- [ButterKnife](https://github.com/JakeWharton/butterknife)
- [java_websocket](https://github.com/TooTallNate/Java-WebSocket)
- [ROSBridgeClient](https://github.com/djilk/ROSBridgeClient)
- [AndroidTreeView](https://github.com/bmelnychuk/AndroidTreeView)
- json-simple