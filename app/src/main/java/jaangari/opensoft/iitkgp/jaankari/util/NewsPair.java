package jaangari.opensoft.iitkgp.jaankari.util;

/**
 * Created by menirus on 27/1/15.
 */
public class NewsPair {
    private News n1;
    private News n2;
    public NewsPair(){
        this.n1 = null;
        this.n2 = null;
    }
    public NewsPair(News nit){
        this.n1 = nit;
        this.n2 = null;
    }
    public NewsPair(News nit1, News nit2){
        this.n1 = nit1;
        this.n2 = nit2;
    }
    public News getFirst(){
        return this.n1;
    }
    public News getSecond(){
        return this.n2;
    }
}
