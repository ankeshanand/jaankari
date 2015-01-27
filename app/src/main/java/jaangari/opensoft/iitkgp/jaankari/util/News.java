package jaangari.opensoft.iitkgp.jaankari.util;

/**
 * Created by rahulanishetty on 1/25/15.
 */

/*
 0 - Top Stories
 1 - World
 2 - India
 3 - Business
 4 - Technology
 5 - Sports
 6 - Entertainment
 */


public class News {
    private int id;
    private String title;
    private String text;
    private String summary;
    private String place;
    private int category;

    public News(){}

    public News(int id,String title,String summary,String text, String place, int category){
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

    public int getCategory(){
        return this.category;
    }
    public void setCategory(int category){
        this.category = category;
    }

}
