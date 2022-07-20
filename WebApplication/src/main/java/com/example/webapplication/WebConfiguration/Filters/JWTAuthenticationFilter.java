package com.example.webapplication.WebConfiguration.Filters;
import com.auth0.jwt.JWT;
import com.example.webapplication.WebConfiguration.AuthenticatedUser;
import com.example.webapplication.WebConfiguration.UserDetailsServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.webapplication.WebConfiguration.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImp userDetailsService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            com.example.webapplication.User.User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), com.example.webapplication.User.User.class);

            AuthenticatedUser authenticatedUser = (AuthenticatedUser) userDetailsService.loadUserByUsername(creds.getUsername());

           /* return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword())
            );*/
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticatedUser,null,
                            authenticatedUser.getAuthorities())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
