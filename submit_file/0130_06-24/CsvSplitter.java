import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * This class is implemented by refering 
 * https://github.com/TNG/rest-demo-jersey/blob/master/src/main/java/com/tngtech/demo/weather/lib/CsvSplitter.java.
 */
public class CsvSplitter {
    public ArrayList<String> split(String line) {
        ArrayList<String> strings = new ArrayList<>();
        Arrays.stream(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1))
            .map(item -> replaceRepeatedDoubleQuotes(item))
            .map(item -> stripDoubleQuote(item))
            .forEach(item -> strings.add(item));
        return strings;
    }

    private String stripDoubleQuote(String item){
        if(item.startsWith("\"") && item.endsWith("\"")){
            return item.substring(1, item.length() - 1);
        }
        return item;
    }

    private String replaceRepeatedDoubleQuotes(String item) {
        return item.replaceAll("\"\"", "\"")
            .replaceAll("''", "'")
            .trim();
    }

    public static void main(String[] args) throws IOException {
        try(Stream<String> lines = Files.lines(Paths.get(args[0]))){
            CsvSplitter splitter = new CsvSplitter();
            lines.map(line -> splitter.split(line))
                .forEach(list -> System.out.printf("%s, %s%n", list.size(), list));
        }
    }
}
