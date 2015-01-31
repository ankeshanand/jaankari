package jaangari.opensoft.iitkgp.jaankari.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rohanraja on 28/01/15.
 */
public class JSONresultsParser {

    private String mJSON;
    JSONObject mObj ;

    public JSONresultsParser(String s)

    {
        mJSON =s ;// "[[\"78\",\"'Narendra Modi my Action Hero,' Says New Censor Board Chief Pahlaj Nihalani\",null,\"News\",\"As questions were raised about the profusion of pro-BJP members in the new Censor Board, its chief, filmmaker Pahlaj Nihalani, underscored his admiration for Prime Minister Narendra Modi.\\r\\n\"],[\"88\",\"Congress Passed 456 Ordinances Over 50 Years. That's 9 a Year.\",null,\"News\",\"Under fire for issuing 9 ordinances in quick succession, the eight-month-old Narendra Modi government is expected to share some numbers soon to show that previous governments too have chosen to use emergency executive orders liberally. \"],[\"104\",\"Budget Session of Parliament Begins on February 23 \",null,\"News\",\"The Budget session of Parliament will begin on February 23 and the Narendra Modi government will present its first full year Budget on Saturday, February 28. \"],[\"108\",\"For Obama's Visit, Plans for 'Special Moment' with PM Modi: Sources\",null,\"News\",\"With preparations and the  itinerary for President Obama's visit in the last-mile phase, officials tell NDTV that US and Indian officials are looking at creating a \\\"special moment\\\" in the league of the spontaneous visit to the Martin Luther King memorial that was taken when Prime Minister Narendra Modi visited the White House late last year.\"],[\"109\",\"60 Planes in the Air: The Juggling Feat of Republic Day\",null,\"News\",\"Over nine minutes on Monday next, 30 aircraft of the Indian Army, Navy and Air Force will roar over Rajpath during the Republic Day parade, watched by US president Barack Obama, President Pranab Mukherjee, Prime Minister Narendra Modi and many others. \"],[\"116\",\"Congress Passed 456 Ordinances in 50 Years. That's 9 a Year.\",null,\"News\",\"Under fire for issuing 9 ordinances in quick succession, the eight-month-old Narendra Modi government is expected to share some numbers soon to show that previous governments too have chosen to use emergency executive orders liberally. \"],[\"144\",\"Modi Election \\\"a Victory of Indian-ness,\\\" Top Congress Leader Reportedly Said\",null,\"News\",\"Congress leader Janardhan Dwivedi, one of his party's most senior members, has reportedly praised the Prime Minister in an interview to news website rediff.com.\"],[\"147\",\"Praise for PM Modi? Sort of, Says Congress Leader Janardan Dwivedi\",null,\"News\",\"Congress leader Janardhan Dwivedi, one of his party's most senior members, has reportedly praised the Prime Minister in an interview to news website rediff.com.\"],[\"155\",\"First Full Budget of PM Modi on February 28\",null,\"News\",\"null\"]]" ;// s;
        try {
            mObj = new JSONObject(mJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ;
    }

    public ArrayList<SearchResults> getResults()
    {


        ArrayList<SearchResults> ans = new ArrayList<>();

        try {

            JSONArray array = mObj.getJSONArray("list");
            int size = array.length();
            for(int i=0;i<size;i++){
                JSONArray obj = (JSONArray) array.get(i);
                SearchResults sr = SearchResults.getSearchResult(obj);
                sr.IP = getIPAddr();
                ans.add(sr);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ans;
    }

    public String getIPAddr()
    {
        try {
            return mObj.getString("IP");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
