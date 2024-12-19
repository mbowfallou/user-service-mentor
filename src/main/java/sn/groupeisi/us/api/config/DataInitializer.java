package sn.groupeisi.us.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.groupeisi.us.api.entity.DomaineEntity;
import sn.groupeisi.us.api.entity.FiliereEntity;
import sn.groupeisi.us.api.repository.DomaineRepository;
import sn.groupeisi.us.api.repository.FiliereRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FiliereRepository filiereRepository;
    private final DomaineRepository domaineRepository;

    public DataInitializer(FiliereRepository filiereRepository, DomaineRepository domaineRepository) {
        this.filiereRepository = filiereRepository;
        this.domaineRepository = domaineRepository;
    }

    @Override
    public void run(String... args) {
        // Créer les filières
        FiliereEntity genieLogiciel = findOrCreateFiliere("Génie Logiciel");
        FiliereEntity reseauxTelecom = findOrCreateFiliere("Réseaux et Télécommunications");
        FiliereEntity iaBigData = findOrCreateFiliere("Intelligence Artificielle et Big Data");

        // Ajouter les domaines aux filières correspondantes
        findOrCreateDomaine("Développement Web", "Génie Logiciel");
        findOrCreateDomaine("Développement Mobile", "Génie Logiciel");
        findOrCreateDomaine("DevOps", "Génie Logiciel");

        findOrCreateDomaine("Administration Systèmes", "Réseaux et Télécommunications");
        findOrCreateDomaine("Sécurité Informatique", "Réseaux et Télécommunications");
        findOrCreateDomaine("Cloud Computing", "Réseaux et Télécommunications");

        findOrCreateDomaine("Machine Learning", "Intelligence Artificielle et Big Data");
        findOrCreateDomaine("Analyse de Données", "Intelligence Artificielle et Big Data");
        findOrCreateDomaine("Vision par Ordinateur", "Intelligence Artificielle et Big Data");

        System.out.println("\n\tFilières et domaines insérés ou mis à jour avec succès!\n");
    }

    private DomaineEntity findOrCreateDomaine(String nom, String filiere) {
        // Vérifier si la filière existe
        FiliereEntity f = filiereRepository.findByNom(filiere)
                .orElseThrow(() -> new IllegalArgumentException("Filière introuvable : " + filiere));

        // Vérifier si le domaine existe déjà pour cette filière
        return domaineRepository.findByNomAndFiliere(nom, f)
                .orElseGet(() -> {
                    DomaineEntity domaine = new DomaineEntity();
                    domaine.setNom(nom);
                    domaine.setFiliere(f);
                    return domaineRepository.save(domaine);
                });
    }

    private FiliereEntity  findOrCreateFiliere(String nom) {
        return filiereRepository.findByNom(nom)
                .orElseGet(() -> {
                    FiliereEntity newFiliere = new FiliereEntity();
                    newFiliere.setNom(nom);
                    return filiereRepository.save(newFiliere);
                });
    }
}