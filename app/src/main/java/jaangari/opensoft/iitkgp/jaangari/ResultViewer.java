package jaangari.opensoft.iitkgp.jaangari;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

import jaangari.opensoft.iitkgp.jaankari.DatabaseHandler;
import jaangari.opensoft.iitkgp.jaankari.util.SearchResults;
import jaangari.opensoft.iitkgp.jaankari.util.Videos;


public class ResultViewer extends ActionBarActivity {

    private static final String TAG = "ResultViewer";
    SearchResults result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_viewer);


        String searchResultStr = getIntent().getStringExtra("SearchResult");
        Log.d(TAG, "Result = " + searchResultStr);

        try {
             result = SearchResults.getSearchResult(new JSONArray(searchResultStr));
             setTitle(result.getTitle());
             launchResult(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected int launchResult(SearchResults result)
    {


        if("Video".equals(result.getCategory())){
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//            Intent intent = new Intent();
//            intent.setAction(android.content.Intent.ACTION_VIEW);
//
//            Videos video = db.getVideobyId(result.getId());
//
//            File file = new File(db.getFilePath(result.getCategory(), result.getId()));
//
//            Log.d(TAG, db.getFilePath(result.getCategory(), result.getId()));
//            intent.setDataAndType(Uri.fromFile(file), "video/*");
            //startActivity(intent);

            VideoView vidView = (VideoView)findViewById(R.id.myVideo);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            MediaController vidControl = new MediaController(this);
            vidControl.setAnchorView(vidView);
            vidView.setMediaController(vidControl);


            vidView.setVideoPath(db.getFilePath(result.getCategory(), result.getId()));
            vidView.start();

            db.closeDB();

        }
        else{

        }

        return 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_viewer, menu);
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
