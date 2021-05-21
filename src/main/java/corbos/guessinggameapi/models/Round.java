package corbos.guessinggameapi.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Round {
    private int id;
    private int gameId;
    private int guess;
    private Timestamp timeOfGuess;
    private String result;

    public Round(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public Timestamp getTimeOfGuess() {
        return timeOfGuess;
    }

    public void setTimeOfGuess(Timestamp timeOfGuess) {
        this.timeOfGuess = timeOfGuess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return id == round.id &&
                gameId == round.gameId &&
                guess == round.guess &&
                result.equals(round.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,gameId,guess,timeOfGuess,result);
    }
}
