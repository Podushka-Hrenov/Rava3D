package graphics.core;

import graphics.math.Color3;
import graphics.math.Vec3;

public abstract class BasePart extends PVInstance {
    public Color3 color = Color3.Gray;
    public Vec3 size = Vec3.one.muln(5);

    public BasePart() {}

    public MeshData meshData() {return null;}
}
