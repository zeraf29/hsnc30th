package pe.sbk.gcmsample;

import java.util.Iterator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String tag = "GCMIntentService";
	public static final String SEND_ID = "824653015657";

	public GCMIntentService(){ this(SEND_ID); }
	static String re_message=null;
	public GCMIntentService(String senderId) { super(senderId); }

	@Override
	protected void onMessage(Context context, Intent intent) {
		Bundle b = intent.getExtras();
		Log.e("test","test");
		Iterator<String> iterator = b.keySet().iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			String value = b.get(key).toString();
			Log.d(tag, "onMessage. "+key+" : "+value);
		}
		Vibrator vibrator = (Vibrator)getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        
        WakeUpScreen.acquire(getApplicationContext(), 10000); 
		String msg = intent.getStringExtra("msg");
		generateNotification(context, msg);
	}
	
	private static void generateNotification(Context context, String message) {
		  int icon = R.drawable.ic_action_search;
		  long when = System.currentTimeMillis();

		  NotificationManager notificationManager = (NotificationManager) context
		    .getSystemService(Context.NOTIFICATION_SERVICE);
		  Notification notification = new Notification(icon, message, when);

		  String title = context.getString(R.string.app_name);
		  Intent notificationIntent = new Intent(context, GCMSampleActivity.class);
		  re_message=message;

		  notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		  PendingIntent intent = PendingIntent.getActivity(context, 0,notificationIntent, 0);

		  notification.setLatestEventInfo(context, title, message, intent);
		  notification.flags |= Notification.FLAG_AUTO_CANCEL;
		  notificationManager.notify(0, notification);
	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.d(tag, "onError. errorId : "+errorId);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Log.d(tag, "onRegistered. regId : "+regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.d(tag, "onUnregistered. regId : "+regId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.d(tag, "onRecoverableError. errorId : "+errorId);
		return super.onRecoverableError(context, errorId);
	}
}