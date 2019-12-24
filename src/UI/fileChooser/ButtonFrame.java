package UI.fileChooser;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import util.FileUtil;

/**
 * Created by HuGuodong on 2019/11/16.
 */
public class ButtonFrame extends JPanel {

  private JPanel buttonPanel;
  private List<IconJButton> allThumbs = new ArrayList<>();

  private static ButtonFrame picPanel = new ButtonFrame();

  private ButtonFrame() {
    buttonPanel = new JPanel();
  }

  public static ButtonFrame instance() {
    return picPanel;
  }

  public void addFileIcons(List<File> files) {
    files.forEach(f -> {
      IconJButton thumbButton = genIconJButton(f);
      buttonPanel.add(thumbButton);
      allThumbs.add(thumbButton);
    });

    buttonPanel.setLayout(new GridLayout(files.size() / 4 + 1, 4));
    add(buttonPanel, BorderLayout.CENTER);
  }


  public void updateButtonFrame() {
    buttonPanel.removeAll();
    // TODO: add all buttons
    allThumbs.forEach(x -> {
      buttonPanel.add(x);
    });
    buttonPanel.updateUI();
  }

  private IconJButton genIconJButton(File f) {
    ImageIcon icon = new ImageIcon(f.getAbsolutePath());
    icon = new ImageIcon(icon.getImage().getScaledInstance(
        200, 200, Image.SCALE_DEFAULT));

    IconJButton thumbButton = new IconJButton(icon, f);

    ActionListener listener = new MDLinkActionListener(f);
    thumbButton.addActionListener(listener);
    return thumbButton;
  }

  public void addButtonIcon(Path p) {

    IconJButton thumbButton = genIconJButton(p.toFile());

    allThumbs.add(thumbButton);
    allThumbs.sort((x, y) ->
        Long.compare(y.lastModified,x.lastModified));
    allThumbs = allThumbs.subList(0, Math.min(allThumbs.size(), 20));
  }


  /**
   * remove button icon (because of remove file)
   * @param p
   */
  public void removeButtonIcon(Path p) {

  }

  /**
   * MDLinkActionListener
   */
  private class MDLinkActionListener implements ActionListener {

    private File file;

    public MDLinkActionListener(File file) {
      this.file = file;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      // set lint to clip board
      FileUtil.genGitHubLink(file.getName());
    }
  }

  /**
   * IconJButton Used in Panel
   */
  private class IconJButton extends JButton {

    // file lastModified time
    private long lastModified;

    public IconJButton(ImageIcon icon, File file) {
      super(icon);
      lastModified = file.lastModified();
    }

  }


}


