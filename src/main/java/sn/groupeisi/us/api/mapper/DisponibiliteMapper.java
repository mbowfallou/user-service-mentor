package sn.groupeisi.us.api.mapper;
import org.mapstruct.Mapper;
import sn.groupeisi.us.api.dto.DisponibiliteDto;
import sn.groupeisi.us.api.entity.DisponibiliteEntity;

@Mapper(componentModel = "spring")
public interface DisponibiliteMapper {
    DisponibiliteDto toDto(DisponibiliteEntity disponibiliteEntity);

    DisponibiliteEntity toEntity(DisponibiliteDto disponibiliteDTO);
}
