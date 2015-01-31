package jaangari.opensoft.iitkgp.jaankari.util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * Created by rahulanishetty on 1/28/15.
 */
public class SearchResults implements Comparable<SearchResults>{

    int id;
    String title,summary,text,category;
    public String IP = null ;

    public SearchResults(int id,String title,String summary,String text,String category){
        this.id = id;
        this.title = title;
        this.text = text;
        this.category = category;
        this.summary = summary;
    }

    public JSONArray getArray()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(String.valueOf(id));
        res.add(title);
        res.add(summary);
        res.add(text);
        res.add(category);



        return new JSONArray(res);
    }

    public static SearchResults getSearchResult(JSONArray theArray) throws JSONException {

        SearchResults searchResults = new SearchResults(
                Integer.parseInt(theArray.getString(0)),
                theArray.getString(1),
                theArray.getString(2),
                theArray.getString(3),
                theArray.getString(4)
        );

        return searchResults;
    }


    public static ArrayList<SearchResults> mergeTwoList(ArrayList<SearchResults> a,ArrayList<SearchResults> toBeAppended){
        int size = toBeAppended.size();
        TreeMap<SearchResults,Boolean> map = new TreeMap<SearchResults,Boolean>();
        int sz = a.size();
        for(int i=0;i<sz;i++){
            map.put(a.get(i),true);
        }
        for(int i=0;i<size;i++){
            if(!map.containsKey(toBeAppended.get(i)) ){
                a.add(toBeAppended.get(i));
                map.put(toBeAppended.get(i),true);
            }
        }

        return a;
    }

    @Override
    public int compareTo(SearchResults another) {
        if(id==another.id && category.equals(another.category))
            return 0;
        else return 1;
    }

    public int getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getSummary(){
        return this.summary;
    }

    public String getCategory(){
        return this.category;
    }

}
