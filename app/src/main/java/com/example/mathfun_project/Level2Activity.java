package com.example.mathfun_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Level2Activity extends AppCompatActivity {

    private EditText etAnswer;
    private Button btnJawab;
    private ImageButton btnSettings;
    private TextView tvTimer, tvScore;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 1 menit

    // Kunci SharedPreferences yang konsisten dengan Level 1
    private static final String PREFS_NAME = "GamePrefs";
    private static final String KEY_SCORE = "total_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level2);

        // Pengaturan Insets agar tidak tertutup Notch/Status Bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi View
        tvTimer = findViewById(R.id.tv_timer);
        tvScore = findViewById(R.id.tv_score);
        etAnswer = findViewById(R.id.et_answer);
        btnJawab = findViewById(R.id.btn_jawab);
        btnSettings = findViewById(R.id.btn_setting);

        // Update tampilan poin awal (diambil dari Level 1 via SharedPreferences)
        updateScoreDisplay();

        // Tombol Setting
        btnSettings.setOnClickListener(v -> {
            Intent pengaturanIntent = new Intent(Level2Activity.this, PengaturanLevelActivity.class);
            pengaturanIntent.putExtra("LEVEL_NAME", "Level2");
            startActivity(pengaturanIntent);
        });

        // Tombol Jawab
        btnJawab.setOnClickListener(v -> {
            String userAnswer = etAnswer.getText().toString().trim();

            if (userAnswer.equals("10")) {
                Toast.makeText(Level2Activity.this, "Anda benar!", Toast.LENGTH_SHORT).show();
                if (countDownTimer != null) countDownTimer.cancel();

                // Tambah poin 20 ke SharedPreferences
                addScore(20);

                // Lanjut ke Level 3
                Intent nextIntent = new Intent(Level2Activity.this, Level3Activity.class);
                startActivity(nextIntent);
                finish();
            } else {
                // Jawaban salah: Kurangi poin 5
                subtractScore(5);
                Toast.makeText(Level2Activity.this, "Jawaban salah, poin -5", Toast.LENGTH_SHORT).show();
            }
            etAnswer.setText(""); // Kosongkan input
        });

        startTimer();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                // Waktu habis: Pindah ke KalahActivity
                goToKalahActivity();
            }
        }.start();
    }

    private void goToKalahActivity() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int currentScore = prefs.getInt(KEY_SCORE, 0);

        Intent intent = new Intent(Level2Activity.this, KalahActivity.class);
        intent.putExtra("SCORE", currentScore); // Kunci "SCORE" sesuai standar kita
        intent.putExtra("FAILED_LEVEL", "Level2");
        intent.putExtra("LEVEL_TERAKHIR", "Level2");
        startActivity(intent);
        finish();
    }

    private void addScore(int points) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int currentScore = prefs.getInt(KEY_SCORE, 0);
        prefs.edit().putInt(KEY_SCORE, currentScore + points).apply();
        updateScoreDisplay();
    }

    private void subtractScore(int points) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int currentScore = prefs.getInt(KEY_SCORE, 0);
        int newScore = Math.max(0, currentScore - points); // Agar tidak minus
        prefs.edit().putInt(KEY_SCORE, newScore).apply();
        updateScoreDisplay();

        if (newScore <= 0) {
            if (countDownTimer != null) countDownTimer.cancel();
            goToKalahActivity(); // Poin habis = Kalah
        }
    }

    private void updateScoreDisplay() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int currentScore = prefs.getInt(KEY_SCORE, 0);
        tvScore.setText("Poin: " + currentScore);
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}