package com.example.funnychat.util;

import org.apache.commons.codec.digest.Crypt;

public class Hash {

    public static String getHashedString(String s) {
        return Crypt.crypt(s);
    }

    public static boolean checkHashedString(String s, String hashedString){
        String tmpHashedStrinf= Crypt.crypt(s,hashedString);

        return tmpHashedStrinf.equalsIgnoreCase(hashedString);
    }
}
