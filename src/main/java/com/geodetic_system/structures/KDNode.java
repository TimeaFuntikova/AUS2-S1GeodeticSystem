package com.geodetic_system.structures;

import com.geodetic_system.geodeticObjects.IObjectInSystem;

/**
 * Trieda 'KDNode' reprezentuje uzol v strome.
 * @param <T> typ objektu, ktory je ulozeny v uzle
 */
public class KDNode<T extends IObjectInSystem<T>> {

    private T data;
    private KDNode<T> left;
    private KDNode<T> right;
    private KDNode<T> parent;

    public KDNode(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public KDNode(T data, KDNode<T> left, KDNode<T> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public KDNode<T> getLeft() {
        return left;
    }

    public void setLeft(KDNode<T> left) {
        this.left = left;
    }

    public KDNode<T> getRight() {
        return right;
    }

    public void setRight(KDNode<T> right) {
        this.right = right;
    }

    public KDNode<T> getParent() {
        return parent;
    }

    public void setParent(KDNode<T> parent) {
        this.parent = parent;
    }
}
