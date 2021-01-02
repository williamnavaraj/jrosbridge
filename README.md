### RosClient: Android app for ROS
Android communicate with ROS(Robot Operating System),based on [rosbridge protocol](https://github.com/RobotWebTools/rosbridge_suite/blob/groovy-devel/ROSBRIDGE_PROTOCOL.md)

In the MainActivity class
OnConnect can be changed to start the different activity either for messaging or for video streaming
startActivity(new Intent(MainActivity.this, VideoActivity.class));
//startActivity(new Intent(MainActivity.this, MessageExampleActivity.class));
