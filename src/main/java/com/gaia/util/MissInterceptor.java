package com.gaia.util;

import com.estee.entity.User;
import com.estee.service.IUserService;
import com.gaia.constant.LoginRequired;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Log4j2
@Component
public class MissInterceptor implements HandlerInterceptor {
    @Resource
    private IUserService foxUserService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("preHandle :" +request.getContextPath());
//        BufferedReader reader = request.getReader();
//        Stream<String> lines = reader.lines();
//        lines.forEach(r->{
//            log.info(r);
//        });

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //if(request.getMethod().toLowerCase().equals("get")){return true;}
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);

        if (method.isAnnotationPresent(LoginRequired.class)) {
            if (methodAnnotation != null) {
                String token = request.getHeader("token");
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                } else {
                    Long id = Long.valueOf(JWTUtil.getName(token));
                    User foxUser = foxUserService.getById(id);
                    log.info("get user from jwt token:"+foxUser);
                    if (foxUser == null) {
                        throw new RuntimeException("用户不存在，请重新登录");
                    }
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }
}
