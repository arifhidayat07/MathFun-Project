package com.example.mathfun_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenangActivity extends AppCompatActivity {

    private TextView tvScore, tvWinnerMessage;
    private Button btnKembali;
    private LinearLayout btnShare;

    private static final String USER_PREFS = "user_prefs";
    private static final String USERNAME_KEY = "username";

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menang);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi komponen UI
        tvScore = findViewById(R.id.tv_score);
        tvWinnerMessage = findViewById(R.id.tv_winner_message);
        btnKembali = findViewById(R.id.btn_kembalikeberanda);
        btnShare = findViewById(R.id.btn_share);

        // Ambil username dari SharedPreferences
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        username = prefs.getString(USERNAME_KEY, "");

        Log.d("MenangActivity", "Username dari SharedPreferences: " + username);

        if (!username.isEmpty()) {
            tvWinnerMessage.setText(username + ", Kamu Menang. \nAyo Coba Lagi");
        } else {
            tvWinnerMessage.setText("Kamu Menang. Ayo Coba Lagi");
        }

        // Ambil skor dari Intent (bukan SharedPreferences lagi)
        int totalScore = getIntent().getIntExtra("score", 0);
        tvScore.setText("Poin: " + totalScore);

        // Tombol kembali ke beranda
        btnKembali.setOnClickListener(view -> {
            Intent intent = new Intent(MenangActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Tombol Share
        btnShare.setOnClickListener(view -> {
            String shareText = !username.isEmpty()
                    ? username + " berhasil menang di game MathFun! Yuk coba juga!"
                    : "Saya berhasil menang di game MathFun! Yuk coba juga!";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Bagikan melalui"));
        });
    }
}
