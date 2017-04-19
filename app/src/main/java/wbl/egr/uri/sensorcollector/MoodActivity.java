package wbl.egr.uri.sensorcollector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import wbl.egr.uri.sensorcollector.adapters.MoodAdapt;
import wbl.egr.uri.sensorcollector.models.MoodEntry;
import wbl.egr.uri.sensorcollector.receivers.AlarmReceiver;
import wbl.egr.uri.sensorcollector.services.DataLogService;

/**
 * Created by root on 4/9/17.
 */

public class MoodActivity extends AppCompatActivity {
    private Button send;
    private TextView title;
    private EditText note;
    private ListView moods;
    private String allMoods[] = {"Happy", "Sad", "Angry", "Anxious", "Irritated"};
    private int[] colors = {Color.YELLOW, Color.CYAN, Color.RED, Color.GRAY, Color.GREEN};
    private Boolean moodSelected = false;
    private String currMood;
    private Context context;
    private File file;
    private final String HEADER = "Date,Time,Mood,Notes";
    private boolean headerWrote;
    protected String[] defualt_Times = {"07:00:00 AM", "02:00:00 PM", "07:00:00 PM"};
    protected int bc = 0;
    boolean first;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        boolean extra = getIntent().getBooleanExtra("ALARM", false);

        if(extra)
        {
            first = false;
            Log.d("FILE", "Extra not null");
        }
        else
        {
            first = true;
            Log.d("FILE", "Extra Null");
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        edit = prefs.edit();
        update();

        context = this;

        final ArrayList<MoodEntry> listMoods = new ArrayList<MoodEntry>();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        send = (Button) findViewById(R.id.send);
        moods = (ListView) findViewById(R.id.moodList);
        title = (TextView)findViewById(R.id.title);
        note = (EditText)findViewById(R.id.note);

        if (!prefs.getBoolean("firstTime", false)) {

            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, bc, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            edit = prefs.edit();
            edit.putBoolean("firstTime", true);
            edit.apply();
            bc++;
        }

        if (!prefs.getBoolean("secTime", false)) {

            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, bc, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 14);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            edit = prefs.edit();
            edit.putBoolean("secTime", true);
            edit.apply();
            bc++;
        }

        if (!prefs.getBoolean("thirdTime", false)) {

            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, bc, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            edit = prefs.edit();
            edit.putBoolean("thirdTime", true);
            edit.apply();
            bc = 0;
        }

        Log.d("FILE", "ONCREATE");

        for(int x = 0; x < allMoods.length; x++)
        {
            MoodEntry test = new MoodEntry(allMoods[x]);
            listMoods.add(test);
        }

        moods.setAdapter(new MoodAdapt(getApplicationContext(), R.layout.mood_list, listMoods));

        moods.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                moodSelected = true;
                currMood = listMoods.get(position).getMood();
                Date date = Calendar.getInstance().getTime();
                String dateString = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(date);
                String timeString = new SimpleDateFormat("kk:mm:ss.SSS", Locale.US).format(date);
                String content = dateString + ", " +  timeString + ", " + currMood + ",NA";

                File file = new File(MainActivity.getRootFile(context), "MoodLog.csv");
                DataLogService.log(context, file, content, HEADER);

                Toast.makeText(getApplicationContext(), "Mood Logged!", Toast.LENGTH_SHORT).show();
            }
        });

//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//                String date = df.format(Calendar.getInstance().getTime());
//                String currTime = DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());;
//                String mood = currMood;
//                String notes = "Not Entered";
//
//                //Log into CSV File
//                //Use Loading Fragment
//
//
//            }
//        });

        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(note.getText().toString().length() < 1)
                {
                    Toast.makeText(getApplicationContext(), "Please enter content",
                            Toast.LENGTH_SHORT).show();
                }

                else
                {
//                    Intent intent = new Intent(MainActivity.this, NoteFragment.class);
//                    intent.putExtra("MOOD", currMood);
//                    startActivity(intent);
                    String notes = note.getText().toString();

                    Date date = Calendar.getInstance().getTime();
                    String dateString = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(date);
                    String timeString = new SimpleDateFormat("kk:mm:ss.SSS", Locale.US).format(date);
                    String content = dateString + ", " +  timeString + ", " + "NA," + notes;

                    File file = new File(MainActivity.getRootFile(context), "MoodLog.csv");
                    DataLogService.log(context, file, content, HEADER);

                    Toast.makeText(getApplicationContext(), "Mood Logged!", Toast.LENGTH_SHORT).show();

                    //Hide the keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //Reset the text field & undo the auto focus
                    note.setText("");
                    note.setFocusable(true);
                }
            }
        });
    }

    private void update()
    {
        if(first)
        {
            Log.d("FILE", "In Update");
            edit.putBoolean("firstTime", false);
            edit.apply();
            edit.putBoolean("secTime", false);
            edit.apply();
            edit.putBoolean("thirdTime", false);
            edit.apply();
            first = false;
        }
    }

//    private void updateNotification(String status, int icon) {
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification.Builder notificationBuilder = new Notification.Builder(this)
//                .setContentTitle("EAR is Active")
//                .setContentText("Band Status: " + status)
//                .setSmallIcon(icon);
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
//    }
}
