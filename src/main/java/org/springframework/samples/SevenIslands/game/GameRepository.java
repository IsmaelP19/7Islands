package org.springframework.samples.SevenIslands.game;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface GameRepository extends CrudRepository<Game, Integer>{

  @Query("SELECT P from Game P WHERE P.privacity = 'PUBLIC'") // check if necessary for the project
  Collection<Game> findAllPublic() throws DataAccessException;

  @Query("SELECT P from Game P WHERE P.privacity = 'PUBLIC' AND P.has_started = True")
  Collection<Game> findAllPublicPlaying() throws DataAccessException;

  @Query("SELECT P from Game P WHERE P.privacity = 'PUBLIC' AND P.has_started = False")
  Collection<Game> findAllPublicNotPlaying() throws DataAccessException;

  @Query("SELECT P from Game P WHERE P.has_started = True")
  Collection<Game> findAllPlaying() throws DataAccessException;

  @Query(value = "SELECT * FROM GAMES WHERE PLAYER_ID LIKE ?1", nativeQuery = true)
  Collection<Game> findByOwnerId(@Param("playerId") int playerId) throws DataAccessException;

  @Query("SELECT g FROM Game g INNER JOIN g.players p WHERE p.id =:id")
  List<Game> findGamesByPlayerId(@Param("id") int id);

  @Query("SELECT P from Game P WHERE P.code = :code")
  Iterable<Game> findGamesByRoomCode(String code) throws DataAccessException;



}