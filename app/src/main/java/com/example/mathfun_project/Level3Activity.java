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

public class Level3Activity extends AppCompatActivity {

    private EditText etAnswer;
    private Button btnJawab;
    private ImageButton btnSettings;
    private TextView tvTimer, tvScore;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 1 menit

    // Gunakan KEY yang konsisten dengan Level 1, 2, dan KalahActivity
    private static final String PREFS_NAME = "GamePrefs";
    private static final String KEY_SCORE = "total_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level3);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi View
        tvTimer = findViewById(R.id.tv_timer);
        etAnswer = findViewById(R.id.et_answer);
        btnJawab = findViewById(R.id.btn_jawab);
        btnSettings = findViewById(R.id.btn_setting);
        tvScore = findViewById(R.id.tv_score);

        // Ambil dan tampilkan skor akumulasi dari SharedPreferences
        updateScoreDisplay();

        btnSettings.setOnClickListener(v -> {
            Intent pengaturanIntent = new Intent(Level3Activity.this, PengaturanLevelActivity.class);
            pengaturanIntent.putExtra("LEVEL_NAME", "Level3");
            startActivity(pengaturanIntent);
        });

        btnJawab.setOnClickListener(v -> {
            String userAnswer = etAnswer.getText().toString().trim();

            if (userAnswer.equals("6")) { // Jawaban benar Level 3
                Toast.makeText(Level3Activity.this, "Anda benar!", Toast.LENGTH_SHORT).show();
                if (countDownTimer != null) countDownTimer.cancel();

                // Tambah poin 20 ke SharedPreferences
                addScore(20);

                // Lanjut ke Level 4
                Intent nextIntent = new Intent(Level3Activity.this, Level4Activity.class);
                startActivity(nextIntent);
                finish();
            } else {
                // Jawaban salah: Kurangi poin 5
                subtractScore(5);
                Toast.makeText(Level3Activity.this, "Jawaban salah, poin -5", Toast.LENGTH_SHORT).show();
            }
            etAnswer.setText("");
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
                goToKalahActivity();
            }
        }.start();
    }

    private void goToKalahActivity() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int currentScore = prefs.getInt(KEY_SCORE, 0);

        Intent intent = new Intent(Level3Activity.this, KalahActivity.class);
        // Kunci "SCORE" harus sama dengan yang dipanggil di KalahActivity
        intent.putExtra("SCORE", currentScore);
        intent.putExtra("FAILED_LEVEL", "Level3");
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
        int newScore = Math.max(0, currentScore - points);
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