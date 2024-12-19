package sn.groupeisi.us.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sn.groupeisi.us.api.dto.UserDto;
import sn.groupeisi.us.api.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "role.nom", target = "roleNom")
    @Mapping(source = "statut", target = "statut", qualifiedByName = "statutToString")
    UserDto toDto(UserEntity userEntity);

    @Mapping(target = "role", ignore = true) // Gestion du rôle dans le service
    @Mapping(source = "statut", target = "statut", qualifiedByName = "stringToStatut")
    UserEntity toEntity(UserDto userDto);

    @Named("statutToString")
    default String statutToString(UserEntity.StatutUser statut) {
        return statut != null ? statut.name() : null;
    }

    @Named("stringToStatut")
    default UserEntity.StatutUser stringToStatut(String statut) {
        return statut != null ? UserEntity.StatutUser.valueOf(statut) : null;
    }
}
