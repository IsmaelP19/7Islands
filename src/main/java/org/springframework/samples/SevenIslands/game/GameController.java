package org.springframework.samples.SevenIslands.game;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.SevenIslands.admin.AdminController;
import org.springframework.samples.SevenIslands.general.GeneralService;
import org.springframework.samples.SevenIslands.player.Player;
import org.springframework.samples.SevenIslands.player.PlayerController;
import org.springframework.samples.SevenIslands.player.PlayerService;
import org.springframework.samples.SevenIslands.util.SecurityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/games")
public class GameController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired	
	private GeneralService generalService;

    @Autowired
    private AdminController adminController;

    @Autowired
    private PlayerController playerController;
    

    @GetMapping(path = "/new")
    public String createGame(Player player, ModelMap modelMap) {
        String view = "games/createOrUpdateGameForm"; 
        modelMap.addAttribute("game", new Game());
        return view;
    }

    @PostMapping(path = "/save")
    public String salvarEvento(@Valid Game game, BindingResult result, ModelMap modelMap) {
        String view = "games/lobby";
        if (result.hasErrors()) {
            modelMap.addAttribute("game", game);
            return "games/createOrUpdateGameForm";
        } else {
            Player currentPlayer = securityService.getCurrentPlayer();

            game.setPlayer(currentPlayer); 


            game.addPlayerinPlayers(currentPlayer);
            currentPlayer.addGameinGames(game);

            gameService.save(game);

            modelMap.addAttribute("game", game);
            modelMap.addAttribute("player", currentPlayer);

        }
        return view;
    }

    @GetMapping(path = "/delete/{gameId}")
    public String deleteGame(@PathVariable("gameId") int gameId, ModelMap modelMap) {
        Optional<Game> game = gameService.findGameById(gameId); 
        generalService.insertIdUserModelMap(modelMap);
        if (game.isPresent()) {
            gameService.delete(game.get());
            modelMap.addAttribute("message", "Game successfully deleted!");
        } else {
            modelMap.addAttribute("message", "Game not Found!");
        }

        return adminController.rooms(modelMap);
    }

    private static final String VIEWS_GAMES_CREATE_OR_UPDATE_FORM = "games/createOrUpdateGameForm";

    @GetMapping(path = "/edit/{gameId}")
    public String updateGame(@PathVariable("gameId") int gameId, ModelMap model) {
        Game game = gameService.findGameById(gameId).get();
        securityService.insertIdUserModelMap(model); 
        model.put("game", game);
        return VIEWS_GAMES_CREATE_OR_UPDATE_FORM;
    }

    @GetMapping(path = "/{gameId}/lobby")
    public String lobby(@PathVariable("gameId") int gameId, ModelMap model) {

        String view;
        securityService.insertIdUserModelMap(model);
        if (gameService.findGameById(gameId).isPresent()) {
            Game game = gameService.findGameById(gameId).get(); 
            model.addAttribute("game", game);

            int playerId = securityService.getCurrentUserId(); // Id of player that is logged

            Player pay = playerService.findPlayerById(playerId).get();
            model.addAttribute("player", pay);

            view = "games/lobby";
        } else {
            view = "/errors"; 
        }

        return view;
    }

    /**
     *
     * @param game
     * @param result
     * @param gameId
     * @param model
     * @param name
     * @param code
     * @param model
     * @return
     */

    @PostMapping(value = "/edit/{gameId}")
    public String processUpdateForm(@Valid Game game, BindingResult result, @PathVariable("gameId") int gameId,
            ModelMap model) {
        if (result.hasErrors()) {
            model.put("game", game);
            return VIEWS_GAMES_CREATE_OR_UPDATE_FORM;
        } else {
            Game gameToUpdate = this.gameService.findGameById(gameId).get();
            BeanUtils.copyProperties(game, gameToUpdate, "id", "actualPlayer", "endTime", "starttime", "has_started", "code", "deck", "nameOfPlayers", "numberOfTurn", "player", "players", "points", "remainsCards");
            try {
                this.gameService.save(gameToUpdate);

            } catch (Exception ex) {
                //result.rejectValue("name", "duplicate", "already exists");
                return VIEWS_GAMES_CREATE_OR_UPDATE_FORM;
            }
            return "redirect:/games";
        }

    }

    // ROOMS VIEW (PUBLIC ONES)
    @GetMapping(path = "/rooms")
    public String publicRooms(ModelMap modelMap) {

        String view = "/welcome";
        securityService.insertIdUserModelMap(modelMap);

        if (securityService.authenticationNotNull()) {
            if (securityService.isAuthenticatedUser()) {
                
                if (securityService.isAdmin()) {
                    view = adminController.rooms(modelMap);
                } else {
                    view = playerController.games(modelMap);
                }
            } else {

                return "redirect:/welcome"; 

            }
        }
        return view;
    }

    //Games by room code
    @GetMapping(path = "/rooms/{code}")
    public String gameByCode(@PathVariable("code") String code, ModelMap modelMap) {
        String view;
        securityService.insertIdUserModelMap(modelMap);
        Iterable<Game> games;
        if (securityService.authenticationNotNull()) {
            view = "games/publicRooms";
            games = gameService.findGamesByRoomCode(code);
            modelMap.addAttribute("games", games);
            if(games.spliterator().getExactSizeIfKnown()==0l){
                modelMap.addAttribute("message", "Game not found");
            }
            
        
        } else {
            return "welcome"; 
        }
        return view;
    }

    // Games currently playing
    @GetMapping(path = "/rooms/playing")
    public String currentlyPlaying(ModelMap modelMap) {

        securityService.insertIdUserModelMap(modelMap);
        Collection<Game> games;

        if(securityService.isAuthenticatedUser()) {
                // If the user has admin perms then:
                if (securityService.isAdmin()) {
                    games = gameService.findAllPlaying();
                    modelMap.addAttribute("games", games);
                    // here we can see all the games currently being played, both public and private

                } else { 
                    games = gameService.findAllPublicPlaying();
                    modelMap.addAttribute("games", games);
                    // here we can see only the public games which are currently being played, in order to watch it by streaming

                }
        }

        return "games/currentlyPlaying";


    }

}
