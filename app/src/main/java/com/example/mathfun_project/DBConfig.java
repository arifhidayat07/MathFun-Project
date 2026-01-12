package com.example.mathfun_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBConfig extends SQLiteOpenHelper {

    // Konfigurasi Database
    private static final String DATABASE_NAME = "MathFun.db";
    private static final int DATABASE_VERSION = 1;

    // Struktur Tabel
    public static final String TABLE_NAME = "user_scores";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "username";
    public static final String COLUMN_SCORE = "score";

    // Query untuk membuat tabel
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_SCORE + " INTEGER);";

    public DBConfig(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // --- METODE UNTUK OPERASI DATA ---

    /**
     * Menambahkan skor baru menggunakan Objek UserScore.
     * Digunakan di KalahActivity: db.addScore(new UserScore("Nama", skor));
     */
    public void addScore(UserScore userScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, userScore.getUsername());
            values.put(COLUMN_SCORE, userScore.getScore());

            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("DB_ERROR", "Gagal insert data: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * Menambahkan skor baru menggunakan Parameter String & Int.
     */
    public void addScore(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_SCORE, score);

            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("DB_ERROR", "Gagal insert data: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * Mengambil semua skor dan diurutkan dari yang tertinggi (DESC).
     * Digunakan di LeaderboardActivity untuk mengisi RecyclerView.
     */
    public List<UserScore> getAllScores() {
        List<UserScore> scoreList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query: Urutkan berdasarkan Skor Terbesar (DESC)
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_SCORE + " DESC";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                    int score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE));

                    scoreList.add(new UserScore(name, score));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Gagal ambil data: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return scoreList;
    }

    /**
     * Opsional: Menghapus semua data skor (Reset Leaderboard).
     */
    public void clearAllScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}