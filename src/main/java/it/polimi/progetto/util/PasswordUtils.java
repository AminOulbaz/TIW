package it.polimi.progetto.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // 12 = cost factor
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static void main(String[] args) {
        String password = "test";
        String hash = hashPassword(password);

        System.out.println("Hash: " + hash);
        System.out.println("Verifica: " + checkPassword("test", hash)); // true
        System.out.println("Verifica: " + checkPassword("altraPassword", hash));  // false
    }
}

