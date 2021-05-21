package corbos.guessinggameapi.models;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import corbos.guessinggameapi.filter.GameFilter;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@JsonFilter("myGameFilter")
public class Game {
    private int id;
    private int answer;
    private boolean finished;

    private List<Round> rounds= new ArrayList<>();

    public Game() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }



    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id &&
                answer == game.answer &&
                finished == game.finished &&
                rounds.equals(game.rounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,answer, finished, rounds);
    }


}

@Configuration
class FilterConfiguration {
    public FilterConfiguration (ObjectMapper objectMapper) {
        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider().setFailOnUnknownId(true);
        simpleFilterProvider.addFilter("myGameFilter", new GameFilter());

        objectMapper.setFilterProvider(simpleFilterProvider);
    }
}
