package no.hiof.bjornap.pilocker;

import com.jcraft.jsch.Session;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatusViewModel extends ViewModel {

    /**
     * Under construction, currently not working.
     * Use this for updating status in unlockfragment
     */

    private MutableLiveData<String> names = new MutableLiveData<>();

    public MutableLiveData<String> getNames() {
        return names;
    }

    public void setNames(MutableLiveData<String> names) {
        this.names = names;
    }


}

