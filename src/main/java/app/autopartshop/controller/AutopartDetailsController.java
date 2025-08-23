package app.autopartshop.controller;

import app.autopartshop.model.*;
import app.autopartshop.model.DTO.OrderDTO;
import app.autopartshop.service.CategoryService;
import app.autopartshop.service.OrderService;
import app.autopartshop.util.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for displaying details of an Autopart, including its name, description,
 * category, price, and associated orders in a table format.
 * It allows users to view detailed information about each order related to the autopart.
 */
@Controller
public class AutopartDetailsController {


   @FXML private Label nameLabel;
   @FXML private Label descriptionLabel;
   @FXML private Label categoryLabel;
   @FXML private Label priceLabel;

   @FXML private Label warningLabel;

   @FXML private TableView<OrderDTO> orderTable;
   @FXML private TableColumn<OrderDTO, Integer> idColumn;
   @FXML private TableColumn<OrderDTO, String> nameColumn;
   @FXML private TableColumn<OrderDTO, String> descriptionColumn;
   @FXML private TableColumn<OrderDTO, String> statusColumn;
   @FXML private TableColumn<OrderDTO, LocalDateTime> createdColumn;
   @FXML private TableColumn<OrderDTO, Double> totalColumn;

   @FXML private Button viewDetails;


   @Autowired
   private SpringFXMLLoader springFXMLLoader;
   @Autowired
   private OrderService orderService;
   @Autowired
   private CategoryService categoryService;

   private Autopart currentAutopart;
   private final ObservableList<OrderDTO> orderDTOs = FXCollections.observableArrayList();

   /**
    * Sets the current autopart and updates the UI labels and table.
    *
    * @param autopart The Autopart object to display details for.
    */
   public void setAutopart(Autopart autopart){
      this.currentAutopart = autopart;
      nameLabel.setText(autopart.getName());
      descriptionLabel.setText(autopart.getDescription());
      categoryLabel.setText(categoryService.getAutopartCategorySeq(autopart.getId()));
      priceLabel.setText(String.format("%.2f", autopart.getPrice()));

      fillDTOs();
   }

   /**
    * Fills the order table with OrderDTOs based on the current autopart's order items.
    * Each OrderDTO contains details about the order associated with the autopart.
    */
   private void fillDTOs() {
      orderDTOs.clear();
      for (OrderItem item : currentAutopart.getOrderItems()) {
         Order order = item.getOrder();
         OrderDTO dto = new OrderDTO(
               order.getId(),
               order.getName(),
               order.getDescription(),
               String.valueOf(order.getOrderStatus()),
               order.getCreatedAt(),
               orderService.calculateOrderTotal(order.getId())
         );
         orderDTOs.add(dto);
      }
      orderTable.setItems(orderDTOs);
   }

   /**
    * Initializes the controller by setting up the table columns and their cell value factories.
    * Also sets up the action for the view details button.
    */
   @FXML
   public void initialize() {
      // Set up the columns to use properties from AutoPart
      idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
      nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
      descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
      statusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

      createdColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
      createdColumn.setCellFactory(col -> new TableCell<OrderDTO, LocalDateTime>() {
         private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
         @Override
         protected void updateItem(LocalDateTime item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
               setText(null);
            } else {
               setText(fmt.format(item));
            }
         }
      });

      totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
      totalColumn.setCellFactory(
            col -> new TableCell<OrderDTO, Double>(){
               @Override
               protected void updateItem(Double item, boolean empty) {
                  super.updateItem(item, empty);
                  if (empty || item == null) {
                     setText(null);
                  } else {
                     setText(String.format("%.2f", item));
                  }
               }
            }
      );

      viewDetails.setOnAction(event -> onViewDetails());

      orderTable.setItems(orderDTOs);

//      viewDetails.disableProperty().bind(orderTable.getSelectionModel().selectedItemProperty().isNull());
   }

   /**
    * Handles the action when the "View Details" button is clicked.
    * It retrieves the selected order from the table and opens a new window
    * to display the order details.
    */
   @FXML
   private void onViewDetails() {
      OrderDTO selectedOrder = orderTable.getSelectionModel().getSelectedItem();

      if (selectedOrder == null) {
         showWarningMessage();
         return;
      }

      try {
         FXMLLoader loader = springFXMLLoader.getLoader("/fxml/order_details.fxml");
         Parent root = loader.load();
         OrderDetailsController  controller = loader.getController();

         Order order = orderService.findByIdWithItems(selectedOrder.getId())
               .orElseThrow(()-> new RuntimeException("Order not found"));
         controller.setOrder(order);

         Stage stage = new Stage();
         stage.setTitle("Order Details");
         stage.initModality(Modality.APPLICATION_MODAL);
         stage.setScene(new Scene(root));
         stage.show();

         Stage currentStage = (Stage) viewDetails.getScene().getWindow();
         currentStage.close();

      } catch (IOException ex) {
         new Alert(Alert.AlertType.ERROR, "Cannot load window: " + ex.getMessage())
               .showAndWait();
      }
   }

   /**
    * Displays a warning message to the user if no order is selected.
    * The message will be visible for 3 seconds before disappearing.
    */
   private void showWarningMessage() {
      warningLabel.setVisible(true);

      // Auto-hide after 3 seconds
      new java.util.Timer().schedule(
            new java.util.TimerTask() {
               @Override
               public void run() {
                  javafx.application.Platform.runLater(() ->
                        warningLabel.setVisible(false));
               }
            },
            3000
      );
   }
}
