DROP DATABASE IF EXISTS guessingGame;
CREATE DATABASE guessingGame;
USE guessingGame;

CREATE TABLE game(
id INT PRIMARY KEY AUTO_INCREMENT,
answer int NOT NULL,
finished BOOLEAN DEFAULT false);

CREATE TABLE round(
id INT PRIMARY KEY AUTO_INCREMENT,
gameId int NOT NULL,
guess int NOT NULL,
timeOfGuess timestamp,
result varchar(255));

