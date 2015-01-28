package jaangari.opensoft.iitkgp.jaankari.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rohanraja on 28/01/15.
 */
public class JSONresultsParser {

    private String mJSON;

    public JSONresultsParser(String s)
    {
        mJSON = "{\"list\":[{\"Sports\":[33,66]},{\"News\":[33,66]}],\"IP\":\"\\/192.168.43.1\"}" ;// s;
    }

    public void getResults()
    {
        try {
            JSONObject jResults = new JSONObject(mJSON);
            String theIP = jResults.getString("IP");
            Log.d("BroadcastRec", "IP is : " + theIP);

            JSONArray results = jResults.getJSONArray("list");

            for(int i=0; i< results.length(); i++ )
            {
                JSONObject oneCat = (JSONObject) results.get(i);
                Log.d("BroadcastRec", "OUTPUT : " + oneCat.keys().next());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
