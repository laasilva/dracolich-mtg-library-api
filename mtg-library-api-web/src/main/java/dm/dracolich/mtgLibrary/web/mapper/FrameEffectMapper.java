package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.FrameEffectDto;
import dm.dracolich.mtgLibrary.web.entity.FrameEffectEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface FrameEffectMapper {
    FrameEffectDto entityToDto(FrameEffectEntity entity);
    FrameEffectEntity dtoToEntity(FrameEffectDto dto);
}
