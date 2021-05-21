package corbos.guessinggameapi.service;

import corbos.guessinggameapi.TestApplicationConfiguration;
import corbos.guessinggameapi.data.GameToDao;
import corbos.guessinggameapi.data.RoundToDao;
import corbos.guessinggameapi.models.Game;
import corbos.guessinggameapi.models.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameToDaoServiceTest {

    @Autowired
    GameToDao gameToDao;

    @Autowired
    RoundToDao roundToDao;

    @Autowired
    private RoundToDaoService roundToDaoService;

    @Before
    public void setUp(){
        System.out.println("Delete all rounds and games before each test");
        roundToDao.deleteAllRounds();
        gameToDao.deleteAllGames();
    }

    @Test
    public void testAddGame(){
        Game game = new Game(); //Create game
        game.setFinished(false);
        game.setRounds(new ArrayList<Round>());

        int id = gameToDao.createGame(); //Set id to the game we locally created
        game.setId(id);

        Game fromDao = gameToDao.findById(id);

        //game.setAnswer(fromDao.getAnswer()); //set the answer to the locally created game
        assertEquals(game.getId(), fromDao.getId()); //compare if the id of the game(s) are equal
    }

    @Test
    public void testAddRoundToGame(){
        Game game = new Game(); //Create game
        game.setFinished(false);
        game.setRounds(new ArrayList<Round>());
        int id = gameToDao.createGame(); //Set id to the game we locally created
        game.setId(id);

        Game fromDao = gameToDao.findById(id);

        game.setAnswer(fromDao.getAnswer()); //set the answer to the locally created game

        Round round = new Round();
        round.setGameId(game.getId());
        round.setGuess(1337);

        Round roundFromDao = roundToDao.add(round);
        round.setId(roundFromDao.getId());
        round.setResult(roundFromDao.getResult());
        round.setTimeOfGuess(roundFromDao.getTimeOfGuess());

        assertEquals(round,roundFromDao);
    }

    @Test
    public void testGetAllGames(){
        Game game = new Game(); //Create game
        game.setFinished(false);
        game.setRounds(new ArrayList<Round>());
        int id = gameToDao.createGame(); //Set id to the game we locally created
        game.setId(id);
        Game fromDao = gameToDao.findById(id);
        game.setAnswer(fromDao.getAnswer()); //set the answer to the locally created game

        Game game2 = new Game(); //Create game
        game2.setFinished(false);
        game2.setRounds(new ArrayList<Round>());
        int id2 = gameToDao.createGame(); //Set id to the game we locally created
        game2.setId(id2);
        Game fromDao2 = gameToDao.findById(id2);
        game2.setAnswer(fromDao2.getAnswer()); //set the answer to the locally created game

        List<Game> games = gameToDao.getAll();
        assertEquals(2, games.size());
        assertTrue(games.contains(game));
        assertTrue(games.contains(game2));
    }

    @Test
    public void testGetGameById(){
        Game game = new Game(); //Create game
        game.setFinished(false);
        game.setRounds(new ArrayList<Round>());
        int id = gameToDao.createGame(); //Set id to the game we locally created
        game.setId(id);
        Game fromDao = gameToDao.findById(id);
        game.setAnswer(fromDao.getAnswer()); //set the answer to the locally created game
        assertEquals(game, fromDao); //compare if it is equal
    }

    @Test
    public void testRoundsByGameId(){
        Game game = new Game(); //Create game
        game.setFinished(false);
        game.setRounds(new ArrayList<Round>());
        int id = gameToDao.createGame(); //Set id to the game we locally created

        Round round = new Round();
        round.setGameId(id);
        round.setGuess(1337);
        Round roundFromDao = roundToDao.add(round);
        round.setId(roundFromDao.getId());
        round.setResult(roundFromDao.getResult());
        round.setTimeOfGuess(roundFromDao.getTimeOfGuess());

        Round round2 = new Round();
        round2.setGameId(id);
        round2.setGuess(7331);
        Round roundFromDao2 = roundToDao.add(round2);
        round2.setId(roundFromDao2.getId());
        round2.setResult(roundFromDao2.getResult());
        round2.setTimeOfGuess(roundFromDao2.getTimeOfGuess());

        List<Round> rounds = roundToDao.getAll(id);
        assertEquals(2, rounds.size());
        assertTrue(rounds.contains(round));
        assertTrue(rounds.contains(round2));

    }





}