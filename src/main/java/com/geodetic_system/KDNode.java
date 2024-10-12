package com.geodetic_system;

public class KDNode<T extends IObjectInSystem<T, R>, R> {

    private final T data;
    private KDNode<T, R> left, right, parent;

    public KDNode(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public T getData() {
        return data;
    }

    public KDNode<T, R> getLeft() {
        return left;
    }

    public void setLeft(KDNode<T, R> left) {
        this.left = left;
    }

    public KDNode<T, R> getRight() {
        return right;
    }

    public void setRight(KDNode<T, R> right) {
        this.right = right;
    }

    public KDNode<T, R> getParent() {
        return parent;
    }

    public void setParent(KDNode<T, R> parent) {
        this.parent = parent;
    }
}
