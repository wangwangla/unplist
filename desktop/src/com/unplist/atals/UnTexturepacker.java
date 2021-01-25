package com.unplist.atals;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 *
 */
public class UnTexturepacker extends Game {
    public static void main(String[] args) throws Exception {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 600;
        config.height = 400;
        config.title = "Temp Viewer";
        new LwjglApplication(new UnTexturepacker(), config);
    }

    @Override
    public void create() {
        String arr[] = {"country"};
        for (int i = 0; i < arr.length; i++) {
            String path = "atlas/" + arr[i];

            File file = new File("atlas/"+arr[i]+"/");
            file.mkdir();

            FileHandle handle = Gdx.files.internal(path+".atlas");
            TextureAtlas atlas = new TextureAtlas(handle);
            BufferedImage image = null;
            try {
                image = ImageIO.read(Gdx.files.internal(path+".png").read());
                for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
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
                            "png", new FileOutputStream("atlas/"+arr[i]+"/" + s + ".png"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("全部结束");
    }
}
