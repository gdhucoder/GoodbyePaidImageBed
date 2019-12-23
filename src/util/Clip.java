package util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Created by HuGuodong on 11/24/19.
 */
public class Clip {

  public static void main(String[] args) {

    // This represents a cut-copy (Ctrl+X/Ctrl+C) operation.
    // The text will be copied to the clipboard
    // StringSelection is a Transferable implementation
    // class.

    StringSelection data = new StringSelection
        ("This is copied to the clipboard");
    Clipboard cb = Toolkit.getDefaultToolkit()
        .getSystemClipboard();
    cb.setContents(data, null);

  }
}
