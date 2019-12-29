package com.xgzq.yu.reader.model.enums;

import android.os.Parcel;
import android.os.Parcelable;

public enum DisplayMode implements Parcelable {
    LIST(1),
    GRID(2);

    int value;

    DisplayMode(int mode) {
        value = mode;
    }

    public static final Creator<DisplayMode> CREATOR = new Creator<DisplayMode>() {
        @Override
        public DisplayMode createFromParcel(Parcel in) {
            int v = in.readInt();
            switch (v) {
                case 1:
                    return LIST;
                case 2:
                    return GRID;
            }
            return LIST;
        }

        @Override
        public DisplayMode[] newArray(int size) {
            return new DisplayMode[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value);
    }
}
