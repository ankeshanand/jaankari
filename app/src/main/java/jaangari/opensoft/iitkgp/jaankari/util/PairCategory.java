package jaangari.opensoft.iitkgp.jaankari.util;


        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.util.ArrayList;

/**
 * Created by rohanraja on 28/01/15.
 */
public class PairCategory {
    String category;
    ArrayList<Integer> ids;
    public PairCategory(String s, ArrayList<Integer> id){
        category = s;
        ids = id;
    }

    public JSONObject getJSONOut() throws Exception
    {
        return  new JSONObject().put(category, new JSONArray(ids));
    }
}