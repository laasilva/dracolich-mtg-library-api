package dm.dracolich.mtgLibrary.dto.enums;

public enum Color {
    W("WHITE", "PLAINS"),
    U("BLUE", "ISLANDS"),
    B("BLACK", "SWAMP"),
    R("RED", "MOUNTAIN"),
    G("GREEN", "FOREST"),
    C("COLORLESS", "WASTES");

    private String value;
    private String land;

    private Color(String value, String land) {
        this.value = value;
        this.land = land;
    }
}
