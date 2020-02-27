package no.hiof.bjornap.pilocker.Utility;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;

public class SSHReader extends AsyncTask<String, Void, String>  {

    public AsyncResponseInterface response = null;

    String a = "a";

    /**
     * Connect => Run Command => Disconnect
     * This works as intended, but reconnecting is required for each action
     */

    //Pew pew
    public SSHReader() {

    }

    @Override
    protected String doInBackground(String... strings) {
        String user = strings[0];
        String password = strings[1];
        String host = strings[2];
        int port = 22;
        Session session = null;

        Log.i("SSHREADER", "strings[0] = " + strings[0]);
        Log.i("SSHREADER", "strings[1] = " + strings[1]);
        Log.i("SSHREADER", "strings[2] = " + strings[2]);
        Log.i("SSHREADER", "strings[3] = " + strings[3]);

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();
            ChannelExec channel = (ChannelExec)session.openChannel("exec");
            channel.setCommand(strings[3]);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            channel.setOutputStream(output);
            channel.connect();

            try {
                Thread.sleep(500);
            }
            catch (Exception e){
                Log.w("SSHREADER", "thread sleep failed " + e.getMessage());
            }

            channel.disconnect();
            a = output.toString();
            Log.i("SSHREADER", "OUTPUT TOSTRINGA?!: " + output.toString());
            return output.toString();
        }
        catch (JSchException ex){
            //Show error in UI
            String errorMessage = "JSCH exception: " + ex.getMessage();
            Log.d("SSHREADER", errorMessage);
        }
        catch (Exception ex){
            //anycatcher
            String errorMessage = "any other: " + ex.getMessage();
            Log.d("SSHREADER", errorMessage);
        }
        finally {
            Log.i("SSHREADER", "Finally: " + a);
            session.disconnect();

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Not working?!
        Log.i("SSHREADER", "onPostExecute: " + s);
        response.onComplete(s);

    }

}
