package com.example.quiz;

import androidx.annotation.NonNull;

public class qustion {

    String qu;
    String an1;
    String an2;
    String an3;
    String an4;
    int ran;

    public qustion(String all, int ran) {
        String[] parts = all.split("\\|");
        if (parts.length >= 5) {
            this.qu = parts[0];
            this.an1 = parts[1];
            this.an2 = parts[2];
            this.an3 = parts[3];
            this.an4 = parts[4];
        }
        this.ran = ran;

    }

    public String getQu() {
        return qu;
    }

    public void setQu(String qu) {
        this.qu = qu;
    }

    public String getAn1() {
        return an1;
    }

    public void setAn1(String an1) {
        this.an1 = an1;
    }

    public String getAn2() {
        return an2;
    }

    public void setAn2(String an2) {
        this.an2 = an2;
    }

    public String getAn3() {
        return an3;
    }

    public void setAn3(String an3) {
        this.an3 = an3;
    }

    public String getAn4() {
        return an4;
    }

    public void setAn4(String an4) {
        this.an4 = an4;
    }

    public int getRan() {
        return ran;
    }

    public void setRan(int ran) {
        this.ran = ran;
    }

    @NonNull
    @Override
    public String toString() {
        return "qustion{" +
                "qu='" + qu + '\'' +
                ", an1='" + an1 + '\'' +
                ", an2='" + an2 + '\'' +
                ", an3='" + an3 + '\'' +
                ", an4='" + an4 + '\'' +
                ", ran=" + ran +
                '}';
    }
}
