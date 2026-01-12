package com.example.mathfun_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat; // IMPORT INI HARUS BENAR

public class PengaturanActivity extends AppCompatActivity {

    // UBAH TIPE VARIABEL INI
    private SwitchCompat switchSound;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "settings_prefs";
    private static final String SOUND_KEY = "sound_enabled";
    private LinearLayout btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pengaturan);

        // Pastikan hardware volume control mengarah ke musik
        setVolumeControlStream(android.media.AudioManager.STREAM_MUSIC);

        // Inisialisasi View - Casting sekarang akan berhasil
        switchSound = findViewById(R.id.switch_sound);
        btnKembali = findViewById(R.id.btn_kembali);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSoundEnabled = sharedPreferences.getBoolean(SOUND_KEY, true);

        // Atur posisi switch sesuai data tersimpan
        switchSound.setChecked(isSoundEnabled);

        // Logika Switch
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

        // Tombol Kembali
        btnKembali.setOnClickListener(v -> finish());
    }
}