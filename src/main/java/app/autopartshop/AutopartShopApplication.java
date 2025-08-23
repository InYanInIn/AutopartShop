package app.autopartshop;

import app.autopartshop.controller.AutopartDetailsController;
import app.autopartshop.controller.OrderDetailsController;
import app.autopartshop.service.AutopartService;
import app.autopartshop.service.OrderService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AutopartShopApplication extends Application {

	private ConfigurableApplicationContext springContext;

	public static void main(String[] args) {
		// Kick off JavaFX, which in turn starts Spring
		Application.launch(args);
	}

	@Override
	public void init() {
		springContext = new SpringApplicationBuilder(AutopartShopApplication.class)
				.headless(false)
				.run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/autopart_details.fxml"));
		loader.setControllerFactory(springContext::getBean);
		Parent root = loader.load();

		AutopartDetailsController controller = loader.getController();

		AutopartService autopartService = springContext.getBean(AutopartService.class);
		autopartService.findByIdWithItems(1L).ifPresent(controller::setAutopart);

//		OrderDetailsController controller = loader.getController();

//		OrderService orderService = springContext.getBean(OrderService.class);
//		orderService.findByIdWithItems(1L).ifPresent(controller::setOrder);

		stage.minHeightProperty().set(500);
		stage.minWidthProperty().set(650);
		stage.setTitle("Autopart Details");
		stage.setScene(new Scene(root));
		stage.show();
	}

	@Override
	public void stop() {
		springContext.close();
	}
}
