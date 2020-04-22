package no.hiof.bjornap.pilocker.Utility;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;

import java.io.ByteArrayOutputStream;

public class RSAGenerator {

    private static ByteArrayOutputStream privateKeyBuff;
    private static ByteArrayOutputStream publicKeyBuff;

    public static WheresMyTuples generateRSAPairs(){

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

        return new WheresMyTuples<>(privateKeyBuff.toString(), publicKeyBuff.toString());
    }
}


