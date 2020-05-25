package no.hiof.bjornap.pilocker.Utility;

public class Tuples<X, Y> {

    /**
     * Tuples are non-existent in Java, so here's a custom made class for
     * passing public and private RSA keys.
     */

    public final X priv;
    public final Y pub;

    public Tuples(X priv, Y pub) {
        this.priv = priv;
        this.pub = pub;

    }


}
