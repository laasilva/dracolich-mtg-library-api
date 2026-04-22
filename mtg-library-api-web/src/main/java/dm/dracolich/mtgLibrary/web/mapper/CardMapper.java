package dm.dracolich.mtgLibrary.web.mapper;

import dm.dracolich.mtgLibrary.dto.ArtPropertyDto;
import dm.dracolich.mtgLibrary.dto.CardDto;
import dm.dracolich.mtgLibrary.dto.records.CardResumedRecord;
import dm.dracolich.mtgLibrary.web.entity.ArtPropertyEntity;
import dm.dracolich.mtgLibrary.web.entity.CardEntity;
import dm.dracolich.mtgLibrary.web.entity.GameplayPropertyEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD,
        uses = { CardFaceMapper.class, RelatedCardPartMapper.class })
public interface CardMapper {

    // defaultArt is populated service-side from a separate ArtPropertyEntity query.
    @Mapping(target = "defaultArt", ignore = true)
    CardDto entityToDto(CardEntity entity);

    @Mapping(target = "allParts", ignore = true)
    @Mapping(target = "defaultFace", ignore = true)
    @Mapping(target = "flippedFace", ignore = true)
    CardEntity dtoToEntity(CardDto dto);

    // Convenience overload: service passes the already-mapped ArtPropertyDto
    // (chosen by cheapest-price rule or explicit user selection).
    default CardDto entityToDtoWithArt(CardEntity card, ArtPropertyDto chosenArt) {
        CardDto dto = entityToDto(card);
        if (dto != null) {
            dto.setDefaultArt(chosenArt);
        }
        return dto;
    }

    // CardResumedRecord joins fields from CardEntity, its default face, and one chosen
    // ArtPropertyEntity. Set code is passed separately because ArtPropertyEntity only
    // holds setId — the service resolves it to the human-readable code.
    default CardResumedRecord toResumedRecord(CardEntity card, ArtPropertyEntity chosenArt, String setCode) {
        if (card == null) {
            return null;
        }
        var face = card.getDefaultFace();
        GameplayPropertyEntity gp = face != null ? face.getGameplayProperty() : null;
        String imageUri = null;
        if (chosenArt != null && chosenArt.getImageUris() != null) {
            // Prefer "normal" size; fall back to any available URI.
            imageUri = chosenArt.getImageUris().getOrDefault(
                    "normal",
                    chosenArt.getImageUris().values().stream().findFirst().orElse(null)
            );
        }
        return new CardResumedRecord(
                card.getId(),
                card.getOracleId(),
                card.getName(),
                face != null ? face.getFullType() : null,
                gp != null ? gp.getManaCost() : null,
                gp != null ? gp.getManaValue() : null,
                gp != null ? gp.getColors() : null,
                gp != null ? gp.getColorIdentity() : null,
                chosenArt != null ? chosenArt.getRarity() : null,
                setCode,
                imageUri
        );
    }
}
