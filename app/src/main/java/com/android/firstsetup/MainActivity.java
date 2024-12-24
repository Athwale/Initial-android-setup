package com.android.firstsetup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final int REQUEST_1 = 1;
    public static final int REQUEST_2 = 2;
    public static final int REQUEST_3 = 3;

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

    @Override
    public void onClick(View view) {
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
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                try {
                    Intent intent = new Intent();
                    // Power:
                    // todo device status to turn off debugging? Can it be turned off by default somewhere?
                    intent.setClassName("com.android.settings",
                            "com.android.settings.PowerSettings");
                    startActivityForResult(intent, REQUEST_2);
                } catch (Exception e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            }
            case (2) : {
                try {
                    Intent intent = new Intent();
                    // Encryption:
                    intent.setClassName("com.android.settings",
                            "com.android.settings.Settings$CryptKeeperSettingsActivity");
                    startActivityForResult(intent, REQUEST_3);
                } catch (Exception e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            }
            case (3) : {
                finish();
                break;
            }
        }
    }
}