package gov.inl.glass3.linesolver.impl.files;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import gov.inl.glass3.linesolver.impl._default.DefaultModelPoints;
import gov.inl.glass3.linesolver.loaders.ModelPointsLoader;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.modelpoints.ModelPoints;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.measure.Measure;
import javax.measure.unit.NonSI;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Ricardo Marquez
 */
public class FilesModelPointsLoader implements ModelPointsLoader {
  
  public static final String SECTION_NAME_NA = "SECTION_NAME_NA";
  private final InputStream file;
  private final String sectionName;
  
  
  /**
   *
   * @param file
   */
  public FilesModelPointsLoader(InputStream file, String sectionName) {
    this.file = file;
    this.sectionName = sectionName;
  }
  
  /**
   *
   * @return
   */
  @Override
  public ModelPoints loadModelPoints() {
    DefaultModelPoints result;
    List<String> section; 
    if (!this.sectionName.equals(SECTION_NAME_NA)) {
     section = FilesUtils.readSection(this.file, this.sectionName); 
    } else {
      try { 
        section = IOUtils.readLines(this.file, Charset.forName("UTF-8"));
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    List<ModelPoint> modelPoints = this.getModelPointsFromSection(section);
    result = new DefaultModelPoints(modelPoints);
    return result;
  }

  /**
   *
   * @param section
   * @return
   */
  public List<ModelPoint> getModelPointsFromSection(List<String> section) {
    if (section.size() < 2) {
      throw new IllegalStateException("No model points found");
    }
    List<ModelPoint> result = new ArrayList<>();
    String headerLine = section.remove(0);
    FilesUtils.readLines(headerLine, section, Header.values(), (row) -> {
      ModelPoint modelPoint = this.createModelPoint(row);
      result.add(modelPoint); 
    });
    return result;
  }
  
  /**
   * 
   * @param row
   * @return 
   */
  private ModelPoint createModelPoint(Row row) {
    String modelPointId = row.get(Header.MidPointName);
    String weatherStnId = row.get(Header.WeatherStationName);
    String lineId = row.get(Header.LineSectionName);
    ModelPointGeometry geometry = new ModelPointGeometry.Builder()
      .setAzimuth(Measure.valueOf(row.getDouble(Header.LineAzimuth), NonSI.DEGREE_ANGLE))
      .setElevation(Measure.valueOf(row.getDouble(Header.Elevation), NonSI.FOOT))
      .setPoint(new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
        .createPoint(new Coordinate(row.getDouble(Header.Long), row.getDouble(Header.Lat))))
      .build();
    ModelPoint modelPoint = new ModelPoint(modelPointId, weatherStnId, lineId, geometry);
    return modelPoint;
  }

  /**
   *
   */
  public enum Header implements LineHeader {
    LineSectionName("LineSectionName"),
    MidPointName("MidPointName"),
    Long("Long"),
    Lat("Lat"),
    Elevation("Elevation(feet)"),
    LineAzimuth("LineAzimuth(deg)"),
    WeatherStationName("WeatherStationName")
    ;
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
