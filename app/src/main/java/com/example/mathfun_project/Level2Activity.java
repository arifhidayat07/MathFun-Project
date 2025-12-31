package com.example.mathfun_project;

import android.content.Intent;
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

public class Level2Activity extends AppCompatActivity {

    private EditText etAnswer;
    private Button btnJawab;
    private ImageButton btnSettings;
    private TextView tvTimer, tvScore;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 1 menit
    private int score = 20; // Default score jika tidak ada data masuk

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ambil score dari level sebelumnya jika ada
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 20); // default 20 jika tidak ada

        tvTimer = findViewById(R.id.tv_timer);
        etAnswer = findViewById(R.id.et_answer);
        btnJawab = findViewById(R.id.btn_jawab);
        btnSettings = findViewById(R.id.btn_setting);
        tvScore = findViewById(R.id.tv_score);

        updateScoreText();

        btnSettings.setOnClickListener(v -> {
            Intent pengaturanIntent = new Intent(Level2Activity.this, PengaturanLevelActivity.class);
            pengaturanIntent.putExtra("LEVEL_NAME", "Level2");
            startActivity(pengaturanIntent);
        });

        btnJawab.setOnClickListener(v -> {
            String userAnswer = etAnswer.getText().toString().trim();

            if (userAnswer.equals("10")) {
                score += 20;
                Toast.makeText(Level2Activity.this, "Anda benar", Toast.LENGTH_SHORT).show();
                if (countDownTimer != null) countDownTimer.cancel();

                Intent nextIntent = new Intent(Level2Activity.this, Level3Activity.class);
                nextIntent.putExtra("score", score); // kirim score ke Level3
                startActivity(nextIntent);
                finish();
            } else {
                score -= 5;
                if (score <= 0) {
                    Toast.makeText(Level2Activity.this, "Poin habis! Kembali ke menu utama.", Toast.LENGTH_SHORT).show();
                    if (countDownTimer != null) countDownTimer.cancel();

                    Intent backIntent = new Intent(Level2Activity.this, MainActivity.class);
                    backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(backIntent);
                    finish();
                } else {
                    Toast.makeText(Level2Activity.this, "Jawaban salah, poin dikurangi 5.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Level2Activity.this, "Waktu Habis!", Toast.LENGTH_SHORT).show();

                Intent timeoutIntent = new Intent(Level2Activity.this, MainActivity.class);
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
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
