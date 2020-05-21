package com.staffchannel.controller;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.staffchannel.model.Menu;
import com.staffchannel.model.User;
import com.staffchannel.model.UserProfile;
import com.staffchannel.service.CaseService;
import com.staffchannel.service.MenuService;
import com.staffchannel.service.UserProfileService;
import com.staffchannel.service.UserService;

@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {

    @Autowired
    UserService userService;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    MenuService menuService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

    @Autowired
    AuthenticationTrustResolver authenticationTrustResolver;

    @Autowired
    CaseService caseService;

    @ModelAttribute("roles")
    public List<UserProfile> initializeProfiles() {
        return userProfileService.findAll();
    }

    /**
     * 待審核案件
     */
    @RequestMapping(value = "/reviewCase", method = RequestMethod.GET)
    public String reviewCase(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("待審核案件");
        return "reviewCase/ReviewCase";
    }

    /**
     * 資料審核
     */
    @RequestMapping(value = "/gotoDataReview", method = RequestMethod.GET)
    public String gotoDataReview(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("資料審核");
        return "reviewCase/DataReview";
    }

    /**
     * 登入頁面 如果以登入會導到首頁
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        if (isCurrentAuthenticationAnonymous()) {
            return "login";
        } else {
            return "redirect:/mainPage";
        }
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // new SecurityContextLogoutHandler().logout(request, response,
            // auth);
            persistentTokenBasedRememberMeServices.logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login?logout";
    }

    /*
     * 首頁
     */
    @RequestMapping(value = { "/", "/main" }, method = RequestMethod.GET)
    public String mainPage(ModelMap model) {
        return "mainPage";
    }

    // 使用者 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 使用者 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // 使用者 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 使用者 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // 使用者 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 使用者 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // 使用者 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 使用者 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // 使用者 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 使用者 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // 使用者 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 使用者 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // 使用者 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    /*
     * 管理使用者列表
     */
    @RequestMapping(value = { "/admin/usersList" }, method = RequestMethod.GET)
    public String listUsers(ModelMap model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user/usersList";
    }

    /*
     * 新增使用者GET
     */
    @RequestMapping(value = { "/admin/newUser" }, method = RequestMethod.GET)
    public String newUser(ModelMap model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("edit", false);
        return "user/registration";
    }

    /**
     * 新增使用者POST
     */
    @RequestMapping(value = { "/admin/newUser" }, method = RequestMethod.POST)
    public String saveUser(@Valid User user, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "user/registration";
        }

        // 檢查是否unique
        if (!userService.isUserSSOUnique(user.getId(), user.getSsoId())) {
            FieldError ssoError = new FieldError("user", "ssoId", messageSource.getMessage("non.unique.ssoId", new String[] { user.getSsoId() }, Locale.getDefault()));
            result.addError(ssoError);
            return "user/registration";
        }

        userService.saveUser(user);

        model.addAttribute("success", "使用者 " + user.getName() + " 新增成功!");
        // return "success";
        return "user/registrationsuccess";
    }

    /**
     * 修改使用者GET
     */
    @RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
    public String editUser(@PathVariable String ssoId, ModelMap model) {

        if (!ssoId.equals(getUserSSO())) {
            if (!isAdmin()) {
                return "accessDenied";
            }
        }

        User user = userService.findBySSO(ssoId);
        model.addAttribute("user", user);
        model.addAttribute("edit", true);
        return "user/registration";
    }

    /**
     * 修改使用者POST
     */
    @RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)
    public String updateUser(@Valid User user, BindingResult result, ModelMap model, @PathVariable String ssoId) {

        if (!ssoId.equals(getUserSSO())) {
            if (!isAdmin()) {
                return "accessDenied";
            }
        }

        if (result.hasErrors()) {
            return "user/registration";
        }

        // 檢查是否unique
        if (!userService.isUserSSOUnique(user.getId(), user.getSsoId())) {
            FieldError ssoError = new FieldError("user", "ssoId", messageSource.getMessage("non.unique.ssoId", new String[] { user.getSsoId() }, Locale.getDefault()));
            result.addError(ssoError);
            return "user/registration";
        }

        userService.updateUser(user);

        model.addAttribute("success", "使用者 " + user.getName() + " 更新成功!");
        return "user/registrationsuccess";
    }

    /**
     * 刪除使用者
     */
    @RequestMapping(value = { "/admin/delete-user-{ssoId}" }, method = RequestMethod.GET)
    public String deleteUser(@PathVariable String ssoId) {
        userService.deleteUserBySSO(ssoId);
        return "redirect:/admin/usersList";
    }
    // 使用者 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // 菜單 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    @RequestMapping(value = "/menulist", method = RequestMethod.GET)
    public String menulist(ModelMap model) {
        List<Menu> menuexisting = menuService.findAllMenus();
        model.addAttribute("menuexisting", menuexisting);
        return "menu/Menulist";
    }

    @RequestMapping(value = "/edit-user-menu-{id}", method = RequestMethod.GET)
    public String listedit(@PathVariable Integer id, ModelMap model) {
        Menu menu = menuService.findById(id);
        model.addAttribute("menu", menu);
        model.addAttribute("edit", true);
        return "menu/EditMenu";
    }

    @RequestMapping(value = { "/newMenu" }, method = RequestMethod.GET)
    public String newMenu(ModelMap model) {
        Menu menu = new Menu();
        model.addAttribute("menu", menu);
        model.addAttribute("edit", false);
        return "menu/EditMenu";
    }

    @RequestMapping(value = "/delete-user-menu-{id}", method = RequestMethod.GET)
    public String deleteMenu(@PathVariable String id) {
        menuService.delete(Integer.parseInt(id));
        return "redirect:/menulist";
    }

    @RequestMapping(value = "/managerEditMenu", method = RequestMethod.GET)
    public String managerEditMenu(ModelMap model) {
        List<Menu> menuexisting = menuService.findAllMenus();
        model.addAttribute("menuexisting", menuexisting);
        return "ManagerEditMenu";
    }

    @RequestMapping(value = "/testProfileMenu", method = RequestMethod.GET)
    public String testProfileMenu(ModelMap model) {
        Menu mm = menuService.findById(1);
        UserProfile sss = userProfileService.findById(1);

        sss.getMenuList().add(mm);
        mm.getUserProfile().add(sss);

        userProfileService.save(sss);

        return "Menulist";
    }
    

    /**
     * 修改使用者POST
     */
    @RequestMapping(value = { "/newMenu" }, method = { RequestMethod.POST})
    public String saveMenu(@Valid Menu menu, BindingResult result, ModelMap model) {
//        if (!ssoId.equals(getUserSSO())) {
//            if (!isAdmin()) {
//                return "accessDenied";
//            }
//        }
//
//        if (result.hasErrors()) {
//            return "user/registration";
//        }
//
//        // 檢查是否unique
//        if (!userService.isUserSSOUnique(user.getId(), user.getSsoId())) {
//            FieldError ssoError = new FieldError("user", "ssoId", messageSource.getMessage("non.unique.ssoId", new String[] { user.getSsoId() }, Locale.getDefault()));
//            result.addError(ssoError);
//            return "user/registration";
//        }

        menuService.save(menu);
        
        model.addAttribute("success", "清單 :  " + menu.getMenuName() + " 更新成功!");
        return "menu/menuSaveSuccess";
    }
    
    // 菜單 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * This method handles Access-Denied redirect.
     */
    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        return "accessDenied";
    }

    private boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }

    private String getUserSSO() {
        String userSSO = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userSSO = ((UserDetails) principal).getUsername();
        } else {
            userSSO = principal.toString();
        }
        return userSSO;
    }

    private boolean isAdmin() {
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }
        return isAdmin;
    }

}