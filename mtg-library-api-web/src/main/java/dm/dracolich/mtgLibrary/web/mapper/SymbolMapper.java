package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.SymbolDto;
import dm.dracolich.mtgLibrary.web.entity.SymbolEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface SymbolMapper {
    SymbolDto entityToDto(SymbolEntity entity);
    SymbolEntity dtoToEntity(SymbolDto dto);
}
