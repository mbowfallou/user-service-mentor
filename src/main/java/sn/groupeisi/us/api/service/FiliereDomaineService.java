package sn.groupeisi.us.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.groupeisi.us.api.dto.DomaineDto;
import sn.groupeisi.us.api.dto.FiliereDto;
import sn.groupeisi.us.api.entity.DomaineEntity;
import sn.groupeisi.us.api.entity.FiliereEntity;
import sn.groupeisi.us.api.exception.EntityAlreadyExistsException;
import sn.groupeisi.us.api.exception.EntityNotFoundException;
import sn.groupeisi.us.api.mapper.DomaineMapper;
import sn.groupeisi.us.api.mapper.FiliereMapper;
import sn.groupeisi.us.api.repository.DomaineRepository;
import sn.groupeisi.us.api.repository.FiliereRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FiliereDomaineService {
    private final FiliereRepository filiereRepository;
    private final DomaineRepository domaineRepository;
    private final FiliereMapper filiereMapper;
    private final DomaineMapper domaineMapper;

    public FiliereDomaineService(FiliereRepository filiereRepository,
                                 DomaineRepository domaineRepository,
                                 FiliereMapper filiereMapper,
                                 DomaineMapper domaineMapper) {
        this.filiereRepository = filiereRepository;
        this.domaineRepository = domaineRepository;
        this.filiereMapper = filiereMapper;
        this.domaineMapper = domaineMapper;
    }

    // Gestion des filières

    public FiliereDto addFiliere(String nom) {
        filiereRepository.findByNom(nom).ifPresent(f -> {
            throw new EntityAlreadyExistsException("La filière '" + nom + "' existe déjà.");
        });

        FiliereEntity filiere = new FiliereEntity();
        filiere.setNom(nom);
        return filiereMapper.toDto(filiereRepository.save(filiere));
    }

    public FiliereDto getFiliere(Long id) {
        FiliereEntity filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec l'ID '" + id + "' introuvable."));
        return filiereMapper.toDto(filiere);
    }

    public List<FiliereDto> findAllFilieres() {
        return filiereRepository.findAll().stream()
                .map(filiereMapper::toDto)
                .collect(Collectors.toList());
    }

    public FiliereDto findFiliereByNom(String nom) {
        FiliereEntity filiere = filiereRepository.findByNom(nom)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec le nom '" + nom + "' introuvable."));
        return filiereMapper.toDto(filiere);
    }

    public void deleteFiliere(Long id) {
        FiliereEntity filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec l'ID '" + id + "' introuvable."));
        filiereRepository.delete(filiere);
    }

    // Gestion des Domaines

    public DomaineDto addDomaineToFiliere(String nomDomaine, String nomFiliere) {
        FiliereEntity filiere = filiereRepository.findByNom(nomFiliere)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec le nom '" + nomFiliere + "' introuvable."));

        domaineRepository.findByNomAndFiliere(nomDomaine, filiere).ifPresent(d -> {
            throw new EntityAlreadyExistsException("Le domaine '" + nomDomaine + "' existe déjà pour la filière '" + nomFiliere + "'.");
        });

        DomaineEntity domaine = new DomaineEntity();
        domaine.setNom(nomDomaine);
        domaine.setFiliere(filiere);
        return domaineMapper.toDto(domaineRepository.save(domaine));
    }

    // Ajouter un domaine à une filière (par ID)
    public DomaineDto addDomaine(String nom, Long filiereId) {
        FiliereEntity filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec l'ID '" + filiereId + "' introuvable."));

        domaineRepository.findByNomAndFiliere(nom, filiere).ifPresent(d -> {
            throw new EntityAlreadyExistsException("Le domaine '" + nom + "' existe déjà pour la filière '" + filiere.getNom() + "'.");
        });

        DomaineEntity domaine = new DomaineEntity();
        domaine.setNom(nom);
        domaine.setFiliere(filiere);
        return domaineMapper.toDto(domaineRepository.save(domaine));
    }

    // Gestion des domaines

    public DomaineDto getDomaine(Long id) {
        DomaineEntity domaine = domaineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domaine avec l'ID '" + id + "' introuvable."));
        //System.out.println(domaine);
        DomaineEntity de = new DomaineEntity();
        de.setId(12L);
        de.setNom("TestName");
        de.setFiliere(filiereRepository.findById(1L).get());
        System.out.println("AVANT\n");
        System.out.println(domaineMapper.toDto(de));
        System.out.println("APRES\n");
        return domaineMapper.toDto(domaine);
    }

    public List<DomaineDto> findAllDomaines() {
        return domaineRepository.findAll().stream()
                .map(domaineMapper::toDto)
                .collect(Collectors.toList());
    }

    public DomaineDto findDomaineByNomAndFiliere(String nom, String nomFiliere) {
        FiliereEntity filiere = filiereRepository.findByNom(nomFiliere)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec le nom '" + nomFiliere + "' introuvable."));

        DomaineEntity domaine = domaineRepository.findByNomAndFiliere(nom, filiere)
                .orElseThrow(() -> new EntityNotFoundException("Domaine avec le nom '" + nom + "' pour la filière '" + nomFiliere + "' introuvable."));

        return domaineMapper.toDto(domaine);
    }

    public List<DomaineDto> getDomainesByFiliere(String nomFiliere) {
        FiliereEntity filiere = filiereRepository.findByNom(nomFiliere)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec le nom '" + nomFiliere + "' introuvable."));
        return domaineRepository.findByFiliere(filiere).stream()
                .map(domaineMapper::toDto)
                .collect(Collectors.toList());
    }

    // Récupérer tous les domaines d'une filière par ID
    public List<DomaineDto> getDomainesByFiliereId(Long filiereId) {
        // Vérifiez que la filière existe
        filiereRepository.findById(filiereId)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec l'ID '" + filiereId + "' introuvable."));

        // Récupérez les domaines associés
        return domaineRepository.findByFiliere_Id(filiereId).stream()
                .map(domaineMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteDomaine(Long id) {
        DomaineEntity domaine = domaineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domaine avec l'ID '" + id + "' introuvable."));
        domaineRepository.delete(domaine);
    }








    /*private final FiliereRepository filiereRepository;
    private final DomaineRepository domaineRepository;

    public FiliereDomaineService(FiliereRepository filiereRepository, DomaineRepository domaineRepository) {
        this.filiereRepository = filiereRepository;
        this.domaineRepository = domaineRepository;
    }

    // Gestion des filières

    // Ajouter une nouvelle filière
    public FiliereEntity addFiliere(String nom) {
        filiereRepository.findByNom(nom).ifPresent(f -> {
            throw new EntityAlreadyExistsException("La filière '" + nom + "' existe déjà.");
        });

        FiliereEntity filiere = new FiliereEntity();
        filiere.setNom(nom);
        return filiereRepository.save(filiere);
    }

    // Récupérer une filière par ID
    public FiliereEntity getFiliere(Long id) {
        return filiereRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec l'ID '" + id + "' introuvable."));
    }

    // Trouver toutes les filières
    public List<FiliereEntity> findAllFilieres() {
        return filiereRepository.findAll();
    }

    // Rechercher une filière par nom
    public FiliereEntity findFiliereByNom(String nom) {
        return filiereRepository.findByNom(nom)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec le nom '" + nom + "' introuvable."));
    }

    // Supprimer une filière par ID
    public void deleteFiliere(Long id) {
        FiliereEntity filiere = getFiliere(id); // Vérifie l'existence
        filiereRepository.delete(filiere);
    }

    // Ajouter un domaine à une filière
    public DomaineEntity addDomaine(String nom, Long filiereId) {
        // Vérifier si la filière existe
        FiliereEntity filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec l'ID '" + filiereId + "' introuvable."));

        // Vérifier si le domaine existe déjà pour cette filière
        domaineRepository.findByNomAndFiliere(nom, filiere).ifPresent(d -> {
            throw new EntityAlreadyExistsException("Le domaine '" + nom + "' existe déjà pour la filière '" + filiere.getNom() + "'.");
        });

        // Ajouter le domaine
        DomaineEntity domaine = new DomaineEntity();
        domaine.setNom(nom);
        domaine.setFiliere(filiere);
        return domaineRepository.save(domaine);
    }

    // Ajouter un domaine à une filière en utilisant le nom de la filière
    public DomaineEntity addDomaineToFiliereName(String nomDomaine, String nomFiliere) {
        // Vérifier si la filière existe
        FiliereEntity filiere = filiereRepository.findByNom(nomFiliere)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec le nom '" + nomFiliere + "' introuvable."));

        // Vérifier si le domaine existe déjà pour cette filière
        domaineRepository.findByNomAndFiliere(nomDomaine, filiere).ifPresent(d -> {
            throw new EntityAlreadyExistsException("Le domaine '" + nomDomaine + "' existe déjà pour la filière '" + nomFiliere + "'.");
        });

        // Créer le domaine
        DomaineEntity domaine = new DomaineEntity();
        domaine.setNom(nomDomaine);
        domaine.setFiliere(filiere);
        return domaineRepository.save(domaine);
    }


    // Récupérer un domaine par ID
    public DomaineEntity getDomaine(Long id) {
        return domaineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domaine avec l'ID '" + id + "' introuvable."));
    }

    // Trouver tous les domaines
    public List<DomaineEntity> findAllDomaines() {
        return domaineRepository.findAll();
    }

    // Rechercher un domaine par nom suivant une Filiere
    public DomaineEntity findDomaineByNomAndFiliere(String nom, String nomFiliere) {
        FiliereEntity filiere = filiereRepository.findByNom(nomFiliere)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec le nom '" + nomFiliere + "' introuvable."));

        return domaineRepository.findByNomAndFiliere(nom, filiere)
                .orElseThrow(() -> new EntityNotFoundException("Domaine avec le nom '" + nom + "' pour la filière '" + nomFiliere + "' introuvable."));
    }

    // Rechercher un domaine par nom
    public List<DomaineEntity> findDomainesByNom(String nom) {
        List<DomaineEntity> domaines = domaineRepository.findByNom(nom);
        if (domaines.isEmpty()) {
            throw new EntityNotFoundException("Aucun domaine avec le nom '" + nom + "' n'a été trouvé.");
        }
        return domaines;
    }


    // Récupérer tous les domaines d'une filière par son nom
    public List<DomaineEntity> getDomainesByFiliereNom(String nomFiliere) {
        FiliereEntity filiere = filiereRepository.findByNom(nomFiliere)
                .orElseThrow(() -> new EntityNotFoundException("Filière avec le nom '" + nomFiliere + "' introuvable."));
        return domaineRepository.findByFiliere(filiere);
    }


    // Supprimer un domaine par ID
    public void deleteDomaine(Long id) {
        DomaineEntity domaine = getDomaine(id); // Vérifie l'existence
        domaineRepository.delete(domaine);
    }

    // Récupérer tous les domaines d'une filière
    public List<DomaineEntity> getDomainesByFiliere(Long filiereId) {
        FiliereEntity filiere = getFiliere(filiereId); // Vérifie l'existence
        return domaineRepository.findByFiliere(filiere);
    }*/
}
