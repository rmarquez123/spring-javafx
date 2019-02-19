package gov.inl.glass3.linesolver.impl.files;

import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesections.LineSections;
import gov.inl.glass3.linesections.RadiativeProperties;
import gov.inl.glass3.linesections.StaticEnvironmentConditions;
import gov.inl.glass3.linesolver.impl._default.DefaultLineSections;
import gov.inl.glass3.linesolver.loaders.LineSectionsLoader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.measure.Measure;
import javax.measure.unit.SI;

/**
 *
 * @author Ricardo Marquez
 */
public class FilesLineSectionsLoader implements LineSectionsLoader {

  private final InputStream file;

  public FilesLineSectionsLoader(InputStream file) {
    this.file = file;
  }

  @Override
  public LineSections loadLineSections() {
    DefaultLineSections result;
    List<String> section = this.readLineSectionsSection();
    List<LineSection> lineSections = this.getLineSectionsFromSection(section);
    result = new DefaultLineSections(lineSections);
    return result;
  }

  /**
   *
   * @param conductorsSection
   * @return
   */
  private List<LineSection> getLineSectionsFromSection(List<String> section) {
    if (section.size() < 2) {
      throw new IllegalStateException("No conductors found");
    }
    List<LineSection> result = new ArrayList<>();
    String headerLine = section.remove(0);
    FilesUtils.readLines(headerLine, section, Header.values(), (row) -> {
      LineSection lineSection = new LineSection.Builder()
        .setConductorId(row.get(Header.CONDUCTOR_ID))
        .setLineSectionId(row.get(Header.LINE_SECTION_ID))
        .setAirQuality("clean".equals(row.get(Header.AIR_QUALITY)) ? 1 : 2)
        .setDerating(row.getInteger(Header.DERATING))
        .setBundle(row.getInteger(Header.BUNDLE))
        .setMaxTemperature(Measure.valueOf(row.getDouble(Header.MAX_TEMPERATURE), SI.CELSIUS))
        .setEmergencyTemperature(Measure.valueOf(row.getDouble(Header.EMERGENCY_TEMPERATURE), SI.CELSIUS))
        .setRadProps(new RadiativeProperties(row.getDouble(Header.ABSORPTIVITY),
          row.getDouble(Header.EMISSIVITY)))
        .setStaticWeather(
          new StaticEnvironmentConditions(
            row.getDouble(Header.STATIC_WIND_SPEED),
            row.getDouble(Header.STATIC_WIND_DIR),
            row.getDouble(Header.STATIC_SOLAR)
          ))
        .build();
      result.add(lineSection);
    });
    return result;
  }

  /**
   *
   * @return @throws RuntimeException
   */
  private List<String> readLineSectionsSection() throws RuntimeException {
    List<String> section = FilesUtils.readSection(file, "LineSection");
    return section;
  }

  /**
   *
   */
  public static enum Header implements LineHeader {
    CONDUCTOR_ID("Conductor ID"),
    LINE_ID("Line ID"),
    LINE_SECTION_ID("Line Section ID"),
    AIR_QUALITY("Air Quality"),
    DERATING("Derating"),
    CURRENT("Current"),
    BUNDLE("Bundle"),
    MAX_TEMPERATURE("Tmax(C)"),
    EMERGENCY_TEMPERATURE("Temergency(C)"),
    EMISSIVITY("Emissivity"),
    ABSORPTIVITY("Absorptivity"),
    STATIC_WIND_SPEED("Static Wind Speed"),
    STATIC_WIND_DIR("Static Wind Direction"),
    STATIC_SOLAR("Static Solar"),;

    private final String text;

    Header(String text) {
      this.text = text;
    }

    @Override
    public String getText() {
      return text;
    }

  }

}
