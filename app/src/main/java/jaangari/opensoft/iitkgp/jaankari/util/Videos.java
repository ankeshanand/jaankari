package jaangari.opensoft.iitkgp.jaankari.util;

/**
 * Created by rahulanishetty on 1/25/15.
 */
public class Videos {

    private int id;
    private String filename;
    private String path;

    public Videos(int id,String filename,String path){
        this.id = id;
        this.filename = filename;
        this.path = path;
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

}
