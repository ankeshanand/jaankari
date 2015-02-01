package jaangari.opensoft.iitkgp.jaankari;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jaangari.opensoft.iitkgp.jaankari.util.Commodity;
import jaangari.opensoft.iitkgp.jaankari.util.Download;
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
    private static final String TABLE_COMMODITY = "Commodity";
    private static final String TABLE_TODOWNLOAD = "Download";

    //Video Columns
    private static final String VIDEOS_ID = "id";
    private static final String VIDEOS_FILENAME = "filename";
    private static final String VIDEOS_PATH = "path";
    private static final String VIDEOS_CATEGORY = "category";
    private static final String VIDEOS_RATING = "rating";
    private static final String VIDEOS_HISTORY = "history";
    private static final String VIDEOS_RATED = "isRated";
    private static final String VIDEOS_IN_DATABASE = "isPresent";

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


    //Commodity Column
    private static final String COMM_ID = "id";
    private static final String COMM_NAME = "name";
    private static final String COMM_MIN = "min";
    private static final String COMM_MAX = "max";


    //To Download
    private static final String DOWNLOAD_ID = "id";
    private static final String DOWNLOAD_CATEGORY = "category";


    private final String TAG = "Database";
    //TODO Education/Books Database Schema on Server and App

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VIDEOS_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_VIDEOS+"(" +VIDEOS_ID+" INTEGER PRIMARY KEY," +
                VIDEOS_FILENAME +" TEXT,"+VIDEOS_PATH + " TEXT,"+VIDEOS_CATEGORY + " INTEGER,"+VIDEOS_RATING + " FLOAT,"+VIDEOS_HISTORY
                + " INTEGER, "+VIDEOS_RATED + " INTEGER,"+VIDEOS_IN_DATABASE +" BOOLEAN" +");";

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

        String CREATE_COMMODITY_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_COMMODITY+"(" +COMM_ID+" CHAR PRIMARY KEY," +
                COMM_NAME +" TEXT,"+COMM_MIN + " TEXT,"+ COMM_MAX + " TEXT" + " );";

        String CREATE_DOWNLOAD_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TODOWNLOAD + "(" + DOWNLOAD_ID + " INTEGER," + DOWNLOAD_CATEGORY + " TEXT" + ");";

        db.execSQL(CREATE_VIDEOS_TABLE);
        Log.v(TAG, "Videos Table created");
        db.execSQL(CREATE_WEATHER_TABLE);
        Log.v(TAG, "Weather Table Created");
        db.execSQL(CREATE_NEWS_TABLE);
        Log.v(TAG, "News Table Created");
        db.execSQL(CREATE_HEALTH_TABLE);
        Log.v(TAG, "Health Table Created");
        db.execSQL(CREATE_VIRTUAL_TABLE);
        Log.v(TAG, "Virtual Table Created");
        db.execSQL(CREATE_COMMODITY_TABLE);
        Log.v(TAG, "Commodity Table Created");
        db.execSQL(CREATE_DOWNLOAD_TABLE);
        Log.v(TAG,"Download Table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIRTUAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMODITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOWNLOAD);
        onCreate(db);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    public void addDownload(int id,String category){
        deleteDownload(id,category);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DOWNLOAD_ID,id);
        if("Video".equals(category)){
            values.put(DOWNLOAD_CATEGORY,"Video");
        }
        else if("News".equals(category)){
            values.put(DOWNLOAD_CATEGORY,"News");
        }
        else if("Health".equals(category)){
            values.put(DOWNLOAD_CATEGORY,"Health");
        }
        else if("Commodity".equals(category)){
            values.put(DOWNLOAD_CATEGORY,"Commodity");
        }
        else if("Weather".equals(category)){
            values.put(DOWNLOAD_CATEGORY,"Weather");
        }
        db.insert(TABLE_TODOWNLOAD,null,values);
        db.close();
    }

    public void deleteDownload(int id, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOWNLOAD,DOWNLOAD_ID+"=" + id + "  AND " + DOWNLOAD_CATEGORY +" = '" + category+"';",null);
        db.close();
    }


    public void addVideo(Videos video) {
        deleteVideo(video.getID());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEOS_ID, video.getID());
        values.put(VIDEOS_FILENAME, video.getName());
        values.put(VIDEOS_PATH, video.getPath());
        values.put(VIDEOS_CATEGORY, video.getCategory());
        values.put(VIDEOS_RATING, video.getRating());
        values.put(VIDEOS_HISTORY, 0);
        values.put(VIDEOS_RATED, 0);
        values.put(VIDEOS_IN_DATABASE, 0);
        db.insert(TABLE_VIDEOS, null, values);
        values = new ContentValues();
        values.put(VIRTUAL_ID, video.getID());
        values.put(VIRTUAL_CATEGORY, "Video");
        values.put(VIRTUAL_TITLE, video.getName());
        values.put(VIRTUAL_SUMMARY, (String) null);
        values.put(VIRTUAL_TEXT, (String) null);
        db.insert(TABLE_VIRTUAL, null, values);
        db.close();
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
            video.setIsPresent(c.getInt(c.getColumnIndex(VIDEOS_IN_DATABASE)));
        }
        Log.d(TAG,video.getName() + " : " + video.getPath() );
        return  video;
    }

    public List<Videos> getAllVideosbyCategory(int category){
        List<Videos> videos = new ArrayList<Videos>();
        String selectQuery = null;
        if(category!=2){
            selectQuery = "SELECT * FROM "+ TABLE_VIDEOS + " WHERE "+VIDEOS_IN_DATABASE + "=1 AND ("+ VIDEOS_CATEGORY + "=" + category+ " OR " + VIDEOS_CATEGORY + "=2);";
        }
        else{
            selectQuery = "SELECT * FROM " + TABLE_VIDEOS + " WHERE " + VIDEOS_HISTORY + ">0 AND " + VIDEOS_IN_DATABASE + "=1;";
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

    public void deleteVideo(int id){
        Log.d(TAG, "Deleting video by : " + id );
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VIDEOS,VIDEOS_ID+"=" + id,null);
        db.delete(TABLE_VIRTUAL,VIRTUAL_ID+"="+id +" AND "+ VIRTUAL_CATEGORY+" = 'Video'",null);
        db.close();
    }

    public void updateVideo(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEOS_IN_DATABASE,1);
        db.update(TABLE_VIDEOS,values,VIDEOS_ID+ " = ?",new String[]{String.valueOf(id)});
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

    public void updateRated(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEOS_RATED,1);
        db.update(TABLE_VIDEOS,values,VIDEOS_ID+ " = ?",new String[]{String.valueOf(id)});
        Log.e(TAG,"Updated isRated");
        db.close();
    }

    public void addNews(News news){
        deleteNews(news.getID());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NEWS_ID, news.getID());
        values.put(NEWS_PLACE,news.getPlace());
        values.put(NEWS_SUMMARY,news.getSummary());
        values.put(NEWS_TEXT,news.getText());
        values.put(NEWS_TITLE, news.getTitle());
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

    public void deleteNews(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NEWS,NEWS_ID+"=" + id,null);
        db.delete(TABLE_VIRTUAL,VIRTUAL_ID+"="+id +" AND "+ VIRTUAL_CATEGORY+" = 'News'",null);
        db.close();
    }

    public List<News> getAllNewsbyCategory(int category){
        List<News> news = new ArrayList<News>();
        String selectQuery = "SELECT * FROM " + TABLE_NEWS + " WHERE " + NEWS_CATEGORY + "=" + category+";";
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

    public void updateNews(News news){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NEWS_CATEGORY,news.getCategory());
        values.put(NEWS_SUMMARY,news.getSummary());
        values.put(NEWS_PLACE,news.getPlace());
        values.put(NEWS_TEXT,news.getText());
        db.update(TABLE_NEWS, values, NEWS_ID + " = ?", new String[]{String.valueOf(news.getID())});
        values = new ContentValues();
        values.put(VIRTUAL_SUMMARY,news.getSummary());
        values.put(VIRTUAL_TEXT,news.getText());
        values.put(VIRTUAL_TITLE,news.getTitle());
        values.put(VIRTUAL_CATEGORY,"News");
        db.update(TABLE_VIRTUAL, values, VIRTUAL_ID + " = ?", new String[]{String.valueOf(news.getID())});
        db.close();
    }

    public void addWeather(Weather weather){
        deleteWeather(weather.getID());
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

    public void deleteWeather(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEATHER,NEWS_ID+"=" + id,null);
        db.close();
    }

    public void updateWeather(Weather weather){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WEATHER_TEMP,weather.getTemp());
        values.put(WEATHER_MIN_TEMP,weather.getMinTemp());
        values.put(WEATHER_MAX_TEMP,weather.getMaxTemp());
        values.put(WEATHER_CITY,weather.getCity());
        values.put(WEATHER_DESCRIPTION,weather.getDescription());
        values.put(WEATHER_HUMIDITY,weather.getHumidity());
        values.put(WEATHER_MAIN,weather.getMain());
        db.update(TABLE_WEATHER, values, WEATHER_ID + " = ?" + " AND " + VIRTUAL_CATEGORY + "= Weather", new String[]{String.valueOf(weather.getID())});
        db.close();
    }


    public void addHealth(Health health){
        deleteHealth(health.getID());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HEALTH_ID,health.getID());
        values.put(HEALTH_TEXT,health.getText());
        values.put(HEALTH_TITLE,health.getTitle());
        db.insert(TABLE_HEALTH, null, values);
        values = new ContentValues();
        values.put(VIRTUAL_ID,health.getID());
        values.put(VIRTUAL_CATEGORY,"Health");
        values.put(VIRTUAL_TITLE,health.getTitle());
        values.put(VIRTUAL_SUMMARY,(String)null);
        values.put(VIRTUAL_TEXT,health.getText());
        db.insert(TABLE_VIRTUAL,null,values);
        db.close();
    }

    public List<Health> getAllHealth(){
        List<Health> healths = new ArrayList<Health>();
        String selectQuery = "SELECT * FROM " + TABLE_HEALTH+"";
        Log.e(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                Health health1 = new Health();
                health1.setID(c.getInt(c.getColumnIndex(NEWS_ID)));
                health1.setTitle(c.getString(c.getColumnIndex(NEWS_TITLE)));
                health1.setText(c.getString(c.getColumnIndex(NEWS_TEXT)));
                healths.add(health1);
                Log.e(TAG,health1.getTitle());
            }while(c.moveToNext());
        }
        else{
            Log.e(TAG,c.toString());
        }
        db.close();
        return healths;
    }

    public void deleteHealth(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HEALTH,HEALTH_ID+"=" + id,null);
        db.delete(TABLE_VIRTUAL,VIRTUAL_ID+"="+id +" AND "+ VIRTUAL_CATEGORY+" = 'Health'",null);
        db.close();
    }

    public void updateHealth(Health health){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HEALTH_TITLE,health.getTitle());
        values.put(HEALTH_TEXT,health.getText());
        db.update(TABLE_HEALTH, values, HEALTH_ID + " = ?", new String[]{String.valueOf(health.getID())});
        values = new ContentValues();
        values.put(VIRTUAL_TITLE,health.getTitle());
        values.put(VIRTUAL_TEXT,health.getText());
        db.update(TABLE_VIRTUAL,values,HEALTH_ID +" = ?" +  " AND " + VIRTUAL_CATEGORY + " = Health", new String[]{String.valueOf(health.getID())});
        db.close();
    }

    public void addCommodity(Commodity commodity){
        deleteCommodity(commodity.getID());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMM_ID,commodity.getID());
        values.put(COMM_NAME, commodity.getName());
        values.put(COMM_MIN,commodity.getMin());
        values.put(COMM_MAX,commodity.getMax());
        db.insert(TABLE_COMMODITY,null,values);
        db.close();
    }

    public void updateCommodity(Commodity commodity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMM_MAX,commodity.getMax());
        values.put(COMM_MIN,commodity.getMin());
        values.put(COMM_NAME,commodity.getName());
        db.update(TABLE_VIRTUAL,values,COMM_ID+ " = ?", new String[]{String.valueOf(commodity.getID())});
        db.close();
    }

    public void deleteCommodity(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMMODITY,COMM_ID+"=" + id,null);
        db.close();
    }

    public Weather getCurrentWeather(String CURR_CITY){
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


//
//<<<<<<< HEAD
//    public Weather getCurrentWeather(){
//        String query = "SELECT * FROM " + TABLE_WEATHER + " WHERE " + WEATHER_CITY + "='" + CURR_CITY +  "';";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(query, null);
//        Weather weather = null;
//        if(c.moveToFirst()){
//            weather.setCity(c.getString(c.getColumnIndex(WEATHER_CITY)));
//            weather.setTemp(c.getFloat(c.getColumnIndex(WEATHER_TEMP)));
//            weather.setHumidity(c.getInt(c.getColumnIndex(WEATHER_HUMIDITY)));
//            weather.setMain(c.getString(c.getColumnIndex(WEATHER_MAIN)));
//            weather.setDescription(c.getString(c.getColumnIndex(WEATHER_DESCRIPTION)));
//        }
//        return weather;
//    }
////TODO:Gets list of category,ids based on the search query.
//    public ArrayList<SearchableActivity.PairCategory> fetchIndexList(String query) {
//        return null;
//    }
////TODO: Returnd the filepath corresponding the category and id. Null if n.a
//    public String checkLocalDatabase(String category, int id) {
//            return null;
//    }
////TODO:Returns Filepath even though file isnot present in the local db.
//=======

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
                if("Video".equals(category)){
                    Videos video = this.getVideobyId(id);
                    Log.d(TAG,"Result :" + video.getName() + "is Present : " + video.getIsPresent());
                    if(video.getIsPresent()==1){
                        list.add(searchResults);
                    }
                }
                else {
                    list.add(searchResults);
                }
            }while(results.moveToNext());
        }
        db.close();
        return list;

    }


    public List<Commodity> getCommodityPrices(){
        List<Commodity> prices = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COMMODITY + " LIMIT 25";

        Log.e(TAG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                Commodity commodity = new Commodity();
                commodity.setID(c.getString(c.getColumnIndex(COMM_ID)));
                commodity.setName(c.getString(c.getColumnIndex(COMM_NAME)));
                commodity.setMin(c.getString(c.getColumnIndex(COMM_MIN)));
                commodity.setMax(c.getString(c.getColumnIndex(COMM_MAX)));
                prices.add(commodity);
                Log.e(TAG,commodity.getName());
            }while(c.moveToNext());
        }
        else{
            Log.e(TAG,c.toString());
        }
        return prices;
    }

    public ArrayList<Download> getToDoDownload() {
        ArrayList<Download> list = new ArrayList<Download>();
        String selectQuery = "SELECT * FROM " + TABLE_TODOWNLOAD + " WHERE 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Download down = new Download();
                down.id = c.getInt(c.getColumnIndex(DOWNLOAD_ID));
                down.category = c.getString(c.getColumnIndex(DOWNLOAD_CATEGORY));
                list.add(down);
            } while (c.moveToNext());
        }
        db.close();
        return list;
    }
//TODO:Gets list of category,ids based on the search query.


    //TODO:Gets list of category,ids based on the search query.

    public String fetchIndexList(String query) {

        ArrayList<SearchResults> list = searchMatches(query,null);

        JSONArray ar = new JSONArray();
        for(SearchResults i : list)
        {
            ar.put(i.getArray());
        }

        return ar.toString();

    }

    //TODO:Returns Filepath even though file isnot present in the local db.
    public String getFilePath(String category, int id) {
        Videos video = getVideobyId(id);
        String path = video.getPath();

        // TODO : REMOVE HARDCODE String ans = Environment.getExternalStorageDirectory() + "/Jaankari" + "/Videos/" + path.substring(path.lastIndexOf("/"));

        String ans = Environment.getExternalStorageDirectory() + "/Jaankari" + "/Videos" + path.substring(path.lastIndexOf("/"));

        Log.d("CommDevice", Environment.getExternalStorageDirectory().toString() );

        return ans;
    }
}
