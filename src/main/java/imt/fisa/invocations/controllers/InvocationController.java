package imt.fisa.invocations.controllers;

import imt.fisa.invocations.controllers.httpdto.InvocationResponse;
import imt.fisa.invocations.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController


public class InvocationController {

    private final AuthService authService;
    public InvocationController(AuthService authService) {
        this.authService = authService;
        System.out.println("[*] InvocationController");
    }


    @PostMapping("/invoke")
    public ResponseEntity<InvocationResponse> invoke(@RequestHeader("Authorization") String token) {
        System.out.println("[*] /xp-progress");
        String identifiant = authService.getUserFromToken(token);

    }


}
