package jaangari.opensoft.iitkgp.jaankari;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.text.DecimalFormat;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.BackgroundServices.FileServer;
import jaangari.opensoft.iitkgp.jaankari.BackgroundServices.QueryHandler;
import jaangari.opensoft.iitkgp.jaankari.BackgroundServices.ResultsHandler;
import jaangari.opensoft.iitkgp.jaankari.BackgroundServices.WifiHandler;
import jaangari.opensoft.iitkgp.jaankari.hotspotUtils.CommDevice;
import jaangari.opensoft.iitkgp.jaankari.util.Weather;


public class HomeScreen extends ActionBarActivity {
    DatabaseHandler db;

    public void videoIntent(View view){
        Intent intent = new Intent(getApplicationContext(),VideoActivity.class);
        startActivity(intent);
    }

    public void newsIntent(View view){
        Intent intent = new Intent(getApplicationContext(),NewsListActivity.class);
        startActivity(intent);
    }

    public void healthIntent(View view){
        Intent intent = new Intent(getApplicationContext(),HealthListActivity.class);
        startActivity(intent);
    }

    public void commodityIntent(View view){
        Intent intent = new Intent(getApplicationContext(),CommodityActivity.class);
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
//        else if(id==R.id.update_profile_picture){
//            Intent intent =  new Intent(this,UpdateProfilePicActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        Intent inten1 = new Intent(this,QueryAlarmReciever.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, QueryAlarmReciever.REQUEST_CODE,inten1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        long firstMillis = System.currentTimeMillis();
        long intervalMillis=86400000;
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);

        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){
                    while (true) {
                        Intent temp = new Intent(getApplicationContext(), DownloadFilesService.class);
                        try{
                        startService(temp);
                        Thread.sleep(10000);
                    }catch(Exception e){
                            e.printStackTrace();
                        }
                }
            }
        });


        db = new DatabaseHandler(this.getApplicationContext());
        Weather weather = null;
        try {
            weather = db.getCurrentWeather("Kharagpur");
            if (weather != null) {
                TextView temp = (TextView) findViewById(R.id.temp);
                ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
                TextView humidity = (TextView) findViewById(R.id.humidity);
                Log.e("Home-Screen", Float.toString(weather.getTemp()));
                DecimalFormat df = new DecimalFormat("#.#");
                temp.setText(""+df.format((weather.getTemp()-273))+"\u00b0");
                switch (weather.getDescription()) {
                    case "Clear":
                        weatherIcon.setImageResource(R.drawable.clear);
                        break;
                    case "Cloudy":
                        weatherIcon.setImageResource(R.drawable.cloudy);
                        break;
                    case "Rain":
                        weatherIcon.setImageResource(R.drawable.rain);
                        break;
                }

                humidity.setText(Integer.toString(weather.getHumidity()) + " % Humidity");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        db.closeDB();
        Intent intent = new Intent(getApplicationContext(),DownloadUpdated.class);
        startService(intent);


        Intent bgServiceIntent = new Intent(getApplicationContext(), WifiHandler.class);
        startService(bgServiceIntent);


        Intent commService = new Intent(getApplicationContext(), QueryHandler.class);
        startService(commService); // TODO - ROhan - Reopen this handler

        Intent resultsHandler = new Intent(getApplicationContext(), ResultsHandler.class);
        startService(resultsHandler);

        Intent fileServer = new Intent(getApplicationContext(), FileServer.class);
        startService(fileServer);

        // ToDo - Rohan : Remove This IP TESTING PART

        try {
            CommDevice cd = new CommDevice(getApplicationContext());
            String myIp = cd.getMyIp();
            Log.d("MyIp", myIp);
            Log.d("MyIpBroadCast", cd.getBroadcast());


        } catch (Exception e) {
            e.printStackTrace();
        }


//        ImageView mImageView = (ImageView)findViewById(R.id.pro_pic_menu);
//        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
//        String path = sp1.getString("proPic",null);
//        if(path!=null)
//            Log.v(PRINT_SERVICE,"Path" + path);
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
