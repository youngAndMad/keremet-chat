package danekerscode.keremetchat.model.dto;

public record KeyPair<T>(
        String key,
        T value
) {
}
