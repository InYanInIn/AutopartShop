package app.autopartshop.util;

import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringFXMLLoader {
   private final ApplicationContext context;

   @Autowired
   public SpringFXMLLoader(ApplicationContext context) {
      this.context = context;
   }

   public FXMLLoader getLoader(String fxmlPath) {
      FXMLLoader loader = new FXMLLoader();
      loader.setControllerFactory(context::getBean);
      loader.setLocation(getClass().getResource(fxmlPath));
      return loader;
   }
}