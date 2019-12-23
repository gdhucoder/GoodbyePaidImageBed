package util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by HuGuodong on 11/24/19.
 */
public class ClipTest {

  public static void main(String[] args) {
    for(;;){
      try {
        Clipboard cb = Toolkit.getDefaultToolkit()
            .getSystemClipboard();
        Transferable t = cb.getContents(null);
        if (t.isDataFlavorSupported(DataFlavor.stringFlavor))
          System.out.println(t.getTransferData(DataFlavor
              .stringFlavor));
      } catch (UnsupportedFlavorException | IOException ex) {
        System.out.println("");
      }
    }

  }
}
