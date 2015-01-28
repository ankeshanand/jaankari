package jaangari.opensoft.iitkgp.jaankari.util;

/**
 * Created by rahulanishetty on 1/28/15.
 */
public class SearchResults{
    int id;
    String title,summary,text,category;
    public SearchResults(int id,String title,String summary,String text,String category){
        this.id = id;
        this.title = title;
        this.text = text;
        this.category = category;
        this.summary = summary;
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
