package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.RelatedCardPartDto;
import dm.dracolich.mtgLibrary.web.entity.RelatedCardPartEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface RelatedCardPartMapper {
    RelatedCardPartDto entityToDto(RelatedCardPartEntity entity);
    RelatedCardPartEntity dtoToEntity(RelatedCardPartDto dto);
}
