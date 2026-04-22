package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.CollectionSetDto;
import dm.dracolich.mtgLibrary.web.entity.CollectionSetEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface CollectionSetMapper {
    CollectionSetDto entityToDto(CollectionSetEntity entity);
    CollectionSetEntity dtoToEntity(CollectionSetDto dto);
}
