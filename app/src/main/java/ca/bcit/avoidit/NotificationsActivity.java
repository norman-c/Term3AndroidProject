package ca.bcit.avoidit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationsActivity extends AppCompatActivity {

    Switch sw_alarm;
    TextView txt_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        txt_alarm = findViewById(R.id.txt_alarm);
        sw_alarm = findViewById(R.id.sw_alarm);
        sw_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_alarm.setText(R.string.notification_push_setting_enable);
                    Toast.makeText(NotificationsActivity.this,
                            R.string.notification_push_setting_enable,
                            Toast.LENGTH_LONG).show();
                }else{
                    txt_alarm.setText(R.string.notification_push_setting_disable);
                    Toast.makeText(NotificationsActivity.this,
                            R.string.notification_push_setting_disable,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
