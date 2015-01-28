package jaangari.opensoft.iitkgp.jaankari;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rahulanishetty on 1/28/15.
 */

//For Downloading recommended files

public class AlarmDownloadUpdated extends BroadcastReceiver{
    public static final int REQUEST_CODE = 45678;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,DownloadUpdated.class);
        context.startService(intent);
    }
}
