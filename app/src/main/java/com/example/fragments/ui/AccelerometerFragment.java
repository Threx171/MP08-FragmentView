package com.example.fragments.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fragments.R;

public class AccelerometerFragment extends Fragment {

    private SensorManager sManager;
    private Sensor sensor;

    SensorEventListener sensorListener;

    private GestureDetector gestureDetector;

    public static AccelerometerFragment newInstance() {
        return new AccelerometerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_accelerometer, container, false);

        ProgressBar barX = root.findViewById(R.id.barX);
        ProgressBar barY = root.findViewById(R.id.barY);
        ProgressBar barZ = root.findViewById(R.id.barZ);

        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // Valors de l'acceleròmetre en m/s^2
                float xAcc = sensorEvent.values[0];
                float yAcc = sensorEvent.values[1];
                float zAcc = sensorEvent.values[2];

                // Processament o visualització de dades...
                barX.setProgress((int) ((Math.abs(xAcc)) * 100));
                barY.setProgress((int) ((Math.abs(yAcc)) * 100));
                barZ.setProgress((int) ((Math.abs(zAcc)) * 100));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Es pot ignorar aquesta CB de moment
            }
        };

        // Seleccionem el tipus de sensor (veure doc oficial)
        sManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // registrem el Listener per capturar els events del sensor
        if( sensor!=null ) {
            sManager.registerListener(sensorListener,sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        root.setOnTouchListener(new View.OnTouchListener() {
            private long lastTouchTime;
            private static final long DOUBLE_CLICK_TIME_DELTA = 300; // en milisegundos

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                long now = System.currentTimeMillis();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (now - lastTouchTime < DOUBLE_CLICK_TIME_DELTA) {
                        showToast("Doble toque detectado!");
                    }
                    lastTouchTime = now;
                }
                return true;
            }
        });

        return root;
    }

    private void showToast(String message) {
        Context context = requireActivity().getApplicationContext();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}