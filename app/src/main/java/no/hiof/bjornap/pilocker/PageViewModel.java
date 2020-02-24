package no.hiof.bjornap.pilocker;

import com.jcraft.jsch.Session;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    /**
     * Under construction, currently not working.
     */

    private Session currentSession;

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }
}

    /* GRAVEYAAAAAAAAARD.

    private MutableLiveData<String> names = new MutableLiveData<>();

    public MutableLiveData<String> getNames() {
        return names;
    }

    public void setNames(MutableLiveData<String> names) {
        this.names = names;
    }

     */
