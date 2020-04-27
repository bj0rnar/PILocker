package no.hiof.bjornap.pilocker.SSHConnection;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;

public class SSHExecuter extends AsyncTask<String, Void, String> {

    public AsyncResponseInterface response = null;

    @Override
    protected String doInBackground(String... strings) {
        String user = strings[0];
        String host = strings[1];
        String script = strings[2];
        String privatekey = strings[3];

        Log.i("SSHREADER", "SSHEXECTURE: strings[0] = " + strings[0]);
        Log.i("SSHREADER", "SSHEXECTURE: strings[1] = " + strings[1]);
        Log.i("SSHREADER", "SSHEXECTURE: strings[2] = " + strings[2]);
        Log.i("SSHREADER", "SSHEXECTURE: strings[3] = " + strings[3]);


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
            ChannelExec channel = (ChannelExec)session.openChannel("exec");
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            channel.setOutputStream(output);
            channel.setCommand(script);
            channel.connect();

            //Sleep it for a bit..
            Thread.sleep(3000);


            channel.disconnect();
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

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.i("FINALSTAGE", "SSHEXECUTER POSTEXECUTE: " + s);

        response.onComplete(s);

    }
}
