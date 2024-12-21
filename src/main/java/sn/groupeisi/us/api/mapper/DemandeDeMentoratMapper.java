package sn.groupeisi.us.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sn.groupeisi.us.api.dto.DemandeDeMentoratDto;
import sn.groupeisi.us.api.entity.DemandeDeMentoratEntity;
import sn.groupeisi.us.api.entity.DomaineEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DemandeDeMentoratMapper {

    @Mapping(target = "etudiantId", source = "etudiant.id")
    @Mapping(target = "etudiantNom", expression = "java(demande.getEtudiant().getPrenom() + \" \" + demande.getEtudiant().getNom())")
    @Mapping(target = "mentorId", source = "mentor.id")
    @Mapping(target = "mentorNom", expression = "java(demande.getMentor().getPrenom() + \" \" + demande.getMentor().getNom())")
    @Mapping(target = "domaineIds", source = "domaines", qualifiedByName = "extractDomaineIds")
    @Mapping(target = "domaineNoms", source = "domaines", qualifiedByName = "extractDomaineNoms")
    DemandeDeMentoratDto toDto(DemandeDeMentoratEntity demande);

    @Named("extractDomaineIds")
    default Set<Long> extractDomaineIds(Set<DomaineEntity> domaines) {
        return domaines.stream().map(DomaineEntity::getId).collect(Collectors.toSet());
    }

    @Named("extractDomaineNoms")
    default Set<String> extractDomaineNoms(Set<DomaineEntity> domaines) {
        return domaines.stream().map(DomaineEntity::getNom).collect(Collectors.toSet());
    }
}