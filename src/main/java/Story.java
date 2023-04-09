import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс Story хранит в себе номер клиента и список ранее написанных сообщений в чат.
 *
 * @autor Петров Даниил Денисович
 */
public class Story {

    private Integer numberClient;
    private List<String> lines;

    /**
     * Конструктор класса Story
     *
     * @param numberClient - номер клиента
     *
     */
    public Story(Integer numberClient) {
        this.numberClient = numberClient;
        lines = new ArrayList<>();
    }

    public Integer getNumberClient() {
        return numberClient;
    }

    public void addLines(String line) {
        String date = String.valueOf(LocalDate.now());
        lines.add(date + " - " + line);
    }

    public List<String> getStory() {
        return lines;
    }

}
