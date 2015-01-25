package jaangari.opensoft.iitkgp.jaankari;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import jaangari.opensoft.iitkgp.jaankari.util.Health;
import jaangari.opensoft.iitkgp.jaankari.util.News;
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

    //TODO Education/Books Database Schema on Server and App

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VIDEOS_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_VIDEOS+"(" +VIDEOS_ID+" INTEGER PRIMARY KEY," +
                VIDEOS_FILENAME +" TEXT,"+VIDEOS_PATH + " TEXT"+");";

        String CREATE_WEATHER_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_WEATHER+"(" +WEATHER_ID+" INTEGER PRIMARY KEY," +
                WEATHER_CITY +" TEXT,"+WEATHER_DESCRIPTION + " TEXT,"+ WEATHER_MAIN + " TEXT,"+ WEATHER_TEMP + " REAL,"+
                WEATHER_MAX_TEMP + " REAL,"+WEATHER_MIN_TEMP + " REAL,"+WEATHER_HUMIDITY + " INTEGER"+");";

        String CREATE_NEWS_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NEWS+"(" +NEWS_ID+" INTEGER PRIMARY KEY," +
                NEWS_PLACE +" TEXT,"+NEWS_TITLE + " TEXT,"+NEWS_SUMMARY +" TEXT,"+NEWS_TEXT +" TEXT"+");";

        String CREATE_HEALTH_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_HEALTH+"(" +HEALTH_ID+" INTEGER PRIMARY KEY," +
                HEALTH_TITLE +" TEXT,"+HEALTH_TEXT + " TEXT"+");";

        db.execSQL(CREATE_VIDEOS_TABLE);
        db.execSQL(CREATE_WEATHER_TABLE);
        db.execSQL(CREATE_NEWS_TABLE);
        db.execSQL(CREATE_HEALTH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTH);
        onCreate(db);
    }


    public void addVideo(Videos video){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEOS_ID,video.getID());
        values.put(VIDEOS_FILENAME,video.getName());
        values.put(VIDEOS_PATH,video.getPath());
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


}
