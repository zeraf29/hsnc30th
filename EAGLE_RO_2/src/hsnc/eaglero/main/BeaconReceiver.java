package hsnc.eaglero.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author lk.kim
 *
 */
public class BeaconReceiver extends BroadcastReceiver {

    @Override
	public void onReceive(Context context, Intent intent) {
    	System.out.println("receiver receive action : " + intent.getAction());
    	context.startService(intent);
	}
}
