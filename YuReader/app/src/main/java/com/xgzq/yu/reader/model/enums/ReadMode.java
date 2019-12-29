package com.xgzq.yu.reader.model.enums;

import android.os.Parcel;
import android.os.Parcelable;

public enum ReadMode implements Parcelable {
    TURN(1),
    ROLL(2);

    int value;

    ReadMode(int mode) {
        value = mode;
    }

    public static final Creator<ReadMode> CREATOR = new Creator<ReadMode>() {
        @Override
        public ReadMode createFromParcel(Parcel in) {
            int v = in.readInt();
            switch (v) {
                case 1:
                    return TURN;
                case 2:
                    return ROLL;
            }
            return TURN;
        }

        @Override
        public ReadMode[] newArray(int size) {
            return new ReadMode[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
