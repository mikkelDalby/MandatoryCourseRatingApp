package com.mikkeldalby.mandatorycourseratingapp.model;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CombinedRating {
    private Lesson lesson;
    private List<Rate> ratings = new ArrayList<>();

    private DecimalFormat df = new DecimalFormat("#.00");

    public CombinedRating() {
    }

    public CombinedRating(Lesson lesson) {
        this.lesson = lesson;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void addRating(Rate rate){
        ratings.add(rate);
    }

    public List<Rate> getRatings() {
        return ratings;
    }

    public double getAverage(){
        double count = 0;
        double total = 0;

        for (Rate r: ratings){
            double sub = 0;
            sub += Double.parseDouble(r.getQ1().toString());
            sub += Double.parseDouble(r.getQ2().toString());
            sub += Double.parseDouble(r.getQ3().toString());
            sub += Double.parseDouble(r.getQ4().toString());
            sub += Double.parseDouble(r.getQ5().toString());
            sub += Double.parseDouble(r.getQ6().toString());
            sub = sub / 6;
            total += sub;
            count += 1;
        }
        return total/count;
    }

    @Override
    public String toString() {
        if ((getAverage()+"").equals("NaN")){
            return lesson.getName() + " - No ratings";
        } else {
            return lesson.getName() + " - avg: " + df.format(getAverage());
        }
    }
}
