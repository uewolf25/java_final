import java.util.ArrayList;

public class Cast{
    String name;
    Integer count = 0;
    ArrayList<String> genres = new ArrayList<>();

    void update(String genreString){
        String items = genreString.split("[,/] *"); // ,/で区切る。
    }

    String string(){
        return String.format("適切な文字列に置き換える");
    }

}