package no.hiof.bjornap.pilocker.Model;

/**
 * Example class for how Door could be stored in a multidoor application
 */


public class Door {
    private String ip;
    private String rsa;

    public Door(String ip, String rsa){
        this.rsa = rsa;
        this.ip = ip;
    }

}
