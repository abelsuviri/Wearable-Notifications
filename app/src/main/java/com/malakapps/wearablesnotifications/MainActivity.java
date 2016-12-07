package com.malakapps.wearablesnotifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1;
    private static final int NOTIFICATION_ID_1 = 2;
    private static final int NOTIFICATION_ID_2 = 3;
    private static final int NOTIFICATION_ID_3 = 4;
    private static final int REQUEST_CODE = 0;
    private static final String GROUP = "GROUP";

    private NotificationManagerCompat mNotificationManager;
    private boolean isLocal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = NotificationManagerCompat.from(this);

        Button basicButton = (Button) findViewById(R.id.buttonBasicNotification);
        Button pagesButton = (Button) findViewById(R.id.buttonPagesNotification);
        Button stackButton = (Button) findViewById(R.id.buttonStackNotification);
        CheckBox local = (CheckBox) findViewById(R.id.checkBox);

        basicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBasicNotification();
            }
        });

        pagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPagesNotification();
            }
        });

        stackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStackNotifications();
            }
        });

        local.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isLocal = isChecked;
            }
        });
    }

    /**
     * Create a basic notification
     */
    private void createBasicNotification() {
        Intent intent = new Intent(this, DetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, 0);

        Intent wearIntent = new Intent(Intent.ACTION_VIEW);
        Uri locationUri = Uri.parse("geo:36.720928, -4.421735");
        wearIntent.setData(locationUri);
        PendingIntent wearPendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, wearIntent, 0);

        NotificationCompat.Action wearAction = new NotificationCompat.Action.Builder(R.drawable.ic_map,
            getString(R.string.watch_action), wearPendingIntent).build();

        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText(getString(R.string.big_notification_text));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_android)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setContentIntent(pendingIntent)
            .setStyle(bigStyle)
            .addAction(R.drawable.ic_open, getString(R.string.show_activity), pendingIntent)
            .setLocalOnly(isLocal)
            .extend(new NotificationCompat.WearableExtender().addAction(wearAction));

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Create a notification with different pages
     */
    private void createPagesNotification() {
        Intent intent = new Intent(this, DetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_android)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setLocalOnly(isLocal)
            .setContentIntent(pendingIntent);

        NotificationCompat.BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
        secondPageStyle.setBigContentTitle(getString(R.string.second_page_title))
            .bigText(getString(R.string.second_page_description));

        Notification secondPage = new NotificationCompat.Builder(this)
            .setStyle(secondPageStyle)
            .build();

        NotificationCompat.BigTextStyle thirdPageStyle = new NotificationCompat.BigTextStyle();
        thirdPageStyle.setBigContentTitle(getString(R.string.third_page_title))
            .bigText(getString(R.string.third_page_description));

        Notification thirdPage = new NotificationCompat.Builder(this)
            .setStyle(thirdPageStyle)
            .build();

        ArrayList<Notification> pages = new ArrayList<>();
        pages.add(secondPage);
        pages.add(thirdPage);

        Notification notification = builder.extend(new NotificationCompat.WearableExtender()
            .addPages(pages))
            .setLocalOnly(isLocal)
            .build();

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    /**
     * Create a stack of notifications, similar to a messaging app.
     */
    private void createStackNotifications() {
        Notification notification = new NotificationCompat.Builder(this)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_android)
            .setGroup(GROUP)
            .setLocalOnly(isLocal)
            .build();

        mNotificationManager.notify(NOTIFICATION_ID, notification);

        Notification notification2 = new NotificationCompat.Builder(this)
            .setContentTitle(getString(R.string.second_page_title))
            .setContentText(getString(R.string.second_page_description))
            .setSmallIcon(R.drawable.ic_android)
            .setGroup(GROUP)
            .setLocalOnly(isLocal)
            .build();

        mNotificationManager.notify(NOTIFICATION_ID_1, notification2);

        Notification notification3 = new NotificationCompat.Builder(this)
            .setContentTitle(getString(R.string.third_page_title))
            .setContentText(getString(R.string.third_page_description))
            .setSmallIcon(R.drawable.ic_android)
            .setGroup(GROUP)
            .setLocalOnly(isLocal)
            .build();

        mNotificationManager.notify(NOTIFICATION_ID_2, notification3);

        Notification summary = new NotificationCompat.Builder(this)
            .setContentTitle(getString(R.string.three_notifications))
            .setSmallIcon(R.drawable.ic_android)
            .setStyle(new NotificationCompat.InboxStyle()
                .addLine(getString(R.string.notification_title))
                .addLine(getString(R.string.second_page_title))
                .addLine(getString(R.string.third_page_title)))
            .setGroup(GROUP)
            .setLocalOnly(isLocal)
            .setGroupSummary(true)
            .build();

        mNotificationManager.notify(NOTIFICATION_ID_3, summary);
    }
}
