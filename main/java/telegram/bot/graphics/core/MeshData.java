package telegram.bot.graphics.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import telegram.bot.graphics.math.Vec3;

public class MeshData {
    public Vec3[] vertexs;
    public int[] tris;
    public Vec3[] normals;

    public MeshData(Vec3[] vertexs, int[] tris) {
        this.vertexs = vertexs;
        this.tris = tris;

        for (int i = 0; i <= tris.length; i += 3) {
            Vec3 v1 = vertexs[tris[i+0]];
            Vec3 v2 = vertexs[tris[i+1]];
            Vec3 v3 = vertexs[tris[i+2]];

            normals[i/3] = v2.sub(v1).cross(v2.sub(v3)).unit();
        }
    }

    public MeshData(FileReader fileReader) throws IOException {
        parseOBJ(fileReader);
    }

    private void parseOBJ(FileReader fileReader) throws IOException {
        BufferedReader reader = new BufferedReader(fileReader);
        String line;

        for (int i = 1; i <= 3; i++) {line = reader.readLine();}

        ArrayList<Vec3> vertexs = new ArrayList<>();
        ArrayList<Vec3> normals = new ArrayList<>();
        ArrayList<Integer> tris = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) continue;

            line = line.replace(',', '.');
            String[] data = line.split("\\s+");
                
            switch (data[0]) {
                case "v": {
                    float x = Float.parseFloat(data[1]);
                    float y = Float.parseFloat(data[2]);
                    float z = Float.parseFloat(data[3]);

                    vertexs.add(new Vec3(x, y, z));
                    break;
                }  
                case "vn": {
                    float x = Float.parseFloat(data[1]);
                    float y = Float.parseFloat(data[2]);
                    float z = Float.parseFloat(data[3]);

                    normals.add(new Vec3(x, y, z));
                    break;
                }
                case "f": {
                    for (int i = 1; i <= 3; i++) {
                        String[] trisData = data[i].split("/");

                        int vertexIndex = Integer.parseInt(trisData[0]) - 1;
                        int normalIndex = Integer.parseInt(trisData[2]) - 1;

                        tris.add(vertexIndex);
                        tris.add(normalIndex);
                    }
                }
            }
        }
            
        this.vertexs = vertexs.toArray(Vec3[]::new);
        this.normals = normals.toArray(Vec3[]::new);
        this.tris = tris.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public String toString() {
        return "MeshData";
    }
}
