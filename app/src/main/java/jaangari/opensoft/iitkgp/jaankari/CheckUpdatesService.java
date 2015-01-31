package jaangari.opensoft.iitkgp.jaankari;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.net.SocketException;

import jaangari.opensoft.iitkgp.jaangari.R;

public class CheckUpdatesService extends Service {

    private String TAG = "CheckUpdatesService";

    public CheckUpdatesService() {
    }

    private Update update = null;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG,"starting AsyncTask");
        update  = new Update();
        update.execute((Void) null);
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class Update extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String url = "http://" + getString(R.string.ip_address) + "/checkUpdates.php";
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse response = httpclient.execute(httpGet);
                String json_string = EntityUtils.toString(response.getEntity());
                Log.v(TAG,json_string);
                if(json_string.contains("true")){
                    return true;
                }
                else{
                    return false;
                }
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putBoolean("Weather",false);
                Ed.putBoolean("News",false);
                Ed.putBoolean("Health",false);
                Ed.putBoolean("Videos",false);
                Ed.commit();
                Intent intent = new Intent(getApplicationContext(),GlobalDatabaseImageService.class);
                startService(intent);
            } else {

            }
        }

    }

}
