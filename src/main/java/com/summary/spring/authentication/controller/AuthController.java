package com.summary.spring.authentication.controller;

import com.summary.spring.authentication.Entity.User;
import com.summary.spring.authentication.Model.RegisterDTO;
import com.summary.spring.authentication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ConsumerTokenServices tokenService;

    @PostMapping(value = "/register")
    public User registerNewUser(@RequestBody RegisterDTO user) {
        User newUser = new User(user.getUserName(), user.getPassWord(), user.getPhoneNumber(), user.getEmail());

        String rawPassword = newUser.getPassWord();
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        newUser.setPassWord(encryptedPassword);

        return userRepository.save(newUser);
    }

    @GetMapping(value = "/user")
    public String getUsrInfo() {
        // method 1
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
        } else {
            String username = principal.toString();
        }

        // method 2
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return authentication.getName();
    }

    @GetMapping(value = "/users/{userName}")
    public User getUserByName(@PathVariable(value = "userName") String userName) {
        return userRepository.findByUserName(userName);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer")) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);

            tokenStore.removeAccessToken(accessToken);
            tokenService.revokeToken(String.valueOf(accessToken));

            return new ResponseEntity("logout successful!", HttpStatus.OK);
        }
        return new ResponseEntity("logout failed!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/oauth/tokens")
    public List<Object> getAllTokens(@RequestParam("clientId") String clientId) {
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(clientId);
        return tokens.stream()
                     .map((Function<OAuth2AccessToken, Object>) OAuth2AccessToken::getValue)
                     .collect(Collectors.toList());

    }
}
