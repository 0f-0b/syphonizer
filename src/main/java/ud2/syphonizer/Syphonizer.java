package ud2.syphonizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class Syphonizer implements PreLaunchEntrypoint {
  public static final Logger LOGGER = LoggerFactory.getLogger("syphonizer");

  @Override
  public void onPreLaunch() {
    var arch = System.getProperty("os.arch");
    try {
      var root = Path.of(getProperty("java.library.path"));
      Files.createDirectories(root);
      for (var filename : new String[] { "libJSyphon.jnilib", "Syphon" }) {
        var path = root.resolve(filename);
        LOGGER.debug("copying to {}", path);
        try (var in = Syphonizer.class.getResourceAsStream("/natives/" + arch + "/" + filename)) {
          if (in == null)
            throw new RuntimeException(filename + " is not available for " + arch);
          Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load natives", e);
    }
  }

  private static String getProperty(String key) {
    var value = System.getProperty(key);
    LOGGER.debug("{} = {}", key, value);
    return value;
  }
}
