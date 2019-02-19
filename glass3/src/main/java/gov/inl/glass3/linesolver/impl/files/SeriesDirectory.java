package gov.inl.glass3.linesolver.impl.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ricardo Marquez
 */
public class SeriesDirectory {

  private final File directory;

  /**
   *
   * @param directory
   */
  public SeriesDirectory(File directory) {
    if (directory == null) {
      throw new NullPointerException("directory cannot be null");
    }
    if (!directory.isDirectory()) {
      throw new IllegalArgumentException("File object is not a directory");
    }
    this.directory = directory;
  }

  /**
   *
   * @return
   */
  public File getDirectory() {
    return this.directory;
  }

  /**
   *
   * @param date
   * @return
   */
  InputStream getFileForDate(LocalDate date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String dateText = formatter.format(date) + "-00-00-00";
    Pattern pattern = Pattern.compile(dateText);
    String[] files = this.directory.list((File dir, String name) -> {
      Matcher matcher = pattern.matcher(name);
      boolean find = matcher.find();
      return find;
    });
    InputStream result;
    switch (files.length) {
      case 0:
        throw new RuntimeException("File does not exist for date : '" + date + "'");
      case 1:
        try {
          result = new FileInputStream(new File(this.directory, files[0]));
        } catch (FileNotFoundException ex) {
          throw new RuntimeException(ex);
        }
        break;
      default:
        throw new RuntimeException("Multiple files found for date : '" + dateText + "'");
    }
    return result;
  }

}
