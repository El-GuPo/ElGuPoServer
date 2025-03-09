package com.elgupo.elguposerver.hashing;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordHasher {

    public static String genSalt() {
        return BCrypt.gensalt(12);
    }

    public static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }
}
