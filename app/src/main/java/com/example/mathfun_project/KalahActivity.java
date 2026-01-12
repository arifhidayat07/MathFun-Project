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

        // 1. Inisialisasi View
        MaterialButton btnCobaLagi = findViewById(R.id.btn_coba_lagi);
        MaterialButton btnMenuUtama = findViewById(R.id.btn_menu_utama);
        TextView tvSkorAkhir = findViewById(R.id.tv_skor_akhir);

        // 2. Ambil data dari SharedPreferences (SUMBER PALING AKURAT)
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);

        // Ambil skor akumulasi terbaru (kunci "total_score" harus sama dengan di LevelActivity)
        int skorTerakhir = prefs.getInt("total_score", 0);

        // Ambil nama pemain (kunci "username" harus sama dengan saat Login/Input Nama)
        String namaPemain = prefs.getString("username", "Pemain");

        // Tampilkan skor di UI
        tvSkorAkhir.setText("Skor Akhir: " + skorTerakhir);

        // 3. Simpan ke Database SQLite untuk Leaderboard
        // Pastikan skor lebih dari 0 agar leaderboard tetap bersih dari skor kosong
        if (skorTerakhir > 0) {
            DBConfig db = new DBConfig(this);
            db.addScore(new UserScore(namaPemain, skorTerakhir));
        }

        // 4. Tombol Coba Lagi (Reset poin dan balik ke Level 1)
        btnCobaLagi.setOnClickListener(v -> {
            resetGameScore(); // Reset total_score menjadi 0
            Intent intent = new Intent(KalahActivity.this, Level1Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 5. Tombol Menu Utama
        btnMenuUtama.setOnClickListener(v -> {
            resetGameScore(); // Reset total_score menjadi 0
            Intent intent = new Intent(KalahActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Sangat Penting: Menghapus poin sesi ini agar saat mulai lagi tidak membawa poin lama.
     */
    private void resetGameScore() {
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        prefs.edit().putInt("total_score", 0).apply();
    }
}