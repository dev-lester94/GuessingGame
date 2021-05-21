package corbos.guessinggameapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import corbos.guessinggameapi.data.GameToDao;
import corbos.guessinggameapi.filter.GameFilter;
import corbos.guessinggameapi.models.Game;
import corbos.guessinggameapi.models.Round;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameToDaoService implements GameToDao{

    private final JdbcTemplate jdbcTemplate;

    public GameToDaoService(JdbcTemplate jdbcTemplate){
        System.out.println("GameToDaoController can talk to GameToDaoService");
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int createGame() {

        int answer = generateAnswer();
        System.out.println(answer);

        final String sql = "INSERT INTO game(answer) VALUES(?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) ->{
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, answer);
            return statement;
        }, keyHolder);


        int createdGameId = keyHolder.getKey().intValue();

        return createdGameId;
    }


    @Override
    public List<Game> getAll() {

        final String sql = "select * from game";
        //final String sql = "select id, case when finished=false then -1 else answer end as answer, finished from game";
        final String getRoundsForGame = "SELECT * from round where gameId = ?";


        List<Game> games = jdbcTemplate.query(sql, new GameMapper());

        for(Game game : games){
            /*if(!game.isFinished()){
                game.setAnswer(-1);
            }*/

            List<Round> roundsForGame = jdbcTemplate.query(getRoundsForGame, new RoundToDaoService.RoundMapper(), game.getId());

            game.setRounds(roundsForGame);
        }


        return games;
    }

    @Override
    public Game findById(int gameId) {
        final String sql = "SELECT id, answer, finished from game where id = ?";

        //Check is there is a game iwth the id
        List<Game> games = jdbcTemplate.query(sql, new GameMapper() ,gameId);
        if(games.isEmpty()){
            return null;
        }

        Game game = games.get(0); //game we want is at index 0;

        final String getRoundsForGame = "SELECT * from round where gameId = ?"; //grab all rounds for that gameId

        List<Round> roundsForGame = jdbcTemplate.query(getRoundsForGame, new RoundToDaoService.RoundMapper(), game.getId());

        game.setRounds(roundsForGame);

        /*if(!game.isFinished()){
            game.setAnswer(-1);
        }*/

        return game;
    }

    @Override
    public boolean update(Game game) {
        return false;
    }

    @Override
    public void deleteAllGames() {
        final String DELETE_ALL_GAMES= "DELETE FROM game";
        jdbcTemplate.update(DELETE_ALL_GAMES);
    }

    private int generateAnswer() {

        int answer;
        ArrayList<Integer> randomArrayList = new ArrayList<Integer>();
        for(int i = 0; i<9; i++){
            randomArrayList.add(i);
        }

        Collections.shuffle(randomArrayList);

        String answerString = "";

        for(int i =  0; i<4;i++){
            answerString += randomArrayList.get(i);
        }


        answer = Integer.parseInt(answerString);
        if(answer < 1000){ //If the answer is less than 1000 because the first digit is 0 after parsing
            answerString+=randomArrayList.get(randomArrayList.size()-1); //append random digit
            answer = Integer.parseInt(answerString); //parse the new 4 digit number and return
            return answer;

        }
        return answer;
    }

    public static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setAnswer(rs.getInt("answer"));
            game.setFinished(rs.getBoolean("finished"));

            return game;
        }


    }

}
