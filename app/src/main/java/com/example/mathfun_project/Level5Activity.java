package com.example.mathfun_project;

import android.content.Intent;
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

public class Level5Activity extends AppCompatActivity {

    private EditText etAnswer;
    private Button btnJawab;
    private ImageButton btnSettings;
    private TextView tvTimer;
    private TextView tvScore;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000; // 30 detik
    private int score = 20; // default poin awal jika tidak diterima dari Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level5);

        // Set padding untuk sistem bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ambil score dari Intent sebelumnya (default 20)
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 20);

        // Inisialisasi komponen UI
        tvTimer = findViewById(R.id.tv_timer);
        tvScore = findViewById(R.id.tv_score);
        etAnswer = findViewById(R.id.et_answer);
        btnJawab = findViewById(R.id.btn_jawab);
        btnSettings = findViewById(R.id.btn_setting);

        updateScoreText();

        btnSettings.setOnClickListener(v -> {
            Intent intentSettings = new Intent(Level5Activity.this, PengaturanLevelActivity.class);
            intentSettings.putExtra("LEVEL_NAME", "Level5");
            startActivity(intentSettings);
        });

        btnJawab.setOnClickListener(v -> {
            String userAnswer = etAnswer.getText().toString().trim();

            if (userAnswer.equals("5")) {
                score += 20;
                Toast.makeText(Level5Activity.this, "Anda benar!", Toast.LENGTH_SHORT).show();

                if (countDownTimer != null) countDownTimer.cancel();

                // Kirim score ke MenangActivity
                Intent nextIntent = new Intent(Level5Activity.this, MenangActivity.class);
                nextIntent.putExtra("score", score);
                startActivity(nextIntent);
                finish();
            } else {
                score -= 5;
                if (score <= 0) {
                    Toast.makeText(Level5Activity.this, "Poin habis! Kembali ke menu utama.", Toast.LENGTH_SHORT).show();

                    if (countDownTimer != null) countDownTimer.cancel();

                    Intent intentMain = new Intent(Level5Activity.this, MainActivity.class);
                    intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentMain);
                    finish();
                } else {
                    Toast.makeText(Level5Activity.this, "Jawaban salah, poin -5!", Toast.LENGTH_SHORT).show();
                    updateScoreText();
                }
            }

            etAnswer.setText(""); // Kosongkan input setelah menjawab
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
                tvTimer.setText("00:00");
                Toast.makeText(Level5Activity.this, "Waktu habis!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Level5Activity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    private void updateScoreText() {
        tvScore.setText("Poin: " + score);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
