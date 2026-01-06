package telegram.bot.graphics.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MeshPart extends BasePart {
    private MeshData meshData;

    public MeshPart(File modelFile) throws IOException {
        meshData = new MeshData(new FileReader(modelFile));
    }

    @Override
    public MeshData meshData() {
        return meshData;
    }
}
