package UI.fileChooser;


import static config.AppConfig.*;

import config.AppConfig;
import config.AppConstants;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

/**
 * A frame that has a menu for loading an image and a display area for the loaded image.
 */
public class ImageViewerFrame extends JFrame {

  private JLabel label;
  private JFileChooser chooser;
  private ButtonFrame instance;

  private AppConfig appConfig;

  public ImageViewerFrame() {

    appConfig = AppConfig.getInstance();

    //
    String picDate = appConfig.getCurDate();


    setLocation(appConfig.left(), appConfig.right());

    setSize(appConfig.width(), appConfig.height());
    String directory = appConfig.getImageDirectory();
    if (!directory.equals(".")) {
      openDirectory(directory);
      pack();
    }

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
//        setPreference(node);
      }

      @Override
      public void windowClosed(WindowEvent e) {
        setPreference();
      }
    });

    // set up menu bar
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    // File -> choose directory
    JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);

    // setting
    JMenu settingMenu = new JMenu("Setting");
    menuBar.add(settingMenu);

    // about
    JMenu about = new JMenu("Help");
    menuBar.add(about);

    JMenuItem aboutItem = new JMenuItem("About");
    about.add(aboutItem);

    // about, copy
    JDialog aboutDialog = new JDialog();
    aboutDialog.setTitle("Copyright");
    aboutDialog.add(new JLabel("<html>© 2019 辣么大(GuodongHu)<br>All rights reserved.</html>"),
        BorderLayout.CENTER);
    aboutDialog.setSize(200, 200);

    aboutItem.addActionListener(e -> {
      aboutDialog.setVisible(true);
    });

    // preference setting

    JMenu preferenceItem = new JMenu("Preference");
    settingMenu.add(preferenceItem);

    JRadioButtonMenuItem Windows = new JRadioButtonMenuItem("Windows");
    JRadioButtonMenuItem Mac = new JRadioButtonMenuItem("Mac");
    var group = new ButtonGroup();
    group.add(Windows);
    group.add(Mac);
    Windows.setSelected(true);

    preferenceItem.add(Windows);
    preferenceItem.add(Mac);

    Windows.addActionListener(e -> {
      // TODO
      // if use
    });

    JMenuItem openItem = new JMenuItem("Open");
    fileMenu.add(openItem);

    addFileChooser(openItem);

    var exitItem = new JMenuItem("Exit");
    fileMenu.add(exitItem);
    exitItem.addActionListener(event -> {
      // save
      setPreference();
      System.exit(0);

    });

    // use a label to display the images
    label = new JLabel();
    add(label);

    // set up file chooser
    setUpFileChooser();

    // accept all image files ending with .jpg, .jpeg, .gif
//    var filter = new FileNameExtensionFilter(
//        "Image files", "jpg", "jpeg", "gif");
//    chooser.setFileFilter(filter);
//
//    chooser.setAccessory(new ImagePreviewer(chooser));

    //chooser.setFileView(new FileIconView(filter, new ImageIcon("palette.gif")));
  }

  private void setPreference() {
    Preferences node = Preferences.userNodeForPackage(ImageViewerFrame.class);
    node.putInt(AppConstants.LEFT, getX());
    node.putInt(AppConstants.RIGHT, getY());
    node.putInt(AppConstants.WIDTH, getWidth());
    node.putInt(AppConstants.HEIGHT, getHeight());
    // current date
    node.put(AppConstants.FILE_DATE, appConfig.getCurDate());

    node.put(AppConstants.GIT_REPO, appConfig.getGitRepo());
    node.getInt(AppConstants.IMAGE_INDEX, appConfig.getCurIdx());
    node.put(AppConstants.IMAGE_DIRECTORY, appConfig.getImageDirectory());
    node.putInt(AppConstants.IMAGE_INDEX, appConfig.getCurIdx());

    // update date
    node.put(AppConstants.CUR_DATE, appConfig.getConfigDate());
  }

  private void setUpFileChooser() {
    chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.setDialogTitle("Choose you image directory.");
  }

  /**
   * add file chooser to choose image directory
   *
   * @param openItem
   */
  private void addFileChooser(JMenuItem openItem) {
    openItem.addActionListener(event -> {
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setCurrentDirectory(new File("."));

      // show file chooser dialog
      int result = chooser.showOpenDialog(ImageViewerFrame.this);

      // if image file accepted, set it as icon of the label
      if (result == JFileChooser.APPROVE_OPTION) {
        String name = chooser.getSelectedFile().getPath();

        // list all image file in reverse order of last modified time.

        // show them one by one on panel or button
        System.out.println("Directory: " + name);
        // add to preferences
        appConfig.setImageDirectory(name);
        Preferences node = Preferences.userNodeForPackage(ImageViewerFrame.class);
        node.put(AppConstants.IMAGE_DIRECTORY, appConfig.getImageDirectory());

        openDirectory(name);

        //
//         Files.isDirectory(new File(name));
        pack();
      }
    });
  }

  private void openDirectory(String name) {
    var dir = new File(name);
    if (dir.isDirectory()) {
      // sorted by last mod time
      List<File> files = Arrays.stream(dir.listFiles()).filter(x -> x.getName().endsWith("jpg"))
          .sorted((x, y) ->
              (int) (y.lastModified() - x.lastModified())
          ).collect(Collectors.toList());
      files = files.subList(0, Math.min(files.size(), 20));
      files.forEach(
          System.out::println
      );

      instance = ButtonFrame.instance();
      instance.addFileIcons(files);
      add(instance);
    }
  }
}
