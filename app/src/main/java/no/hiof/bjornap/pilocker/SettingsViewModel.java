package no.hiof.bjornap.pilocker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    /**
     * ViewModel for displaying how long the Raspberry PI has been running
     * and which email account is currently registered in the settings fragment
     */

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
