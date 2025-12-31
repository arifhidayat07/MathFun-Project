package com.example.mathfun_project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Toast;


public class PengaturanLevelActivity extends AppCompatActivity {

    private Switch switchSound;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "settings_prefs";
    private static final String SOUND_KEY = "sound_enabled";
    private LinearLayout btnKembali;
    Button kembaliKeBeranda;

    private String levelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pengaturan_level);

        switchSound = findViewById(R.id.switch_sound);
        btnKembali = findViewById(R.id.btn_kembali);
        kembaliKeBeranda = findViewById(R.id.btn_kembalikeberanda);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSoundEnabled = sharedPreferences.getBoolean(SOUND_KEY, true);
        switchSound.setChecked(isSoundEnabled);

        levelName = getIntent().getStringExtra("LEVEL_NAME");

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
            Toast.makeText(PengaturanLevelActivity.this, message, Toast.LENGTH_SHORT).show();
        });

        btnKembali.setOnClickListener(v -> {
            Intent intent;
            if (levelName == null) {
                intent = new Intent(PengaturanLevelActivity.this, MainActivity.class);
            } else {
                switch (levelName) {
                    case "Level1":
                        intent = new Intent(PengaturanLevelActivity.this, Level1Activity.class);
                        break;
                    case "Level2":
                        intent = new Intent(PengaturanLevelActivity.this, Level2Activity.class);
                        break;
                    case "Level3":
                        intent = new Intent(PengaturanLevelActivity.this, Level3Activity.class);
                        break;
                    case "Level4":
                        intent = new Intent(PengaturanLevelActivity.this, Level4Activity.class);
                        break;
                    case "Level5":
                        intent = new Intent(PengaturanLevelActivity.this, Level5Activity.class);
                        break;
                    default:
                        intent = new Intent(PengaturanLevelActivity.this, MainActivity.class);
                }
            }
            startActivity(intent);
            finish();
        });

        kembaliKeBeranda.setOnClickListener(v -> {
            Intent intent = new Intent(PengaturanLevelActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
