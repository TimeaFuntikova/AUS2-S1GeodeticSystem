package com.geodetic_system;

import java.util.logging.Level;
import java.util.logging.Logger;

public class KDNode {
    private final Logger log = Logger.getLogger(KDNode.class.getName());

    private final GPSPosition position;
    private final IObjectInSystem<?> data;
    private KDNode left;
    private KDNode right;
    private KDNode parent;

    public KDNode(GPSPosition position, IObjectInSystem<?> data) {
        this.position = position;
        this.data = data;
        this.left = null;
        this.right = null;
        this.parent = null;
        log.setLevel(Level.FINE);  // Set the log level to FINE to capture detailed logs
        log.fine("Created new node with position: " + position.toString());
    }

    public GPSPosition getPosition() {
        return position;
    }

    public IObjectInSystem<?> getData() {
        return data;
    }

    public KDNode getLeft() {
        return left;
    }

    public void setLeft(KDNode left) {
        this.left = left;
        if (left != null) {
            left.parent = this;
            log.fine("Set parent of left child to: " + this.position.toString());
        }
    }

    public KDNode getRight() {
        return right;
    }

    public void setRight(KDNode right) {
        this.right = right;
        if (right != null) {
            right.parent = this;
            log.fine("Set parent of right child to: " + this.position.toString());
        }
    }

    public KDNode getParent() {
        return parent;
    }

    public void setParent(KDNode parent) {
        this.parent = parent;
    }

}
