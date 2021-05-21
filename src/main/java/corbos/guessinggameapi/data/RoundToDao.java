package corbos.guessinggameapi.data;

import corbos.guessinggameapi.models.Round;

import java.util.List;

public interface RoundToDao {

    Round add(Round addRoundToGame);

    List<Round> getAll(int gameId);

    Round findBy(int gameId);

    void deleteAllRounds();

}
