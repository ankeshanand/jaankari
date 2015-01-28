package jaangari.opensoft.iitkgp.jaankari.util;

/**
 * Created by rahulanishetty on 1/25/15.
 */
public class Health {
    private int id;
    private String title;
    private String text;

    public Health(){}

    public Health(int id,String title, String text){
        this.id = id;
        this.title = title;
        this.text = text;
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

    public void setTitle(String name){
        this.title = name;
    }

    public String getText(){
        return this.text;
    }

    public void setText(String path){
        this.text = path;
    }
}
