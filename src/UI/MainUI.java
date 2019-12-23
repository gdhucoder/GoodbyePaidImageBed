package UI;

import UI.fileChooser.ImageViewerFrame;
import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * Created by HuGuodong on 2019/11/16.
 */
public class MainUI {

  /**
   * start main UI
   */
  public static  void startMainUI(){
    EventQueue.invokeLater(() -> {
      var frame = new ImageViewerFrame();
      frame.setTitle("FuckImageBed");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
    });
  }

  /**
   * Start Main UI and watch dir
   * @param dir
   */
  public static void start(String dir){
    dir = "";
    startMainUI();
//    FileModifiedWatchDog.startWatch(dir);
  }



  public static void main(String[] args) {
    String dir = "";
    start(dir);
  }
}
