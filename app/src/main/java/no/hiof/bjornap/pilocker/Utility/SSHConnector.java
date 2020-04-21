package no.hiof.bjornap.pilocker.Utility;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHConnector extends AsyncTask<String, Void, String> {

    String returnMessage = "";

    /**
     * Connect => Run Command => Disconnect
     * This works as intended, but reconnecting is required for each action
     */

    public AsyncResponseInterface response = null;

    //Pew pew
    public SSHConnector() {

    }

    @Override
    protected String doInBackground(String... strings) {
        String user = "bjornar";
        String password = "toor";
        String host = "192.168.10.153";
        int port = 22;
        Session session = null;
        //C:\Users\bj0rn\AppData\Local\Android\Sdk
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();
            ChannelExec channel = (ChannelExec)session.openChannel("exec");
            channel.setCommand("./input_test.sh heiframeg");
            channel.connect();
            channel.disconnect();
            returnMessage += "Success!";
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
