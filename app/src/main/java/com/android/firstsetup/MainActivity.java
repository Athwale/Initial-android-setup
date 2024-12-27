package com.android.firstsetup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final int REQUEST_1 = 1;
    public static final int REQUEST_2 = 2;
    private static final int REQUEST_WRITE_STORAGE = 3089;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Button mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void copy_files() throws IOException {
        Path src = Paths.get("/system/media/reader.apk");
        Path dst = Paths.get("/storage/emulated/0/Download/reader.apk");
        if (!Files.exists(dst)) {
            Files.copy(src, dst);
        }

        src = Paths.get("/system/media/browser.apk");
        dst = Paths.get("/storage/emulated/0/Download/browser.apk");
        if (!Files.exists(dst)) {
            Files.copy(src, dst);
        }
        Toast.makeText(this, "Apks copied to Download", Toast.LENGTH_LONG).show();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermission = (ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (hasPermission) {
            try {
                copy_files();
            } catch (IOException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

            try {
                Intent intent = new Intent();
                // Start by setting the password, rest is handled one by one in onActivityResult
                // Password:
                intent.setClassName("com.android.settings",
                        "com.android.settings.Settings$ScreenLockSuggestionActivity");
                startActivityForResult(intent, REQUEST_1);
            } catch (Exception e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "File access permission missing",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        boolean hasPermission = (ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },
                    REQUEST_WRITE_STORAGE);
        } else {
            try {
                copy_files();
            } catch (IOException e) {
                Toast.makeText(this, "Setup skipped", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_1) : {
                try {
                    Intent intent = new Intent();
                    // Encryption:
                    intent.setClassName("com.android.settings",
                            "com.android.settings.Settings$CryptKeeperSettingsActivity");
                    startActivityForResult(intent, REQUEST_2);
                } catch (Exception e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            }
            case (REQUEST_2) : {
                finish();
                break;
            }
        }
    }
}