package com.example.depthmapping.ui.home;

public class NNPoint {
    String type;
    String accuracy;

    public NNPoint(String type, String accuracy) {
        this.type = type;
        this.accuracy = accuracy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}
