package gov.inl.glass3.linesolver.impl.files;

import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.conductors.ConductorCatalogue;
import gov.inl.glass3.customunits.RmSI;
import gov.inl.glass3.linesolver.impl._default.DefaultConductorCatalogue;
import gov.inl.glass3.linesolver.loaders.ConductorsLoader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.measure.Measure;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

/**
 *
 * @author Ricardo Marquez
 */
public class FilesConductorsLoader implements ConductorsLoader {
  private final InputStream file;
  /**
   *
   * @param file
   */
  public FilesConductorsLoader(InputStream file) {
    if (file == null) {
      throw new NullPointerException("File inputstream cannot be null");
    }
    this.file = file;
  }

  /**
   *
   * @return
   */
  @Override
  public ConductorCatalogue loadConductors() {
    DefaultConductorCatalogue result;
    List<String> conductorsSection = this.readConductorsSection();
    List<Conductor> conductors = this.getConductorsFromSection(conductorsSection);
    result = new DefaultConductorCatalogue(conductors);
    return result;
  }

  /**
   *
   * @return @throws RuntimeException
   */
  private List<String> readConductorsSection() throws RuntimeException {
    List<String> section = FilesUtils.readSection(file, "Conductor");
    return section;
  }

  /**
   *
   * @param conductorsSection
   * @return
   */
  private List<Conductor> getConductorsFromSection(List<String> conductorsSection) {
    if (conductorsSection.size() < 2) {
      throw new IllegalStateException("No conductors found");
    }
    List<Conductor> result = new ArrayList<>();
    String headerLine = conductorsSection.remove(0);
    FilesUtils.readLines(headerLine, conductorsSection, Header.values(), (row) -> {
      Conductor conductor = new Conductor.Builder()
        .setName(row.get(Header.CodeWord))
        .setType(row.get(Header.ConductorType))
        .setDiameter(Measure.valueOf(row.getDouble(Header.MetalOD), NonSI.INCH))
        .setCond_strand(row.get(Header.CondStrand))
        .setCore_strand(row.get(Header.CoreStrand))
        .setCore_od(row.get(Header.CoreOD))
        .setMetal_od(row.get(Header.MetalOD))
        .setSteelCore(false)
        .setMinTemperature(Measure.valueOf(row.getDouble(Header.LowTemp), SI.CELSIUS))
        .setMinResistence(Measure.valueOf(row.getDouble(Header.LowResistance), RmSI.OHMS_PER_MILE))
        .setMaxTemperature(Measure.valueOf(row.getDouble(Header.HiTemp), SI.CELSIUS))
        .setMaxResistence(Measure.valueOf(row.getDouble(Header.HiResistance), RmSI.OHMS_PER_MILE))
        .setAlWeight(row.get(Header.AlWeight))
        .setStWeight(row.get(Header.StWeight))
        .build();
      result.add(conductor);
    });
    return result;
  }

  /**
   *
   */
  public static enum Header implements LineHeader {
    ConductorType("Conductor Type"),
    CodeWord("CodeWord"),
    Size("Size"),
    CondStrand("Cond Strand"),
    CoreStrand("Core Strand"),
    CondWireDia("Cond Wire Dia"),
    CoreWireDia("Core Wire Dia"),
    CoreOD("Core OD"),
    MetalOD("Metal OD"),
    StrandLay("Strand Lay"),
    TWType("TW Type"),
    TWLayers("TW Layers"),
    LowTemp("Low Temp"),
    LowResistance("Low Resistance"),
    HiTemp("Hi Temp"),
    HiResistance("Hi Resistance"),
    AlWeight("Al Weight"),
    StWeight("St Weight");
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
