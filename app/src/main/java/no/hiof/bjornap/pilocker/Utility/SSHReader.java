package no.hiof.bjornap.pilocker.Utility;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;

import no.hiof.bjornap.pilocker.PageViewModel;

public class SSHReader extends AsyncTask<String, Void, String> {

    private String returnMessage = "";

    private PageViewModel pvm = new PageViewModel();

    /**
     * Connect => Run Command => Disconnect
     * This works as intended, but reconnecting is required for each action
     */

    private OutputMessage outputMessage;

    //Pew pew
    public SSHReader(OutputMessage outputMessage) {
        this.outputMessage = outputMessage;
    }

    @Override
    protected String doInBackground(String... strings) {
        String user = "bjornar";
        String password = "toor";
        String host = "192.168.10.153";
        int port = 22;
        Session session = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();
            ChannelExec channel = (ChannelExec)session.openChannel("exec");
            channel.setCommand("./readtest.sh");
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            channel.setOutputStream(output);
            channel.connect();
            channel.disconnect();
            Log.d("SSH", output.toString());
            returnMessage += output.toString();
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
        //super.onPostExecute(s);
        //Not working?!
        if (outputMessage != null){
            outputMessage.returnMessage(s);
        }

    }

    public interface OutputMessage {
        void returnMessage(String message);
    }
}
