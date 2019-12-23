package monitor;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import static util.FileUtil.reFormatFileName;
import static util.Git.submitGitChanges;

import UI.MainUI;
import UI.fileChooser.ButtonFrame;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuGuodong on 2019/11/19.
 */
public class WatchDirBak {

  private final WatchService watcher;
  private final Map<WatchKey, Path> keys;
  private final boolean recursive;
  private boolean trace = false;

  @SuppressWarnings("unchecked")
  static <T> WatchEvent<T> cast(WatchEvent<?> event) {
    return (WatchEvent<T>) event;
  }

  /**
   * Register the given directory with the WatchService
   */
  private void register(Path dir) throws IOException {

    WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    if (trace) {
      Path prev = keys.get(key);
      if (prev == null) {
        System.out.format("register: %s\n", dir);
      } else {
        if (!dir.equals(prev)) {
          System.out.format("update: %s -> %s\n", prev, dir);
        }
      }
    }
    keys.put(key, dir);
  }

  /**
   * Register the given directory, and all its sub-directories, with the WatchService.
   */
  private void registerAll(final Path start) throws IOException {
    // register directory and sub-directories
    Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
          throws IOException {
        register(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }

  /**
   * Creates a WatchService and registers the given directory
   */
  WatchDirBak(Path dir, boolean recursive) throws IOException {
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<WatchKey, Path>();
    this.recursive = recursive;

    if (recursive) {
      System.out.format("Scanning %s ...\n", dir);
      registerAll(dir);
      System.out.println("Done.");
    } else {
      register(dir);
    }

    // enable trace after initial registration
    this.trace = true;
  }

  /**
   * Process all events for keys queued to the watcher
   */
  void processEvents() {
    for (; ; ) {

      // wait for key to be signalled
      WatchKey key;
      try {
        key = watcher.take();
      } catch (InterruptedException x) {
        return;
      }

      Path dir = keys.get(key);
      if (dir == null) {
        System.err.println("WatchKey not recognized!!");
        continue;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind kind = event.kind();

        // TBD - provide example of how OVERFLOW event is handled
        if (kind == OVERFLOW) {
          continue;
        }

        // Context for directory entry event is the file name of entry
        WatchEvent<Path> ev = cast(event);
        Path name = ev.context();
        Path child = dir.resolve(name);

//        if (child.startsWith("Screen Shot") && kind == ENTRY_CREATE) {
//          continue;
//        }

        // print out event
        System.out.format("%s: %s\n", event.kind().name(), child);

        // if new screen capture comes, rename the image file name
//        if (kind == ENTRY_CREATE && name.toString().indexOf("Screen Shot") != -1) {
        if (kind == ENTRY_CREATE && name.toString().indexOf("Screen Shot") != -1) {
          reFormatFileName(child);
        } else if (name.toString().startsWith("2019") || name.toString().startsWith("2020")) {
          submitGitChanges();
          if (kind == ENTRY_CREATE) {
            ButtonFrame.instance().addButtonIcon(child);
            ButtonFrame.instance().updateButtonFrame();
          } else if (kind == ENTRY_DELETE) {
            // TODO add feature delete file and update ui

          }

        }

        // submit to changes to git
//        submitGitChanges();

        // if directory is created, and watching recursively, then
        // register it and its sub-directories
        if (recursive && (kind == ENTRY_CREATE)) {
          try {
            if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
              registerAll(child);
            }
          } catch (IOException x) {
            // ignore to keep sample readbale
          }
        }
      }

      // reset key and remove from set if directory no longer accessible
      boolean valid = key.reset();
      if (!valid) {
        keys.remove(key);

        // all directories are inaccessible
        if (keys.isEmpty()) {
          break;
        }
      }
    }
  }

  static void usage() {
    System.err.println("usage: java WatchDir [-r] dir");
    System.exit(-1);
  }

  public static void main(String[] args) throws IOException {
    // parse arguments
    if (args.length == 0 || args.length > 2)
      usage();
    boolean recursive = false;
    int dirArg = 0;
    if (args[0].equals("-r")) {
      if (args.length < 2)
        usage();
      recursive = true;
      dirArg++;
    }

    // register directory and process its events
    // monitor.test
    // /Users/gdhu/projects/personal/fuckImageBed/src/monitor/test
    Path dir = Paths.get(args[dirArg]);
    new Runnable() {
      @Override
      public void run() {
        System.out.println("Start Main UI");
        MainUI.start("");
      }
    }.run();

    new WatchDirBak(dir, recursive).processEvents();

  }
}