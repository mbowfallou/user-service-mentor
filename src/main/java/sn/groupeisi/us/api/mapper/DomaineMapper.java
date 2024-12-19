package sn.groupeisi.us.api.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.groupeisi.us.api.dto.DomaineDto;
import sn.groupeisi.us.api.entity.DomaineEntity;

@Mapper(componentModel = "spring")
public interface DomaineMapper {
    @Mapping(source = "filiere.nom", target = "filiereNom")
    DomaineDto toDto(DomaineEntity domaineEntity);

    @Mapping(target = "filiere", ignore = true)
    DomaineEntity toEntity(DomaineDto domaineDto);
}
