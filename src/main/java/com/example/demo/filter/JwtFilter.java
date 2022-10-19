package com.example.demo.filter;

import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader=request.getHeader("Authorization");
        String token = null;
        String userName = null;

        //Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTcHJpbmdTZWN1cml0eSIsImV4cCI6MTY2NjE4ODc2MywiaWF0IjoxNjY2MTg1MTYzfQ.kvScL8wyDycczQA38ehuoEPekUjstk8uH6vuJDG7wW8
        if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")){
            token=authorizationHeader.substring(7);
            userName=jwtUtil.extractUsername(token);
        }

        if(userName !=null && SecurityContextHolder.getContext().getAuthentication()==null){
           UserDetails userDetails= service.loadUserByUsername(userName);

           if(jwtUtil.validateToken(token,userDetails)){
               UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                       new UsernamePasswordAuthenticationToken(
                               userName,null,userDetails.getAuthorities());
               usernamePasswordAuthenticationToken.setDetails(
                       new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
           }
        }
        filterChain.doFilter(request,response);

    }

}
