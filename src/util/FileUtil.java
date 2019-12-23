package util;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

import config.AppConfig;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by HuGuodong on 11/20/19.
 */
public class FileUtil {

  private FileUtil() {

  }

  private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("000");


  /**
   * get current index number from config
   * @return
   */
  public static int getIndex() {
    return AppConfig.getInstance().getCurIdx();
  }

  private static void updateIdx(int idx) {
    AppConfig.getInstance().setCurIdx(idx);
  }


  public static String genNewFileName() {

    // get new idx
    int idx = getIndex();
    // generate file name
    String fileName = String.format("%s_%s.jpg", AppConfig.getInstance().getCurDate(), NUMBER_FORMAT
        .format(idx));
    // updateIdx in properties
    updateIdx(++idx);

    return fileName;
  }


  /**
   * Reformat Image File Name to YYYY-MM-dd-000 e.g. 2019-11-20-001
   *
   * @param original
   */
  public static void reFormatFileName(Path original) {

    String newFileName = genNewFileName();
    System.out.println(genGitHubLink(newFileName));

    try {
      Files.move(original, original.resolveSibling(newFileName), ATOMIC_MOVE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * getGitHubLink and set it to clipboard
   * @param fileName
   * @return
   */
  public static String genGitHubLink(String fileName) {
    String prefix = AppConfig.getInstance().getGitRepo();
    String link = prefix + fileName;
    String mdLink = String.format("![%s](%s)",fileName,link);
    // set link to clipboard
    setToClipBoard(mdLink);
    return link;
  }


  /**
   * @param original
   */
  public static void reFormatFileName(String original) {
    if (new File(original).isFile()) {
      reFormatFileName(Paths.get(original));
    }
  }

  /**
   * Reformat a directory of image files
   *
   * @param dir
   * @param suffix
   */
  public static void reFormatFileName(String dir, String suffix) {
    File file = new File(dir);
    FileFilter fileFilter = new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        // Screen Shot 2019-11-23 at 05.50.59
        if (pathname.getName().lastIndexOf(suffix) != -1
            && pathname.getName().lastIndexOf("Screen Shot") != -1) {
          return true;
        } else {
          return false;
        }
      }
    };
    if (file.isDirectory()) {
      for (File f : file.listFiles(fileFilter)) {
        reFormatFileName(f.getAbsolutePath());
      }
    }
  }

  /**
   * set string data to clipboard, that can be further used to `copy` to anywhere
   *
   * @param str
   */
  public static void setToClipBoard(String str) {
    StringSelection data = new StringSelection
        (str);
    Clipboard cb = Toolkit.getDefaultToolkit()
        .getSystemClipboard();
    cb.setContents(data, null);
  }


  public static void main(String[] args) {

    String directory = "/Users/gdhu/projects/giteeimagebed/testimagebed";
    reFormatFileName(directory, "jpg");

  }
}
