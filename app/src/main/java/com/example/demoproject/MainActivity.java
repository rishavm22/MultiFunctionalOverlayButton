package com.example.demoproject;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;



import com.example.demoproject.service.OverlayButtonService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button okayButton = findViewById(R.id.okay_button);
        okayButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(MainActivity.this)) {
                    // Request permission to draw over other apps
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 123);
                } else {
                    // Start the overlay service
                    startOverlayService();
                }
            } else {
                // Start the overlay service directly
                startOverlayService();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (Settings.canDrawOverlays(this)) {
                // Permission granted, start the overlay service
                startOverlayService();
            } else {
                // Permission denied, show a message
                Toast.makeText(this, "Permission to draw over other apps is required.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startOverlayService() {
        Intent serviceIntent = new Intent(this, OverlayButtonService.class);
        startService(serviceIntent);
        finish();
    }
}