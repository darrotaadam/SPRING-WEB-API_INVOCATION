package imt.fisa.invocations.services;

import imt.fisa.invocations.controllers.httpdto.AuthResponse;
import imt.fisa.invocations.exceptions.InternalServerErrorException;
import imt.fisa.invocations.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Value("${auth.hostname}") String authHostname;
    @Value("${auth.port}") String authPort;

    private final RestTemplate restTemplate;

    public AuthService(){
        this.restTemplate = new RestTemplate();
    }


    public String getUserFromToken(String authorizationHeader) {
        System.out.println("[*] AuthService::getUserFromToken called with token: " + authorizationHeader);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try{
            ResponseEntity<AuthResponse> response =
                    restTemplate.exchange("http://" + authHostname + ":" + authPort + "/authorize",
                            HttpMethod.POST,
                            requestEntity,
                            AuthResponse.class
                    );
            System.out.println(response.toString());
                return response.getBody().getIdentifiant();
        }catch(HttpClientErrorException.Unauthorized e){
            System.out.println(e.toString());
            throw new UnauthorizedException("Invalid or expired token");
        }catch(NullPointerException e){
            throw new InternalServerErrorException("Failed granting authorization for request.");
        }
    }


}
