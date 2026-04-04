package imt.fisa.invocations.services;

import imt.fisa.invocations.controllers.httpdto.CreationMonstreReponse;
import imt.fisa.invocations.controllers.httpdto.PlayerProfileResponse;
import imt.fisa.invocations.exceptions.BadRequestException;
import imt.fisa.invocations.exceptions.InternalServerErrorException;
import imt.fisa.invocations.exceptions.UnauthorizedException;
import imt.fisa.invocations.persistence.dto.MonsterTemplateEntity;
import imt.fisa.invocations.persistence.repositories.MonsterTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class InvocationService {


    private final RestTemplate restTemplate;


    @Value("${auth.internal.secret}")
    private String internalSecret;

    @Value("${auth.hostname}") String authHostname;
    @Value("${auth.port}") String authPort;

    @Value("${monstres.hostname}") String monstresHostname;
    @Value("${monstres.port}") String monstresPort;

    @Value("${joueur.hostname}") String joueurHostname;
    @Value("${joueur.port}")     String joueurPort;



    @Autowired
    private MonsterTemplateRepository monsterTemplateRepository;

    public InvocationService() {
        this.restTemplate = new RestTemplate();
    }


    /**
     * Sélectionne un monstre aléatoirement en fonction de leur drop rate.
     * @return le monstre (template) sélectionné
     */
    public MonsterTemplateEntity pickMonster(){
        List<MonsterTemplateEntity> monsterTemplates = monsterTemplateRepository.findAll();

        Double summedDropProbas = 0d;
        for (MonsterTemplateEntity template : monsterTemplates ){
            summedDropProbas += template.getLootRate();
        }

        Double seuil = Math.random() * summedDropProbas;

        Double cumulativeLootRate = 0d;

        for (MonsterTemplateEntity template : monsterTemplates ){
            cumulativeLootRate += template.getLootRate();
            if(seuil <= cumulativeLootRate){
                return template;
            }
        }

        throw new InternalServerErrorException("Aucun monstre n'a été sélectionné, cela ne devrait jamais arriver. Vérifiez les données en base de données.");
    }


    public String ajouteMonstreAuJoueur(String idJoueur, MonsterTemplateEntity monstre){
        if( compteEmplacementsMonstresLibres(idJoueur) < 1 ){
            throw new UnauthorizedException("Le joueur n'a plus d'emplacement libre pour un nouveau monstre.");
        }

        String idMonstre = this.saveMonstre(monstre);

        ajouterIdMonstreAuJoueur(idJoueur, idMonstre);

        return idMonstre;
    }



    private int compteEmplacementsMonstresLibres(String idJoueur) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-INTERNAL-API-KEY", internalSecret);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<PlayerProfileResponse> response = restTemplate.exchange(
                    "http://" + joueurHostname + ":" + joueurPort + "/profile?identifiant=" + idJoueur,
                    HttpMethod.GET,
                    requestEntity,
                    PlayerProfileResponse.class
            );
            PlayerProfileResponse profile = response.getBody();

            return 10 + profile.getLevel() - profile.getMonstres().size();
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new UnauthorizedException("Clé interne invalide.");
        } catch (NullPointerException e) {
            throw new InternalServerErrorException("Impossible de récupérer le profil joueur.");
        }
    }




    public String saveMonstre(MonsterTemplateEntity monstre) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-INTERNAL-API-KEY", internalSecret);

        HttpEntity<MonsterTemplateEntity> requestEntity = new HttpEntity<MonsterTemplateEntity>(monstre, headers);

        try{
            ResponseEntity<CreationMonstreReponse> response =
                    restTemplate.exchange("http://" + monstresHostname + ":" + monstresPort + "/ajoute-monstre",
                            HttpMethod.POST,
                            requestEntity,
                            CreationMonstreReponse.class
                    );
            System.out.println(response.toString());

            return response.getBody().getId();

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new UnauthorizedException("Clé interne invalide.");
        } catch (NullPointerException e) {
            throw new InternalServerErrorException("Échec de la création du monstre.");
        }
    }



    private void ajouterIdMonstreAuJoueur(String idJoueur, String monstreId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-INTERNAL-API-KEY", internalSecret);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                    "http://" + joueurHostname + ":" + joueurPort
                            + "/ajout-monstre?idMonstre=" + monstreId + "&idJoueur=" + idJoueur,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new UnauthorizedException("Clé interne invalide.");
        } catch (HttpClientErrorException.BadRequest e) {
            throw new BadRequestException("Le joueur ne peut plus recevoir de monstre.");
        } catch (NullPointerException e) {
            throw new InternalServerErrorException("Échec de l'ajout du monstre au joueur.");
        }
    }

}
