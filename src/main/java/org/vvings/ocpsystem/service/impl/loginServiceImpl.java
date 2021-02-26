package org.vvings.ocpsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.dao.*;
import org.vvings.ocpsystem.pojo.*;
import org.vvings.ocpsystem.service.loginService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/23 20:42
 * @Version 1.0
 */
@Service
public class loginServiceImpl implements loginService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private UserToRoleMapper userToRoleMapper;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Override
    @ResponseBody
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userMapper.findUserByUserName(username);
        if (user==null){
            throw new UsernameNotFoundException("username: " + username + " do not exist!");
        }
        else if (!user.getStatus()){
            throw new UsernameNotFoundException(username + " has been banned");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList <>();
            Set<Authority> authorities=getAllAuthorities(user);
            authorities.forEach(i -> {
                if (i!=null&&i.getAuthorityName()!=null){
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(i.getAuthorityName());
                    grantedAuthorities.add(grantedAuthority);
                }
            });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
    private Set<Authority> getAllAuthorities (User user){
            List<UserToRole> userToRoles=userToRoleMapper.selectByUserID(user.getId());
            List<Role> roles=new ArrayList<>();
            userToRoles.forEach(i -> roles.add(roleMapper.selectByPrimaryKey(i.getRoleId())) );
            List<RoleAuthority> roleAuthorities=new ArrayList<>();
            Set<Authority> authorities=new HashSet<>();
            roles.forEach(i -> {
                if (i.getStatus()){
                    roleAuthorities.addAll(roleAuthorityMapper.selectByRoleId(i.getId()));
                }
            });

            roleAuthorities.forEach(i -> authorities.add(authorityMapper.selectByPrimaryKey(i.getAuthorityId())));
            return authorities;
    }

    @Override
    public ServerResponse<Set<Authority>> getAllAuthority(String username) {
        User user=userMapper.findUserByUserName(username);
        return ServerResponse.createBySuccess(getAllAuthorities(user));
    }

    @Override
    public void kickOutUser(String username){
        List<Object> users = sessionRegistry.getAllPrincipals();
        for (Object principal : users) {
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                final org.springframework.security.core.userdetails.User loggedUser = (org.springframework.security.core.userdetails.User) principal;
                    if (username.equals(loggedUser.getUsername())) {
                        List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false); // false代表不包含过期session
                        if (null != sessionsInfo && sessionsInfo.size() > 0) {
                            for (SessionInformation sessionInformation : sessionsInfo) {
                                sessionInformation.expireNow(); // 使session过期
                            }
                        }
                    }
            }
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findUserByUserName(username);
    }

    @Override
    public void startorStopUsr(String username,Boolean flag) {
        userMapper.updateStatusbyusername(username,flag);
    }

    @Override
    public void dealUserByUsername(String username) {
        User user=userMapper.findUserByUserName(username);
        userToRoleMapper.deleteByuid(user.getId());
        userMapper.deleteByUsername(username);
    }

}
