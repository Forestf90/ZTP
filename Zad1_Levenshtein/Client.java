import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Client {

    public static void main(String[] args) {

        List<String> persons = new ArrayList<>();
        String path =args[0];
        String rootPerson = args[1];

        try {
            persons = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int minimumDistance =Integer.MAX_VALUE;
        int minimumLine =0;

        for(String s: persons){
            int currentDistance = Levenshtein.minimalDistance(rootPerson, s);
            if(currentDistance<minimumDistance){
                minimumDistance=currentDistance;
                minimumLine=persons.indexOf(s);
            }
        }
        // +1 przy założeniu że linie w pliku txt numerujemy od 1
        System.out.println(minimumLine+1);
    }
}
