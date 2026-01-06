package telegram.bot.graphics.core;

import telegram.bot.graphics.math.CFrame;
import telegram.bot.graphics.math.Vec2;
import telegram.bot.graphics.math.Vec3;
import telegram.bot.graphics.ui.ImagePanel;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.tan;

public class Camera extends PVInstance {
    private float aspect, xScale, yScale, zProjection, zTranslation;
    private Vec2 imageSize;
    private ImagePanel image;

    public Camera(Vec2 imageSize) {
        aspect = imageSize.x / imageSize.y;

        image = new ImagePanel((short)imageSize.x, (short)imageSize.y);
        this.imageSize = imageSize;
    }

    public void settings(double fov, float near, float far) {
        xScale = (float) (1.0 / (aspect * tan(fov / 2)));
        yScale = (float) (1.0 /tan(fov/2));
        zProjection = -(far + near) / (far - near);
        zTranslation = -2.0f * far * near / (far - near);
    }

    private Vec3 project3dVertexTo2d(CFrame modelOrigin, Vec3 vertex) {
        Vec3 i = cframe.inverse().mul(modelOrigin).mulv(vertex);

        return new Vec3(
            xScale*i.x/i.z*imageSize.x, 
            yScale*i.y/i.z*imageSize.y, 
            zProjection * i.z + zTranslation
        );
    }

    public File render() throws IOException {
        image.clear(new Color(0,0,0));
        List<Instance> instances = parent().children();

        for (Instance instance : instances) {
            if (!(instance instanceof BasePart)) {continue;}

            BasePart basePart = (BasePart) instance;
            MeshData meshData = basePart.meshData();

            Vec3[] TranslatedVertexsToWorld = Arrays.stream(meshData.vertexs)
                .map(vec -> project3dVertexTo2d(basePart.cframe, vec))
                .toArray(Vec3[]::new)
            ;

            for (int i = 0; i < meshData.tris.length; i += 6) {
                Vec3 normal = basePart.cframe.rotation().mulv(meshData.normals[meshData.tris[i+1]]);
                
                Vec3 v1 = TranslatedVertexsToWorld[meshData.tris[i+0]];
                Vec3 v2 = TranslatedVertexsToWorld[meshData.tris[i+2]];
                Vec3 v3 = TranslatedVertexsToWorld[meshData.tris[i+4]];

                float scalar = Vec3.yAxis.dot(normal);
                Color color = basePart.color.muln(scalar + 1.2f).toColor();

                image.writeTris(v1, v2, v3, color);
            }
        }

        return image.createFile("C:\\Users\\User\\Desktop\\", "Render.png");
    }
}