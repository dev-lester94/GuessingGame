package corbos.guessinggameapi.controllers;

import corbos.guessinggameapi.data.GameToDao;
import corbos.guessinggameapi.data.RoundToDao;
import corbos.guessinggameapi.models.Game;
import corbos.guessinggameapi.models.Round;
import corbos.guessinggameapi.service.GameToDaoService;
import corbos.guessinggameapi.service.RoundToDaoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Repository
public class GameToDaoController {

    @Autowired
    private GameToDaoService gameToDaoService;

    @Autowired
    private RoundToDaoService roundToDaoService;

    @PostMapping("/{begin}")
    @ResponseStatus(HttpStatus.CREATED)
    public int addGame(){
        int createdGameId = gameToDaoService.createGame();
        return createdGameId;
    }


    @PostMapping("/guess")
    public ResponseEntity<Round> addRoundToGame(@RequestBody Round round){

        Round createdRound = roundToDaoService.add(round);

        if(createdRound == null){
            ResponseEntity response = new ResponseEntity(createdRound, HttpStatus.UNPROCESSABLE_ENTITY);
            return response;
        }

        ResponseEntity response = new ResponseEntity(createdRound, HttpStatus.CREATED);

        return response;
    }

    @GetMapping("/game")
    public ResponseEntity<List<Game>> getAllGames(){

        List<Game> games = gameToDaoService.getAll();

        ResponseEntity response = new ResponseEntity(games, HttpStatus.OK);

        return response;
    }

    @GetMapping("/game/{id}")
    public ResponseEntity<Game> getAGame(@PathVariable int id){

        Game game = gameToDaoService.findById(id);

        if(game == null){
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }

        ResponseEntity response = new ResponseEntity(game,HttpStatus.OK);
        return response;
    }

    @GetMapping("/rounds/{id}")
    public ResponseEntity<List<Round>> getRoundsForAGame(@PathVariable int id){

        List<Round> rounds = roundToDaoService.getAll(id);

        if(rounds == null){
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }

        ResponseEntity response = new ResponseEntity(rounds,HttpStatus.OK);

        return response;
    }




}
