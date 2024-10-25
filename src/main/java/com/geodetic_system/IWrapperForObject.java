package com.geodetic_system;

public interface IWrapperForObject<T> {
    double getLeftTopX();
    double getLeftTopY();
    double getRightBottomX();
    double getRightBottomY();
    boolean dontTheyIntersect(T a, T b);
}
