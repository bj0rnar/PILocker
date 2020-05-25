package no.hiof.bjornap.pilocker;

import com.jcraft.jsch.Session;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatusViewModel extends ViewModel {

    /**
     * ViewModel for dynamically changing the status, time and date of
     * last command sent in the unlock screen
     */

    private MutableLiveData<String> status = new MutableLiveData<>();
    private MutableLiveData<String> time = new MutableLiveData<>();
    private MutableLiveData<String> date = new MutableLiveData<>();


    public MutableLiveData<String> getStatus() {
        return status;
    }

    public MutableLiveData<String> getTime() { return time; }

    public MutableLiveData<String> getDate() { return date; }
}

