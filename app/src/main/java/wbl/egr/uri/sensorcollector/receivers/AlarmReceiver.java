package wbl.egr.uri.sensorcollector.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import wbl.egr.uri.sensorcollector.MainActivity;
import wbl.egr.uri.sensorcollector.MoodActivity;
import wbl.egr.uri.sensorcollector.R;
import wbl.egr.uri.sensorcollector.services.AudioRecordManager;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by mconstant on 2/23/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String KEY_ACTION = "alarm_key_action";
    public static final String KEY_ALARM_ID = "key_alarm_id";
    public static final int AUDIO_ID = 430;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(AudioRecordManager.INTENT_TAG)) {
            AudioRecordManager.start(context, intent.getIntExtra(KEY_ACTION, -1));
        } else {
            Intent resultIntent = new Intent(context, MoodActivity.class);
            resultIntent.putExtra("ALARM", true);

            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, resultIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.ic_dialog_alert)
                            .setContentTitle("Mood Log Entry")
                            .setContentText("Please enter a mood log.")
                            .setAutoCancel(true)
                            .setContentIntent(contentIntent);

            Notification n = mBuilder.getNotification();
            n.defaults |= Notification.DEFAULT_SOUND;
            n.defaults |= Notification.FLAG_AUTO_CANCEL;

//        n.defaults |= Notification.DEFAULT_ALL;
            notificationManager.notify(0, n);

            Log.d("FILE", "Alarm Rec");
        }
    }
}
