package no.hiof.bjornap.pilocker.Utility;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;

import java.io.ByteArrayOutputStream;

public class RSAGenerator {

    /**
     * Uses the JSch library to generate a keypair with RSA encryption.
     * These are sent as a custom Tuples class as String value.
     */


    private static ByteArrayOutputStream privateKeyBuff;
    private static ByteArrayOutputStream publicKeyBuff;

    public static Tuples generateRSAPairs(){
        
        try {
            JSch jSch = new JSch();
            KeyPair keyPair = KeyPair.genKeyPair(jSch, KeyPair.RSA);
            privateKeyBuff = new ByteArrayOutputStream(2048);
            publicKeyBuff = new ByteArrayOutputStream(2048);

            keyPair.writePublicKey(publicKeyBuff, "");
            keyPair.writePrivateKey(privateKeyBuff);

            System.out.println(privateKeyBuff.toString());
            System.out.println(publicKeyBuff.toString());

        } catch (JSchException e) {
            e.printStackTrace();
        }

        return new Tuples(privateKeyBuff.toString(), publicKeyBuff.toString());
    }
}
