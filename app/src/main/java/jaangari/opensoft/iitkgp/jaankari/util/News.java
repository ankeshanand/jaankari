package jaangari.opensoft.iitkgp.jaankari.util;

/**
 * Created by rahulanishetty on 1/25/15.
 */
public class News {
    private int id;
    private String title;
    private String text;
    private String summary;
    private String place;
    private String category;

    public News(int id,String title,String summary,String text, String place, String category){
        this.id = id;
        this.title = title;
        this.text = text;
        this.summary = summary;
        this.category = category;
        this.place = place;
    }

    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getText(){
        return this.text;
    }
    public void setText(String text){
        this.text = text;
    }

    public String getSummary(){
        return this.summary;
    }
    public void setSummary(String text){
        this.summary = text;
    }

    public String getPlace(){
        return this.place;
    }
    public void setPlace(String text){
        this.place = text;
    }

    public String getCategory(){
        return this.category;
    }
    public void setCategory(String text){
        this.category = text;
    }

}
