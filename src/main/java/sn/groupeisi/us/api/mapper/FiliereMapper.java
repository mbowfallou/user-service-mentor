package sn.groupeisi.us.api.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.groupeisi.us.api.dto.FiliereDto;
import sn.groupeisi.us.api.entity.FiliereEntity;

@Mapper(componentModel = "spring")
public interface FiliereMapper {
    @Mapping(target = "domaines", source = "domaines")
    FiliereDto toDto(FiliereEntity filiereEntity);

    @Mapping(target = "domaines", ignore = true)
    FiliereEntity toEntity(FiliereDto filiereDto);
}
