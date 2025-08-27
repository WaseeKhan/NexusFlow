package com.nexusflow.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

                // get  data from google/github
            DefaultOAuth2User user =  (DefaultOAuth2User)authentication.getPrincipal();

            logger.info(user.getName());

            user.getAttributes().forEach((key, value)->{
                logger.info("{} : {}", key,value);
            });

            logger.info(user.getAuthorities().toString());
            logger.info("OAuthAuthenticationSucessHandler");
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
            

            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

  

    

}
