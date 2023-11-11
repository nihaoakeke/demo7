package com.blue.utils;

import com.blue.domain.Blog;

import java.util.Comparator;

public class ComparatorUtils implements Comparator<Blog> {

    private double weight1;
    private double weight2;

    public ComparatorUtils(double weight1, double weight2) {
        this.weight1 = weight1;
        this.weight2 = weight2;
    }

    @Override
    public int compare(Blog blog1, Blog blog2) {
        double weightedValue1 = blog1.getBlogLike() * weight1 + blog1.getBlogView() * weight2;
        double weightedValue2 = blog2.getBlogLike() * weight1 + blog2.getBlogView() * weight2;

        return -Double.compare(weightedValue1, weightedValue2);
    }
}