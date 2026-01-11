package graphics.core;

import graphics.core.enums.PartType;

public class Part extends BasePart {
    public PartType shape = PartType.BLOCK;

    public MeshData meshData() {
        return shape.meshData;
    }
}
