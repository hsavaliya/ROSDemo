package com.demo.rosdemo;

import android.Manifest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.rosjava_tutorial_pubsub.Talker;

import java.util.ArrayList;

import std_msgs.String;

public class MainActivity extends RosActivity {

    private RosTextView<String> rosTextView;
    private Talker talker;

    public MainActivity() {
        // The RosActivity constructor configures the notification title and ticker
        // messages.
        super("Pubsub Demo", "Pubsub Demo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askForPermission();
        }

        rosTextView = (RosTextView<String>) findViewById(R.id.text);
        rosTextView.setTopicName("chatter");
        rosTextView.setMessageType(String._TYPE);
        rosTextView.setMessageToStringCallable(new MessageCallable<java.lang.String, String>() {
            @Override
            public java.lang.String call(String message) {
                return message.getData();
            }
        });
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        talker = new Talker();

        // At this point, the user has already been prompted to either enter the URI
        // of a master to use or to start a master locally.

        // The user can easily use the selected ROS Hostname in the master chooser
        // activity.
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(talker, nodeConfiguration);
        // The RosTextView is also a NodeMain that must be executed in order to
        // start displaying incoming messages.
        nodeMainExecutor.execute(rosTextView, nodeConfiguration);
    }
    /************************ Permission Methods ******************/
    final int ACCESS_COARSE_LOCATION_CODE=1;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askForPermission()
    {
        ArrayList<java.lang.String> permissions = new ArrayList<java.lang.String>();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)==-1)
            permissions.add(Manifest.permission.INTERNET);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)==-1)
            permissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW);


        if(permissions.size()>0)
            this.requestPermissions(permissions.toArray(new java.lang.String[permissions.size()]),ACCESS_COARSE_LOCATION_CODE);
        else {


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull java.lang.String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_CODE:
                //create Logger file

                break;
            default:
                break;
        }
    }
    /*********************** Permission END **********************************/
}
