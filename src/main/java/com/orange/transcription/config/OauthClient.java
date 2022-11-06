package com.orange.transcription.config;

import com.orange.transcription.dtos.CheckTokenResponse;
import com.orange.transcription.dtos.UserPrincipal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Base64;

@Service
public class OauthClient {

    private Logger logger = LogManager.getLogger(OauthClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${oauth.client.id}")
    private String oauthClientId;

    @Value("${oauth.client.secret}")
    private String oauthClientSecret;

    @Value("${oauth.server.host}")
    private String oauthHost;

    @Value("${oauth.server.port}")
    private Integer oauthPort;

    @Value("${oauth.server.checkTokenUrl}")
    private String oauthCheckTokenUrl;

    @Value("${oauth.protocol}")
    private String protocol;



    public Principal getUserPrincipal(String token) {

           /* defaultTokenServices.readAccessToken(authorization);
            OAuth2AccessToken token = this.resourceServerTokenServices.readAccessToken(authorization);

            if (token == null) {
                throw new InvalidTokenException("Token was not recognised");
            } else if (token.isExpired()) {
                throw new InvalidTokenException("Token has expired");
            } else {
                OAuth2Authentication authentication = this.resourceServerTokenServices.loadAuthentication(token.getValue());
                return authentication;
            }
*/

        String appCred = oauthClientId + ":" + oauthClientSecret;
        String clientBasicAuthHeader = new String(Base64.getEncoder().encode(appCred.getBytes()));
        String[] split = token.split("\\s+");
        if (split.length > 1) token = split[1];
        String url = String.format("%s://%s:%s%s", protocol, oauthHost, oauthPort, oauthCheckTokenUrl);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("token", token);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Basic %s", clientBasicAuthHeader));
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity entity = new HttpEntity(headers);
        try {
            HttpEntity<CheckTokenResponse> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    CheckTokenResponse.class);
            CheckTokenResponse user = response.getBody();
            UserPrincipal principal = new UserPrincipal();
            principal.setName(user.getUser_name());//body.getAsString("user_name").toString());
            return principal;
        } catch (Exception e) {
            logger.error("An exception occurred!", e);
        }
        throw new RuntimeException("") ;//messageSource.getMessage("conference.id.error", null, locale));
    }

}
