package org.vvings.ocpsystem.config;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @Author vvings
 * @Date 2020/12/24 14:58
 * @Version 1.0
 */
public class ReLoginExpiredSessionStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
         RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        event.getResponse().setContentType("application/json;charset=UTF-8");
        ServerResponse.createByErrorCode(ResponseCode.RELOGIN.getCode(), "您的权限已被更改请重新登陆");
        redirectStrategy.sendRedirect(event.getRequest(),event.getResponse(),"/login");
    }
}
