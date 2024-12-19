package sn.groupeisi.us.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.groupeisi.us.api.dto.ExperienceDto;
import sn.groupeisi.us.api.entity.ExperienceEntity;

@Mapper(componentModel = "spring")
public interface ExperienceMapper {

    @Mapping(source = "user.id", target = "utilisateurId")
    ExperienceDto toDto(ExperienceEntity experience);

    @Mapping(target = "user", ignore = true)
    ExperienceEntity toEntity(ExperienceDto experienceDto);
}