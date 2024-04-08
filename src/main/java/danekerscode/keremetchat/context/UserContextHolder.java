package danekerscode.keremetchat.context;

import danekerscode.keremetchat.model.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserContextHolder {

    private final ThreadLocal<User> userContext = new ThreadLocal<>();

    public static void setContext(User context) {
        userContext.set(context);
    }

    public static User getContext() {
        return userContext.get();
    }

    public static void clear() {
        userContext.remove();
    }

    public static boolean isEmpty() {
        return userContext.get() == null;
    }
}
