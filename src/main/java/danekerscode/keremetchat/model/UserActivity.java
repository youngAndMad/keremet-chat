package danekerscode.keremetchat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivity implements Serializable {
    private boolean isOnline;
    private LocalDateTime lastActive;
    private Long userId;


    public static UserActivity defaultUserActivityForUserId(@NonNull final Long userId) {
        return new UserActivity(false, null, userId);
    }

    public UserActivity withLastActive(@NonNull final LocalDateTime lastActive) {
        this.setLastActive(lastActive);
        return this;
    }

    public UserActivity withOnline(@NonNull final boolean isOnline) {
        this.setOnline(isOnline);
        return this;
    }
}
