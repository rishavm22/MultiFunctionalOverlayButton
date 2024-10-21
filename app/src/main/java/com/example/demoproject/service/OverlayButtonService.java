package com.example.demoproject.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.app.NotificationCompat;

import com.example.demoproject.MainActivity;
import com.example.demoproject.R;

public class OverlayButtonService extends Service {

    private static final String NOTIFICATION_CHANNEL_ID = "overlay_button_channel";

    private WindowManager windowManager;
    private View overlayView;
    private View menuOptionView;
    private WindowManager.LayoutParams params;
    private int initialX, initialY;

    private boolean isAudioRecording = false;
    private boolean isVideoRecording = false;
    private boolean isScreenSharing = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int 
            startId) {
        // Create notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, 

                    "Overlay Button Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 

            notificationManager.createNotificationChannel(channel); 

        }

        // Initialize initialX and initialY
        initialX = 0;
        initialY = 0;

        // Set window manager parameters with touch handling
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,

                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.LEFT;

        // Set initial position of the overlay view
        params.x = 100; // Adjust the x coordinate as needed
        params.y = 100; // Adjust the y coordinate as needed



        // Create overlay button layout
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_button, null);

        // Set window manager parameters
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, 

                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE 
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
        PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.LEFT; 


        // Add overlay button to window
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlayView, params);

        // Implement click listeners for options
        ImageButton audioButton = overlayView.findViewById(R.id.audio_button);
        Button myAppButton = overlayView.findViewById(R.id.floating_button); // Assuming "floating_button" is the ID of your "MyApp" button
        ImageButton videoButton = overlayView.findViewById(R.id.video_button);
        ImageButton screenshareButton = overlayView.findViewById(R.id.screenshare_button);
        ImageButton backButton = overlayView.findViewById(R.id.back_button);
        menuOptionView = overlayView.findViewById(R.id.menu_option_layout);

        audioButton.setOnClickListener(v -> {
            if (isAudioRecording) {
                // Stop audio recording
                // ...
                isAudioRecording = false;
            } else {
                // Start audio recording
                // ...
                isAudioRecording = true;
            }
        });

        videoButton.setOnClickListener(v -> {
            if (isVideoRecording) {
                // Stop video recording
                // ...
                isVideoRecording = false;
            } else {
                // Start video recording
                // ...
                isVideoRecording = true;
            }
        });

        screenshareButton.setOnClickListener(v -> {
            if (isScreenSharing) {
                // Stop screen sharing
                // ...
                isScreenSharing = false;
            } else {
                // Start screen sharing
                // ...
                isScreenSharing = true;
            }
        });

        backButton.setOnClickListener(v -> {
            // Stop all recordings and close overlay
            stopSelf();

            // Start the main activity or bring it to the foreground
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        });

        myAppButton.setOnClickListener(v -> menuOptionView.setVisibility(View.VISIBLE));

        // Set onTouchListener for overlayView
        overlayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleTouch(event);
                return true;
            }
        });


        // Create and start notification
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        // Replace with your icon
                .setContentTitle("Overlay Button Service")
                .setContentText("Service is running")
                .setOngoing(true)
                .build();
        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayView != null) {
            windowManager.removeView(overlayView); 

        }
    }

    private void handleTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Store initial x and y coordinates
                initialX = (int) event.getRawX();
                initialY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // Calculate new x and y coordinates
                int newX = (int) (event.getRawX() - initialX);
                int newY = (int) (event.getRawY() - initialY);

                // Update window manager parameters
                params.x += newX;
                params.y += newY;
                windowManager.updateViewLayout(overlayView, params);
                break;
            case MotionEvent.ACTION_UP:
                // Reset initial coordinates
                initialX = 0;
                initialY = 0;
                break;
        }
    }
}
