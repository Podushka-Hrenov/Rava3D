package graphics.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Function;

import graphics.math.ScreenPoint;

class DrawingCache {
    public ScreenPoint TrisB = ScreenPoint.identily();
    public ScreenPoint TrisA = ScreenPoint.identily();
    public ScreenPoint TrisV1p = ScreenPoint.identily();
}

public class ImagePanel {
    public final int height, width;
    public final BufferedImage image;

    private final float[] zBuffer;
    private final Function<Float, Float> depthDistorter;
    private final DrawingCache drawingCache;
    
    public ImagePanel(int width, int height, Function<Float, Float> depthDistorter) {
        this.height = height; this.width = width;
        this.depthDistorter = depthDistorter;
        
        zBuffer = new float[width*height];
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawingCache = new DrawingCache();

        Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);
    }

    public boolean write(ScreenPoint point, Color color) {
        return write(point.x, point.y, point.depth, color);
    }

    public boolean write(int x, int y, float depth, Color color) {
        x += width/2; y += height/2;

        if (x < 0 || x >= width || y < 0 || y >= height) return false;

        int bufferIndex = y * width + x;
        if (depth > zBuffer[bufferIndex]) return true;

        zBuffer[bufferIndex] = depth;
        image.setRGB(x, y, color.getRGB());
        
        return true;
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public void writeTris(ScreenPoint v1, ScreenPoint v2,
    ScreenPoint v3, Color color) {
        
        if (v3.y > v1.y) {var temp = v1; v1 = v3; v3 = temp;}
        if (v2.y > v1.y) {var temp = v1; v1 = v2; v2 = temp;}
        if (v2.y < v3.y) {var temp = v3; v3 = v2; v2 = temp;}

        var v1p = drawingCache.TrisV1p.lerpLocal(
            v3, v1, (v2.y - v3.y)/(float)(v1.y - v3.y)
        );

        var A = drawingCache.TrisA; var B = drawingCache.TrisB;
        float segHeight = v2.y - v3.y;

        for (int y = 0; y < segHeight; y++) {
            float alpha = y / segHeight;

            A.lerpLocal(v3, v2, alpha);
            B.lerpLocal(v3, v1p, alpha);
             
            if (A.x > B.x) {var temp = A; A = B; B = temp;}
            float width = B.x - A.x;

            for (int x = A.x; x < B.x; x++) {
                float depth = 1.0f / lerp(A.depth, B.depth, (x - A.x)/width);
                write(x, v3.y + y, depthDistorter.apply(depth), color);
            }
        }
        
        segHeight = v1.y - v2.y;

        for (int y = 0; y < segHeight; y++) {
            float alpha = y / segHeight;

            A.lerpLocal(v2, v1, alpha);
            B.lerpLocal(v1p, v1, alpha);
                
            if (A.x > B.x) {var temp = A; A = B; B = temp;}
            float width = B.x - A.x;

            for (int x = A.x; x < B.x; x++) {
                float depth = 1.0f / lerp(A.depth, B.depth, (x - A.x)/width);
                write(x, v2.y + y, depthDistorter.apply(depth), color);
            }
        }
    }

    public void clear(Color fillColor) {
        int rgb = fillColor.getRGB();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, rgb);
            }
        }

        Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);
    }
}