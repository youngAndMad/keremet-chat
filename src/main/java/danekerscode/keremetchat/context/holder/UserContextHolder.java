package danekerscode.keremetchat.context.holder;

import danekerscode.keremetchat.model.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserContextHolder {

    private final static ThreadLocal<User> userContext = new ThreadLocal<>();

    public static void set(User context) {
        userContext.set(context);
    }

    public static User get() {
        return userContext.get();
    }

    public static void clear() {
        userContext.remove();
    }

    public static boolean isEmpty() {
        return userContext.get() == null;
    }

    public static boolean isPresent(){
        return !isEmpty();
    }
}
