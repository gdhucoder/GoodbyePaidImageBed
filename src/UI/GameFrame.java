package UI;

/**
 * Created by HuGuodong on 12/8/19.
 */
import UI.fileChooser.ImageViewerFrame;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameFrame extends JFrame implements ActionListener{

  CardLayout cardLayout;
  JPanel mainPanel;
  MenuPanel menu;
  GamePanel game;

  public GameFrame() {
    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);
    menu = new MenuPanel();
    game = new GamePanel();
    mainPanel.add(menu, "menu");
    mainPanel.add(game, "game");


    JButton goGame = new JButton("Go TO Game");
    goGame.addActionListener(this);

    add(mainPanel);
    add(goGame, BorderLayout.SOUTH);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationByPlatform(true);
    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    gameOn();
  }

  public void gameOn() {
    cardLayout.show(mainPanel, "game");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable(){
      @Override
      public void run() {
        GameFrame gameFrame = new GameFrame();
      }
    });
  }
}

class MenuPanel extends JPanel {

  public MenuPanel() {
    setBackground(Color.GREEN);
    add(new JLabel("Menu"));
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(300, 300);
  }
}

class GamePanel extends JPanel {

  public GamePanel() {
    setBackground(Color.BLUE);
    add(new JLabel("Game"));
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(300, 300);
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      var frame = new GameFrame();
      frame.setTitle("FuckImageBed");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
    });
  }
}
