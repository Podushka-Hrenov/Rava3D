package telegram.bot.graphics.math;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class CFrame {
    public final float x, y, z;
    public final float r00, r01, r02;
    public final float r10, r11, r12;
    public final float r20, r21, r22;

    public CFrame(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;

        r00 = 1f; r10 = 0f; r20 = 0f;
        r01 = 0f; r11 = 1f; r21 = 0f;
        r02 = 0f; r12 = 0f; r22 = 1f;
    }

    public CFrame(float x, float y, float z, 
        float r00, float r10, float r20,
        float r01, float r11, float r21,
        float r02, float r12, float r22) {
        
        this.x = x; this.y = y; this.z = z;

        this.r00 = r00; this.r10 = r10; this.r20 = r20;
        this.r01 = r01; this.r11 = r11; this.r21 = r21;
        this.r02 = r02; this.r12 = r12; this.r22 = r22;
    }

    public CFrame(float x, float y, float z, double rx, double ry, double rz) {
        this.x = x; this.y = y; this.z = z;
    
        double crx = cos(rx), srx = sin(rx);
        double cry = cos(ry), sry = sin(ry);
        double crz = cos(rz), srz = sin(rz);

        r00 = (float)(cry * crx);
        r10 = (float)(srz * sry * crx - crz * srx);
        r20 = (float)(crz * sry * crx + srz * srx);

        r01 = (float)(cry * crx);
        r11 = (float)(srz * sry * srx + crz * crx);
        r21 = (float)(crz * sry * srx - srz * crx);

        r02 = (float)(-sry);
        r12 = (float)(srz * cry);
        r22 = (float)(crz * cry);
    }

    public CFrame inverse() {
        return new CFrame(
            -(r00 * x + r01 * y + r02 * z),
            -(r10 * x + r11 * y + r12 * z),
            -(r20 * x + r21 * y + r22 * z),

            r00, r01, r02,
            r10, r11, r12,
            r20, r21, r22
        );
    }

    public Vec3 mulv(Vec3 other) {
        return new Vec3(
            x + r00 * other.x + r01 * other.y + r02 * other.z,
            y + r10 * other.x + r11 * other.y + r12 * other.z,
            z + r20 * other.x + r21 * other.y + r22 * other.z
        );
    }

    public CFrame mul(CFrame other) {
        return new CFrame(
            x + r00 * other.x + r01 * other.y + r02 * other.z,
            y + r10 * other.x + r11 * other.y + r12 * other.z,
            z + r20 * other.x + r21 * other.y + r22 * other.z,
            r00 * other.r00 + r01 * other.r10 + r02 * other.r20,
            r10 * other.r00 + r11 * other.r10 + r12 * other.r20,
            r20 * other.r00 + r21 * other.r10 + r22 * other.r20,
            r00 * other.r01 + r01 * other.r11 + r02 * other.r21,
            r10 * other.r01 + r11 * other.r11 + r12 * other.r21,
            r20 * other.r01 + r21 * other.r11 + r22 * other.r21,
            r00 * other.r02 + r01 * other.r12 + r02 * other.r22,
            r10 * other.r02 + r11 * other.r12 + r12 * other.r22,
            r20 * other.r02 + r21 * other.r12 + r22 * other.r22
        );
    }

    public CFrame rotation() {
        return new CFrame(0, 0, 0, 
            r00, r10, r20, r01, r11, r21, r02, r12, r22
        );
    }

    public Vec3 position() {
        return new Vec3(x, y, z);
    }

    public Vec3 xVector() {
        return new Vec3(r00, r10, r20);
    }

    public Vec3 yVector() {
        return new Vec3(r01, r11, r21);
    }

    public Vec3 zVector() {
        return new Vec3(r02, r12, r22);
    }

    @Override
    public String toString() {
        return String.format(
            "CFrame %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f",
            x, y, z, r00, r10, r20, r01, r11, r21, r02, r12, r22
        );
    }
}