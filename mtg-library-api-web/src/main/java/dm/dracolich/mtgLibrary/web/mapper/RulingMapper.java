package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.RulingDto;
import dm.dracolich.mtgLibrary.web.entity.RulingEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface RulingMapper {
    RulingDto entityToDto(RulingEntity entity);
    RulingEntity dtoToEntity(RulingDto dto);
}
