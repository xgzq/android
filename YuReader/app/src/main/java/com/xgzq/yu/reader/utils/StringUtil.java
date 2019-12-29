package com.xgzq.yu.reader.utils;

public class StringUtil {
    public static final int POSITION_LEFT = 1;
    public static final int POSITION_RIGHT = 2;


    public static String leftShift(String target, String addStr, int len) {
        return shiftString(target, addStr, len, POSITION_LEFT);
    }

    public static String leftShift(String target, int len) {
        return shiftString(target, " ", len, POSITION_LEFT);
    }

    public static String rightShift(String target, String addStr, int len) {
        return shiftString(target, addStr, len, POSITION_RIGHT);
    }

    public static String rightShift(String target, int len) {
        return shiftString(target, " ", len, POSITION_RIGHT);
    }

    public static String shiftString(String target, String addStr, int len, int position) {
        if (target == null) {
            throw new NullPointerException("leftShift => target is null!");
        }
        if (addStr == null) {
            addStr = " ";
        }
        if (addStr.length() != 1) {
            throw new IllegalArgumentException("addStr length must be 1");
        }
        while (target.length() < len) {
            if (position == POSITION_LEFT) {
                target = addStr + target;
            } else if (position == POSITION_RIGHT) {
                target = target + addStr;
            }
        }
        return target;
    }
}
