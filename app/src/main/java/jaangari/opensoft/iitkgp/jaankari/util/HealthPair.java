package jaangari.opensoft.iitkgp.jaankari.util;

/**
 * Created by menirus on 28/1/15.
 */
public class HealthPair {
    private Health h1;
    private Health h2;

    public HealthPair(){
        this.h1 = null;
        this.h2 = null;
    }

    public HealthPair(Health hitem){
        this.h1 = hitem;
        this.h2 = null;
    }

    public HealthPair(Health hit1, Health hit2){
        this.h1 = hit1;
        this.h2 = hit2;
    }

    public Health getFirst(){
        return this.h1;
    }

    public Health getSecond(){
        return this.h2;
    }
}
