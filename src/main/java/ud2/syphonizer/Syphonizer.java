package ud2.syphonizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.StringUtils;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class Syphonizer implements PreLaunchEntrypoint {
  @Override
  public void onPreLaunch() {
    try {
      addLibrary("libJSyphon.jnilib", "Syphon");
    } catch (IOException e) {
      throw new RuntimeException("Failed to load natives.", e);
    }
  }

  private static void addLibrary(String... filenames) throws IOException {
    var root = Path.of(StringUtils.split(System.getProperty("java.library.path"), File.pathSeparatorChar)[0]);
    Files.createDirectories(root);
    for (var filename : filenames) {
      var path = root.resolve(filename);
      try (var in = Syphonizer.class.getResourceAsStream("/natives/" + filename)) {
        Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
      }
    }
  }
}
