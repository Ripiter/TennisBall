package com.peter.tennisball;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SensorManager sm;
    ImageView ball;
    List sensorList;
    final String TAG = "Main";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        ball = findViewById(R.id.ball_img);
        sensorList = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener(sel, (Sensor) sensorList.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }

    SensorEventListener sel = new SensorEventListener(){
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;

            float newX = ball.getX() - values[0] * 5;
            float newY = ball.getY() + values[1] * 5;

            if(isPositionOutOfBounds(newX, newY) == false){
                ball.setX(newX);
                ball.setY(newY);
            }
        }
    };

    boolean isPositionOutOfBounds(float newX, float newY){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        Float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        if(newX > 0 - ball.getMeasuredWidth() / 2  && (newX + ball.getMeasuredWidth()) / 2 < dpWidth * 2 &&
           newY > 0 - ball.getMeasuredHeight() / 2  && (newY - ball.getMeasuredHeight() / 2) < dpHeight * 2)
        {
            ball.animate().rotationBy((newX + newY) * 5).start();
            return false;
        }
        ball.animate().rotation(0);
        return true;
    }

    @Override
    protected void onStop() {
        sm.unregisterListener(sel);

        super.onStop();
    }
}