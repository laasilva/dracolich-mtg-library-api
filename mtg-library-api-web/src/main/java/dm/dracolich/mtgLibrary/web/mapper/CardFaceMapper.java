package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.CardFaceDto;
import dm.dracolich.mtgLibrary.web.entity.CardFaceEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD,
        uses = { GameplayPropertyMapper.class })
public interface CardFaceMapper {
    CardFaceDto entityToDto(CardFaceEntity entity);
    CardFaceEntity dtoToEntity(CardFaceDto dto);
}
