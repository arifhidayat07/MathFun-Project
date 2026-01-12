package com.example.mathfun_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnSettings, btnInfo, btnPeringkat;
    private Button btnMulai;
    private EditText etUsername;
    private ImageView logo;

    // Nama file dan Key harus konsisten di seluruh Activity
    private static final String PREFS_NAME = "settings_prefs";
    private static final String SOUND_KEY = "sound_enabled";

    private static final String GAME_PREFS = "GamePrefs";
    private static final String USERNAME_KEY = "username";
    private static final String SCORE_KEY = "total_score";

    private SharedPreferences sharedPreferences;
    private FrameLayout particleContainer;
    private ParticleEffect particleEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Pengaturan padding untuk sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Inisialisasi View
        btnSettings = findViewById(R.id.btn_setting);
        btnInfo = findViewById(R.id.btn_info);
        btnMulai = findViewById(R.id.btn_mulai);
        btnPeringkat = findViewById(R.id.btn_peringkat);
        etUsername = findViewById(R.id.et_username);
        logo = findViewById(R.id.logo);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // 2. Animasi Logo
        if (logo != null) {
            Animation floatingAnimation = AnimationUtils.loadAnimation(this, R.anim.floating_animation);
            logo.startAnimation(floatingAnimation);
        }

        // 3. Logika Tombol Mulai
        btnMulai.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            // SIMPAN DATA KE GAME_PREFS
            SharedPreferences gamePrefs = getSharedPreferences(GAME_PREFS, MODE_PRIVATE);
            gamePrefs.edit()
                    .putString(USERNAME_KEY, username)
                    .putInt(SCORE_KEY, 0) // Reset skor menjadi 0 setiap kali mulai baru
                    .apply();

            // Pindah ke Level 1
            Intent intent = new Intent(MainActivity.this, Level1Activity.class);
            startActivity(intent);
        });

        // 4. Tombol Navigasi Lainnya
        btnPeringkat.setOnClickListener(v -> startActivity(new Intent(this, LeaderboardActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, PengaturanActivity.class)));
        btnInfo.setOnClickListener(v -> startActivity(new Intent(this, InformasiActivity.class)));

        setupParticles();
    }

    private void setupParticles() {
        particleContainer = findViewById(R.id.particle_container);
        if (particleContainer != null) {
            particleEffect = new ParticleEffect(particleContainer);
            final android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (particleContainer.getWidth() > 0) {
                        particleEffect.emitParticles(3);
                    }
                    handler.postDelayed(this, 1500);
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cek pengaturan suara
        boolean isSoundEnabled = sharedPreferences.getBoolean(SOUND_KEY, true);
        if (isSoundEnabled) {
            MusicManager.getInstance().startMusic(this);
        } else {
            MusicManager.getInstance().pauseMusic();
        }

        // Pastikan skor di-reset ke 0 saat kembali ke menu utama
        getSharedPreferences(GAME_PREFS, MODE_PRIVATE).edit().putInt(SCORE_KEY, 0).apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (logo != null) logo.clearAnimation(); //
    }
}