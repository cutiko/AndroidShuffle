package cl.cutiko.androidshuffle.background;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cl.cutiko.androidshuffle.R;
import cl.cutiko.androidshuffle.models.Song;

public class PlayerService extends Service {

    private final IBinder binder = new LocalBinder();

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<Song> songList = new ArrayList<>();
    private int position = 0;

    public static final String SONG_UPDATE = "cl.cutiko.androidshuffle.background.PlayerService.UPDATE_SONG";

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

    public void playSong() {
        if (songList.size() > 0) {
            Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, random());
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getApplicationContext(), songUri);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        Intent broadcastSongName = new Intent();
                        broadcastSongName.setAction(SONG_UPDATE);
                        sendBroadcast(broadcastSongName);
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playSong();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private long random() {
        int size = songList.size();
        if (size == 1) {
            this.position = 0;
            return songList.get(0).getId();
        } else {
            int max = size+2;
            int min = 0;
            Random random = new Random();
            int position = random.nextInt(max-min)+min;

            long songId =  0L;
            if (position > songList.size()+1) {
                random();
            } else {
                songId = songList.get(position).getId();
            }
            this.position = position;
            return songId;
        }
    }

    public void stopSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public String getSongName() {
        if (songList.size() > 0) {
            return songList.get(position).getName();
        } else {
            return getString(R.string.background_player_service_warning);
        }
    }
}
