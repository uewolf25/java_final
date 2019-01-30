// import java.util.ArrayList;

public class TitleExplore_step5{ //ステップ4用

    String name = ""; //役者名
    Integer count = 1; //出演数
    String genre = ""; //ジャンル

    String title = ""; //タイトル


    public TitleExplore_step5(String name, String genre, String title){
        this.name = name;
        // this.count = num;
        this.genre = "[" + genre + "]";
        this.title = title;
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

    public String getTitle(){
        return this.title;
    }
}