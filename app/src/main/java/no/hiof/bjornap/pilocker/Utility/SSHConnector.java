package no.hiof.bjornap.pilocker.Utility;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SSHConnector extends AsyncTask<String, Void, String> {

    String returnMessage = "";

    /**
     * Connect => Run Command => Disconnect
     * This works as intended, but reconnecting is required for each action
     */

    public AsyncResponseInterface response = null;
    public Session session = null;

    //Pew pew
    public SSHConnector() {

    }

    @Override
    protected String doInBackground(String... strings) {
        String user = "bjornar";
        String password = "toor";
        String host = "192.168.10.153";
        int port = 22;
        //C:\Users\bj0rn\AppData\Local\Android\Sdk
        try {
            JSch jsch = new JSch();
            //jsch.addIdentity(strings[0], password);
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();

            String remoteDir = "/home/bjornar/testfolder";

            Channel channel3 = session.openChannel("sftp");
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            channel3.connect();
            channel3.setOutputStream(output);

            ChannelSftp sftp = (ChannelSftp) channel3;
            sftp.put("src/main/java/no/hiof/bjornap/pilocker/Model/test.txt", remoteDir + "jschFile.txt");

            sftp.exit();

            channel3.disconnect();
            /*
            ChannelExec channel = (ChannelExec)session.openChannel("exec");
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            channel.setOutputStream(output);
            channel.setCommand("ssh-copy-id -i " + strings[0] +  " bjornar@192.168.10.153");
            channel.connect();


            Thread.sleep(5000);

            channel.disconnect();



            ChannelExec channel2 = (ChannelExec)session.openChannel("exec");
            channel2.setCommand(password);
            channel2.connect();
            channel2.disconnect();


            return output.toString();

             */
            return output.toString();
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
        return returnMessage;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.i("FINALSTAGE", "SSHEXECUTER POSTEXECUTE: " + s);

        response.onComplete(s);

    }
}
