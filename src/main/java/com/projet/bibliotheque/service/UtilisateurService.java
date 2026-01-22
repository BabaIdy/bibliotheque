package com.projet.bibliotheque.service;
import com.projet.bibliotheque.config.ApplicationProperties;
import com.projet.bibliotheque.domain.Utilisateur;
import com.projet.bibliotheque.domain.enumeration.RoleUtilisateur;
import com.projet.bibliotheque.repository.UtilisateurRepository;
import com.projet.bibliotheque.service.dto.UtilisateurRequest;
import com.projet.bibliotheque.service.dto.UtilisateurResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class UtilisateurService {
    private final ApplicationProperties applicationProperties;
    private final RestTemplate restTemplate;
    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(
        ApplicationProperties applicationProperties,
        RestTemplate restTemplate,
        UtilisateurRepository utilisateurRepository
    ) {
        this.applicationProperties = applicationProperties;
        this.restTemplate = restTemplate;
        this.utilisateurRepository = utilisateurRepository;
    }

    private String authentification() {
        String tokenUrl = applicationProperties.getKcurl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", applicationProperties.getClientId());
        map.add("client_secret", applicationProperties.getClientSecret());
        map.add("username", applicationProperties.getUserName());
        map.add("password", applicationProperties.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        } else {
            throw new RuntimeException("Échec lors de l'authentification à Keycloak");
        }
    }
    public Utilisateur creerUtilisateur(UtilisateurRequest dto) {

        // Vérifier si l’utilisateur existe localement
        if (utilisateurRepository.existsByEmail(dto.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("Utilisateur déjà existant pour cet email");
        }

        // Authentification Keycloak
        String token = authentification();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        // Préparer les données pour Keycloak
        Map<String, Object> data = new HashMap<>();
        data.put("username", dto.getEmail());
        data.put("enabled", true);

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", "MotDePassebibliotheque123");
        credentials.put("temporary", false);
        data.put("credentials", List.of(credentials));

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("nomComplet", List.of(dto.getPrenom() + " " + dto.getNom()));
        data.put("attributes", attributes);

        HttpEntity<Map<String, Object>> requestKeycloak = new HttpEntity<>(data, headers);
        String urlCreation = applicationProperties.getCreateUser();
        ResponseEntity<String> responseKeycloak = restTemplate.postForEntity(urlCreation, requestKeycloak, String.class);

        if (!responseKeycloak.getStatusCode().equals(HttpStatus.CREATED)) {
            throw new RuntimeException("Échec lors de la création dans Keycloak");
        }

        // Créer l’utilisateur localement
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail().toLowerCase());
        utilisateur.setRole(RoleUtilisateur.valueOf(dto.getRole().toUpperCase()));
        utilisateur.setActif(true);

        return utilisateurRepository.save(utilisateur);
    }
    @Transactional(readOnly = true)
    public UtilisateurResponse obtenirUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        return mapToResponse(utilisateur);
    }
    @Transactional(readOnly = true)
    public List<UtilisateurResponse> obtenirTousLesUtilisateurs() {
        return utilisateurRepository.findAllByOrderByDateCreationDesc().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    private UtilisateurResponse mapToResponse(Utilisateur utilisateur) {
        UtilisateurResponse response = new UtilisateurResponse();
        response.setId(utilisateur.getId());
        response.setNom(utilisateur.getNom());
        response.setPrenom(utilisateur.getPrenom());
        response.setNomComplet(utilisateur.getNomComplet());
        response.setEmail(utilisateur.getEmail());
        response.setRole(utilisateur.getRole().name());
        response.setActif(utilisateur.getActif());
        response.setNombreEmpruntsActifs(utilisateur.getNombreEmpruntsActifs());
        response.setPeutEmprunter(utilisateur.peutEmprunter());
        response.setDateCreation(utilisateur.getDateCreation());
        return response;
    }
}
