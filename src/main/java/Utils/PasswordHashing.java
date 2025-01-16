package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashing {

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] result = md.digest();

        StringBuilder sb= new StringBuilder();
        for(byte b:result) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
