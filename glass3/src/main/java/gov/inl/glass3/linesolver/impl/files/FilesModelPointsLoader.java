package gov.inl.glass3.linesolver.impl.files;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import gov.inl.glass3.linesolver.impl._default.DefaultModelPoints;
import gov.inl.glass3.linesolver.loaders.ModelPointsLoader;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.modelpoints.ModelPoints;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.measure.Measure;
import javax.measure.unit.NonSI;

/**
 *
 * @author Ricardo Marquez
 */
public class FilesModelPointsLoader implements ModelPointsLoader {

  private final InputStream file;

  /**
   *
   * @param file
   */
  public FilesModelPointsLoader(InputStream file) {
    this.file = file;
  }

  /**
   *
   * @return
   */
  @Override
  public ModelPoints loadModelPoints() {
    DefaultModelPoints result;
    List<String> section = FilesUtils.readSection(file, "ModelPoint");
    List<ModelPoint> modelPoints = this.getModelPointsFromSection(section);
    result = new DefaultModelPoints(modelPoints);
    return result;
  }

  /**
   *
   * @param section
   * @return
   */
  private List<ModelPoint> getModelPointsFromSection(List<String> section) {
    if (section.size() < 2) {
      throw new IllegalStateException("No conductors found");
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
