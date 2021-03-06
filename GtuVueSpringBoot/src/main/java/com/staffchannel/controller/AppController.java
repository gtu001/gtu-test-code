package com.staffchannel.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.validation.ObjectError;
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

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

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

    //  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    //  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

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
            debugErrorMessage(result);
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
            debugErrorMessage(result);
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

    @RequestMapping(value = "/edit-menu-{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/delete-menu-{id}", method = RequestMethod.GET)
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
    @RequestMapping(value = { "/newMenu" }, method = { RequestMethod.POST })
    public String saveMenu(@Valid Menu menu, BindingResult result, ModelMap model) {
        if (!isAdmin()) {
            return "accessDenied";
        }
        if (result.hasErrors()) {
            debugErrorMessage(result);
            return "redirect:/newMenu";
        }
        menuService.save(menu);
        model.addAttribute("success", "清單 :  " + menu.getMenuName() + " 更新成功!");
        return "menu/menuSaveSuccess";
    }

    /**
     * 修改使用者POST
     */
    @RequestMapping(value = { "/edit-menu-{id}" }, method = RequestMethod.POST)
    public String updateMenu(@Valid Menu menu, BindingResult result, ModelMap model, @PathVariable String id) {
        if (!isAdmin()) {
            return "accessDenied";
        }
        if (result.hasErrors()) {
            debugErrorMessage(result);
            return "redirect:/edit-menu-{id}";
        }
        Menu menu2 = menuService.findById(Integer.parseInt(id));
        if (menu2 != null) {
            menuService.update(menu);
        } else {
            menuService.save(menu);
        }
        model.addAttribute("success", "清單 :  " + menu.getMenuName() + " 更新成功!");
        return "menu/menuSaveSuccess";
    }

    // 菜單 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // 角色 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    /*
     * 管理角色列表
     */
    @RequestMapping(value = { "/admin/rolesList" }, method = RequestMethod.GET)
    public String listRoles(ModelMap model) {
        List<UserProfile> roles = userProfileService.findAll();
        model.addAttribute("roles", roles);
        return "role/rolesList";
    }

    /*
     * 新增角色GET
     */
    @RequestMapping(value = { "/admin/newRole" }, method = RequestMethod.GET)
    public String newRole(ModelMap model) {
        UserProfile userProfile = new UserProfile();
        List<Menu> menuexisting = menuService.findAllMenus();
        model.addAttribute("userHasMenusChecked", getUserProfileHasMenuChecked(userProfile));
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("menuexisting", menuexisting);
        model.addAttribute("edit", false);
        return "role/role_edit";
    }

    /**
     * 修改角色GET
     */
    @RequestMapping(value = { "/edit-role-{roleId}" }, method = RequestMethod.GET)
    public String editRole(@PathVariable String roleId, ModelMap model) {
        UserProfile userProfile = userProfileService.findById(Integer.parseInt(roleId));
        List<Menu> menuexisting = menuService.findAllMenus();
        model.addAttribute("userHasMenusChecked", getUserProfileHasMenuChecked(userProfile));
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("menuexisting", menuexisting);
        model.addAttribute("edit", true);
        return "role/role_edit";
    }

    /**
     * 修改角色POST
     */
    @RequestMapping(value = { "/edit-role-{roleId}" }, method = RequestMethod.POST)
    public String updateRole(@Valid UserProfile userProfile, BindingResult result, ModelMap model, @PathVariable String roleId) {
        processUserProfileMenuDetails(userProfile);
        if (!isAdmin()) {
            return "accessDenied";
        }
        if (result.hasErrors()) {
            debugErrorMessage(result);
            return "redirect:/edit-role-{roleId}";
        }
        // UserProfile profile2 =
        // userProfileService.findById(Integer.parseInt(roleId));
        // if (profile2 != null) {
        // userProfileService.update(userProfile);
        // } else {
        userProfileService.save(userProfile);
        // }
        model.addAttribute("success", "角色 " + userProfile.getType() + " 更新成功!");
        return "role/roleEditSuccess";
    }

    /**
     * 修改角色POST
     */
    @RequestMapping(value = { "/admin/newRole" }, method = { RequestMethod.POST })
    public String saveRole(@Valid UserProfile userProfile, BindingResult result, ModelMap model, HttpServletRequest request) {
        debugParameters(request);
        processUserProfileMenuDetails(userProfile);
        if (!isAdmin()) {
            return "accessDenied";
        }
        if (result.hasErrors()) {
            debugErrorMessage(result);
            return "redirect:/admin/newRole";
        }
        userProfileService.save(userProfile);
        model.addAttribute("success", "角色 " + userProfile.getType() + " 更新成功!");
        return "role/roleEditSuccess";
    }

    private Map<Integer, String> getUserProfileHasMenuChecked(UserProfile userProfile) {
        Map<Integer, String> hasMenuMap = new HashMap<Integer, String>();
        if (userProfile.getMenuList() == null) {
            return hasMenuMap;
        }
        for (Menu menu : userProfile.getMenuList()) {
            if (menu.getId() != null) {
                hasMenuMap.put(menu.getId(), "checked");
            }
            if (menu.getSubMenu() != null && !menu.getSubMenu().isEmpty()) {
                for (Menu m2 : menu.getSubMenu()) {
                    if (m2.getId() != null) {
                        hasMenuMap.put(m2.getId(), "checked");
                    }
                }
            }
        }
        return hasMenuMap;
    }

    private void processUserProfileMenuDetails(UserProfile userProfile) {
        if (userProfile.getMenuList() == null) {
            return;
        }
        List<Menu> appendMenuLst = new ArrayList<Menu>();
        for (Menu menu : userProfile.getMenuList()) {
            if (menu.getId() != null) {
                appendMenuLst.add(menu);
            }
            if (menu.getSubMenu() != null && !menu.getSubMenu().isEmpty()) {
                for (Menu m2 : menu.getSubMenu()) {
                    if (m2.getId() != null) {
                        appendMenuLst.add(m2);
                    }
                }
            }
        }
        userProfile.setMenuList(appendMenuLst);
    }

    /**
     * 刪除角色
     */
    @RequestMapping(value = { "/admin/delete-role-{roleId}" }, method = RequestMethod.GET)
    public String deleteRole(@PathVariable String roleId) {
        UserProfile userProfile = userProfileService.findById(Integer.parseInt(roleId));
        if (userProfile != null) {
            userProfileService.delete(userProfile);
        }
        return "redirect:/admin/rolesList";
    }
    // 角色 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

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

    private void debugParameters(HttpServletRequest request) {
        logger.info("debugParameters ...start");
        for (Enumeration enu = request.getParameterNames(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String[] values = request.getParameterValues(key);
            logger.info("\t" + key + "\tvalue:" + Arrays.toString(values));
        }
        logger.info("debugParameters ...end");
    }

    private void debugErrorMessage(BindingResult result) {
        for (Object object : result.getAllErrors()) {
            if (object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;
                logger.error(messageSource.getMessage(fieldError, null) + " : " + fieldError);
            }
            if (object instanceof ObjectError) {
                ObjectError objectError = (ObjectError) object;
                logger.error(messageSource.getMessage(objectError, null) + " : " + objectError);
            }
        }
    }
}