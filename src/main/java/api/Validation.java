package api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Validation {
    public static boolean validateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null;
    }
    public static boolean validateUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return false;
    }
}
