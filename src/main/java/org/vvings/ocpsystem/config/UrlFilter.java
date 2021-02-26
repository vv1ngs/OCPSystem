package org.vvings.ocpsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.vvings.ocpsystem.dao.AuthorityMapper;
import org.vvings.ocpsystem.pojo.Authority;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author vvings
 * @Date 2020/12/23 20:28
 * @Version 1.0
 */
@Service
public class UrlFilter implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private AuthorityMapper authorityMapper;
    private HashMap<String, Collection<ConfigAttribute>> map = null;
    public void loadResourceDefine() {
        map = new HashMap<String, Collection<ConfigAttribute>>();

        List<Authority> authorities = authorityMapper.getAll();
        for (Authority authority : authorities) {
            ConfigAttribute configAttribute = new SecurityConfig(authority.getAuthorityName());
            List<ConfigAttribute> list = new ArrayList<>();
            list.add(configAttribute);
            map.put(authority.getUri(), list);
        }

    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (map==null){
            loadResourceDefine();
        }
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : map.entrySet()) {
            String url = entry.getKey();
            if (new AntPathRequestMatcher(url).matches(request)) {
                return map.get(url);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
