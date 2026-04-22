package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.SubtypeDto;
import dm.dracolich.mtgLibrary.web.entity.SubtypeEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface SubtypeMapper {
    SubtypeDto entityToDto(SubtypeEntity entity);
    SubtypeEntity dtoToEntity(SubtypeDto dto);
}
