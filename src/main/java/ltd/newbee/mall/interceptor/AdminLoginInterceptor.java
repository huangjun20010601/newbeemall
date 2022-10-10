package ltd.newbee.mall.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台系统身份验证拦截器
 */
@Component
public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("进入拦截器...");
        String uri=request.getRequestURI();
        //System.out.println(uri); //   /admin/goods/edit
        if(uri.startsWith("/admin") && null ==request.getSession().getAttribute("loginUser")){
            request.getSession().setAttribute("errorMsg","请登录");
            response.sendRedirect(request.getContextPath()+"/admin/login");
//            System.out.println("未登录，拦截成功...");
            return false;
        }else {
            request.getSession().removeAttribute("errorMsg");
//            System.out.println("已登录，放行...");
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
