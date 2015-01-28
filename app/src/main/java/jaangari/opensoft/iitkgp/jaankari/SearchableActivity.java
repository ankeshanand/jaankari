package jaangari.opensoft.iitkgp.jaankari;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.hotspotUtils.CommDevice;
import jaangari.opensoft.iitkgp.jaankari.util.Videos;


public class SearchableActivity extends ListActivity {
    private DatabaseHandler dbHandler;
    private DatabaseHandler db;
    private final String TAG = "Searchable";
    static final String JARGON = "Searchable";
    private String[] videoTitle;
    private String[] otherTitle;
    private String[] otherSummary;
    private Bitmap[] image;
    private float[] rating;
    private boolean video_set = false;
    private int video_id;
    private int video_history;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String jsonResults = bundle.getString("JSON");

                Log.d("BroadcastRec", jsonResults);

                try {
                    final JSONObject jResults = new JSONObject(jsonResults);
                    final String theIP = jResults.getString("IP");
                    Log.d("BroadcastRec", "IP is : " + theIP);

                    JSONArray results = jResults.getJSONArray("list");

                    for(int i=0; i< results.length(); i++ )
                    {
                        JSONObject oneCat = (JSONObject) results.get(i);
                        Log.d("BroadcastRec", "OUTPUT : " + oneCat.keys().next());
                    }

                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            CommDevice cd = null;
                            try {
                                cd = new CommDevice(getApplicationContext());
                                cd.requestFile(theIP,"",1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    th.start();


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

    protected void contentSearch(String Query){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_searchable);
        Intent intent  =  getIntent();
        dbHandler = new DatabaseHandler(getApplicationContext());

        if(intent.ACTION_SEARCH.equals(intent.getAction()) || true){
            //final String Query = intent.getStringExtra(SearchManager.QUERY);
            final String Query = "Obama";//intent.getStringExtra(SearchManager.QUERY);
            contentSearch(Query);
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    CommDevice cd = null;
                    try {
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









