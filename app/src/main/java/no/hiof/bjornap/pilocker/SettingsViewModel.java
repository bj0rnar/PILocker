package no.hiof.bjornap.pilocker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private MutableLiveData<String> uptime = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();

    public MutableLiveData<String> getUptime () {
        return uptime;
    }
    public MutableLiveData<String> getEmail () { return email; }

    public void setUptime(MutableLiveData<String> uptime) {
        this.uptime = uptime;
    }

}
