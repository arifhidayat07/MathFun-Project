package com.example.mathfun_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class KalahActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalah);

        MaterialButton btnCobaLagi = findViewById(R.id.btn_coba_lagi);
        MaterialButton btnMenuUtama = findViewById(R.id.btn_menu_utama);
        TextView tvSkorAkhir = findViewById(R.id.tv_skor_akhir);

        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        int skorTerakhir = prefs.getInt("total_score", 0);
        String namaPemain = prefs.getString("username", "Pemain");

        // Ambil info level mana yang gagal dari Intent
        String levelGagal = getIntent().getStringExtra("LEVEL_TERAKHIR");

        tvSkorAkhir.setText("Skor Akhir: " + skorTerakhir);

        // Simpan ke Leaderboard sebelum pindah
        if (skorTerakhir > 0) {
            DBConfig db = new DBConfig(this);
            db.addScore(namaPemain, skorTerakhir);
        }

        // --- MODIFIKASI TOMBOL COBA LAGI ---
        btnCobaLagi.setOnClickListener(v -> {
            Intent intent;

            // Cek level mana yang terakhir dimainkan pemain
            if ("Level2".equals(levelGagal)) {
                intent = new Intent(KalahActivity.this, Level2Activity.class);
            } else if ("Level3".equals(levelGagal)) {
                intent = new Intent(KalahActivity.this, Level3Activity.class);
            } else if ("Level4".equals(levelGagal)) {
                intent = new Intent(KalahActivity.this, Level4Activity.class);
            } else if ("Level5".equals(levelGagal)) {
                intent = new Intent(KalahActivity.this, Level5Activity.class);
            } else {
                // Default jika Level 1 atau info tidak ditemukan
                intent = new Intent(KalahActivity.this, Level1Activity.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnMenuUtama.setOnClickListener(v -> {
            resetGameScore(); // Skor hanya direset jika pemain benar-benar menyerah ke Menu Utama
            Intent intent = new Intent(KalahActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void resetGameScore() {
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        prefs.edit().putInt("total_score", 0).apply();
    }
}