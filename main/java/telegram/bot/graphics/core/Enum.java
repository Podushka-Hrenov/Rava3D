package telegram.bot.graphics.core;

import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;

public class Enum {
    public static HashMap<String, MeshData> PartType = new HashMap<>() {{
        String resources = "C:\\Users\\User\\Desktop\\telegram-bot\\app\\src\\main\\resources\\";

        try {
            put("Block", new MeshData(new FileReader(
                resources + "models\\cube.obj"
            )));
            put("Monkey", new MeshData(new FileReader(
                resources + "models\\monkey.obj"
            )));
        } catch (IOException e) {
            throw new RuntimeException("attempt to load model", e);
        }
    }};
}
