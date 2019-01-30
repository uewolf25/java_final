import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

public class MovieAnalyzer2{
    TreeMap<Integer, Integer> map; //①Key=年代, Value=回数

    void run(String[] args) throws IOException {
        this.initialize(); //マップの初期化
        
        //ファイル名ミス時の例外処理。
        try {
            //引数有る無しで読み込み方法を変える
            this.judgeBinary(args);
        } catch (Exception e) {
            System.out.println("ファイル名が正しくない又は存在しません。");
        }

        //結果を出力
        this.printTreeMap();
    }

    /* マップの初期化 */
    void initialize(){
        this.map = new TreeMap<>();
    }

    /* 引数有る無しで読み込み方法を変える。 */
    void judgeBinary(String[] args) throws IOException {
        BufferedReader in = null;
        if(args.length == 0){ 
            //標準入力を受け付ける
            in = new BufferedReader( new InputStreamReader(System.in) );
            //ファイルの読み込み
            this.readFile(in);
        } else{ 
            //ステップ１と同じ作業をする。
            for (String item : args) {
                in = new BufferedReader( new FileReader( item ) );
                //ファイルの読み込み
                this.readFile(in);
            }
        }
        //ファイルストリームを閉じる
        in.close();
    }

    /* ファイルの読み込み */
    void readFile( BufferedReader in) throws IOException {
        String line;
        while( ( line = in.readLine() ) != null ){
            //公開年の回数を調べ、格納＆初期化。
            this.frequencyCount(line);
        }
    }

    /* 公開年の回数を調べ、格納＆初期化 */
    void frequencyCount(String line) throws IOException {
        String[] items = line.split(","); //文字の切り離し
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
        MovieAnalyzer2 pra2 = new MovieAnalyzer2();
        pra2.run(args);
    }
}