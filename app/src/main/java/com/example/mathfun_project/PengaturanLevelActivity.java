package com.example.mathfun_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat; // Gunakan SwitchCompat agar sesuai XML

public class PengaturanLevelActivity extends AppCompatActivity {

    // Menggunakan SwitchCompat untuk menghindari ClassCastException
    private SwitchCompat switchSound;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "settings_prefs";
    private static final String SOUND_KEY = "sound_enabled";
    private LinearLayout btnKembali;
    private Button kembaliKeBeranda;

    private String levelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pengaturan_level);

        // Menyelaraskan volume hardware dengan aliran musik sistem
        setVolumeControlStream(android.media.AudioManager.STREAM_MUSIC);

        // Inisialisasi View
        switchSound = findViewById(R.id.switch_sound);
        btnKembali = findViewById(R.id.btn_kembali);
        kembaliKeBeranda = findViewById(R.id.btn_kembalikeberanda);

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSoundEnabled = sharedPreferences.getBoolean(SOUND_KEY, true);

        // Atur status switch saat ini
        switchSound.setChecked(isSoundEnabled);

        // Ambil levelName untuk menentukan tombol kembali akan ke level mana
        levelName = getIntent().getStringExtra("LEVEL_NAME");

        // Kelola musik saat Activity dibuka
        if (isSoundEnabled) {
            MusicManager.getInstance().startMusic(this);
        } else {
            MusicManager.getInstance().pauseMusic();
        }

        // Listener untuk Switch Suara
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(SOUND_KEY, isChecked).apply();

            if (isChecked) {
                MusicManager.getInstance().startMusic(this);
            } else {
                MusicManager.getInstance().pauseMusic();
            }

            String message = isChecked ? "Suara diaktifkan" : "Suara dimatikan";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        // Logika Tombol Kembali (Berdasarkan level asal)
        btnKembali.setOnClickListener(v -> {
            if (levelName == null) {
                finish(); // Jika level tidak diketahui, tutup saja
            } else {
                Intent intent;
                switch (levelName) {
                    case "Level1": intent = new Intent(this, Level1Activity.class); break;
                    case "Level2": intent = new Intent(this, Level2Activity.class); break;
                    case "Level3": intent = new Intent(this, Level3Activity.class); break;
                    case "Level4": intent = new Intent(this, Level4Activity.class); break;
                    case "Level5": intent = new Intent(this, Level5Activity.class); break;
                    default: intent = new Intent(this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });

        // Logika Tombol Kembali ke Beranda
        kembaliKeBeranda.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            // Membersihkan semua stack activity agar kembali ke beranda dengan bersih
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}