package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.ArtPropertyDto;
import dm.dracolich.mtgLibrary.web.entity.ArtPropertyEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface ArtPropertyMapper {
    ArtPropertyDto entityToDto(ArtPropertyEntity entity);
    ArtPropertyEntity dtoToEntity(ArtPropertyDto dto);
}
