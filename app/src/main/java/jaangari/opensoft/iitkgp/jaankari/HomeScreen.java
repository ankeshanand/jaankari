package jaangari.opensoft.iitkgp.jaankari;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.BackgroundServices.QueryHandler;
import jaangari.opensoft.iitkgp.jaankari.BackgroundServices.WifiHandler;


public class HomeScreen extends ActionBarActivity {
    DatabaseHandler db;

    public void videoIntent(View view){
        Intent intent = new Intent(getApplicationContext(),VideoActivity.class);
        startActivity(intent);
    }

    public void NewsIntent(View view){
        Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
        startActivity(intent);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout_home_screen){
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
        else if(id == R.id.update_password){
            Intent intent = new Intent(this,PasswordChangeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(id==R.id.update_profile_picture){
            Intent intent =  new Intent(this,UpdateProfilePicActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent bgServiceIntent = new Intent(getApplicationContext(), WifiHandler.class);
        startService(bgServiceIntent);

        Intent commService = new Intent(getApplicationContext(), QueryHandler.class);
        startService(commService);

        Intent resultsHandler = new Intent(getApplicationContext(), QueryHandler.class);
        startService(resultsHandler);

        Intent intent = new Intent(getApplicationContext(),VideoDownloadService.class);
        setContentView(R.layout.activity_home_screen);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(),SearchableActivity.class)));
        searchView.setIconifiedByDefault(false);
       // return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putBoolean(SearchableActivity.JARGON, true);
        startSearch(null, false, appData, false);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


}
