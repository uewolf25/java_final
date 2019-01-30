// import java.util.ArrayList;

public class TitleExplore{ //ステップ4用

    String name = ""; //役者名
    Integer count = 1; //出演数
    String genre = ""; //ジャンル


    public TitleExplore(String name, String genre){
        this.name = name;
        // this.count = num;
        this.genre = "[" + genre + "]";
    }

    public String getName(){
        return this.name;
    }

    public Integer getNum(){
        return this.count;
    }

    public String getGenre(){
        return this.genre;
    }
}