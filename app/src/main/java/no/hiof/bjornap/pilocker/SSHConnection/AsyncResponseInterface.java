package no.hiof.bjornap.pilocker.SSHConnection;

/**
 * Interface used for dependency injection between async class and fragments
 */


public interface AsyncResponseInterface {
    void onComplete(String result);
}
