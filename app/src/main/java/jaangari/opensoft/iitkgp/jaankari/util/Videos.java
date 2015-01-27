package jaangari.opensoft.iitkgp.jaankari.util;

/**
 * Created by rahulanishetty on 1/25/15.
 */
public class Videos {

    private int id;
    private String filename;
    private String path;
    private int category;
    private float rating;
    private int history;
    private int isRated;

    public Videos(){}

    public Videos(int id,String filename,String path,int category,float rating){
        this.id = id;
        this.filename = filename;
        this.path = path;
        this.category = category;
        this.rating = rating;
    }

    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getName(){
        return this.filename;
    }

    public void setName(String name){
        this.filename = name;
    }

    public String getPath(){
        return this.path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public int getCategory(){
        return this.category;
    }

    public void setCategory(int category){
        this.category = category;
    }

    public float getRating(){
        return this.rating;
    }

    public void setRating(float rating){
        this.rating = rating;
    }

    public int getHistory(){
        return this.history;
    }

    public void setHistory(int history){
        this.history = history;
    }

    public void setIsRated(int isRated){
        this.isRated = isRated;
    }
    public int getIsRated(){
        return this.isRated;
    }

}
