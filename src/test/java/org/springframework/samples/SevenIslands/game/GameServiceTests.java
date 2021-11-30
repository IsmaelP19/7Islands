package org.springframework.samples.SevenIslands.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.SevenIslands.player.PlayerService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class GameServiceTests {
    
    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;
    
    @Test
    public void testCountWithInitialData(){
        int count = gameService.gameCount();
        assertEquals(5, count);
    }

    @Test
    public void testFindGameById(){ 
        Game game = gameService.findGameById(1).get();
        assertThat(game.getName()).isEqualTo("Prueba0");
        assertThat(game.getCode()).isEqualTo("ABCD124");

    }

    @Test
    public void testFindAll(){
        long count = gameService.findAll().spliterator().getExactSizeIfKnown();
        assertEquals(5, count);
    }

    @Test
    public void testFindAllPublic(){
        long count = gameService.findAllPublic().spliterator().getExactSizeIfKnown();
        assertEquals(3, count);
    }
    
    @Test
    public void testFindAllPlaying(){
        int count = gameService.findAllPlaying().size();
        assertEquals(2, count);
    }

    @Test
    public void testFindAllPublicPlaying(){
        int count = gameService.findAllPublicPlaying().size();
        assertEquals(1, count);
    }
    
    @Test
    public void testfindByOwnerId(){  // counts the games where the player as created
        Iterable<Game> games = gameService.findByOwnerId(1);
        long count = games.spliterator().getExactSizeIfKnown();
        assertEquals(4, count);
    }

    @Test
    public void shouldInsertGame(){

        Game game = new Game();
        game.setName("Partida 1");
        game.setCode("AAAABBBB1");
        game.setPrivacity(PRIVACITY.PUBLIC);
        game.setHas_started(false);
        game.setPlayer(playerService.findPlayerById(1).get());

        
        gameService.save(game);

        assertThat(game.getId().longValue()).isNotEqualTo(0);

        assertThat(gameService.findGamesByRoomCode(game.getCode()).stream().collect(Collectors.toList()).get(0).getCode().toString()).isEqualTo("AAAABBBB1");


    }

    @Test
    public void shouldDeleteGame(){

        Game game = new Game();
        game.setName("Partida 1");
        game.setCode("AAAABBBB1");
        game.setPrivacity(PRIVACITY.PUBLIC);
        game.setHas_started(false);
        game.setPlayer(playerService.findPlayerById(1).get());

        
        gameService.save(game);

        int countBefore = gameService.gameCount();

        gameService.delete(game);

        int countAfter = gameService.gameCount();

        assertNotEquals(countBefore, countAfter);

    }

     
    @Test
    public void testFindGamesWhereIPlayedByPlayedId(){  // HU7 counts the games where the player played
        Iterable<Game> games = gameService.findGamesByPlayerId(1);
        long count = games.spliterator().getExactSizeIfKnown();
        assertEquals(5, count);
    }

    
}
