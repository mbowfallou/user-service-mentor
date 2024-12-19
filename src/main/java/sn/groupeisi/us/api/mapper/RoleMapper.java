package sn.groupeisi.us.api.mapper;

import org.mapstruct.Mapper;
import sn.groupeisi.us.api.dto.RoleDto;
import sn.groupeisi.us.api.entity.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {

//    @Mapping(source = "user.id", target = "utilisateurId")
    RoleDto toDto(RoleEntity roleEntity);

//    @Mapping(target = "user", ignore = true)
    RoleEntity toEntity(RoleDto roleDto);
}