package ud2.syphonizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.StringUtils;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class Syphonizer implements PreLaunchEntrypoint {
  @Override
  public void onPreLaunch() {
    try {
      addLibrary("libJSyphon.jnilib");
      addLibrary("Syphon");
    } catch (IOException e) {
      throw new RuntimeException("Failed to load natives.", e);
    }
  }

  private static void addLibrary(String filename) throws IOException {
    String[] paths = StringUtils.split(System.getProperty("java.library.path"), File.pathSeparatorChar);
    Path path = FileSystems.getDefault().getPath(paths[paths.length - 1], filename);
    try (InputStream in = Syphonizer.class.getResourceAsStream("/natives/" + filename)) {
      Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
    }
  }
}
