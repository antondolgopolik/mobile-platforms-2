package by.bsuir.mobileplatforms2.entity;

import java.io.Serializable;

public class Rating implements Serializable {
    private Float rate;
    private Integer count;

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
