package dm.dracolich.mtgLibrary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dm.dracolich.mtgLibrary.dto.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArtPropertyDto {
    private String id;
    @JsonProperty("scryfall_id")
    private String scryfallId;
    @JsonProperty("card_id")
    private String cardId;
    @JsonProperty("set_id")
    private String setId;
    @JsonProperty("artist_ids")
    private Set<String> artistIds;
    @JsonProperty("variation_of")
    private String variationOf;
    @JsonProperty("back_card_art_id")
    private String backCardArtId;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    private Rarity rarity;
    @JsonProperty("hi_res_image")
    private Boolean hiResImage;
    @JsonProperty("image_uris")
    private Map<String, String> imageUris;
    private Map<String, Double> prices;
    @JsonProperty("purchase_uris")
    private Map<String, String> purchaseUris;
    private Boolean booster;
    @JsonProperty("collector_number")
    private String collectorNumber;
    @JsonProperty("content_warning")
    private Boolean contentWarning;
    @JsonProperty("digital_only")
    private Boolean digitalOnly;
    private Set<CardFinish> finishes;
    @JsonProperty("flavor_name")
    private String flavorName;
    @JsonProperty("flavor_text")
    private String flavorText;
    private Set<Game> games;
    private Language lang;
    @JsonProperty("border_color")
    private BorderColor borderColor;
    private Frame frame;
    @JsonProperty("frame_effects")
    private Set<String> frameEffects;
    @JsonProperty("promo_types")
    private Set<String> promoTypes;
    @JsonProperty("full_art")
    private Boolean fullArt;
    private Boolean textless;
    private Boolean variation;
    private String watermark;
    @JsonProperty("illustration_id")
    private String illustrationId;
}
