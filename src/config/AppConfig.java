package config;

import UI.fileChooser.ImageViewerFrame;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.prefs.Preferences;

/**
 * Created by HuGuodong on 12/23/19.
 */
public class AppConfig {

  private static final String date = "YYYY-MM-dd";
  private static final SimpleDateFormat sdf = new SimpleDateFormat(date);
  private static final NumberFormat df = new DecimalFormat("000");

  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 400;

  private static AppConfig appConfig = new AppConfig();

  public static AppConfig getInstance() {
    return appConfig;
  }

  private Window window;
  private String gitRepo;
  private String curDate;
  private String configDate;
  private String imageDirectory;
  private int curIdx;

  private AppConfig() {
    window = new Window();
    Preferences node = Preferences.userNodeForPackage(ImageViewerFrame.class);
    window.left = node.getInt(AppConstants.LEFT, 0);
    window.right = node.getInt(AppConstants.RIGHT, 0);
    window.width = node.getInt(AppConstants.WIDTH, DEFAULT_WIDTH);
    window.height = node.getInt(AppConstants.HEIGHT, DEFAULT_HEIGHT);
    imageDirectory = node.get(AppConstants.IMAGE_DIRECTORY, ".");
    curIdx = node.getInt(AppConstants.IMAGE_INDEX, 0);
    // local date
    configDate = sdf.format(Calendar.getInstance().getTime());

    // date saved in config, may be less than config date(local date)
    curDate = node.get(AppConstants.CUR_DATE, "");
    if (!curDate.equals(configDate)) {
      curIdx = 0;
      curDate = configDate;
    }
    System.out.println("default date: " + configDate);
    System.out.println("current date: " + curDate);
    System.out.println("width: " + window.width);
    System.out.println("height: " + window.height);
    System.out.println("left: " + window.left);
    System.out.println("right: " + window.right);
  }


  public Window getWindow() {
    return window;
  }


  public String getGitRepo() {
    return gitRepo;
  }

  public void setGitRepo(String gitRepo) {
    this.gitRepo = gitRepo;
  }

  public String getCurDate() {
    return curDate;
  }

  public void setCurDate(String curDate) {
    this.curDate = curDate;
  }

  public String getConfigDate() {
    return configDate;
  }

  public void setConfigDate(String configDate) {
    this.configDate = configDate;
  }

  public int getCurIdx() {
    return curIdx;
  }

  public void setCurIdx(int curIdx) {
    this.curIdx = curIdx;
  }

  public String getImageDirectory() {
    return imageDirectory;
  }

  public void setImageDirectory(String imageDirectory) {
    this.imageDirectory = imageDirectory;
  }

  public int width() {
    return window.width;
  }

  public int height() {
    return window.height;
  }

  public int left() {
    return window.left;
  }

  public int right() {
    return window.right;
  }

  private class Window {

    private int left;
    private int right;
    private int height;
    private int width;

    private Window() {
    }

    public int getLeft() {
      return left;
    }

    public void setLeft(int left) {
      this.left = left;
    }

    public int getRight() {
      return right;
    }

    public void setRight(int right) {
      this.right = right;
    }

    public int getHeight() {
      return height;
    }

    public void setHeight(int height) {
      this.height = height;
    }

    public int getWidth() {
      return width;
    }

    public void setWidth(int width) {
      this.width = width;
    }
  }
}
