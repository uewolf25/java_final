import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main{
    void run(String[] args) throws IOException {
        // List_pra a_list = new List_pra();

        ArrayList<List_pra> list = new ArrayList<>();

        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        String line = "";
        Integer count = 0;

        while( ( line = in.readLine() ) != null ){
            List_pra list_list = new List_pra(line, count); //行を読み込むごとにインスタンス化
            // System.out.println(line + ":" + count);
            list.add(list_list);
            count++;
        }

        //ラムダ式
        list.sort( (a,b)-> a.name.compareTo(b.name) );

        for (Integer i = 0; i < list.size(); i++) {
            // if( list.get(i).getName().contains("ne") ){
                System.out.println( list.get(i).getName() + ":" + list.get(i).getNum() );
            // }    
        }

        in.close();
    }

    public static void main(String[] args) throws IOException {
        Main pra = new Main();
        pra.run(args);
    }
}