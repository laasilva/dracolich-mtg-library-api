package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.CardLayoutDto;
import dm.dracolich.mtgLibrary.web.entity.CardLayoutEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface CardLayoutMapper {
    CardLayoutDto entityToDto(CardLayoutEntity entity);
    CardLayoutEntity dtoToEntity(CardLayoutDto dto);
}
