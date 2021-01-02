### RosClient: Android app for ROS
Android communication with ROS(Robot Operating System),based on [rosbridge protocol](https://github.com/RobotWebTools/rosbridge_suite/blob/groovy-devel/ROSBRIDGE_PROTOCOL.md)

In the MainActivity class **OnConnect** can be changed to start either a video streaming activity or a message example

```javascript
startActivity(new Intent(MainActivity.this, VideoActivity.class));
//startActivity(new Intent(MainActivity.this, MessageExampleActivity.class));
```
