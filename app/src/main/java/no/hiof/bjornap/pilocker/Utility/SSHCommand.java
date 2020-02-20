package no.hiof.bjornap.pilocker.Utility;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import no.hiof.bjornap.pilocker.PageViewModel;

public class SSHCommand extends AsyncTask<String, Void, Boolean> {

    private PageViewModel pvm = new PageViewModel();
    private Session session;


    @Override
    protected Boolean doInBackground(String... strings) {
        System.out.println(strings[0]);
        session = pvm.getCurrentSession();

        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(strings[0]);
            channel.connect();
            channel.disconnect();
            return true;
        }
        catch (JSchException e){
            Log.d("SSHCommand", e.getMessage());
        }

        return false;
    }
}
