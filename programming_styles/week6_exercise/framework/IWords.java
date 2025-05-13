import java.util.ArrayList;

public interface IWords {
    ArrayList<String> getWordList(String pathToStopFile, String pathToBookFile);
}
