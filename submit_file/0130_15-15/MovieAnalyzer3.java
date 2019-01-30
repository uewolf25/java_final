import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class MovieAnalyzer3{
    TreeMap<String, Cast> castMap; //出演者ごとの回数とジャンルを格納する。

    void run(String[] args) throws IOException {
        this.initialize(); //マップの初期化
        BufferedReader in = null;
        //引数有る無しで読み込み方法を変える
        in = this.judgeBinary(args, in);

        //ファイルの読み込み
        this.readFile(in);
        //ファイルストリームを閉じる
        in.close();
        //結果を出力
        this.printMap();
    }

    /* マップの初期化 */
    void initialize(){
        this.castMap = new TreeMap<>();
    }

    /* 引数有る無しで読み込み方法を変える。 */
    BufferedReader judgeBinary(String[] args, BufferedReader in) throws IOException {
        if(args.length == 0){ 
            //標準入力を受け付ける
            in = new BufferedReader( new InputStreamReader(System.in) );
        } else{ 
            //ステップ１と同じ作業をする。
            for (String item : args) {
                in = new BufferedReader( new FileReader( item ) );
            }
        }
        return in;
    }

    /* ファイルの読み込み */
    void readFile( BufferedReader in) throws IOException {
        String line;
        while( ( line = in.readLine() ) != null ){
            //データをコンマで区切理、データを格納して行く
            this.someDataCut(line);
        }
    }

    /* データをコンマで区切理、データを格納して行く */
    void someDataCut(String line){
        CsvSplitter splitter = new CsvSplitter(); //CsvSplitterの実体化
        ArrayList<String> dataCut = splitter.split(line); //1行分をコンマで区切る。
        String[] items = dataCut.get(4).split(",");
        //キャスト部分(配列4番目)でも出演者が複数人いるので一人ずつ区切る。
        ArrayList<String> castList = new ArrayList<>( Arrays.asList(items) );

        for(Integer i = 0; i < castList.size(); i++){
            //出演者がいない時はスキップ
            if( castList.get(i).isEmpty() ) continue;
            
            //出演者を1人ずつリストに格納していく。その時のジャンルも格納していく。
            addCastMap( castList.get(i), dataCut.get(5) );
        }
    }

    /* 出演者を1人ずつリストに格納していく。 */
    void addCastMap(String performerName, String genre){
        Cast cast = new Cast(); //castの実体化
        //名前の前の空白を削除
        performerName = deleteBlank(performerName);
        if( this.castMap.containsKey(performerName) ){ //出演者の名前がすでに存在しているとき
            cast = this.castMap.get(performerName); //Cast型(value)の取り出し
            cast.update(performerName, genre); //ジャンルと出演数の更新
        } else{
            cast.update(performerName, genre);
        }

        //出演者とキャスト型をマップに格納
        this.castMap.put(performerName, cast);
    }

    /* 名前の前の空白を削除 */
    String deleteBlank(String performerName){
        return performerName.trim();
    }

    void printMap(){
        for ( Cast cast : castMap.values() ) {
            System.out.println( cast.string() );
        }
    }

    public static void main(String[] args) throws IOException {
        MovieAnalyzer3 pra3 = new MovieAnalyzer3();
        pra3.run(args);
    }
}