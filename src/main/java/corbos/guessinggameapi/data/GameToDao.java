package corbos.guessinggameapi.data;

import corbos.guessinggameapi.models.Game;

import java.util.List;

public interface GameToDao {
    int createGame();

    List<Game> getAll();

    Game findById(int gameId);

    boolean update(Game game);

    void deleteAllGames();
}
