package no.hiof.bjornap.pilocker.Utility;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import no.hiof.bjornap.pilocker.PageViewModel;

public class SSHLogin extends AsyncTask<Void, Void, Session> {

    private String user = "bjornar";
    private String password = "toor";
    private String host = "192.168.10.153";
    private int port = 22;
    private Session session = null;
    private PageViewModel pvm = new PageViewModel();

    @Override
    protected Session doInBackground(Void... voids) {

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();
        }
        catch (JSchException e){
            Log.d("SSHLogin", e.getMessage());
        }

        if(session != null){
            return this.session;
        }
        else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Session session) {
        pvm.setCurrentSession(session);
    }
}
