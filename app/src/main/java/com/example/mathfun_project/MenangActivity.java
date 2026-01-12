package com.example.mathfun_project;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MenangActivity extends AppCompatActivity {

    private TextView tvScore, tvWinnerMessage;
    private Button btnKembali;
    private MaterialButton btnShare;

    // Gunakan Key yang konsisten dengan MainActivity
    private static final String GAME_PREFS = "GamePrefs";
    private static final String USERNAME_KEY = "username";
    private static final String SCORE_KEY = "total_score";

    private String username;
    private int totalScore;
    private DBConfig dbConfig;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menang);

        // Pengaturan Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Inisialisasi Database & View
        dbConfig = new DBConfig(this);
        tvScore = findViewById(R.id.tv_score);
        tvWinnerMessage = findViewById(R.id.tv_winner_message);
        btnKembali = findViewById(R.id.btn_kembalikeberanda);
        btnShare = findViewById(R.id.btn_share);

        // 2. Ambil data dari SharedPreferences agar data Paling Akurat (Nama & Skor)
        SharedPreferences prefs = getSharedPreferences(GAME_PREFS, MODE_PRIVATE);
        username = prefs.getString(USERNAME_KEY, "Player");
        totalScore = prefs.getInt(SCORE_KEY, 0);

        // 3. Tampilkan data ke UI
        tvScore.setText("Poin: " + totalScore);
        tvWinnerMessage.setText(username + ", Kamu Menang! \nAyo Coba Lagi");

        // 4. JALANKAN PROSES SIMPAN (Panggil tanpa parameter agar tidak merah)
        simpanSkorKeDatabase();

        // 5. Logika Tombol
        btnKembali.setOnClickListener(view -> {
            Intent intent = new Intent(MenangActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnShare.setOnClickListener(view -> {
            String shareText = username + " berhasil menang di MathFun dengan skor " + totalScore + "!";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Bagikan melalui"));
        });

        // Jalankan Efek Partikel & Animasi Visual
        setupVisualEffects();
    }

    // Fungsi Simpan Versi Final (Data diambil langsung dari gudang penyimpanan)
    private void simpanSkorKeDatabase() {
        try {
            SharedPreferences prefs = getSharedPreferences(GAME_PREFS, MODE_PRIVATE);
            String namaTerbaru = prefs.getString(USERNAME_KEY, "Player");
            int skorTerakhir = prefs.getInt(SCORE_KEY, 0);

            if (skorTerakhir > 0) {
                // Masukkan ke database SQLite
                dbConfig.addScore(new UserScore(namaTerbaru, skorTerakhir));
                Log.d("DB_LOG", "Berhasil Simpan Leaderboard: " + namaTerbaru + " (" + skorTerakhir + ")");
            }
        } catch (Exception e) {
            Log.e("DB_LOG", "Error SQLite: ", e);
        }
    }

    private void setupVisualEffects() {
        // Efek Partikel Selebrasi
        FrameLayout victoryContainer = findViewById(R.id.victory_particle_container);
        if (victoryContainer != null) {
            ParticleEffect celebration = new ParticleEffect(victoryContainer);
            victoryContainer.post(() -> {
                celebration.emitVictoryConfetti(50);
                final android.os.Handler handler = new android.os.Handler();
                handler.post(new Runnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count < 5) {
                            celebration.emitVictoryConfetti(10);
                            count++;
                            handler.postDelayed(this, 1000);
                        }
                    }
                });
            });
        }

        // Animasi Logo & Card
        ImageView logoKemenangan = findViewById(R.id.logoKemenangan);
        CardView scoreCard = findViewById(R.id.scoreCard);

        if (logoKemenangan != null) {
            Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            logoKemenangan.startAnimation(scaleAnimation);

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(logoKemenangan, "scaleX", 1.0f, 1.05f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(logoKemenangan, "scaleY", 1.0f, 1.05f);
            scaleX.setRepeatCount(ObjectAnimator.INFINITE);
            scaleX.setRepeatMode(ObjectAnimator.REVERSE);
            scaleY.setRepeatCount(ObjectAnimator.INFINITE);
            scaleY.setRepeatMode(ObjectAnimator.REVERSE);

            AnimatorSet pulse = new AnimatorSet();
            pulse.playTogether(scaleX, scaleY);
            pulse.setDuration(1000);
            pulse.start();
        }

        if (scoreCard != null) {
            scoreCard.setAlpha(0f);
            scoreCard.animate().alpha(1f).setDuration(1000).setStartDelay(500).start();
        }
    }
}