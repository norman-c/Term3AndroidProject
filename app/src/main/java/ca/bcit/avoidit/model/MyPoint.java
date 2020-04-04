package ca.bcit.avoidit.model;

import android.graphics.Point;

public class MyPoint extends Point {

    public Double x;
    public Double y;

    public MyPoint(){
        this.x = 0.0;
        this.y = 0.0;
    }

    public MyPoint(Double  x, Double y ){
        this.x = x;
        this.y = y;
    }
}
