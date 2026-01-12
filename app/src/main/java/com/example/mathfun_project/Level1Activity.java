package com.example.mathfun_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
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

// ... (Bagian import tetap sama)

public class Level1Activity extends AppCompatActivity {

    private EditText etAnswer;
    private Button btnJawab;
    private ImageButton btnSettings;
    private TextView tvTimer, tvPoin;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000;

    private static final String PREFS_NAME = "GamePrefs";
    private static final String KEY_SCORE = "total_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level1);

        // Pengaturan Insets (untuk layar penuh)
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        tvTimer = findViewById(R.id.tv_timer);
        tvPoin = findViewById(R.id.tv_score);
        etAnswer = findViewById(R.id.et_answer);
        btnJawab = findViewById(R.id.btn_jawab);
        btnSettings = findViewById(R.id.btn_setting);

        updateScoreDisplay();

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Level1Activity.this, PengaturanLevelActivity.class);
            intent.putExtra("LEVEL_NAME", "Level1");
            startActivity(intent);
        });

        btnJawab.setOnClickListener(v -> {
            String userAnswer = etAnswer.getText().toString().trim();

            if (userAnswer.equals("4")) {
                Toast.makeText(Level1Activity.this, "Anda benar", Toast.LENGTH_SHORT).show();
                if (countDownTimer != null) countDownTimer.cancel();

                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                int currentScore = prefs.getInt(KEY_SCORE, 0);
                int updatedScore = currentScore + 20;

                prefs.edit().putInt(KEY_SCORE, updatedScore).apply();
                updateScoreDisplay();

                Intent intent = new Intent(Level1Activity.this, Level2Activity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Level1Activity.this, "Anda salah", Toast.LENGTH_SHORT).show();
            }
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
                // PANGGIL FUNGSI GAME OVER SAAT WAKTU HABIS
                goToKalahActivity();
            }
        }.start();
    }

    // FUNGSI UNTUK PINDAH KE KALAH ACTIVITY (DIPERBAIKI)
    private void goToKalahActivity() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int currentScore = prefs.getInt(KEY_SCORE, 0);

        Intent intent = new Intent(Level1Activity.this, KalahActivity.class);
        // Pastikan KEY "SCORE" sama dengan yang dipanggil di KalahActivity
        intent.putExtra("SCORE", currentScore);
        intent.putExtra("FAILED_LEVEL", "Level1");

        startActivity(intent);
        finish();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateScoreDisplay() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int currentScore = prefs.getInt(KEY_SCORE, 0);
        tvPoin.setText("Poin: " + currentScore);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
