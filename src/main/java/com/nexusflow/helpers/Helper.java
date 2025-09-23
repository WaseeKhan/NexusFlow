package com.nexusflow.helpers;




import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    @Value("${server.baseUrl}")
    private String baseUrl;


    public static String getEmailOfLoggedInUser(Authentication authentication) {
        //  AuthenticatedPrincipal principal = (AuthenticatedPrincipal)authentication.getPrincipal();
        if (authentication instanceof OAuth2AuthenticationToken) {
            
            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            var oAuth2User = (OAuth2User)authentication.getPrincipal();
            String username = "";

        if (clientId.equalsIgnoreCase("google")) {
            System.out.println("Google Login");
             username = oAuth2User.getAttribute("email").toString();
                   
        } else if (clientId.equalsIgnoreCase("github")) {
            System.out.println("Github Login");
            username = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString() : oAuth2User.getAttribute("login").toString() + "@nexusflow.in";
            
        } 
            return username;
        }else {
            System.out.println("Normal Login");
            return authentication.getName();
        }
    }   

    public String getLinkForEmailVerification(String emailTocken){
        String link = this.baseUrl+"/auth/verify-email?token="+emailTocken;

        return link;

    }
}
