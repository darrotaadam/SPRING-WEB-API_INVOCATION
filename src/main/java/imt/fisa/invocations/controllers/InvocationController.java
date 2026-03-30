package imt.fisa.invocations.controllers;

import imt.fisa.invocations.controllers.httpdto.InvocationResponse;
import imt.fisa.invocations.persistence.dto.MonsterTemplateEntity;
import imt.fisa.invocations.services.AuthService;
import imt.fisa.invocations.services.InvocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvocationController {

    private final AuthService authService;
    private final InvocationService invocationService;

    public InvocationController(AuthService authService, InvocationService invocationService) {
        this.authService = authService;
        this.invocationService = invocationService;
        System.out.println("[*] InvocationController");
    }

    @PostMapping("/invoke")
    public ResponseEntity<InvocationResponse> invoke(@RequestHeader("Authorization") String token) {
        System.out.println("[*] /invoke");
        String identifiant = authService.getUserFromToken(token);
        MonsterTemplateEntity pickedMonster = invocationService.pickMonster();
        String monstreId = invocationService.ajouteMonstreAuJoueur(identifiant, pickedMonster);
        return ResponseEntity.ok(new InvocationResponse(monstreId));
    }
}
