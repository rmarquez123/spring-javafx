package com.windsim.wpls.view.projectopen;

import com.rm.datasources.DbConnection;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import com.windsim.wpls.db.linesolver.pg.impl.PgDbModelLoaderProvider;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.linesolver.ModelLoadersProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javafx.beans.property.Property;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class DbRequestProjectOpenAction implements RequestProjectOpenAction {

  @Autowired
  FxmlInitializer fxmlInitializer;
  @Autowired
  @Qualifier(value = "confirmOpenProjectPopup")
  private Popup confirmOpenProjectPopup;
  @Autowired
  ConfirmProjectOpenController confirmProjectOpenController;

  @Autowired
  @Qualifier("dbConnection")
  Property<DbConnection> dbConnection;

  /**
   *
   * @param onDone
   */
  @Override
  public void onAction(Consumer<Model> onDone) {
    Map<String, Object> credentials = new HashMap<>();
    // Example url: "jdbc:postgresql://localhost:5432/idaho_power"
    String url = String.format("jdbc:postgresql://%s:%d/%s", new Object[]{
      dbConnection.getValue().getUrl(),
      dbConnection.getValue().getPort(),
      dbConnection.getValue().getSchema()
    });
    credentials.put("hibernate.connection.url", url);

    credentials.put("hibernate.connection.username", dbConnection.getValue().getUser());
    credentials.put("hibernate.connection.password", dbConnection.getValue().getPassword());
    EntityManager em = Persistence
      .createEntityManagerFactory("wpls_idaho_power_pu", credentials)
      .createEntityManager();
    ModelLoadersProvider loadersProvider = new PgDbModelLoaderProvider(em);
    confirmProjectOpenController.openModel(loadersProvider, onDone);
    confirmOpenProjectPopup.show();

  }

}
