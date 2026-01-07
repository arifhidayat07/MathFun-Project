package com.example.mathfun_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        // Menjalankan perintah create table saat database pertama kali dibuat
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Menghapus tabel lama jika ada update versi dan membuat yang baru
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // --- METODE HELPER UNTUK OPERASI DATA (CRUD) ---

    // Fungsi untuk Simpan Data
    public void addScore(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SCORE, score);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Fungsi untuk Ambil Semua Data
    public Cursor getAllScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_SCORE + " DESC", null);
    }
}