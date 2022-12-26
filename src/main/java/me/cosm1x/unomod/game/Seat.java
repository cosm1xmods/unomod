package me.cosm1x.unomod.game;

public class Seat {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    
    protected double getX() {
        return x;
    }
    
    protected double getY() {
        return y;
    }

    protected double getZ() {
        return z;
    }

    protected float getYaw() {
        return yaw;
    }

    protected float getPitch() {
        return pitch;
    }

    protected Seat(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
    }

    protected Seat(Double x, Double y, Double z, Float yaw, Float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

}
