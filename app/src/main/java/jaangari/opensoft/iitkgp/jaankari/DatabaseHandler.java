package jaangari.opensoft.iitkgp.jaankari;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jaangari.opensoft.iitkgp.jaankari.util.Health;
import jaangari.opensoft.iitkgp.jaankari.util.News;
import jaangari.opensoft.iitkgp.jaankari.util.PairCategory;
import jaangari.opensoft.iitkgp.jaankari.util.Videos;
import jaangari.opensoft.iitkgp.jaankari.util.Weather;

/**
 * Created by rahulanishetty on 1/25/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Jaankari";
    private static final int DATABASE_VERSION=1;

    //Table Names
    private static final String TABLE_VIDEOS = "Videos";
    private static final String TABLE_WEATHER = "Weather";
    private static final String TABLE_NEWS = "News";
    private static final String TABLE_HEALTH = "Health";

    //Video Columns
    private static final String VIDEOS_ID = "id";
    private static final String VIDEOS_FILENAME = "filename";
    private static final String VIDEOS_PATH = "path";
    private static final String VIDEOS_CATEGORY = "category";
    private static final String VIDEOS_RATING = "rating";
    private static final String VIDEOS_HISTORY = "history";
    private static final String VIDEOS_RATED = "isRated";

    //Weather Column
    private static final String WEATHER_ID = "id";
    private static final String WEATHER_CITY = "city";
    private static final String WEATHER_MAIN = "main";
    private static final String WEATHER_DESCRIPTION = "description";
    private static final String WEATHER_TEMP = "temp";
    private static final String WEATHER_MIN_TEMP = "min_temp";
    private static final String WEATHER_MAX_TEMP = "max_temp";
    private static final String WEATHER_HUMIDITY = "humidity";

    //News Column
    private static final String NEWS_ID = "id";
    private static final String NEWS_TITLE="title";
    private static final String NEWS_TEXT = "text";
    private static final String NEWS_SUMMARY="summary";
    private static final String NEWS_PLACE="place";

    //Health Column
    private static final String HEALTH_ID = "id";
    private static final String HEALTH_TITLE = "title";
    private static final String HEALTH_TEXT = "text";


    private final String TAG = "Database";
    private final String CURR_CITY = "Kharagpur";
    //TODO Education/Books Database Schema on Server and App

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VIDEOS_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_VIDEOS+"(" +VIDEOS_ID+" INTEGER PRIMARY KEY," +
                VIDEOS_FILENAME +" TEXT,"+VIDEOS_PATH + " TEXT,"+VIDEOS_CATEGORY + " INTEGER,"+VIDEOS_RATING + " FLOAT,"+VIDEOS_HISTORY + " INTEGER, "+VIDEOS_RATED + " INTEGER"+");";

        String CREATE_WEATHER_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_WEATHER+"(" +WEATHER_ID+" INTEGER PRIMARY KEY," +
                WEATHER_CITY +" TEXT,"+WEATHER_DESCRIPTION + " TEXT,"+ WEATHER_MAIN + " TEXT,"+ WEATHER_TEMP + " REAL,"+
                WEATHER_MAX_TEMP + " REAL,"+WEATHER_MIN_TEMP + " REAL,"+WEATHER_HUMIDITY + " INTEGER"+");";

        String CREATE_NEWS_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NEWS+"(" +NEWS_ID+" INTEGER PRIMARY KEY," +
                NEWS_PLACE +" TEXT,"+NEWS_TITLE + " TEXT,"+NEWS_SUMMARY +" TEXT,"+NEWS_TEXT +" TEXT"+");";

        String CREATE_HEALTH_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_HEALTH+"(" +HEALTH_ID+" INTEGER PRIMARY KEY," +
                HEALTH_TITLE +" TEXT,"+HEALTH_TEXT + " TEXT"+");";

        db.execSQL(CREATE_VIDEOS_TABLE);
        Log.v(TAG, "Videos created");
        db.execSQL(CREATE_WEATHER_TABLE);
        Log.v(TAG, "Weather Table Created");
        db.execSQL(CREATE_NEWS_TABLE);
        Log.v(TAG,"News Table Created");
        db.execSQL(CREATE_HEALTH_TABLE);
        Log.v(TAG,"Health Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTH);
        onCreate(db);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    public void addVideo(Videos video){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEOS_ID,video.getID());
        values.put(VIDEOS_FILENAME,video.getName());
        values.put(VIDEOS_PATH,video.getPath());
        values.put(VIDEOS_CATEGORY,video.getCategory());
        values.put(VIDEOS_RATING,video.getRating());
        values.put(VIDEOS_HISTORY,0);
        values.put(VIDEOS_RATED,0);
        db.insert(TABLE_VIDEOS,null,values);
        db.close();
    }

    public void addNews(News news){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NEWS_ID,news.getID());
        values.put(NEWS_PLACE,news.getPlace());
        values.put(NEWS_SUMMARY,news.getSummary());
        values.put(NEWS_TEXT,news.getText());
        values.put(NEWS_TITLE,news.getTitle());
        db.insert(TABLE_NEWS,null,values);
        db.close();
    }

    public void addWeather(Weather weather){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WEATHER_ID,weather.getID());
        values.put(WEATHER_CITY,weather.getCity());
        values.put(WEATHER_DESCRIPTION,weather.getDescription());
        values.put(WEATHER_HUMIDITY,weather.getHumidity());
        values.put(WEATHER_MAIN,weather.getMain());
        values.put(WEATHER_MAX_TEMP,weather.getMaxTemp());
        values.put(WEATHER_MIN_TEMP,weather.getMinTemp());
        values.put(WEATHER_TEMP,weather.getTemp());
        db.insert(TABLE_WEATHER,null,values);
        Log.e(TAG,"Weather tuple inserted");
        db.close();
    }

    public void addHealth(Health health){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HEALTH_ID,health.getID());
        values.put(HEALTH_TEXT,health.getText());
        values.put(HEALTH_TITLE,health.getTitle());
        db.insert(TABLE_HEALTH,null,values);
        db.close();
    }


    public void updateVideoHistory(int id, int history){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEOS_HISTORY,history);
        db.update(TABLE_VIDEOS,values,VIDEOS_ID+ " = ?",new String[]{String.valueOf(id)});
        Log.e(TAG,"Updated History");
    }

    public void updateRating(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEOS_RATED,1);
        db.update(TABLE_VIDEOS,values,VIDEOS_ID+ " = ?",new String[]{String.valueOf(id)});
        Log.e(TAG,"Updated isRated");
    }




    public List<Videos> getAllVideosbyCategory(int category){
        List<Videos> videos = new ArrayList<Videos>();
        String selectQuery = null;
        if(category!=2){
            selectQuery = "SELECT * FROM "+ TABLE_VIDEOS + " WHERE "+ VIDEOS_CATEGORY + "=" + category+ " OR " + VIDEOS_CATEGORY + "=2;";
        }
        else{
            selectQuery = "SELECT * FROM " + TABLE_VIDEOS + " WHERE " + VIDEOS_HISTORY + ">0;";
        }

        Log.e(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                Videos video = new Videos();
                video.setID(c.getInt(c.getColumnIndex(VIDEOS_ID)));
                video.setName(c.getString(c.getColumnIndex(VIDEOS_FILENAME)));
                video.setCategory(category);
                video.setPath(c.getString(c.getColumnIndex(VIDEOS_PATH)));
                video.setRating(c.getFloat(c.getColumnIndex(VIDEOS_RATING)));
                video.setHistory(c.getInt(c.getColumnIndex(VIDEOS_HISTORY)));
                video.setIsRated(c.getInt(c.getColumnIndex(VIDEOS_RATED)));
                videos.add(video);
                Log.e(TAG,video.getName());
            }while(c.moveToNext());
        }
        else{
            Log.e(TAG,c.toString());
        }
        return videos;
    }

    public Weather getCurrentWeather(){
        String query = "SELECT * FROM " + TABLE_WEATHER + " WHERE " + WEATHER_CITY + "='" + CURR_CITY +  "';";
        Log.e("Query",query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Weather weather = new Weather();
        Log.e("Query",Integer.toString(c.getCount()));
        if(c.moveToLast()){
            Log.e("Query","Inside iteration");
            weather.setID(c.getInt(c.getColumnIndex(WEATHER_ID)));
            weather.setCity(c.getString(c.getColumnIndex(WEATHER_CITY)));
            weather.setMain(c.getString(c.getColumnIndex(WEATHER_MAIN)));
            weather.setDescription(c.getString(c.getColumnIndex(WEATHER_DESCRIPTION)));
            weather.setTemp(c.getFloat(c.getColumnIndex(WEATHER_TEMP)));
            weather.setMInTemp(c.getFloat(c.getColumnIndex(WEATHER_MIN_TEMP)));
            weather.setMaxTemp(c.getFloat(c.getColumnIndex(WEATHER_MAX_TEMP)));
            weather.setHumidity(c.getInt(c.getColumnIndex(WEATHER_HUMIDITY)));

        }
        return weather;
    }
//TODO:Gets list of category,ids based on the search query.


    public ArrayList<PairCategory> fetchIndexList(String query) {

        ArrayList<Integer> indexes = new ArrayList<Integer>();

        indexes.add(33);

        indexes.add(66);

        ArrayList<PairCategory> res = (new ArrayList<>());

        res.add(new PairCategory("Sports",indexes));

        res.add(new PairCategory("News",indexes));

        return res;

    }


    //TODO: Returnd the filepath corresponding the category and id. Null if n.a
    public String checkLocalDatabase(String category, int id) {
            return null;
    }
//TODO:Returns Filepath even though file isnot present in the local db.
    public String getFilePath(String category, int id) {
        return null;
    }
}
