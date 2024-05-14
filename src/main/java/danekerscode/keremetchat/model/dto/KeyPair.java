package danekerscode.keremetchat.model.dto;

public record KeyPair<T>(
        String key,
        T value
) {
    public static <T> KeyPair<T> of(String key, T value) {
        return new KeyPair<>(key, value);
    }
}
