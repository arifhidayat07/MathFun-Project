package com.example.mathfun_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ImageButton btnSettings, btnInfo;
    Button btnMulai;
    EditText etUsername;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "settings_prefs";
    private static final String SOUND_KEY = "sound_enabled";

    private static final String USER_PREFS = "user_prefs";
    private static final String USERNAME_KEY = "username";

    private static final String GAME_PREFS = "GamePrefs";
    private static final String SCORE_KEY = "total_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSettings = findViewById(R.id.btn_setting);
        btnInfo = findViewById(R.id.btn_info);
        btnMulai = findViewById(R.id.btn_mulai);
        etUsername = findViewById(R.id.et_username);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSoundEnabled = sharedPreferences.getBoolean(SOUND_KEY, true);

        if (isSoundEnabled) {
            MusicManager.getInstance().startMusic(this);
        }

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PengaturanActivity.class);
            startActivity(intent);
        });

        btnInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InformasiActivity.class);
            startActivity(intent);
        });

        btnMulai.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan username ke SharedPreferences USER_PREFS
            SharedPreferences userPrefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editorUser = userPrefs.edit();
            editorUser.putString(USERNAME_KEY, username);
            editorUser.apply();

            // Reset skor total ke 0 saat mulai permainan baru
            SharedPreferences gamePrefs = getSharedPreferences(GAME_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editorGame = gamePrefs.edit();
            editorGame.putInt(SCORE_KEY, 0);
            editorGame.apply();

            // Mulai Level1Activity, bisa juga kirim username lewat intent jika perlu
            Intent intent = new Intent(MainActivity.this, Level1Activity.class);
            intent.putExtra(USERNAME_KEY, username);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isSoundEnabled = sharedPreferences.getBoolean(SOUND_KEY, true);
        if (isSoundEnabled) {
            MusicManager.getInstance().startMusic(this);
        } else {
            MusicManager.getInstance().pauseMusic();
        }

        // Reset skor di onResume juga agar benar-benar fresh jika user kembali ke main
        SharedPreferences gamePrefs = getSharedPreferences(GAME_PREFS, MODE_PRIVATE);
        gamePrefs.edit().putInt(SCORE_KEY, 0).apply();
    }
}
