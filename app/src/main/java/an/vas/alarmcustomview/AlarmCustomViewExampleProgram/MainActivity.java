package an.vas.alarmcustomview.AlarmCustomViewExampleProgram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;


import an.vas.alarmcustomview.CustomViews.ClockView;
import an.vas.alarmcustomview.R;

public class MainActivity extends AppCompatActivity {
    protected Button button70, button180, button250;
    private ClockView clockView;
    private ConstraintLayout clockViewLayout;
    private int curSize = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clockView = findViewById(R.id.clockview);
        button70 = findViewById(R.id.button70);
        button180 = findViewById(R.id.button180);
        button250 = findViewById(R.id.button250);
        clockViewLayout = findViewById(R.id.clockLayout);


        button70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams layoutParams = clockViewLayout.getLayoutParams();
                layoutParams.height = (int) (70 * getResources().getDisplayMetrics().density);
                clockViewLayout.setLayoutParams(layoutParams);

                clockView.setSecHandWidth(2);
                clockView.setMinHandWidth(3);
                clockView.setHourHandWidth(5);
            }
        });

        button180.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams layoutParams = clockViewLayout.getLayoutParams();
                layoutParams.height = (int) (180 * getResources().getDisplayMetrics().density);
                clockViewLayout.setLayoutParams(layoutParams);

                clockView.setSecHandWidth(6);
                clockView.setMinHandWidth(10);
                clockView.setHourHandWidth(15);
            }
        });

        button250.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams layoutParams = clockViewLayout.getLayoutParams();
                layoutParams.height = (int) (250 * getResources().getDisplayMetrics().density);
                clockViewLayout.setLayoutParams(layoutParams);
                clockView.setSecHandWidth(9);
                clockView.setMinHandWidth(14);
                clockView.setHourHandWidth(20);
            }
        });

    }
}