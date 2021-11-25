package org.springframework.samples.SevenIslands.admin;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.SevenIslands.game.Game;
import org.springframework.samples.SevenIslands.game.GameService;
import org.springframework.samples.SevenIslands.general.GeneralService;
import org.springframework.samples.SevenIslands.user.AuthoritiesService;
import org.springframework.samples.SevenIslands.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired	
	private GeneralService generalService;

    @Autowired
	private GameService gameService;

    @Autowired
    private AuthoritiesService authoritiesService;

    @Autowired	
    private UserService userService;
    
    @GetMapping(path="/profile/{adminId}")
    public String profile(@PathVariable("adminId") int adminId, ModelMap modelMap){
        String view = "admins/profile";
        generalService.insertIdUserModelMap(modelMap);
        Optional<Admin> admin = adminService.findAdminById(adminId);
        if(admin.isPresent()){
            modelMap.addAttribute("admin", admin.get());
        }else{
            modelMap.addAttribute("message", "Admin not found");
            view = "/error";
        }
        return view;
    }

    @GetMapping(path="/rooms")
    public String rooms(ModelMap modelMap) {
        String view = "admins/rooms";
        Iterable<Game> games = gameService.findAll();
        modelMap.addAttribute("games", games);
        return view;
    }

    private static final String VIEWS_ADMINS_CREATE_OR_UPDATE_FORM = "admins/createOrUpdateAdminForm";

    @GetMapping(value = "/profile/edit/{adminId}")
    public String updateAdmin(@PathVariable("adminId") int adminId, ModelMap modelMap) {
        generalService.insertIdUserModelMap(modelMap);
        Optional<Admin> admin = adminService.findAdminById(adminId); // optional puede ser error el import
        if(admin.isPresent()){
            modelMap.addAttribute("admin", admin.get());
        }else{
            modelMap.addAttribute("message", "admin not found");
            return "/error";
        }
        return VIEWS_ADMINS_CREATE_OR_UPDATE_FORM;
    }
    
    @PostMapping(value = "/profile/edit/{adminId}")
	public String processUpdateForm(@Valid Admin admin, BindingResult result,@PathVariable("adminId") int adminId, ModelMap model) {
		if (result.hasErrors()) {
            System.out.print(result.getAllErrors());
			model.put("player", admin);
			return VIEWS_ADMINS_CREATE_OR_UPDATE_FORM;
		}
		else {
            
            Optional<Admin> adminToUpdate=this.adminService.findAdminById(adminId);
        
            
            if(!adminToUpdate.isPresent()){
                model.addAttribute("message", "Admin not found");
                return "/error";
            }
            int a = adminToUpdate.get().getUser().getAuthorities().iterator().next().getId();
            String n = adminToUpdate.get().getUser().getUsername();

			BeanUtils.copyProperties(admin, adminToUpdate.get(),"id");                                                                                  
                    try {                    
                        this.adminService.save(adminToUpdate.get());
                        userService.saveUser(adminToUpdate.get().getUser());
		                
		                authoritiesService.saveAuthorities(adminToUpdate.get().getUser().getUsername(), "admin");  
                        authoritiesService.deleteAuthorities(a);
                        if(n != adminToUpdate.get().getUser().getUsername()){
                           
                            userService.delete(n);
                        }
                        
                    } catch (Exception ex) {
                        result.rejectValue("name", "duplicate", "already exists");
                        return VIEWS_ADMINS_CREATE_OR_UPDATE_FORM ;
                    }

                    if(n != adminToUpdate.get().getUser().getUsername()){
                        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                        return "redirect:/welcome";
                    }
			return "redirect:/admins/profile/" + adminId;
		}
	}

    
    // @GetMapping(path = "/new")
    // public String createGame(Player player,ModelMap modelMap) {
    //     String view = "games/editarJuego"; // Hacer pagina
    //     modelMap.addAttribute("game", new Game());
    //     return view;
    // }

    // @PostMapping(path = "/save")
    // public String salvarEvento(@Valid Game game, BindingResult result, ModelMap modelMap) {
    //     String view = "games/myRooms";
    //     if (result.hasErrors()) {
    //         modelMap.addAttribute("game", game);
    //         return "games/editarJuego";
    //     } else {
    //         generalService.save(game);
            
    //         view = myRooms(modelMap);

    //         Authentication authetication = SecurityContextHolder.getContext().getAuthentication();
    //         User currentUser = (User)authetication.getPrincipal();
    //         int playerId=playerService.getIdPlayerByName(currentUser.getUsername());
    //         generalService.insertGP(game.getId(), playerId);

    //         modelMap.addAttribute("message", "Game successfully saved!");
            
    //     }
    //     return view;
    // }

    // @GetMapping(path = "/delete/{gameId}")
    // public String deleteGame(@PathVariable("gameId") int gameId, ModelMap modelMap) {
    //     Optional<Game> game = generalService.findGameById(gameId); // optional puede ser error el import
    //     if (game.isPresent()) {
    //         generalService.delete(game.get());
    //         modelMap.addAttribute("message", "Game successfully deleted!");
    //     } else {
    //         modelMap.addAttribute("message", "Game not Found!");
    //     }
    //     String view = myRooms(modelMap);
    //     return view;
    // }

    // private static final String VIEWS_GAMES_CREATE_OR_UPDATE_FORM = "games/createOrUpdateGameForm";

    // @GetMapping(path = "/edit/{gameId}")
    // public String updateGame(@PathVariable("gameId") int gameId, ModelMap model) {
    //     Game game = generalService.findGameById(gameId).get(); // optional puede ser error el import
    //     model.put("game", game);
    //     return VIEWS_GAMES_CREATE_OR_UPDATE_FORM;
    // }

    //  /**
    //  *
    //  * @param game
    //  * @param result
    //  * @param gameId
    //  * @param model
    //  * @param name
    //  * @param code
    //  * @param model
    //  * @return
    //  */

    // @PostMapping(value = "/edit/{gameId}")
	// public String processUpdateForm(@Valid Game game, BindingResult result,@PathVariable("gameId") int gameId, ModelMap model) {
	// 	if (result.hasErrors()) {
	// 		model.put("game", game);
	// 		return VIEWS_GAMES_CREATE_OR_UPDATE_FORM;
	// 	}
	// 	else {
    //                 Game gameToUpdate=this.generalService.findGameById(gameId).get();
	// 		BeanUtils.copyProperties(game, gameToUpdate, "id","game","games","code");                                                                                  
    //                 try {                    
    //                     this.generalService.save(gameToUpdate);                    
                    
    //                 } catch (Exception ex) {
    //                     result.rejectValue("name", "duplicate", "already exists");
    //                     return VIEWS_GAMES_CREATE_OR_UPDATE_FORM;
    //                 }
	// 		return "redirect:/games";
	// 	}



	// }




    // //ROOMS VIEW (PUBLIC ONES)
    // @GetMapping(path = "/rooms")
    // public String publicRooms(ModelMap modelMap) {
    //     String view = "/welcome"; // Hacer pagina
    //     Authentication authetication = SecurityContextHolder.getContext().getAuthentication();
    //     if(authetication != null){
    //         System.out.println("\n\n\n\n" + authetication.getPrincipal());
    //         if(authetication.isAuthenticated() && authetication.getPrincipal() instanceof User){
    //             //If the user has admin perms then:
    //             if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(x->x.toString().equals("admin"))){
    //                 view = "games/publicRoomsAdmins"; // Hacer pagina
    //             }else{
    //                 view = "games/publicRooms"; // Hacer pagina
    //             }
    //     }else{
    //         return "welcome"; //da error creo que es por que request mapping de arriba
    //     }    
    //     }       
    //     Iterable<Game> games = generalService.findAllPublic();
    //     modelMap.addAttribute("games", games);
    //     return view;
    // }

}
