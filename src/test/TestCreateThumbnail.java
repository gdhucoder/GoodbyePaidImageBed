package test;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * create thumbnail pictures
 * Created by HuGuodong on 2019/11/16.
 */
public class TestCreateThumbnail {
  public static void createThumbnail(String filename, int thumbWidth, int thumbHeight)
      throws InterruptedException, FileNotFoundException, IOException
  {
    // load image from filename
    Image image = Toolkit.getDefaultToolkit().getImage(filename);
    MediaTracker mediaTracker = new MediaTracker(new Container());
    mediaTracker.addImage(image, 0);
    mediaTracker.waitForID(0);
    // use this to test for errors at this point: System.out.println(mediaTracker.isErrorAny());

    // determine thumbnail size from WIDTH and HEIGHT
    double thumbRatio = (double)thumbWidth / (double)thumbHeight;
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    double imageRatio = (double)imageWidth / (double)imageHeight;
    if (thumbRatio < imageRatio) {
      thumbHeight = (int)(thumbWidth / imageRatio);
    } else {
      thumbWidth = (int)(thumbHeight * imageRatio);
    }

    // draw original image to thumbnail image object and
    // scale it to the new size on-the-fly
    BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = thumbImage.createGraphics();
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

    // save thumbnail image to outFilename
    File input = new File(filename);
    File output = new File(input.getParentFile() + File.separator + "thumnail_" + input.getName());
    ImageIO.write(thumbImage, "jpg", output);
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    createThumbnail("imagedir/IMAG0725_BURST021.jpg",350,350);
  }
}
