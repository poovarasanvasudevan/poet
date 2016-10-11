package in.shpt.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import in.shpt.app.application.SHPTBase;
import in.shpt.app.event.ConnectionChangeEvent;

/**
 * Created by iampo on 10/9/2016.
 */

public class NetworkReceivers extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (SHPTBase.getInstance().isOnline(context)) {
            EventBus.getDefault().post(new ConnectionChangeEvent(true));
        } else {
            EventBus.getDefault().post(new ConnectionChangeEvent(false));
        }
    }
}
