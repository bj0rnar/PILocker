package no.hiof.bjornap.pilocker.SSHConnection;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SSHInstaller extends AsyncTask<String, Void, String> {

    public AsyncResponseInterface response = null;

    @Override
    protected String doInBackground(String... strings) {
        String user = strings[0];
        String host = strings[1];
        String password = "gruppe6";
        String script = strings[2];
        String privatekey = strings[3];

        int port = 22;
        Session session = null;


        try {
            JSch jsch = new JSch();
            //jsch.addIdentity(privatekey);
            session = jsch.getSession(user, host, port);
            //Add identity here instead of actual password..
            session.setPassword("gruppe6");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();

            //UPLOAD RSA
            InputStream RSApubinputStream = new ByteArrayInputStream(strings[0].getBytes(StandardCharsets.UTF_8));
            Channel channel3 = session.openChannel("sftp");
            channel3.connect();
            ChannelSftp sftp = (ChannelSftp) channel3;
            sftp.put(RSApubinputStream, "/home/bjornar/id_rsa.pub", sftp.OVERWRITE);
            sftp.exit();
            channel3.disconnect();

            //INSTALL PUBLIC KEY
            ChannelExec channel2 = (ChannelExec)session.openChannel("exec");
            channel2.setCommand("./installkey.exp " + user + "@" + host + " yes " + password);
            channel2.connect();
            Thread.sleep(3000);
            channel2.disconnect();

            //DISABLE PASSWORD, ONLY KEY FROM HERE ON
            ChannelExec channel6 = (ChannelExec)session.openChannel("exec");
            channel6.setCommand("./disablePass.sh");
            channel6.connect();
            Thread.sleep(3000);
            channel2.disconnect();



            return null;
        }
        catch (JSchException ex){
            //Show error in UI
            String errorMessage = "JSCH exception: " + ex.getMessage();
            Log.d("SSH", errorMessage);
        }
        catch (Exception ex){
            //anycatcher
            String errorMessage = "any other: " + ex.getMessage();
            Log.d("SSH", errorMessage);
        }
        finally {
            session.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.i("FINALSTAGE", "SSHEXECUTER POSTEXECUTE: " + s);

        response.onComplete(s);

    }
}

