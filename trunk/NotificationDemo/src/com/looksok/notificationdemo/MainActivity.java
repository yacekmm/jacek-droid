package com.looksok.notificationdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
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
		Notification notification = builder
				.setSmallIcon(R.drawable.me)
				.setContentTitle(getString(R.string.notif_title))
				.setContentText(getString(R.string.notif_text))
				.setAutoCancel(true)
				.setContentIntent(notificationIntent)
				.build();
		
		return notification;
	}

	private void displayNotification(Notification notification) {
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(myNotificationId , notification);
	}
}
