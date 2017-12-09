package com.ddshka.service.impl;

import com.ddshka.dao.UserDao;
import com.ddshka.model.Role;
import com.ddshka.model.User;
import com.ddshka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        User user = userService.get(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " does not exists");
        }
        Role role = user.getRole();

        return buildUser(user, role);
    }

    private com.ddshka.model.UserDetails buildUser(User user, Role role) {
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));

        // TODO: may be it will be fixed it in future...

        return new com.ddshka.model.UserDetails(
                user.getName(),
                user.getPassword(),
                authorities,
                true,
                true,
                true,
                true
        );
    }
}
