package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.GameplayPropertyDto;
import dm.dracolich.mtgLibrary.web.entity.GameplayPropertyEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface GameplayPropertyMapper {
    GameplayPropertyDto entityToDto(GameplayPropertyEntity entity);
    GameplayPropertyEntity dtoToEntity(GameplayPropertyDto dto);
}
