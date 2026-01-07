package com.example.mathfun_project;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView rvLeaderboard;
    private DBConfig dbConfig;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        rvLeaderboard = findViewById(R.id.rv_leaderboard);
        btnBack = findViewById(R.id.btn_back_leaderboard);
        dbConfig = new DBConfig(this);

        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        loadData();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadData() {
        Cursor cursor = dbConfig.getAllScores();
        // Sederhananya, kita bisa kirim cursor ke adapter atau convert ke List
        // Di sini saya asumsikan Anda membuat Adapter bernama ScoreAdapter
        ScoreAdapter adapter = new ScoreAdapter(cursor);
        rvLeaderboard.setAdapter(adapter);
    }
}