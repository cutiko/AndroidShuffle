package cl.cutiko.androidshuffle.background;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cl.cutiko.androidshuffle.models.Song;

public class PlayerService extends Service {

    private final IBinder binder = new LocalBinder();

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<Song> songList = new ArrayList<>();

    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    public void setSongs() {
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC;

        Cursor cursor = getContentResolver().query(uri, projection, selection, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                songList.add(new Song(cursor.getString(0), cursor.getString(1) + " - " + cursor.getString(2)));
            }
            cursor.close();
        }
    }
}
