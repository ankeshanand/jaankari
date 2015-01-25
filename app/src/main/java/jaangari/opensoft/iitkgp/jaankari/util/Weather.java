package jaangari.opensoft.iitkgp.jaankari.util;

/**
 * Created by rahulanishetty on 1/25/15.
 */
public class Weather {
    private int id;
    private String city;
    private String main;
    private String description;
    private float temp;
    private float min_temp;
    private float max_temp;
    private int humidity;

    public Weather(int id,String city, String main, String description,float temp,float min_temp,float max_temp, int humidity){
        this.id = id;
        this.city = city;
        this.main = main;
        this.description = description;
        this.temp = temp;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
        this.humidity = humidity;
    }


    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getCity(){
        return this.city;
    }
    public void setCity(String text){
        this.city = text;
    }

    public String getMain(){
        return this.main;
    }
    public void setMain(String text){
        this.main = text;
    }

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String text){
        this.description = text;
    }

    public float getTemp(){
        return this.temp;
    }
    public void setTemp(float temp){
        this.temp = temp;
    }

    public float getMinTemp(){
        return this.min_temp;
    }
    public void setMInTemp(float temp){
        this.min_temp = temp;
    }

    public float getMaxTemp(){
        return this.max_temp;
    }
    public void setMaxTemp(float temp){
        this.max_temp = temp;
    }

    public int getHumidity(){
        return this.humidity;
    }

    public void setHumidity(int id){
        this.humidity = id;
    }

}
