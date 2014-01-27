package com.looksok.notificationdemo;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final int myNotificationId = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		showNotification();
		super.onPause();
	}

	private void showNotification() {
		PendingIntent notificationIntent = preparePendingIntent();
		Notification notification = createBasicNotification(notificationIntent);
		displayNotification(notification);
	}


	@SuppressLint("InlinedApi")
	private PendingIntent preparePendingIntent() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 
				0, 
				intent, 
				PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}

	private Notification createBasicNotification(PendingIntent notificationIntent) {
		NotificationCompat.Builder builder = new Builder(getApplicationContext());
		long[] vibrationPattern = {0, 200, 800, 200, 600, 200, 400, 200, 200, 200, 100, 200, 50, 200, 50, 200, 50, 200, 50, 200};
		NotificationCompat.InboxStyle inboxStyle = prepareBigNotificationDetails();
		
		Notification notification = builder
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(getString(R.string.notif_title))
				.setContentText(getString(R.string.notif_text))
				.setAutoCancel(true)
				.setContentIntent(notificationIntent)
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.me))
				.setContentInfo(getString(R.string.notif_contentInfo))
				.setWhen(Calendar.getInstance().getTimeInMillis() + 1000*60+60)
				.setVibrate(vibrationPattern)
				.setStyle(inboxStyle)
				.build();
		
		return notification;
	}

	private InboxStyle prepareBigNotificationDetails() {
		NotificationCompat.InboxStyle result = new InboxStyle();
		result.setBigContentTitle(getString(R.string.notif_inboxStyleTitle));
		result.addLine(getString(R.string.notif_inboxStyle_line1));
		result.addLine(getString(R.string.notif_inboxStyle_line2));
		result.addLine(getString(R.string.notif_inboxStyle_line3));
		result.addLine(getString(R.string.notif_inboxStyle_line4));
		result.addLine(getString(R.string.notif_inboxStyle_line5));
		result.addLine(getString(R.string.notif_inboxStyle_line6));
		return result;
	}

	private void displayNotification(Notification notification) {
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(myNotificationId , notification);
	}
}
