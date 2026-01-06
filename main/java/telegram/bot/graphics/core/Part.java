package telegram.bot.graphics.core;

public class Part extends BasePart {
    public String shape = "Block";

    public MeshData meshData() {
        return Enum.PartType.get(shape);
    }
}
