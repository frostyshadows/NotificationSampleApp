package com.example.sherryy.notificationsampleapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MainActivity extends AppCompatActivity {

    private String NOTIFICATION_TITLE = "Notification Sample App";
    private String CONTENT_TEXT = "Expand me to see a detailed message!";

    EditText mEditText;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.edit_text);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
    }

    private void sendNotification() {

        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.view_expanded_notification);
        expandedView.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));
        expandedView.setTextViewText(R.id.notification_message, mEditText.getText());
        // adding action to left button
        Intent leftIntent = new Intent(this, NotificationIntentService.class);
        leftIntent.setAction("left");
        expandedView.setOnClickPendingIntent(R.id.left_button, PendingIntent.getService(this, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        // adding action to right button
        Intent rightIntent = new Intent(this, NotificationIntentService.class);
        rightIntent.setAction("right");
        expandedView.setOnClickPendingIntent(R.id.right_button, PendingIntent.getService(this, 1, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.view_collapsed_notification);
        collapsedView.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // these are the three things a NotificationCompat.Builder object requires at a minimum
                .setSmallIcon(R.drawable.ic_pawprint)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(CONTENT_TEXT)
                // notification will be dismissed when tapped
                .setAutoCancel(true)
                // tapping notification will open MainActivity
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                // setting the custom collapsed and expanded views
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                // setting style to DecoratedCustomViewStyle() is necessary for custom views to display
                .setStyle(new android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle());

        // retrieves android.app.NotificationManager
        NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
