package no.hiof.bjornap.pilocker;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.json.JSONException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import no.hiof.bjornap.pilocker.Utility.WheresMyTuples;

import static android.content.Context.MODE_PRIVATE;
import static no.hiof.bjornap.pilocker.Utility.RSAGenerator.generateRSAPairs;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    String privKeyWithoutSpaces = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXQIBAAKBgQDCYLpKct0aDx/g76gN0gZCSnJlbx5XSmcISj3bs2JN4Efsy6v/\n" +
            "ZoWsjVHiAsliAIH3TMeNTDZEYG/rrfdiF0IMuQe1SZBYa/GAk4Q/oMQXMRL5msNb\n" +
            "nIiEjmVa/yVUncwdP6+P5GjGPNxjLiLnnyez5SCKC4eCPXiLEftm+FlXNQIDAQAB\n" +
            "AoGACuBErDO/IV0lSH3AZn2Q8haeTgSoqvOzzifxTCC+aGMbQymmRxUid6Q0p7PG\n" +
            "NuCWZRL7u3E/PBY1TPef9vIcNObary27BGIO9dD8PuG5VdPdkV64wPdCqXhpBVI1\n" +
            "xcen11aNivSCok3APN0QBk6vU5xrqdAhbatEyGta+oJ4v+UCQQD+rXXGmIiEkL07\n" +
            "8gyOre9s/VEvxv23NdnxevuZ/EOU2PwO3Ex2fytHHUCvXPY+9WCW6XgWN5YdeyIZ\n" +
            "FFCAxLT3AkEAw2McpYLejxyirIcb00+AjXbRy85h8aLCaesoyUOYgoTB8JtPA7AG\n" +
            "BkEU3GYQvdb9qf+fjRD3UOV8gos5jRCGMwJBAPyDWId0rcdjC33oXPMLGp3h/SF2\n" +
            "GBIT3uhDnDvtl8R58FF7tIS5SlvUcVJuxeleukpnap38t2a7s+9R6RAD+bkCQQCD\n" +
            "6iuSF1xZtRSjdQi1TyzU1c7pUerQsHmf91PvJoMWQ7mIz+K186btnA0QjJxq/3b9\n" +
            "yocXP8gqRvYcURFHC0v/AkAHpniAUhx5cY6je5aaXkmcIqp3x+4PouDIpHcEQXMT\n" +
            "Hwqc69ZW7l7CaAaditkWYmM6rFPcGfla2sqUDnwL9SKb\n" +
            "-----END RSA PRIVATE KEY-----";

    @Test
    public void testJSChConnection(){

        String privKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "    MIICXQIBAAKBgQDCYLpKct0aDx/g76gN0gZCSnJlbx5XSmcISj3bs2JN4Efsy6v/\n" +
                "    ZoWsjVHiAsliAIH3TMeNTDZEYG/rrfdiF0IMuQe1SZBYa/GAk4Q/oMQXMRL5msNb\n" +
                "    nIiEjmVa/yVUncwdP6+P5GjGPNxjLiLnnyez5SCKC4eCPXiLEftm+FlXNQIDAQAB\n" +
                "    AoGACuBErDO/IV0lSH3AZn2Q8haeTgSoqvOzzifxTCC+aGMbQymmRxUid6Q0p7PG\n" +
                "    NuCWZRL7u3E/PBY1TPef9vIcNObary27BGIO9dD8PuG5VdPdkV64wPdCqXhpBVI1\n" +
                "    xcen11aNivSCok3APN0QBk6vU5xrqdAhbatEyGta+oJ4v+UCQQD+rXXGmIiEkL07\n" +
                "    8gyOre9s/VEvxv23NdnxevuZ/EOU2PwO3Ex2fytHHUCvXPY+9WCW6XgWN5YdeyIZ\n" +
                "    FFCAxLT3AkEAw2McpYLejxyirIcb00+AjXbRy85h8aLCaesoyUOYgoTB8JtPA7AG\n" +
                "    BkEU3GYQvdb9qf+fjRD3UOV8gos5jRCGMwJBAPyDWId0rcdjC33oXPMLGp3h/SF2\n" +
                "    GBIT3uhDnDvtl8R58FF7tIS5SlvUcVJuxeleukpnap38t2a7s+9R6RAD+bkCQQCD\n" +
                "    6iuSF1xZtRSjdQi1TyzU1c7pUerQsHmf91PvJoMWQ7mIz+K186btnA0QjJxq/3b9\n" +
                "    yocXP8gqRvYcURFHC0v/AkAHpniAUhx5cY6je5aaXkmcIqp3x+4PouDIpHcEQXMT\n" +
                "    Hwqc69ZW7l7CaAaditkWYmM6rFPcGfla2sqUDnwL9SKb\n" +
                "    -----END RSA PRIVATE KEY-----";


        String privKeyWithoutSpaces = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIICXQIBAAKBgQDCYLpKct0aDx/g76gN0gZCSnJlbx5XSmcISj3bs2JN4Efsy6v/\n" +
                "ZoWsjVHiAsliAIH3TMeNTDZEYG/rrfdiF0IMuQe1SZBYa/GAk4Q/oMQXMRL5msNb\n" +
                "nIiEjmVa/yVUncwdP6+P5GjGPNxjLiLnnyez5SCKC4eCPXiLEftm+FlXNQIDAQAB\n" +
                "AoGACuBErDO/IV0lSH3AZn2Q8haeTgSoqvOzzifxTCC+aGMbQymmRxUid6Q0p7PG\n" +
                "NuCWZRL7u3E/PBY1TPef9vIcNObary27BGIO9dD8PuG5VdPdkV64wPdCqXhpBVI1\n" +
                "xcen11aNivSCok3APN0QBk6vU5xrqdAhbatEyGta+oJ4v+UCQQD+rXXGmIiEkL07\n" +
                "8gyOre9s/VEvxv23NdnxevuZ/EOU2PwO3Ex2fytHHUCvXPY+9WCW6XgWN5YdeyIZ\n" +
                "FFCAxLT3AkEAw2McpYLejxyirIcb00+AjXbRy85h8aLCaesoyUOYgoTB8JtPA7AG\n" +
                "BkEU3GYQvdb9qf+fjRD3UOV8gos5jRCGMwJBAPyDWId0rcdjC33oXPMLGp3h/SF2\n" +
                "GBIT3uhDnDvtl8R58FF7tIS5SlvUcVJuxeleukpnap38t2a7s+9R6RAD+bkCQQCD\n" +
                "6iuSF1xZtRSjdQi1TyzU1c7pUerQsHmf91PvJoMWQ7mIz+K186btnA0QjJxq/3b9\n" +
                "yocXP8gqRvYcURFHC0v/AkAHpniAUhx5cY6je5aaXkmcIqp3x+4PouDIpHcEQXMT\n" +
                "Hwqc69ZW7l7CaAaditkWYmM6rFPcGfla2sqUDnwL9SKb\n" +
                "-----END RSA PRIVATE KEY-----";

        String pubKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQDCYLpKct0aDx/g76gN0gZCSnJlbx5XSmcISj3bs2JN4Efsy6v/ZoWsjVHiAsliAIH3TMeNTDZEYG/rrfdiF0IMuQe1SZBYa/GAk4Q/oMQXMRL5msNbnIiEjmVa/yVUncwdP6+P5GjGPNxjLiLnnyez5SCKC4eCPXiLEftm+FlXNQ==";

        byte[] privArray = privKeyWithoutSpaces.getBytes();
        byte[] pubArray = pubKey.getBytes();


        try {
            JSch jsch = new JSch();
            Session session = null;
            jsch.addIdentity("bjornar", privArray, pubArray, null);
            session = jsch.getSession("bjornar", "192.168.10.185", 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();

            ChannelExec channel = (ChannelExec)session.openChannel("exec");
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            channel.setOutputStream(output);
            channel.setCommand("./counter.sh");
            channel.connect();



            channel.disconnect();
            session.disconnect();
        }
        catch (JSchException e){
            System.out.println(e.getMessage());
        }

    }
    @Test
    public void testJSChUpload(){
        try {
            JSch jsch = new JSch();
            Session session = null;
            session = jsch.getSession("ubuntu", "192.168.10.185", 22);
            session.setPassword("gruppe6");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();


            WheresMyTuples myTuples = generateRSAPairs();
            String priv = (String)myTuples.priv;
            String pub = (String)myTuples.pub;


            InputStream RSApubinputStream = new ByteArrayInputStream(pub.getBytes(StandardCharsets.UTF_8));

            Channel channel3 = session.openChannel("sftp");
            channel3.connect();

            ChannelSftp sftp = (ChannelSftp) channel3;
            sftp.put(RSApubinputStream, "/home/ubuntu/id_rsa.pub", sftp.OVERWRITE);

            sftp.exit();

            Thread.sleep(1000);

            channel3.disconnect();

            /*
            Channel channel = session.openChannel("sftp");
            channel.connect();

            String test = "ITS SO BIG";

            InputStream stream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
            //File f = new File(test);

            ChannelSftp sftp = (ChannelSftp) channel;
            //sftp.cd("/home/bjornar/");
            sftp.put(stream, "/home/bjornar/myPENIS.txt", ChannelSftp.OVERWRITE);

            sftp.exit();

            //sftp.disconnect();

            channel.disconnect();


            ChannelExec channel2 = (ChannelExec)session.openChannel("exec");
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            channel2.setOutputStream(output);
            channel2.setCommand("./testgreie.sh");
            channel2.connect();

            Thread.sleep(3000);

            channel2.disconnect();
            */

            session.disconnect();
        }
        catch (JSchException | InterruptedException | SftpException e){
            System.out.println(e.getMessage());
        }
        /*
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

         */

    }
}