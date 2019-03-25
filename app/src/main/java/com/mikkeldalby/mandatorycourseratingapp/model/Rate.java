package com.mikkeldalby.mandatorycourseratingapp.model;

public class Rate {

    private String lessonId;
    private Float q1;
    private Float q2;
    private Float q3;
    private Float q4;
    private Float q5;
    private Float q6;

    public Rate() {
    }

    public Rate(String lessonId, Float q1, Float q2, Float q3, Float q4, Float q5, Float q6) {
        this.lessonId = lessonId;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
        this.q5 = q5;
        this.q6 = q6;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public Float getQ1() {
        return q1;
    }

    public void setQ1(Float q1) {
        this.q1 = q1;
    }

    public Float getQ2() {
        return q2;
    }

    public void setQ2(Float q2) {
        this.q2 = q2;
    }

    public Float getQ3() {
        return q3;
    }

    public void setQ3(Float q3) {
        this.q3 = q3;
    }

    public Float getQ4() {
        return q4;
    }

    public void setQ4(Float q4) {
        this.q4 = q4;
    }

    public Float getQ5() {
        return q5;
    }

    public void setQ5(Float q5) {
        this.q5 = q5;
    }

    public Float getQ6() {
        return q6;
    }

    public void setQ6(Float q6) {
        this.q6 = q6;
    }

    public Float getAverage(){
        Float total = q1 + q2 + q3 + q4 + q5 + q6;
        Float sub = total / 6;
        return sub;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "lessonId='" + lessonId + '\'' +
                ", q1=" + q1 +
                ", q2=" + q2 +
                ", q3=" + q3 +
                ", q4=" + q4 +
                ", q5=" + q5 +
                ", q6=" + q6 +
                '}';
    }
}
