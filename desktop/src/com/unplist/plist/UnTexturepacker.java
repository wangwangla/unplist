package com.unplist.plist;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 *
 */
public class UnTexturepacker {
    public static void main(String[] args) {
        String parentPath = "./";
        String gameFileName = "ziyuan/xxx";

        File file = new File(parentPath + gameFileName);
        String[] nameStrings = file.list();
        for (String nameString : nameStrings) {
//            if (nameString.contains(".atlas")) {
//                String tmpNameString = nameString.substring(0, nameString.lastIndexOf("."));
//                System.out.println("分割：" + tmpNameString);
//                String targetFileName = tmpNameString;
//                String pathTxt = parentPath + gameFileName + "/" + targetFileName + ".atlas";
//                String pathPNG = parentPath + gameFileName + "/" + targetFileName + ".png";
//                String outPath = parentPath + gameFileName + "/" + targetFileName;
//                toPNG(pathTxt, pathPNG, outPath);
//            }
//            nameString.
//            TextureAtlas.TextureAtlasData textureAtlasData = new TextureAtlas.TextureAtlasData(Gdx.files.internal(""), Gdx.files.internal(""), false);

        }

        TextureAtlas atlas = new TextureAtlas();
//         读取信息
//        new TextureAtlas.TextureAtlasData(packFile, imagesDir, flip);

        System.out.println("全部结束");
    }

    public static void toPNG(String pathTxt, String pathPNG, String OUT) {
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> xy = new ArrayList<String>();
        ArrayList<String> size = new ArrayList<String>();
        try {
            String encoding = "GBK";
            File file = new File(pathTxt);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int lineNum = 0, lineNum2 = 0;

                while ((lineTxt = bufferedReader.readLine()) != null) {
                    if (lineTxt.length() > 0) {
                        lineNum++;
                        if (lineNum2 > 0)
                            lineNum2++;
                        if (lineNum == 5)
                            lineNum2 = 1;
                        if (lineNum % 7 == 5)
                            name.add(lineTxt);
                        if (lineNum2 % 7 == 3)
                            xy.add(lineTxt);
                        if (lineNum2 % 7 == 4)
                            size.add(lineTxt);
                    }
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
            BufferedImage image = (BufferedImage) ImageIO.read(new File(pathPNG));
            for (int i = 0; i < name.size(); i++) {
                String p1 = name.get(i), p2 = xy.get(i), p3 = size.get(i);

                int x = 0, y = 0, w = 0, h = 0, flag = 0;
                for (int j = 0; j < p2.length(); j++) {
                    if (p2.charAt(j) <= '9' && p2.charAt(j) >= '0') {
                        if (flag == 0) {
                            x = x * 10 + p2.charAt(j) - '0';
                        } else {
                            y = y * 10 + p2.charAt(j) - '0';
                        }
                    }
                    if (p2.charAt(j) == ',')
                        flag = 1;

                }
                flag = 0;
                for (int j = 0; j < p3.length(); j++) {
                    if (p3.charAt(j) <= '9' && p3.charAt(j) >= '0') {
                        if (flag == 0)
                            w = w * 10 + p3.charAt(j) - '0';
                        else
                            h = h * 10 + p3.charAt(j) - '0';
                    }
                    if (p3.charAt(j) == ',')
                        flag = 1;

                }

                File f = new File(OUT);
                if (!f.exists())
                    f.mkdirs();
                ImageIO.write(image.getSubimage(x, y, w, h), "png", new FileOutputStream(OUT + "/" + p1 + ".png"));
                System.out.println(p1 + ":finished");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错：" + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
