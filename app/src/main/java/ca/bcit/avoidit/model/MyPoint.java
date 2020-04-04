package ca.bcit.avoidit.model;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public class MyPoint implements Parcelable {

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

    protected MyPoint(Parcel in) {
        if (in.readByte() == 0) {
            x = null;
        } else {
            x = in.readDouble();
        }
        if (in.readByte() == 0) {
            y = null;
        } else {
            y = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (x == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(x);
        }
        if (y == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(y);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyPoint> CREATOR = new Creator<MyPoint>() {
        @Override
        public MyPoint createFromParcel(Parcel in) {
            return new MyPoint(in);
        }

        @Override
        public MyPoint[] newArray(int size) {
            return new MyPoint[size];
        }
    };
}
