package gov.inl.glass3.linesolver.impl.files;


import gov.inl.glass3.linesolver.impl._default.DefaultLookupTables;
import gov.inl.glass3.linesolver.loaders.LookupTablesLoader;
import gov.inl.glass3.windmodel.LookupTable;
import gov.inl.glass3.windmodel.LookupTables;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Ricardo Marquez
 */
public class FilesLookupTablesLoader implements LookupTablesLoader{
  
  public final static String SECTION_NAME_NA = "SECTION_NAME_NA"; 
  private final InputStream file;
  private final String sectionName;
  
  /**
   *
   * @param file
   */
  public FilesLookupTablesLoader(InputStream file, String sectionName) {
    this.file = file;
    this.sectionName = sectionName;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public LookupTables loadLookupTables() {
//    List<String> section = FilesUtils.readSection(file, "LookUpTable"); 
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
    
    List<LookupTable> lookupTables = new ArrayList<>(); 
    FilesUtils.readLines(section.remove(0), section, Header.values(), (row)->{
      LookupTable lookupTable = new LookupTable.Builder()
        .setModelPointId(row.get(Header.MidPointNumber))
        .setSectorNum(row.getDouble(Header.SectorNumber))
        .setSpeedUp(row.getDouble(Header.SpeedUp))
        .setDirectionShift(row.getDouble(Header.DirectionShift))
        .build();
      lookupTables.add(lookupTable); 
    });
    DefaultLookupTables result = new DefaultLookupTables(lookupTables); 
    return result; 
  }
  
    
  /**
   * 
   */
  public static enum Header implements LineHeader {
    WeatherStation("Weather Station"),
    MidPointNumber("MidPoint Number"),
    SectorNumber("Sector Number"),
    SpeedUp("Speed Up"),
    DirectionShift("Direction Shift")
    ;
    private final String text;

    Header(String text) {
      this.text = text;
    }

    @Override
    public String getText() {
      return this.text;
    }
    
  }
  
  
}
