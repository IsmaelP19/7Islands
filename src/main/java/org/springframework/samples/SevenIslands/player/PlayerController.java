package org.springframework.samples.SevenIslands.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.SevenIslands.game.Game;
import org.springframework.samples.SevenIslands.game.GameService;
import org.springframework.samples.SevenIslands.general.GeneralService;
import org.springframework.samples.SevenIslands.user.Authorities;
import org.springframework.samples.SevenIslands.user.AuthoritiesService;
// import org.springframework.samples.SevenIslands.user.User;

import org.springframework.samples.SevenIslands.user.UserService;
import org.springframework.samples.SevenIslands.web.CurrentUserController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/players")
public class PlayerController {


    @Autowired
    private PlayerService playerService;

    @Autowired	
	  private GeneralService generalService;

    @Autowired
    private GameService gameService;

    @Autowired	
	  private UserService userService;
    
    @Autowired
    private AuthoritiesService authoritiesService;

    @GetMapping()
    public String listPlayers(ModelMap modelMap, @PathParam("filterName") String filterName, @PathParam("begin") Integer begin, @PathParam("end") Integer end){        //For admins
        String view ="players/listPlayers";
        generalService.insertIdUserModelMap(modelMap);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(x -> x.toString().equals("admin"))) {
                    
                    Iterable<Player> players = playerService.findAll();
                    if(filterName!=null){
                        List<Player> listPlayersFiltered= StreamSupport.stream(players.spliterator(), false).filter(x->x.getUser().getUsername().contains(filterName)).collect(Collectors.toList());
                        modelMap.addAttribute("players", listPlayersFiltered);
                    }else{
                        modelMap.addAttribute("players", players);
                    }
                    if(end==null){
                        end=9;
                    }
                    
                    modelMap.addAttribute("filterName", filterName);
                    modelMap.addAttribute("begin", begin);
                    modelMap.addAttribute("end", end);
            
        }else{
            view = "/errors";
        }
        return view;

    }
  
    @GetMapping(path="/profile/{playerId}")
    public String profile(@PathVariable("playerId") int playerId, ModelMap modelMap){
        String view = "/players/profile";
        
        Optional<Player> player = playerService.findPlayerById(playerId);
        if(player.isPresent()){
            modelMap.addAttribute("player", player.get());
        }else{
            modelMap.addAttribute("message", "Player not found");
            view = "/error"; 
        }
        generalService.insertIdUserModelMap(modelMap);
        return view;
    }

    @GetMapping(path="/profile/{playerId}/statistics")
    public String statistics(@PathVariable("playerId") int playerId, ModelMap modelMap){
        String view = "players/statistics";
        generalService.insertIdUserModelMap(modelMap);
        Optional<Player> player = playerService.findPlayerById(playerId);
        if(player.isPresent()){
            modelMap.addAttribute("player", player.get());
        }else{
            modelMap.addAttribute("message", "Player not found");
            view = "/error";
        }
        return view;
    }

    @GetMapping(path="/rooms")
    public String games(ModelMap modelMap) {
        String view = "games/publicRooms";
        Iterable<Game> games = gameService.findAllPublic();
        modelMap.addAttribute("games", games);
        return view;
    }

    @GetMapping(path="/profile/{playerId}/rooms/created")
    public String gamesCreated(@PathVariable("playerId") int playerId, ModelMap modelMap){
        String view = "players/roomsCreated";
        generalService.insertIdUserModelMap(modelMap);
        Optional<Player> player = playerService.findPlayerById(playerId);
        if(player.isPresent()){
            Collection<Game> games = gameService.findByOwnerId(player.get().getId());
            modelMap.addAttribute("games", games);
            modelMap.addAttribute("player", player.get());
        }else{
            modelMap.addAttribute("message", "Player not found");
            view = "/error";
        }
        return view;
    }

    @GetMapping(path="/profile/{playerId}/rooms/played")
    public String gamesPlayed(@PathVariable("playerId") int playerId, ModelMap modelMap){
        String view = "players/roomsPlayed";
        generalService.insertIdUserModelMap(modelMap);
        Optional<Player> player = playerService.findPlayerById(playerId);

        if(player.isPresent()){
            Collection<Game> games = gameService.findGamesByPlayerId(player.get().getId());
            modelMap.addAttribute("games", games);
            modelMap.addAttribute("player", player.get());
        }else{
            modelMap.addAttribute("message", "Player not found");
            view = "/error";
        }
        return view;
    }

  

    @GetMapping(path="/delete/{playerId}")
    public String deletePlayer(@PathVariable("playerId") int playerId, ModelMap modelMap){
        String view= "players/listPlayers";
        generalService.insertIdUserModelMap(modelMap);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(x -> x.toString().equals("admin"))) {
                    Optional<Player> player = playerService.findPlayerById(playerId);
                    if(player.isPresent()){
                        Player p = player.get();
                        Collection<Game> lg = p.getGames();
                        Collection<Game> col = lg.stream().filter(x->x.getPlayer().getId()!=playerId).collect(Collectors.toCollection(ArrayList::new));
                        col.stream().forEach(x->x.deletePlayerOfGame(p));

                        p.deleteGames(col);
                
                        playerService.delete(player.get());
                        modelMap.addAttribute("message", "Player successfully deleted!");

                        return listPlayers(modelMap, null, 0, 9);

                    }else{
                        modelMap.addAttribute("message", "Player not found");
                        view=listPlayers(modelMap, null, 0, 9);
                    }
        }else{
            view = "/errors";
        }
        return view;

    }

    private static final String VIEWS_PLAYERS_CREATE_OR_UPDATE_FORM = "players/createOrUpdatePlayerForm";

    @GetMapping(path="/edit/{playerId}")
    public String updatePlayer(@PathVariable("playerId") int playerId, ModelMap model) {
        Optional<Player> player = playerService.findPlayerById(playerId); // optional puede ser error el import
        String view = VIEWS_PLAYERS_CREATE_OR_UPDATE_FORM;
        generalService.insertIdUserModelMap(model);
        //Test if currentplayer is admin or the same id
        // TODO: Comprobar que sea o admin o q el usuer registrado tenga el mismo id q el de la url
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream()
            .anyMatch(x -> x.toString().equals("admin"))){
            if(player.isPresent()){
                model.addAttribute("player", player.get());
            }else{
                model.addAttribute("message", "Player not found");
                view = "/error";
            }
        }else if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User currentUser = (User) authentication.getPrincipal();
            int playerLoggedId = playerService.getIdPlayerByName(currentUser.getUsername());
            if(playerId==playerLoggedId){
                if(player.isPresent()){
                    model.addAttribute("player", player.get());
                }else{
                    model.addAttribute("message", "Player not found");
                    view = "/error";
                }
            }else{
                view= "/error";
            }
        }else{
            view = "/errors";
        }
        return view;
    }

    /**
     *
     * @param player
     * @param result
     * @param playerId
     * @param model
     * @param surname
     * @param firstName
     * @param email
     * @param password
     * @param model
     * @return
     */

    @PostMapping(value = "/edit/{playerId}")
	public String processUpdateForm(@Valid Player player, BindingResult result,@PathVariable("playerId") int playerId, ModelMap model) {
		if (result.hasErrors()) {
            System.out.print(result.getAllErrors());
			model.put("player", player);
			return VIEWS_PLAYERS_CREATE_OR_UPDATE_FORM;
		}
		else {
                    
                    Player playerToUpdate=this.playerService.findPlayerById(playerId).get();
                    int a = playerToUpdate.getUser().getAuthorities().iterator().next().getId();
                    String n = playerToUpdate.getUser().getUsername();
 
                    //borrar user antes de grabarlo en playerToUpdate
                    //validador
			BeanUtils.copyProperties(player, playerToUpdate,"id", "profilePhoto","totalGames","totalTimeGames","avgTimeGames","maxTimeGame","minTimeGame","totalPointsAllGames","avgTotalPoints","favoriteIsland","favoriteTreasure","maxPointsOfGames","minPointsOfGames","achievements","cards","watchGames","forums","games","invitations","friend_requests","players_friends","gamesCreador");  //METER AQUI OTRAS PROPIEDADES                                                                                
                    try {                    
                        //this.playerService.savePlayer(playerToUpdate);
                        this.playerService.savePlayer(playerToUpdate);
                        authoritiesService.deleteAuthorities(a);
                        if(n != playerToUpdate.getUser().getUsername()){
                           
                            userService.delete(n);
                        }

                    } catch (Exception ex) {
                        result.rejectValue("name", "duplicate", "already exists");
                        return VIEWS_PLAYERS_CREATE_OR_UPDATE_FORM;
                    }
                    if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(x -> x.toString().equals("admin"))){
                        return "redirect:/players";
                    }else{
                        if(n != playerToUpdate.getUser().getUsername()){
                            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                            return "redirect:/welcome";
                        }
                        return "redirect:/players/profile/{playerId}";
                        
                    }
		}
	}
    
    
}
