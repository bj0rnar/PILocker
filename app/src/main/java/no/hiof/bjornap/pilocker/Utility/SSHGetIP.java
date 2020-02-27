package no.hiof.bjornap.pilocker.Utility;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;

public class SSHGetIP extends AsyncTask<String, Void, String> {

    String returnAddress;
    EthernetIP ethernetIP;

    /**
     * Connect => Run Command => Disconnect
     * This works as intended, but reconnecting is required for each action
     */

    //Pew pew
    public SSHGetIP(EthernetIP ethernetIP) {
        this.ethernetIP = ethernetIP;
    }

    @Override
    protected String doInBackground(String... strings) {
        String user = "ubuntu";
        String password = "gruppe6";
        String host = "10.0.60.1";
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
            channel.setCommand("./getIp.sh");
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            channel.setOutputStream(output);
            channel.connect();
            channel.disconnect();
            returnAddress += output.toString();
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
        return returnAddress;
    }

    @Override
    protected void onPostExecute(String s) {
        if (ethernetIP != null){
            ethernetIP.address(s);
        }


    }

    public interface EthernetIP {
        void address(String address);
    }
}
