import java.util.ArrayList;
import java.util.Arrays;

public class DataItems{ //ステップ４用(の予定やったやつ)：使わない！！！
    /* このクラス内でリストや変数を保存しておき、ここで出力する。 */

    String name = ""; //役者名
    Integer count = 0; //出演数
    ArrayList<String> genres = new ArrayList<>(); //ジャンル
    String titleName = ""; //タイトル名

    public void update(String name, String titleName, String genreString){
        this.name = name;
        this.titleName = titleName;
        String[] items = genreString.split("[,/] *"); // ,/で区切る。
        //コンマで区切ったジャンルを１つずつリストに格納。
        ArrayList<String> genresList = new ArrayList<>( Arrays.asList(items) );
        count++; //出演数の加算
        for(Integer i = 0; i < genresList.size(); i++){
            if( !genres.contains( genresList.get(i) ) ){
                genres.add( genresList.get(i) ); //なかったら追加
            }
        }
    }

    /* 全体の出力 */
    public String string(){
        return String.format("%s,%d,%s", name, count, genres);
    }

}
