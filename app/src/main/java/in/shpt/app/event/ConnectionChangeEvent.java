package in.shpt.app.event;

/**
 * Created by iampo on 10/9/2016.
 */

public class ConnectionChangeEvent {
    boolean status;

    public ConnectionChangeEvent(boolean message) {
        this.status = message;
    }

    public boolean getMessage() {
        return status;
    }

    public void setMessage(boolean message) {
        this.status = message;
    }
}
