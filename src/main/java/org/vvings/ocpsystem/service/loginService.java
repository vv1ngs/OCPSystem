package org.vvings.ocpsystem.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.Authority;
import org.vvings.ocpsystem.pojo.User;

import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/23 20:42
 * @Version 1.0
 */
public interface loginService extends UserDetailsService {
    ServerResponse<Set<Authority>> getAllAuthority(String username);
    void kickOutUser(String username);

    User getUserByUsername(String username);

    void startorStopUsr(String username,Boolean flag);

    void dealUserByUsername(String username);
}
