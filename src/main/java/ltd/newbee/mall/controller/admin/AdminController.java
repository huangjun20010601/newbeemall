package ltd.newbee.mall.controller.admin;

import cn.hutool.captcha.ShearCaptcha;
import ltd.newbee.mall.entity.AdminUser;
import ltd.newbee.mall.service.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminUserService adminUserService;

    @GetMapping({"/index","/"})
    public String indexAll(HttpServletRequest request){
        request.setAttribute("path","index");
        return "admin/index";
    }

    @GetMapping("/login")
    public String login(){
        return "admin/login";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName, @RequestParam("password") String password, @RequestParam("verifyCode") String verifyCode, HttpSession session){
        if (StringUtils.isEmpty(verifyCode)){
            session.setAttribute("errorMsg","验证码不能为空");
            return "admin/login";
        }
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            session.setAttribute("errorMsg","用户名或密码不能为空");
            return "admin/login";
        }
        ShearCaptcha shearCaptcha = (ShearCaptcha) session.getAttribute("verifyCode");
        if(shearCaptcha == null || !shearCaptcha.verify(verifyCode)){
            session.setAttribute("errorMsg","验证码错误");
            return "admin/login";
        }
        AdminUser adminUser=adminUserService.login(userName, password);
        if (adminUser!=null){
            session.setAttribute("loginUser",adminUser.getNickName());
            session.setAttribute("loginUserId",adminUser.getAdminUserId());
            //session过期时间设置为7200秒，即2小时
            return "redirect:/admin/index";
        }else {
            session.setAttribute("errorMsg","登录失败");
            return "admin/login";
        }
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request){
        Integer loginUserId=(int)request.getSession().getAttribute("loginUserId");
        AdminUser adminUser=adminUserService.getUserDetailById(loginUserId);
        if (adminUser==null){
            return "admin/login";
        }
        request.setAttribute("path","profile");
        request.setAttribute("loginUserName",adminUser.getLoginUserName());
        request.setAttribute("nickName",adminUser.getNickName());
        return "admin/profile";
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request,@RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword){
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)){
            return "参数不能为空";
        }
        Integer loginUserId=(int)request.getSession().getAttribute("loginUserId");
        if(adminUserService.updatePassword(loginUserId,originalPassword,newPassword)){
            //修改成功后清空session中的数据，前端控制跳转到登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return "success";
        }else {
            return "修改失败";
        }
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request,@RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName){
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)){
            return "参数不能为空";
        }
        Integer loginUserId=(int)request.getSession().getAttribute("loginUserId");
        if (adminUserService.updateName(loginUserId,loginUserName,nickName)){
            return "success";
        }else {
            return "修改失败";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }
}
