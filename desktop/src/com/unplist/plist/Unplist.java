package com.unplist.plist;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

/**
 * plist unpack   可以使用
 */
public class Unplist extends Game {
    private String[] unplistName = {
            "main"
    };
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 600;
        config.height = 400;
        config.title = "Temp Viewer";
        new LwjglApplication(new Unplist(), config);
    }

    @Override
    public void create() {
        for (String fileName : unplistName) {
            PlistAtlasOther plistAtlas = new PlistAtlasOther(Gdx.files.internal(fileName+".plist"));
            try {
                File file = new File(fileName+"/");
                file.mkdir();
                BufferedImage image = ImageIO.read(Gdx.files.internal(fileName+".png").read());
                for (TextureAtlas.AtlasRegion region : plistAtlas.getRegions()) {
                    String name = region.name;
                    TextureRegion regionTemp = new TextureRegion(region);
                    int x = regionTemp.getRegionX();
                    int y = regionTemp.getRegionY();
                    int width = regionTemp.getRegionWidth();
                    int height = regionTemp.getRegionHeight();
                    String[] split = name.split("/");
                    String s = split[split.length - 1];
                    //最 核心的一句
                    ImageIO.write(image.getSubimage(x, y, width, height),
                            "png", new FileOutputStream(fileName+"/" + s + ".png"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
