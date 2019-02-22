package gov.inl.glass3.linesolver.impl.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ricardo Marquez
 */
public class FilesUtils {

  private static final Pattern SECTION_START_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s]*:$");

  private FilesUtils() {
  }

  /**
   *
   * @param stream
   * @param sectionKey
   * @return
   */
  public static List<String> readSection(InputStream stream, String sectionKey) {
    List<String> section;
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
    try {
      String line;
      boolean conductorSectionStarted = false;
      section = new ArrayList<>();
      while ((line = reader.readLine()) != null) {
        if (line.startsWith(sectionKey + ":")) {
          conductorSectionStarted = true;
        } else if (conductorSectionStarted && nextSectionStarted(line)) {
          break;
        } else if (conductorSectionStarted) {
          section.add(line);
        }
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      try {
        reader.close();
      } catch (IOException ex) {
        Logger.getLogger(FilesUtils.class.getName())
          .log(Level.WARNING, null, ex);
      }
    }
    return section;
  }

  /**
   *
   * @param line
   * @return
   */
  private static boolean nextSectionStarted(String line) {
    Matcher matcher = SECTION_START_PATTERN.matcher(line);
    boolean find = matcher.find();
    return find;
  }

  /**
   *
   * @param headerLine
   * @param section
   * @param headers
   * @param consumer
   */
  public static void readLines(String headerLine, List<String> section, LineHeader[] headers, Consumer<Row> consumer) {
    ColumnKeys keys = getColumnKeys(headerLine, headers);
    int lineCount = -1;
    for (String line : section) {
      lineCount++;
      try {
        Row row = new Row(keys, line);
        consumer.accept(row);
      } catch (Exception ex) {
        throw new RuntimeException("Error on parsing line. Check args : {"
          + "line count = " + lineCount
          + ", line = " + line
          + "}", ex);
      }
    }
  }
  
  /**
   * 
   * @param headerLine
   * @param headers
   * @return 
   */
  private static ColumnKeys getColumnKeys(String headerLine, LineHeader[] headers) {
    String[] headerParts = headerLine.split(",", -1);
    int count = -1;
    Map<LineHeader, Integer> columns = new HashMap<>();
    for (String headerText : headerParts) {
      count++;
      
      LineHeader header = null;
      for (LineHeader h : headers) {
        String trim = headerText.trim();
        if (Objects.equals(h.getText(), trim)) {
          header = h;
          break;
        }
      }
      if (header != null) {
        columns.put(header, count);
      }
    }
    ColumnKeys keys = new ColumnKeys(columns);
    return keys;
  }
}
