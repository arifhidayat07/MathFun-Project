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

public class Level3Activity extends AppCompatActivity {

    private EditText etAnswer;
    private Button btnJawab;
    private ImageButton btnSettings;
    private TextView tvTimer, tvScore;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 1 menit
    private int score = 20; // Default jika tidak ada data dari intent

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

        // Ambil skor dari Intent yang dikirim dari Level2Activity
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 20); // default 20 jika tidak ada

        tvTimer = findViewById(R.id.tv_timer);
        etAnswer = findViewById(R.id.et_answer);
        btnJawab = findViewById(R.id.btn_jawab);
        btnSettings = findViewById(R.id.btn_setting);
        tvScore = findViewById(R.id.tv_score);

        updateScoreText();

        btnSettings.setOnClickListener(v -> {
            Intent pengaturanIntent = new Intent(Level3Activity.this, PengaturanLevelActivity.class);
            pengaturanIntent.putExtra("LEVEL_NAME", "Level3");
            startActivity(pengaturanIntent);
        });

        btnJawab.setOnClickListener(v -> {
            String userAnswer = etAnswer.getText().toString().trim();

            if (userAnswer.equals("6")) { // Jawaban benar Level 3
                score += 20;
                Toast.makeText(Level3Activity.this, "Anda benar", Toast.LENGTH_SHORT).show();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                // Kirim score ke Level4Activity
                Intent nextIntent = new Intent(Level3Activity.this, Level4Activity.class);
                nextIntent.putExtra("score", score);
                startActivity(nextIntent);
                finish();
            } else {
                score -= 5;
                if (score <= 0) {
                    Toast.makeText(Level3Activity.this, "Poin habis! Kembali ke menu utama.", Toast.LENGTH_SHORT).show();
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    Intent backIntent = new Intent(Level3Activity.this, MainActivity.class);
                    backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(backIntent);
                    finish();
                } else {
                    Toast.makeText(Level3Activity.this, "Jawaban salah! Poin dikurangi 5.", Toast.LENGTH_SHORT).show();
                    updateScoreText();
                }
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
                tvTimer.setText("00:00");
                Toast.makeText(Level3Activity.this, "Waktu Habis!", Toast.LENGTH_SHORT).show();

                Intent timeoutIntent = new Intent(Level3Activity.this, MainActivity.class);
                timeoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(timeoutIntent);
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
