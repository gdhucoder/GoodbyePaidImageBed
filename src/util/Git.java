package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Objects;

/**
 * Created by HuGuodong on 2019/11/18.
 */
public class Git {

  public static void gitCommitAndPushCurrentChange(Path dir) {
    gitStage(dir);
    gitCommit(dir, "update");
    gitPush(dir);
  }


  public static void gitInit(Path dir) {
    runCommand(dir, "git", "init");
  }

  public static void gitStage(Path dir) {
    runCommand(dir, "git", "add", "-A");
  }

  public static void gitCommit(Path dir, String message) {
    runCommand(dir, "git", "commit", "-m", message);
  }

  public static void gitPush(Path dir) {
    runCommand(dir, "git", "push");
  }


  public static void gitPull(Path dir) {
    runCommand(dir, "git", "pull");
  }


  public static void gitRemoteAdd(Path dir, String url) {
    runCommand(dir, "git", "remote", "add", "origin", url);
  }

  public static void runCommand(String dir, String... command) {
    runCommand(Paths.get(dir));
  }


  public static void runCommand(Path directory, String... command) {
    try {
      Objects.requireNonNull(directory, "directory");
      if (!Files.exists(directory)) {
        throw new RuntimeException(
            "can't not run command in non-existing directory '" + directory + "'");
      }

      // at designated directory to execute command
      ProcessBuilder pb = new ProcessBuilder().command(command).directory(directory.toFile());
      Process p = pb.start();

      BufferedReader reader =
          new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }

      int exitCode = p.waitFor();
      System.out.println("\nExited with exit code : " + exitCode);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void test1() {
    ///Users/gdhu/projects/personal/fuckImageBed/src/util/Git.java
    String directory = "/Users/gdhu/projects/giteeimagebed/testimagebed";
    Path dir = Paths.get(directory);
    gitStage(dir);
    gitCommit(dir, "update");
    gitPush(dir);

  }

  public static void submitGitChanges() {
    ///Users/gdhu/projects/personal/fuckImageBed/src/util/Git.java
    String directory = "/Users/gdhu/projects/giteeimagebed/testimagebed";
    Path dir = Paths.get(directory);
    gitStage(dir);
    gitCommit(dir, "update");
    gitPush(dir);

  }


  public static void main(String[] args) {
//    Path p = Paths.get(".");

//    runCommand(p, "ls", "-l");
    test1();
  }
}
