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

import static android.content.Context.MODE_PRIVATE;
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

    @Test
    public void testJSChConnection(){
        try {
            JSch jsch = new JSch();
            Session session = null;
            //jsch.addIdentity(strings[0], password);
            session = jsch.getSession("bjornar", "192.168.10.153", 22);
            session.setPassword("toor");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();

            ChannelExec channel = (ChannelExec)session.openChannel("exec");
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            channel.setOutputStream(output);
            channel.setCommand("./counter.sh");
            channel.connect();


            Thread.sleep(5000);

            channel.disconnect();
            session.disconnect();
        }
        catch (JSchException | InterruptedException e){
            System.out.println(e.getMessage());
        }
        /*
        catch (SftpException e) {
            System.out.println(e.getMessage());
        }
        */
    }
    @Test
    public void testJSChUpload(){
        try {
            JSch jsch = new JSch();
            Session session = null;
            session = jsch.getSession("bjornar", "192.168.10.153", 22);
            session.setPassword("toor");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            String test = "HALLO DIN NEGER";

            InputStream stream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
            //File f = new File(test);

            ChannelSftp sftp = (ChannelSftp) channel;
            //sftp.cd("/home/bjornar/");
            sftp.put(stream, "/home/bjornar/uploadhere.txt", ChannelSftp.OVERWRITE);

            sftp.exit();

            //sftp.disconnect();

            channel.disconnect();

            session.disconnect();
        }
        catch (JSchException | SftpException e){
            System.out.println(e.getMessage());
        }
        /*
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

         */

    }
}