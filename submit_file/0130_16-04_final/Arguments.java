import java.util.ArrayList;
import java.util.Objects;

public class Arguments{

    //それぞれ、オプションの有効の確認。
    Boolean help = false;
    Boolean castFlag = false;
    Boolean titleFlag = false;

    String cast = ""; //キャスト名
    String title = ""; //タイトル名
    ArrayList<String> fileList = new ArrayList<>();

    /* オプションの解析を行う。 */
    public void parse(String[] args){
        for(Integer i = 0; i < args.length; i++){
            if( Objects.equals(args[i], "-h") || Objects.equals(args[i], "--help") ){
                this.help = true;
            } 
            else if( Objects.equals(args[i], "-c") || Objects.equals(args[i], "--cast") ){
                this.castFlag = true;
                i++;
                this.cast = args[i];
            }
            else if( Objects.equals(args[i], "-t") || Objects.equals(args[i], "--title") ){
                this.titleFlag = true;
                i++;
                this.title = args[i];
            }
            else{
                this.fileList.add(args[i]); //ここにはファイルが入ってる(movie?.csv)
            }
        }
    }

    /* helpオプション */
    public void printHelp(){
        System.out.println("Usage: java MovieAnalyzer4 [OPTIONS] [Movie Files...]");
        System.out.println("    -h, --help:            このメッセージを表示する．");
        System.out.println("    -c, --cast <Cast名>:   ここで指定されたキャストでフィルタリングする（部分一致）．");
        System.out.println("    -t, --title <タイトル>: ここで指定されたタイトルでフィルタリングする（部分一致）．");        
    }

}