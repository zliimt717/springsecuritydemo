package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = userRepository.findByUserName(username);

        /*UserDetails userDetails= User.withUserDetails(customer.getUserName(),customer.getPassword()).build();
        return userDetails;*/
        return new org.springframework.security.core.userdetails.User(
                customer.getUserName(), customer.getPassword(), new ArrayList<>());
    }

}
