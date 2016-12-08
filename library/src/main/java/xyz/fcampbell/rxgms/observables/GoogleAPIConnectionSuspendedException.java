package xyz.fcampbell.rxgms.observables;

public class GoogleAPIConnectionSuspendedException extends RuntimeException {
    private final int cause;

    GoogleAPIConnectionSuspendedException(int cause) {
        this.cause = cause;
    }

    public int getErrorCause() {
        return cause;
    }
}
