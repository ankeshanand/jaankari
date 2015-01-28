package jaangari.opensoft.iitkgp.jaankari;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rahulanishetty on 1/28/15.
 */

public class QueryAlarmReciever extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent temp = new Intent(context,CheckUpdates.class);
        context.startService(temp);

    }
}
