package br.edu.ifpb.pweb2.boletimDigital.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public abstract class PasswordUtil {

    public static String criptografaSenha(String senha){
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public static Boolean verificaSenha(String senha, String senhaHash){
        if (BCrypt.checkpw(senha, senhaHash)){
            return true;
        } else {
            return false;
        }
    }
}
