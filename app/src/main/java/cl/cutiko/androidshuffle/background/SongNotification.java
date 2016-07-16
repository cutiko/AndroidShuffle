package cl.cutiko.androidshuffle.background;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import cl.cutiko.androidshuffle.R;
import cl.cutiko.androidshuffle.views.MainActivity;


public class SongNotification {

    private static final String NOTIFICATION_TAG = "Song";

    public static void notify(final Context context, final String msg, final int number) {
        final Resources res = context.getResources();
        final Bitmap picture = BitmapFactory.decodeResource(res, R.mipmap.ic_play_circle_filled_white_48dp);
        final Intent intent = new Intent(context, MainActivity.class);
        final PendingIntent notificationIntent = PendingIntent.getActivity(context, number, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                .setDefaults(0)
                .setSmallIcon(R.mipmap.ic_play_circle_filled_white_48dp)
                .setContentTitle(msg)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(picture)
                .setTicker(msg)
                .setContentIntent(notificationIntent)
                .setAutoCancel(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }
}
