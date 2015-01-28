package jaangari.opensoft.iitkgp.jaankari;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jaangari.opensoft.iitkgp.jaankari.util.Health;
import jaangari.opensoft.iitkgp.jaankari.util.News;
import jaangari.opensoft.iitkgp.jaankari.util.PairCategory;
import jaangari.opensoft.iitkgp.jaankari.util.SearchResults;
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
    private static final String TABLE_VIRTUAL = "FTS_TABLE";

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
    private static final String NEWS_CATEGORY = "category";

    //Health Column
    private static final String HEALTH_ID = "id";
    private static final String HEALTH_TITLE = "title";
    private static final String HEALTH_TEXT = "text";

    //Virtual Table
    private static final String VIRTUAL_ID = "id";
    private static final String VIRTUAL_CATEGORY = "Category";
    private static final String VIRTUAL_TITLE = "title";
    private static final String VIRTUAL_SUMMARY = "Summary";
    private static final String VIRTUAL_TEXT = "text";


    private final String TAG = "Database";
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
                NEWS_PLACE +" TEXT,"+NEWS_TITLE + " TEXT,"+NEWS_SUMMARY +" TEXT,"+NEWS_TEXT +" TEXT,"+NEWS_CATEGORY + " INTEGER"+");";

        String CREATE_HEALTH_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_HEALTH+"(" +HEALTH_ID+" INTEGER PRIMARY KEY," +
                HEALTH_TITLE +" TEXT,"+HEALTH_TEXT + " TEXT"+");";

        String CREATE_VIRTUAL_TABLE = "CREATE VIRTUAL TABLE IF NOT EXISTS " + TABLE_VIRTUAL + " USING fts3("
                +VIRTUAL_ID+", "+VIRTUAL_CATEGORY+", "
                +VIRTUAL_TITLE+", "+VIRTUAL_SUMMARY+", "+VIRTUAL_TEXT+", tokenize=porter);";

        db.execSQL(CREATE_VIDEOS_TABLE);
        Log.v(TAG, "Videos Table created");
        db.execSQL(CREATE_WEATHER_TABLE);
        Log.v(TAG, "Weather Table Created");
        db.execSQL(CREATE_NEWS_TABLE);
        Log.v(TAG,"News Table Created");
        db.execSQL(CREATE_HEALTH_TABLE);
        Log.v(TAG,"Health Table Created");
        db.execSQL(CREATE_VIRTUAL_TABLE);
        Log.v(TAG,"Virtual Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIRTUAL);
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
        db.insert(TABLE_VIDEOS, null, values);
        values = new ContentValues();
        values.put(VIRTUAL_ID,video.getID());
        values.put(VIRTUAL_CATEGORY,"Video");
        values.put(VIRTUAL_TITLE,video.getName());
        values.put(VIRTUAL_SUMMARY,(String)null);
        values.put(VIRTUAL_TEXT,(String)null);
        db.insert(TABLE_VIRTUAL,null,values);
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
        values.put(NEWS_CATEGORY,news.getCategory());
        db.insert(TABLE_NEWS,null,values);
        values = new ContentValues();
        values.put(VIRTUAL_ID,news.getID());
        values.put(VIRTUAL_CATEGORY,"News");
        values.put(VIRTUAL_TITLE,news.getTitle());
        values.put(VIRTUAL_SUMMARY,news.getSummary());
        values.put(VIRTUAL_TEXT,news.getText());
        db.insert(TABLE_VIRTUAL,null,values);
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
        values = new ContentValues();
        values.put(VIRTUAL_ID,health.getID());
        values.put(VIRTUAL_CATEGORY,"Health");
        values.put(VIRTUAL_TITLE,health.getTitle());
        values.put(VIRTUAL_SUMMARY,(String)null);
        values.put(VIRTUAL_TEXT,health.getText());
        db.insert(TABLE_VIRTUAL,null,values);
        db.close();
    }


    public void updateVideoHistory(int id, int history){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEOS_HISTORY,history);
        db.update(TABLE_VIDEOS,values,VIDEOS_ID+ " = ?",new String[]{String.valueOf(id)});
        Log.e(TAG,"Updated History");
        db.close();
    }

    public void updateRating(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEOS_RATED,1);
        db.update(TABLE_VIDEOS,values,VIDEOS_ID+ " = ?",new String[]{String.valueOf(id)});
        Log.e(TAG,"Updated isRated");
        db.close();
    }


    public List<News> getAllNewsbyCategory(int category){
        List<News> news = new ArrayList<News>();
        String selectQuery = "SELECT * FROM " + TABLE_NEWS + " WHERE " + NEWS_CATEGORY + "=" + category;
        Log.e(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                News news1 = new News();
                news1.setID(c.getInt(c.getColumnIndex(NEWS_ID)));
                news1.setTitle(c.getString(c.getColumnIndex(NEWS_TITLE)));
                news1.setCategory(category);
                news1.setSummary(c.getString(c.getColumnIndex(NEWS_SUMMARY)));
                news1.setText(c.getString(c.getColumnIndex(NEWS_TEXT)));
                news1.setPlace(c.getString(c.getColumnIndex(NEWS_PLACE)));
                news.add(news1);
                Log.e(TAG,news1.getTitle());
            }while(c.moveToNext());
        }
        else{
            Log.e(TAG,c.toString());
        }
        db.close();
        return news;
    }

    public Videos getVideobyId(int id){
        Videos video = new Videos();
        String selectQuery = "SELECT * FROM "+TABLE_VIDEOS + " WHERE " + VIDEOS_ID + "="+id + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            video.setID(c.getInt(c.getColumnIndex(VIDEOS_ID)));
            video.setName(c.getString(c.getColumnIndex(VIDEOS_FILENAME)));
            video.setCategory(c.getInt(c.getColumnIndex(VIDEOS_CATEGORY)));
            video.setPath(c.getString(c.getColumnIndex(VIDEOS_PATH)));
            video.setRating(c.getFloat(c.getColumnIndex(VIDEOS_RATING)));
            video.setHistory(c.getInt(c.getColumnIndex(VIDEOS_HISTORY)));
            video.setIsRated(c.getInt(c.getColumnIndex(VIDEOS_RATED)));
        }
        return  video;
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
        db.close();
        return videos;
    }


    public ArrayList<SearchResults> searchMatches(String query, String[] columns){
        ArrayList<SearchResults> list = new ArrayList<SearchResults>();
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLE_VIRTUAL);
        String titleQuery =  builder.buildQuery(null,VIRTUAL_TITLE + " MATCH '"+query+"*'",null,null,null,null);
        String summaryQuery = builder.buildQuery(null,VIRTUAL_SUMMARY + " MATCH '"+query+"*'",null,null,null,null);
        String textQuery = builder.buildQuery(null,VIRTUAL_TEXT + " MATCH '"+query+"*'",null,null,null,null);
        String unionQuery = builder.buildUnionQuery(new String[]{titleQuery,summaryQuery,textQuery},null,null);
        Log.e(TAG,unionQuery.toString());
        unionQuery = unionQuery.replace("ALL"," ");
        Log.e(TAG,unionQuery.toString());
        Cursor results = db.rawQuery(unionQuery,null);
        if(results.moveToFirst()){
            do{
                int id = results.getInt(results.getColumnIndex(VIRTUAL_ID));
                String category = results.getString(results.getColumnIndex(VIRTUAL_CATEGORY));
                String title = results.getString(results.getColumnIndex(VIRTUAL_TITLE));
                String summary = results.getString(results.getColumnIndex(VIRTUAL_SUMMARY));
                String text = results.getString(results.getColumnIndex(VIRTUAL_TEXT));
                SearchResults searchResults = new SearchResults(id,title,summary,text,category);
                list.add(searchResults);
            }while(results.moveToNext());
        }
        db.close();
        return  list;
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
