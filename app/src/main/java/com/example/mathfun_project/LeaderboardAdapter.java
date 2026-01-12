package com.example.mathfun_project;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<UserScore> userScoreList;

    public LeaderboardAdapter(List<UserScore> userScoreList) {
        this.userScoreList = userScoreList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leadboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserScore current = userScoreList.get(position);

        holder.tvName.setText(current.getUsername());
        holder.tvScore.setText(String.valueOf(current.getScore()));
        holder.tvRankNumber.setText(String.valueOf(position + 1));

        // --- LOGIKA WARNA TOP 3 ---
        if (position == 0) { // Juara 1 (Emas)
            holder.viewRankBg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD700")));
            holder.tvRankNumber.setTextColor(Color.WHITE);
        } else if (position == 1) { // Juara 2 (Perak)
            holder.viewRankBg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C0C0C0")));
            holder.tvRankNumber.setTextColor(Color.WHITE);
        } else if (position == 2) { // Juara 3 (Perunggu)
            holder.viewRankBg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CD7F32")));
            holder.tvRankNumber.setTextColor(Color.WHITE);
        } else { // Peringkat Biasa
            holder.viewRankBg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F1F1F1")));
            holder.tvRankNumber.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return userScoreList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRankNumber, tvName, tvScore;
        View viewRankBg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRankNumber = itemView.findViewById(R.id.tv_rank_number);
            tvName = itemView.findViewById(R.id.tv_player_name);
            tvScore = itemView.findViewById(R.id.tv_player_score);
            viewRankBg = itemView.findViewById(R.id.view_rank_bg);
        }
    }
}