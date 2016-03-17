package types;

/**
 * Created by javac on 11.03.16.
 */
public enum CoapHeader {
    CODE("code"),
    PATH("path"),
    SERVER_ADDRESS("server_port"),
    SOURCE_ADDRESS("source_address"),
    SOURCE_PORT("source_port"),
    TOKEN("token"),
    EXCHANGE("exchange");

    private String name;

    CoapHeader(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
