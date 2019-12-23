package util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Created by HuGuodong on 2019/11/16.
 */
public class ThumbnailGenerator {

  private ThumbnailGenerator() {

  }

  public static void createThumbnail(File input, int thbWidth, int thbHeight) {
    File output = null;
    BufferedImage img = new BufferedImage(thbWidth, thbHeight, BufferedImage.TYPE_INT_RGB);
    try {
      Graphics2D graphics2D = img.createGraphics();
      graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      graphics2D
          .drawImage(ImageIO.read(input).getScaledInstance(thbWidth, thbHeight, Image.SCALE_SMOOTH),
              0, 0, null
          );
      output = new File(input.getParentFile() + File.separator + "thumnail_" + input.getName());
      ImageIO.write(img, "jpg", output);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void createThumbnail(String filePath, int thbWidth, int thbWeight) {
    createThumbnail(new File(filePath), thbWidth, thbWeight);
  }

  public static void main(String[] args) {
    createThumbnail("imagedir/IMAG0646.jpg", 200, 200);
  }
}
