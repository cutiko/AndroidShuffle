package cl.cutiko.androidshuffle.background;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class PlayerService extends Service {

    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer = new MediaPlayer();

    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        protected PlayerService getService() {
            return  PlayerService.this;
        }
    }
}
