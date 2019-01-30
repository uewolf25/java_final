import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class MovieAnalyzer1{
    TreeMap<Integer, Integer> map; //①Key=年代, Value=回数

    void run(String[] args) throws IOException {
        this.initialize(); //マップの初期化
        for (String item : args) {
            //ファイルの読み込み
            this.readFile( new File(item) );
            //結果を出力
            this.printTreeMap();
        }
    }

    /* マップの初期化 */
    void initialize(){
        this.map = new TreeMap<>();
    }

    /* ファイルの読み込み */
    void readFile(File file) throws IOException {
        BufferedReader in = new BufferedReader( new FileReader(file) );
        String line;
        while( ( line = in.readLine() ) != null ){
            //公開年の回数を調べ、格納＆初期化。
            this.frequencyCount(line);
        }
        in.close();
    }

    /* 公開年の回数を調べ、格納＆初期化 */
    void frequencyCount(String line) throws IOException {
        String[] items = line.split(",");
        Integer releseYear = Integer.valueOf( items[0] ); //文字列から数値への変換
        Integer count = map.get(releseYear);
        if(count == null){
            count = 0; //公開年が登録されてないとき、出現回数を０回に初期化する。
        }
        count++; //すでに出現している時、回数を加算。
        map.put(releseYear, count); //データの追加または上書き。
        
    }

    /* 結果を出力 */
    void printTreeMap(){
        for ( Map.Entry<Integer, Integer> map : map.entrySet() ) {
            System.out.printf("%s: %d%n", map.getKey(), map.getValue());
        }
    }

    public static void main(String[] args) throws IOException {
        MovieAnalyzer1 pra1 = new MovieAnalyzer1();
        pra1.run(args);
    }
}