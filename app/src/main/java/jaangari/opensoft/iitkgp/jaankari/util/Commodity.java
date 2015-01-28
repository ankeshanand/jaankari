package jaangari.opensoft.iitkgp.jaankari.util;

/**
 * Created by ankesh on 28/1/15.
 */
public class Commodity {
    private String id;
    private String name;
    private String min;
    private String max;

    public Commodity(){}

    public Commodity(String id,String name, String min, String max){
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
    }


    public String  getID(){
        return this.id;
    }

    public void setID(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getMin(){
        return this.min;
    }

    public void setMin(String min){
        this.min = min;
    }

    public String getMax(){
        return this.max;
    }

    public void setMax(String max){
        this.max = max;
    }
}
