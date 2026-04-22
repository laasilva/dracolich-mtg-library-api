package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.GameFormatDto;
import dm.dracolich.mtgLibrary.web.entity.GameFormatEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface GameFormatMapper {
    GameFormatDto entityToDto(GameFormatEntity entity);
    GameFormatEntity dtoToEntity(GameFormatDto dto);
}
