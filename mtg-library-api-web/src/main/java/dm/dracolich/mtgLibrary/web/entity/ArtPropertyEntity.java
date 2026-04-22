package dm.dracolich.mtgLibrary.web.entity;

import dm.dracolich.mtgLibrary.dto.enums.BorderColor;
import dm.dracolich.mtgLibrary.dto.enums.CardFinish;
import dm.dracolich.mtgLibrary.dto.enums.Frame;
import dm.dracolich.mtgLibrary.dto.enums.Game;
import dm.dracolich.mtgLibrary.dto.enums.Language;
import dm.dracolich.mtgLibrary.dto.enums.Rarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Document(collection = "art_properties")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtPropertyEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    @Field("scryfall_id")
    private String scryfallId;
    @Indexed
    @Field("card_id")
    private String cardId;
    @Field("set_id")
    private String setId;
    @Field("artist_ids")
    private Set<String> artistIds;
    @Field("variation_of")
    private String variationOf;
    @Field("back_card_art_id")
    private String backCardArtId;
    @Field("release_date")
    private LocalDate releaseDate;
    private Rarity rarity;
    @Field("hi_res_image")
    private Boolean hiResImage;
    @Field("image_uris")
    private Map<String, String> imageUris;
    private Map<String, Double> prices;
    @Field("purchase_uris")
    private Map<String, String> purchaseUris;
    private Boolean booster;
    @Field("collector_number")
    private String collectorNumber;
    @Field("content_warning")
    private Boolean contentWarning;
    @Field("digital_only")
    private Boolean digitalOnly;
    private Set<CardFinish> finishes;
    @Field("flavor_name")
    private String flavorName;
    @Field("flavor_text")
    private String flavorText;
    private Set<Game> games;
    private Language lang;
    @Field("border_color")
    private BorderColor borderColor;
    private Frame frame;
    // References to FrameEffectEntity.id (e.g. "showcase", "extendedart", "spree").
    @Field("frame_effects")
    private Set<String> frameEffects;
    // Scryfall's promo_types vocabulary is open-ended and churns, so keep as free-form strings.
    @Field("promo_types")
    private Set<String> promoTypes;
    @Field("full_art")
    private Boolean fullArt;
    private Boolean textless;
    private Boolean variation;
    private String watermark;
    @Field("illustration_id")
    private String illustrationId;
}
