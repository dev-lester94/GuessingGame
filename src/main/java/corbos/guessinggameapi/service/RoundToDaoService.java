package corbos.guessinggameapi.service;

import corbos.guessinggameapi.data.RoundToDao;
import corbos.guessinggameapi.models.Game;
import corbos.guessinggameapi.models.Round;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoundToDaoService implements RoundToDao {

    private final JdbcTemplate jdbcTemplate;

    public RoundToDaoService(JdbcTemplate jdbcTemplate){
        System.out.println("GameToDaoController can talk to RoundToDaoService");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Round add(Round round) {

        int roundGameId = round.getGameId();
        int roundGuess = round.getGuess();

        boolean validGuess = (roundGuess >= 1000 && roundGuess <= 9999) ? true : false;

        if(validGuess == false){
            return null;
        }

        System.out.println("");

        System.out.println("roundGameId: " + roundGameId);
        System.out.println("roundGuess: " + roundGuess);

        Game game = queryGameForAnswer(roundGameId);
        if(game == null){
            return null;
        }

        System.out.println("gameId: " + game.getId());
        System.out.println("gameAnswer: " + game.getAnswer());

        int gameAnswer = game.getAnswer();

        String resultString = calculateResult(roundGuess, gameAnswer);

        boolean isCorrect = guessCorrect(resultString);
        //System.out.println("result: " + resultString);

        if(isCorrect){
            boolean finished  = gameIsFinished(game);
            if(finished)
                System.out.println("finished: ");
        }

        Round updatedRound = addRoundToGame(round, resultString);


        return updatedRound;
    }

    private boolean guessCorrect(String resultString) {

        String[] result = resultString.split(":");

        //System.out.println(result[1]);

        if(result[1].equals("4")){ //If e:4:p:0 If all digits are correct, return true
            return true;
        }else{
            return false;
        }
    }


    @Override
    public List<Round> getAll(int gameId) {

        //Check if there is a game first with the id
        final String sql = "SELECT id, answer, finished from game where id = ?";
        List<Game> games = jdbcTemplate.query(sql, new GameToDaoService.GameMapper() ,gameId);
        if(games.isEmpty()){
            return null;
        }

        final String getRoundsForGame = "SELECT * from round where gameId = ?";
        List<Round> roundsForGame = jdbcTemplate.query(getRoundsForGame, new RoundMapper(), gameId);


        return  roundsForGame;
    }

    @Override
    public Round findBy(int gameId) {
        return null;
    }

    @Override
    public void deleteAllRounds() {

        final String DELETE_ALL_ROUNDS = "DELETE FROM round";
        jdbcTemplate.update(DELETE_ALL_ROUNDS);


    }

    private Round addRoundToGame(Round round, String result) {
        final String INSERT_ROUND = "INSERT INTO round(gameId, guess, timeOfGuess, result) VALUES(?,?,?,?)";

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        jdbcTemplate.update(INSERT_ROUND, round.getGameId(), round.getGuess(), timestamp, result);

        int newId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        round.setId(newId);
        round.setTimeOfGuess(timestamp);
        round.setResult(result);

        return round;

    }

    private boolean gameIsFinished(Game game) {

        game.setFinished(true);

        final String sql = "UPDATE game SET "
                + "finished = ? "
                + "WHERE id = ?;";

        return jdbcTemplate.update(sql, game.isFinished(),game.getId()) > 0;
    }

    public String calculateResult(int roundGuess, int gameAnswer) {

        //boolean correct = true;
        String guessStr = Integer.toString(roundGuess);
        String gameAnswerStr = Integer.toString(gameAnswer);

        Set<Character> guessStrSet = new HashSet<>();
        for(Character ch: gameAnswerStr.toCharArray()){
            guessStrSet.add(ch);
        }

        int e = 0;
        int p = 0;

        for(int i = 0; i<guessStr.length(); i++){
            Character guessCh = guessStr.charAt(i);
            Character gameAnswerCh = gameAnswerStr.charAt(i);

            if(guessCh == gameAnswerCh){
                e++;
            }else if(guessStrSet.contains(guessCh)){
                p++;
                //correct = false;
            }else{
                //correct = false;
            }
        }

        String result = "e:" + e + ":" + "p:" + p;

        System.out.println(result);

        return result;
    }

    private Game queryGameForAnswer(int gameId) {
        final String sql = "SELECT id, answer, finished FROM game WHERE id = ?;";

        List<Game> games =  jdbcTemplate.query(sql, new GameToDaoService.GameMapper(), gameId); //If no game
        if(games.isEmpty()){
            return null;
        }

        Game game = games.get(0);
        return game;


    }



    public static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
                Round round = new Round();
                round.setId(rs.getInt("id"));
                round.setGameId(rs.getInt("gameId"));
                round.setGuess(rs.getInt("guess"));
                round.setTimeOfGuess(rs.getTimestamp("timeOfGuess"));
                round.setResult(rs.getString("result"));

                return round;
        }
    }
}
