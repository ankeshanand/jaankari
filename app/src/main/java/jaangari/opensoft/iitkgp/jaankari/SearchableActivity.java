package jaangari.opensoft.iitkgp.jaankari;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.hotspotUtils.CommDevice;


public class SearchableActivity extends ListActivity {
    DatabaseHandler dbHandler;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
//                String string = bundle.getString(DownloadService.FILEPATH);
//                int resultCode = bundle.getInt(DownloadService.RESULT);
//                if (resultCode == RESULT_OK) {
//                    Toast.makeText(MainActivity.this,
//                            "Download complete. Download URI: " + string,
//                            Toast.LENGTH_LONG).show();
//                    textView.setText("Download done");
//                } else {
//                    Toast.makeText(MainActivity.this, "Download failed",
//                            Toast.LENGTH_LONG).show();
//                    textView.setText("Download failed");
//                }

                String jsonResults = bundle.getString("JSON");

                Log.d("BroadcastRec", jsonResults);

                try {
                    JSONObject jResults = new JSONObject(jsonResults);
                    String theIP = jResults.getString("IP");
                    Log.d("BroadcastRec", "IP is : " + theIP);

                    JSONArray results = jResults.getJSONArray("list");

                    for(int i=0; i< results.length(); i++ )
                    {
                        JSONObject oneCat = (JSONObject) results.get(i);
                        Log.d("BroadcastRec", "OUTPUT : " + oneCat.toString());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Toast.makeText(SearchableActivity.this, jsonResults, Toast.LENGTH_LONG).show();

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("NOTIFICATION"));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    class PairCategory{
        String category;
        ArrayList<Integer> ids;
    }
    protected void contentSearch(String Query){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Intent intent  =  getIntent();
        dbHandler = new DatabaseHandler(getApplicationContext());

        if(intent.ACTION_SEARCH.equals(intent.getAction())){
            final String Query = intent.getStringExtra(SearchManager.QUERY);
            contentSearch(Query);
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    CommDevice cd = null;
                    try {
                        Thread.sleep(2000);
                        cd = new CommDevice(getApplicationContext());
                        cd.broadcastQuery(Query);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });        th.start();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
