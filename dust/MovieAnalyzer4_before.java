import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MovieAnalyzer4_before{ //全部格納してからタイトル検索をすると、
    //TreeMap<Integer, Integer> countYearMap; //①Key=年代, Value=回数。 公開年がそれぞれ何回出たか格納するマップ
    TreeMap<String, DataItems> fileDataMap; //出演者ごとの回数とジャンルを格納する。 //タイトル名も中に格納する。
    // TreeMap<String, String> titleMap; //Key->出演者、Value->映画タイトル

    void run(String[] args) throws IOException {
        //マップの初期化
        this.initialize();

        //ファイル名ミス時の例外処理。
        try {
            //引数有る無しで読み込み方法を変える
            this.judgeBinary(args);
        } catch (Exception e) {
            System.out.println("ファイル名が正しくない又は存在しません。");
        }
        //引数の確認
        this.optionAnalyze(args);
        //結果を出力
        //this.printTreeMap();
        // this.printMap();
    }

    /* マップの初期化 */
    void initialize(){
        //this.countYearMap = new TreeMap<>();
        this.fileDataMap = new TreeMap<>();
    }

    /* 引数の確認 */
    void optionAnalyze(String[] args){
        Arguments arguments = new Arguments();
        arguments.parse(args); //オプションの解析を行う。

        if(arguments.castFlag){
            //引数と同じ出演者を探して出力する。
            this.compareLettersCast(arguments);
        }
        
        if(arguments.titleFlag){
            //引数と同じタイトル名を探して出力する。
            this.compareLettersTitle(arguments);
        }
    }

    /* 引数と同じ出演者を探して出力する。 */
    void compareLettersCast(Arguments arguments){
        DataItems data = new DataItems();

        for (String name : fileDataMap.keySet()) {
            //キー(出演者)に引数と同じ人であったらその人と回数とジャンルを出力
            if( name.contains(arguments.cast) ){ 
                data = fileDataMap.get(name);
                System.out.println( data.string() );
            }
        }
    }

    /* 引数と同じタイトル名を探して出力する。 */
    void compareLettersTitle(Arguments arguments){
        // System.out.println("aaaaa");************************
        DataItems data = new DataItems();
        //valueからkeyを取得できないのでentryで回す。(びっぷマップはいける?)
        for ( Map.Entry<String, DataItems> map : fileDataMap.entrySet() ) { 
            // String key = map.getKey(); //出演者
            data = map.getValue(); //DataItems型
            if( data.titleName.contains(arguments.title) ){
                //マップに格納されているものと引数が一致したらdata型の該当する部分を出力する。
                System.out.println( data.string() );
                // System.out.println( map.getKey() + ":" + map.getValue() );
            }
            // System.out.println(data.titleName);
        }

    }

    /* 引数有る無しで読み込み方法を変える。 */
    void judgeBinary(String[] args) throws IOException {
        Arguments arguments = new Arguments();
        arguments.parse(args); //オプションの解析を行う。

        //ヘルプオプションの時はメッセージを出力し、プログラムを正常に終了する。
        if(arguments.help){ 
            arguments.printHelp();
            System.exit(0);
        }

        BufferedReader in = null;
        if(args.length == 0 || arguments.fileList.size() == 0){  //パイプ使った時の標準入力の時、引数はあるけどfileがないからエラー********************行けた
            //標準入力を受け付ける
            in = new BufferedReader( new InputStreamReader(System.in) );
            //ファイルの読み込み
            this.readFile(in);
        } else{ 
            //ステップ１と同じ作業をする。
            //Argumentsクラスのリストにファイルが格納されているのでそれを用いる。
            for (String item: arguments.fileList) {
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
            //this.frequencyCountYear(line);

            //データをコンマで区切理、データを格納して行く
            this.someDataCut(line);
        }
    }

    /* データをコンマで区切理、データを格納して行く */
    void someDataCut(String line){
        CsvSplitter splitter = new CsvSplitter(); //CsvSplitterの実体化
        ArrayList<String> dataCut = splitter.split(line); //1行分をコンマで区切る。

        //キャスト部分のデータを取り出す。
        //キャスト部分(配列4番目)でも出演者が複数人いるので一人ずつ区切る。
        String[] casts = dataCut.get(4).split(",");
        ArrayList<String> castList = new ArrayList<>( Arrays.asList(casts) );

        for(Integer i = 0; i < castList.size(); i++){
            //出演者がいない時はスキップ
            if( castList.get(i).isEmpty() ) continue;
            
            //出演者を1人ずつリストに格納していく。その時のタイトル名とジャンルも格納していく。
            addCastMap( castList.get(i), dataCut.get(1), dataCut.get(5) );
        }
    }

    /* 出演者を1人ずつリストに格納していく。 */
    void addCastMap(String performerName, String title, String genre){
        DataItems dataItems = new DataItems(); //castの実体化
        //名前の前の空白を削除
        performerName = deleteBlank(performerName);

        if( this.fileDataMap.containsKey(performerName) ){ //出演者の名前がすでに存在しているとき
            dataItems = this.fileDataMap.get(performerName); //Cast型(value)の取り出し
            dataItems.update(performerName, title, genre); //ジャンルと出演数の更新
        } else{
            dataItems.update(performerName, title, genre);
        }

        //出演者とキャスト型をマップに格納
        this.fileDataMap.put(performerName, dataItems);
    }

    /* 名前の前の空白を削除 */
    String deleteBlank(String performerName){
        return performerName.trim();
    }

    // void printMap(){
    //     for ( DataItems dataItems : fileDataMap.values() ) {
    //         System.out.println( dataItems.string() );
    //     }
    // }


    public static void main(String[] args) throws IOException {
        MovieAnalyzer4_before pra4 = new MovieAnalyzer4_before();
        pra4.run(args);
    }
}