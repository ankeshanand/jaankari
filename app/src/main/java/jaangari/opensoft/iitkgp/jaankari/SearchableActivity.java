package jaangari.opensoft.iitkgp.jaankari;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import jaangari.opensoft.iitkgp.jaankari.util.SearchResults;
import jaangari.opensoft.iitkgp.jaankari.util.Videos;


public class SearchableActivity extends ActionBarActivity {
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

    protected void contentSearch(String Query){
        if(Query!=null){
            db = new DatabaseHandler(getApplicationContext());
            final ArrayList<SearchResults> results= db.searchMatches(Query,null);
            db.closeDB();
            ListView listView = (ListView)findViewById(R.id.search_list_view);
            int size = results.size();
            videoTitle = new String[size];
            otherSummary = new String[size];
            otherTitle = new String[size];
            image = new Bitmap[size];
            rating = new float[size];
            for (int i=0;i<size;i++){
                SearchResults result = results.get(i);
                if ("Video".equals(result.getCategory())){
                    db = new DatabaseHandler(getApplicationContext());
                    Videos video = db.getVideobyId(result.getId());
                    db.closeDB();
                    //TODO - getVideo paths by id;
                    videoTitle[i]=video.getName();
                    otherTitle[i]=null;
                    otherSummary[i]=null;
                    String path = video.getPath();
                    image[i]= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/Videos/"
                            + path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".mp4")) + "_thumbnail.jpg");;
                    rating[i]=video.getRating();
                }
                else{
                    videoTitle[i]=null;
                    otherTitle[i]=result.getTitle();
                    otherSummary[i]=result.getSummary();
                    image[i]=null;
                    rating[i]=0;

                }
            }
            CustomList adapter = new CustomList(this, videoTitle, image, rating,otherTitle,otherSummary);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SearchResults result = results.get(position);
                    if("Video".equals(result.getCategory())){
                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        db = new DatabaseHandler(getApplicationContext());
                        Videos video = db.getVideobyId(result.getId());
                        db.closeDB();
                        String path = video.getPath();
                        File file = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/Videos/" + path.substring(path.lastIndexOf("/")));
                        Log.e(TAG, Environment.getExternalStorageDirectory() +"/"+ getString(R.string.app_name) + "/Videos/" + path.substring(path.lastIndexOf("/")));
                        intent.setDataAndType(Uri.fromFile(file), "video/*");
                        video_id = video.getID();
                        video_history = video.getHistory();
                        startActivity(intent);
                    }
                    else{

                    }
                }
            });

        }
    }

    @Override
    protected void onResume(){
        updateDb(video_history);
        Log.e("DEBUG", "onResume of VideoFragment");
        super.onResume();
    }

    public void updateDb(float rating) {
        DatabaseHandler db = new DatabaseHandler(this.getApplicationContext());
        video_history++;
        db.updateVideoHistory(video_id,video_history);
        db.closeDB();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Intent intent  =  getIntent();
        Bundle appData = getIntent().getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            boolean jargon = appData.getBoolean(SearchableActivity.JARGON);
        }
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
//            dbHandler = new DatabaseHandler(getApplicationContext());
//            dbHandler.closeDB();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("Search Results");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout_search_screen) {
            SharedPreferences sp=getSharedPreferences("Login", 0);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putString("sLogin",null);
            Ed.putString("emailId",null);
            Ed.putString("proPic",null);
            Ed.commit();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.change_to_home_screen){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final String[] title;
        private final Bitmap[] imageId;
        private final float[] rating;
        private final String[] other_title;
        private final String[] other_summary;

        public CustomList(Activity context, String[] title, Bitmap[] imageId, float[] rating,String[] other_title,String[] other_summary) {
            super(context, R.layout.custom_list_view, title);
            this.context = context;
            this.title = title;
            this.imageId = imageId;
            this.rating = rating;
            this.other_summary = other_summary;
            this.other_title = other_title;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.custom_search_list_view, null, true);
            TextView videoTitle = (TextView)rowView.findViewById(R.id.video_title);
//            RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.search_rating_bar);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.search_image_list_view);
            TextView otherTitle = (TextView) rowView.findViewById(R.id.other_title);
            TextView otherSummary = (TextView) rowView.findViewById(R.id.other_summary);

            videoTitle.setText(title[position]);
            imageView.setImageBitmap(imageId[position]);
//            ratingBar.setRating((float) rating[position]);
//            if(rating[position]==0){
//
//            }
//            else {
//                ratingBar.setRating((float) rating[position]);
//            }
            otherTitle.setText(other_title[position]);
            otherSummary.setText(other_summary[position]);
            return rowView;
        }
    }
}
