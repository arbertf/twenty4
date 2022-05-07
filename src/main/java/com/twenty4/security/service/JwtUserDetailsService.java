package com.twenty4.security.service;

import com.twenty4.entity.Role;
import com.twenty4.entity.User;
import com.twenty4.enums.Status;
import com.twenty4.repository.UserRepository;
import com.twenty4.security.model.ApplicationUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).get();

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        }

        List<Role> roles = new ArrayList<>();
        Boolean isEnable;
        if(user.getStatus().equals(Status.ACTIVE)){
            isEnable=Boolean.TRUE;
        }else{
            isEnable=Boolean.FALSE;
        }
        roles.add(user.getRole());

        return new ApplicationUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                roles.stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList()),
                isEnable
        );
    }

}