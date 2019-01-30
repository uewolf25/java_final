import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class MovieAnalyzer5{
    TreeMap<String, Cast> castMap; //出演者ごとの回数とジャンルを格納する。
    ArrayList<TitleExplore_step5> titleList; //出演者と出演回数とジャンルを格納するリスト

    void run(String[] args) throws IOException {
        this.initialize(); //マップの初期化

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
        this.sortAndPrint();
    }

    /* マップの初期化 */
    void initialize(){
        this.castMap = new TreeMap<>();
        this.titleList = new ArrayList<>();
    }

    /* 引数の確認 */
    void optionAnalyze(String[] args){
        Arguments arguments = new Arguments();
        arguments.parse(args); //オプションの解析を行う。

        if(arguments.castFlag){
            //引数と同じ出演者を探して出力する。
            this.compareLettersCast(arguments);
        }        
    }

    /* 引数と同じ出演者を探して出力する。 */
    void compareLettersCast(Arguments arguments){
        Cast cast = new Cast();

        for (String name : castMap.keySet()) {
            //キー(出演者)に引数と同じ人であったらその人と回数とジャンルを出力
            if( name.contains(arguments.cast) ){ 
                cast = castMap.get(name);
                System.out.println( cast.string() );
            }
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
            this.readFile(in, arguments);
        } else{ 
            //ステップ１と同じ作業をする。
            //Argumentsクラスのリストにファイルが格納されているのでそれを用いる。
            for (String item: arguments.fileList) {
                in = new BufferedReader( new FileReader( item ) );
                //ファイルの読み込み
                this.readFile(in, arguments);
            }
        }
        //ファイルストリームを閉じる
        in.close();
    }

    /* ファイルの読み込み */
    void readFile( BufferedReader in, Arguments arguments) throws IOException {
        String line;
        while( ( line = in.readLine() ) != null ){
            //データをコンマで区切理、データを格納して行く
            this.someDataCut(line, arguments);
        }
    }

    /* データをコンマで区切理、データを格納して行く */
    void someDataCut(String line, Arguments arguments){
        CsvSplitter splitter = new CsvSplitter(); //CsvSplitterの実体化
        ArrayList<String> dataCut = splitter.split(line); //1行分をコンマで区切る。
        String[] items = dataCut.get(4).split(",");
        //キャスト部分(配列4番目)でも出演者が複数人いるので一人ずつ区切る。
        ArrayList<String> castList = new ArrayList<>( Arrays.asList(items) );

        for(Integer i = 0; i < castList.size(); i++){
            //出演者がいない時はスキップ
            if( castList.get(i).isEmpty() ) continue;
            
            //引数にタイトルオプションをつけられた時の処理
            if(arguments.titleFlag){
                this.titleOption(dataCut.get(1), castList.get(i), dataCut.get(5), arguments);
            } else{
                //出演者を1人ずつリストに格納していく。その時のジャンルも格納していく。
                addCastMap( castList.get(i), dataCut.get(5) );
            }
        }
    }

    /* 出演者を1人ずつリストに格納していく。 */
    void addCastMap(String performerName, String genre){
        Cast cast = new Cast(); //Castの実体化

        //名前の前の空白を削除
        performerName = deleteBlank(performerName);

        //出演者の名前がすでに存在しているとき
        if( this.castMap.containsKey(performerName) ){
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

    /* 入力したタイトルと部分一致したものをリストに格納していく。 */
    void titleOption(String title, String name, String genre, Arguments arguments){
        name = deleteBlank(name); //名前の前の空白を削除
        if( title.contains(arguments.title) ){ //部分一致の判定
            TitleExplore_step5 titleExplore = new TitleExplore_step5(name, genre, title); //実体化と初期化
            this.duplicationCheck(name, titleExplore); //出演者の重複の確認
        }
    }

    /* 出演者の重複の確認(重複してたら出演回数を加算する) */
    void duplicationCheck(String name, TitleExplore_step5 titleExplore){
        for(Integer i = 0; i < titleList.size(); i++){
            if(titleList.get(i).name.contains(name)){
                titleList.get(i).count++; //重複の場合の加算
            }
        }
        this.titleList.add(titleExplore); //リストへの追加
    }

    /* タイトルオプション指定があった時、格納してるTitleExplore型のリストをソートし、出力 */
    void sortAndPrint(){
        //ラムダ式での出演者のソート
        titleList.sort( (a,b)-> a.name.compareTo(b.name) );
        for(Integer i = 0; i < titleList.size(); i++){
            //キャスト名，出演作品数，ジャンル一覧
            System.out.printf( "%s,%d,%s%n", titleList.get(i).getName(), titleList.get(i).getNum(), titleList.get(i).getGenre() );

            //[tab]タイトル [ジャンル]
            System.out.printf( "\t%s %s%n", titleList.get(i).getTitle(), titleList.get(i).getGenre() );
        }
    }


    public static void main(String[] args) throws IOException {
        MovieAnalyzer5 pra5 = new MovieAnalyzer5();
        pra5.run(args);
    } 
}