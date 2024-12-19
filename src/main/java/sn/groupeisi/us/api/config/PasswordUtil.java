package sn.groupeisi.us.api.config;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean matches(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
