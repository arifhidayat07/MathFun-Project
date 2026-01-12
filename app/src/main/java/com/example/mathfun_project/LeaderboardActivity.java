package com.example.mathfun_project;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView rvLeaderboard;
    private DBConfig dbConfig;
    private MaterialButton btnBack;
    private ImageButton btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // 1. Inisialisasi View berdasarkan ID di XML terbaru
        rvLeaderboard = findViewById(R.id.rv_leaderboard);
        btnBack = findViewById(R.id.btn_back_leaderboard);
        btnClear = findViewById(R.id.btn_clear_leaderboard);

        // Inisialisasi Database
        dbConfig = new DBConfig(this);

        // 2. Setup RecyclerView
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        rvLeaderboard.setHasFixedSize(true);

        // 3. Panggil fungsi loadData untuk pertama kali
        loadData();

        // 4. Logika Tombol Hapus (Clear Leaderboard)
        btnClear.setOnClickListener(v -> {
            showClearConfirmationDialog();
        });

        // 5. Navigasi Kembali
        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Menggunakan onResume agar data selalu segar saat pengguna
     * kembali ke halaman ini dari activity lain.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    /**
     * Menampilkan dialog konfirmasi sebelum menghapus semua data di database.
     */
    private void showClearConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Peringkat")
                .setMessage("Apakah Anda yakin ingin menghapus semua daftar skor terbaik?")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setPositiveButton("Ya, Hapus", (dialog, which) -> {
                    dbConfig.clearAllScores(); // Menjalankan query DELETE di DBConfig
                    loadData(); // Refresh tampilan RecyclerView menjadi kosong
                    Toast.makeText(this, "Data berhasil dibersihkan", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    /**
     * Fungsi untuk mengambil data dari SQLite dan memasangnya ke Adapter.
     */
    private void loadData() {
        // Mengambil list data yang sudah diurutkan dari DBConfig
        List<UserScore> listData = dbConfig.getAllScores();

        // Memasang data ke Adapter
        LeaderboardAdapter adapter = new LeaderboardAdapter(listData);
        rvLeaderboard.setAdapter(adapter);
    }
}