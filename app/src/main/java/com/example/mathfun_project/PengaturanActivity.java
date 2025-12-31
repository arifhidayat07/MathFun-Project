package com.example.mathfun_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PengaturanActivity extends AppCompatActivity {

    private Switch switchSound;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "settings_prefs";
    private static final String SOUND_KEY = "sound_enabled";
    private LinearLayout btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pengaturan);

        switchSound = findViewById(R.id.switch_sound);
        btnKembali = findViewById(R.id.btn_kembali);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSoundEnabled = sharedPreferences.getBoolean(SOUND_KEY, true);
        switchSound.setChecked(isSoundEnabled);

        // Pakai MusicManager untuk kontrol musik
        if (isSoundEnabled) {
            MusicManager.getInstance().startMusic(this);
        } else {
            MusicManager.getInstance().pauseMusic();
        }

        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SOUND_KEY, isChecked);
            editor.apply();

            if (isChecked) {
                MusicManager.getInstance().startMusic(this);
            } else {
                MusicManager.getInstance().pauseMusic();
            }

            String message = isChecked ? "Suara diaktifkan" : "Suara dimatikan";
            Toast.makeText(PengaturanActivity.this, message, Toast.LENGTH_SHORT).show();
        });

        btnKembali.setOnClickListener(v -> {
            Intent intent = new Intent(PengaturanActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Jangan stop mediaPlayer di sini, karena sudah dikelola oleh MusicManager
    }
}
