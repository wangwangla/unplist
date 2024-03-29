package com.unplist.atals;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Untext {
    private static final String OUTPUT_TYPE = "png";
    private static final String ATLAS_FILE_EXTENSION = ".atlas";

    /** Checks the command line arguments for correctness.
     * @return 0 If arguments are invalid, Number of arguments otherwise. */
    private int parseArguments (String[] args) {
        int numArgs = args.length;
        // check if number of args is right
        if (numArgs < 1) return 0;
        // check if the input file's extension is right
        boolean extension = args[0].substring(args[0].length() - ATLAS_FILE_EXTENSION.length()).equals(ATLAS_FILE_EXTENSION);
        // check if the directory names are valid
        boolean directory = true;
        if (numArgs >= 2) directory &= checkDirectoryValidity(args[1]);
        if (numArgs == 3) directory &= checkDirectoryValidity(args[2]);
        return extension && directory ? numArgs : 0;
    }

    private boolean checkDirectoryValidity (String directory) {
        File checkFile = new File(directory);
        boolean path = true;
        // try to get the canonical path, if this fails the path is not valid
        try {
            checkFile.getCanonicalPath();
        } catch (Exception e) {
            path = false;
        }
        return path;
    }

    /** Splits an atlas into seperate image and ninepatch files. */
    public void splitAtlas (TextureAtlas.TextureAtlasData atlas, String outputDir) {
        // create the output directory if it did not exist yet
        File outputDirFile = new File(outputDir);
        if (!outputDirFile.exists()) {
            outputDirFile.mkdirs();
            System.out.println(String.format("Creating directory: %s", outputDirFile.getPath()));
        }

        for (TextureAtlas.TextureAtlasData.Page page : atlas.getPages()) {
            // load the image file belonging to this page as a Buffered Image
            BufferedImage img = null;
            try {
                img = ImageIO.read(page.textureFile.file());
            } catch (IOException e) {
                printExceptionAndExit(e);
            }
            for (Region region : atlas.getRegions()) {
                System.out.println(String.format("Processing image for %s: x[%s] y[%s] w[%s] h[%s], rotate[%s]", region.name,
                        region.left, region.top, region.width, region.height, region.rotate));

                // check if the page this region is in is currently loaded in a Buffered Image
                if (region.page == page) {
                    BufferedImage splitImage = null;
                    String extension = null;

                    // check if the region is a ninepatch or a normal image and delegate accordingly
                        splitImage = extractImage(img, region, outputDirFile, 0);
                        if (region.width != region.originalWidth || region.height != region.originalHeight) {
                            BufferedImage originalImg = new BufferedImage(region.originalWidth, region.originalHeight, img.getType());
                            Graphics2D g2 = originalImg.createGraphics();
                            g2.drawImage(splitImage, (int) region.offsetX, (int) (region.originalHeight - region.height - region.offsetY), null);
                            g2.dispose();
                            splitImage = originalImg;
                        }
                        extension = OUTPUT_TYPE;

                    // check if the parent directories of this image file exist and create them if not
                    File imgOutput = new File(outputDirFile,
                            String.format("%s.%s", region.index == -1 ? region.name : region.name + "_" + region.index, extension));
                    File imgDir = imgOutput.getParentFile();
                    if (!imgDir.exists()) {
                        System.out.println(String.format("Creating directory: %s", imgDir.getPath()));
                        imgDir.mkdirs();
                    }

                    // save the image
                    try {
                        ImageIO.write(splitImage, OUTPUT_TYPE, imgOutput);
                    } catch (Exception e) {
                        printExceptionAndExit(e);
                    }
                }
            }
        }
    }

    /** Extract an image from a texture atlas.
     * @param page The image file related to the page the region is in
     * @param region The region to extract
     * @param outputDirFile The output directory
     * @param padding padding (in pixels) to apply to the image
     * @return The extracted image */
    private BufferedImage extractImage (BufferedImage page, Region region, File outputDirFile, int padding) {
        BufferedImage splitImage = null;

        // get the needed part of the page and rotate if needed
        if (region.rotate) {
            BufferedImage srcImage = page.getSubimage(region.left, region.top, region.height, region.width);
            splitImage = new BufferedImage(region.width, region.height, page.getType());

            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(90.0));
            transform.translate(0, -region.width);
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
            op.filter(srcImage, splitImage);
        } else {
            splitImage = page.getSubimage(region.left, region.top, region.width, region.height);
        }

        // draw the image to a bigger one if padding is needed
        if (padding > 0) {
            BufferedImage paddedImage = new BufferedImage(splitImage.getWidth() + padding * 2, splitImage.getHeight() + padding * 2,
                    page.getType());
            Graphics2D g2 = paddedImage.createGraphics();
            g2.drawImage(splitImage, padding, padding, null);
            g2.dispose();
            return paddedImage;
        } else {
            return splitImage;
        }
    }


    private void printExceptionAndExit (Exception e) {
        e.printStackTrace();
        System.exit(1);
    }

//    public static void main (String[] args) {
//        // Opens the atlas file from the specified filename
//        TextureAtlasData atlas = new TextureAtlasData(new FileHandle(atlasFile), new FileHandle(imageDir), false);
//        unpacker.splitAtlas(atlas, outputDir);
//    }
}
