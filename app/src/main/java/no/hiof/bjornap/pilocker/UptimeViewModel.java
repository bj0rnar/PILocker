package no.hiof.bjornap.pilocker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UptimeViewModel extends ViewModel {

    private MutableLiveData<String> uptime = new MutableLiveData<>();

    public MutableLiveData<String> getUptime () {
        return uptime;
    }

    public void setUptime(MutableLiveData<String> uptime) {
        this.uptime = uptime;
    }

}
