package com.example.mathfun_project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import java.util.Random;

public class ParticleEffect {
    private final ViewGroup container;
    private final Random random = new Random();

    public ParticleEffect(ViewGroup container) {
        this.container = container;
    }

    public void emitParticles(int count) {
        for (int i = 0; i < count; i++) {
            createParticle();
        }
    }

    private void createParticle() {
        Context context = container.getContext();
        View particle = new View(context);

        // Membuat bentuk lingkaran kecil
        int size = random.nextInt(20) + 10;
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);

        // Warna warni ceria (Kuning, Biru, Putih)
        int[] colors = {Color.YELLOW, Color.CYAN, Color.WHITE, Color.parseColor("#FFD700")};
        shape.setColor(colors[random.nextInt(colors.length)]);
        particle.setBackground(shape);

        container.addView(particle, new ViewGroup.LayoutParams(size, size));

        // Posisi awal acak
        particle.setX(random.nextInt(container.getWidth()));
        particle.setY(-50); // Mulai dari atas layar

        // Animasi jatuh
        ObjectAnimator animator = ObjectAnimator.ofFloat(particle, "translationY", container.getHeight() + 100);
        animator.setDuration(random.nextInt(3000) + 2000); // Kecepatan acak
        animator.setInterpolator(new AccelerateInterpolator());

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container.removeView(particle);
            }
        });

        animator.start();

    }

    // Tambahkan metode ini di dalam class ParticleEffect.java yang sudah dibuat sebelumnya
    public void emitVictoryConfetti(int count) {
        for (int i = 0; i < count; i++) {
            createVictoryParticle();
        }
    }

    private void createVictoryParticle() {
        Context context = container.getContext();
        View particle = new View(context);

        // Ukuran bervariasi antara kotak dan lingkaran (seperti potongan kertas)
        int size = random.nextInt(15) + 10;
        GradientDrawable shape = new GradientDrawable();
        if (random.nextBoolean()) {
            shape.setShape(GradientDrawable.OVAL);
        } else {
            shape.setShape(GradientDrawable.RECTANGLE);
        }

        // Warna-warna Kemenangan
        int[] colors = {
                Color.parseColor("#FFD700"), // Gold
                Color.parseColor("#C0C0C0"), // Silver
                Color.parseColor("#FF4500"), // Orange Red
                Color.parseColor("#FFFFFF"), // Putih
                Color.parseColor("#FF69B4")  // Pink ceria
        };
        shape.setColor(colors[random.nextInt(colors.length)]);
        particle.setBackground(shape);

        container.addView(particle, new ViewGroup.LayoutParams(size, size));

        // Posisi awal acak di seluruh lebar layar (bagian atas)
        particle.setX(random.nextInt(container.getWidth()));
        particle.setY(-100);

        // Rotasi agar terlihat seperti kertas terbang
        particle.setRotation(random.nextInt(360));

        // Animasi jatuh dengan sedikit goyangan (translationX)
        ObjectAnimator fall = ObjectAnimator.ofFloat(particle, "translationY", container.getHeight() + 100);
        ObjectAnimator shake = ObjectAnimator.ofFloat(particle, "translationX", particle.getX() + (random.nextInt(200) - 100));
        ObjectAnimator rotate = ObjectAnimator.ofFloat(particle, "rotation", random.nextInt(720));

        android.animation.AnimatorSet set = new android.animation.AnimatorSet();
        set.playTogether(fall, shake, rotate);
        set.setDuration(random.nextInt(2000) + 3000);
        set.setInterpolator(new android.view.animation.LinearInterpolator());

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container.removeView(particle);
            }
        });

        set.start();
    }


}