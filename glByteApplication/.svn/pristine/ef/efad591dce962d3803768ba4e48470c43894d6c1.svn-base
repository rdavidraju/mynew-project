package com.nspl.app.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class IDORUtils {

    /**
     * SALT used for the generation of the HASH of the real item identifier in order to prevent to forge it on front end side.
     */
    private static final String SALT = "SomeRandomValue";

    /**
     * Compute a identifier that will be send to the front end and be used as item unique identifier on client side.
     *
     * @param realItemBackendIdentifier Identifier of the item on the backend storage (real identifier)
     * @return A string representing the identifier to use
     * @throws UnsupportedEncodingException If string's byte cannot be obtained
     * @throws NoSuchAlgorithmException If the hashing algorithm used is not supported is not available
     */
    public static String computeFrontEndIdentifier(String realItemBackendIdentifier){
        String frontEndId = null;
        if (realItemBackendIdentifier != null && !realItemBackendIdentifier.trim().isEmpty()) {
            //Prefix the value with the SALT
            String tmp = SALT + realItemBackendIdentifier;
            MessageDigest digester = null;
			try {
				digester = MessageDigest.getInstance("sha1");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //Compute the hash
            byte[] hash = null;
			try {
				hash = digester.digest(tmp.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //Encode is in HEX
            frontEndId = DatatypeConverter.printHexBinary(hash);
        }
        return frontEndId;
    }

}
