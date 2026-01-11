package graphics.core;

import graphics.math.CFrame;
import graphics.math.Vec3;

public abstract class PVInstance extends Instance {
    public CFrame cframe;

    public PVInstance() {
        cframe = new CFrame(0, 0, 0);
    }

    public Vec3 position() {
        return cframe.position();
    }
}
