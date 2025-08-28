package com.nexusflow.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.nexusflow.entities.Providers;
import com.nexusflow.entities.User;
import com.nexusflow.helpers.AppConstant;
import com.nexusflow.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSucessHandler implements AuthenticationSuccessHandler{

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSucessHandler.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

            logger.info("OAuthAuthenticationSucessHandler");

            var oAuth2User  =  (DefaultOAuth2User)authentication.getPrincipal();
            oAuth2User.getAttributes().forEach((key, value)->{
                logger.info("{} : {}", key,value);
            });
            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setRoleList(List.of(AppConstant.ROLE_USER));
            user.setEnabled(true);
            user.setEmailVerified(true);
            user.setPassword("dont_know");

            //identity of the provider
          var OAuth2AuthenticationToken =  (OAuth2AuthenticationToken)authentication;
          String authorizedClientRegistrationId = OAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
          logger.info("Authorized Client Registration Id : {}", authorizedClientRegistrationId);
            

          if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            user.setEmail(oAuth2User.getAttribute("email").toString());
            user.setProfilePic(oAuth2User.getAttribute("picture").toString());
            user.setName(oAuth2User.getAttribute("name").toString());
            user.setProviderUserId(oAuth2User.getName());
            user.setProvider(Providers.GOOGLE);
            user.setAbout("This account is created from Google");
           
            
          }else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
            String email = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString() : oAuth2User.getAttribute("login").toString() + "@nexusflow.in";
            String picture = oAuth2User.getAttribute("avatar_url").toString();
            String name = oAuth2User.getAttribute("login").toString();
            String providerUserId = oAuth2User.getName();
            
            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProviderUserId(providerUserId); 
            user.setProvider(Providers.GITHUB);
            user.setAbout("This account is created from GitHub");

            
          } else {
            logger.error("Login with {} is not supported", authorizedClientRegistrationId);
            
          }
                

/*

                // get  data from google/github
            DefaultOAuth2User user =  (DefaultOAuth2User)authentication.getPrincipal();

            logger.info(user.getName());

            user.getAttributes().forEach((key, value)->{
                logger.info("{} : {}", key,value);
            });

            logger.info(user.getAuthorities().toString());
           
            String email = user.getAttribute("email").toString();
            String name = user.getAttribute("name").toString();
            String picture = user.getAttribute("picture").toString();
            
            User user1 = new User();

            user1.setName(name);
            user1.setEmail(email);
            user1.setProfilePic(picture);
            user1.setPassword("don't_know");
            user1.setUserId(UUID.randomUUID().toString());
            user1.setProvider(Providers.GOOGLE);
            user1.setEnabled(true);
            user1.setEmailVerified(true);
            user1.setProviderUserId(user.getName());
            user1.setRoleList(List.of(AppConstant.ROLE_USER));
            user1.setAbout("This account is created from Google");

            User user2 = userRepository.findByEmail(email).orElse(null);

            if (user2 ==null) {
                userRepository.save(user1);
                logger.info("User saved from google login" +email);
                
            }
            

 */



            User user2 = userRepository.findByEmail(user.getEmail()).orElse(null); 
            

            if (user2 ==null) {
                userRepository.save(user);
                logger.info("User saved:" +user.getEmail());
                
            }         

            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

  

    

}
