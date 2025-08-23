package app.autopartshop.controller;

import app.autopartshop.model.*;
import app.autopartshop.model.DTO.OrderDTO;
import app.autopartshop.model.DTO.OrderItemDTO;
import app.autopartshop.service.AutopartService;
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
import java.time.format.DateTimeFormatter;


/**
 * * Controller for handling order details in the application.
 * This controller manages the display and interaction with order details,
 * including viewing autopart details and changing discounts.
 */
@Controller
public class OrderDetailsController {

   @FXML private Button changeDiscount;

   @FXML private Label nameLabel;
   @FXML private Label descriptionLabel;
   @FXML private Label dateLabel;
   @FXML private Label statusLabel;
   @FXML private Label totalPriceLabel;

   @FXML private Label successLabel;
   @FXML private Label warningLabel;

   @FXML private TableView<OrderItemDTO> autopartTable;
   @FXML private TableColumn<OrderItemDTO, String> autopartColumn;
   @FXML private TableColumn<OrderItemDTO, Double> priceColumn;
   @FXML private TableColumn<OrderItemDTO, String> descriptionColumn;
   @FXML private TableColumn<OrderItemDTO, Integer> quantityColumn;
   @FXML private TableColumn<OrderItemDTO, Double> discountColumn;
   @FXML private TableColumn<OrderItemDTO, Double> totalColumn;

   @FXML private Button viewDetails;

   @Autowired
   private SpringFXMLLoader springFXMLLoader;
   @Autowired
   private OrderService orderService;
   @Autowired
   private AutopartService autopartService;

   private Order currentOrder;
   private final ObservableList<OrderItemDTO> orderItemDTOs = FXCollections.observableArrayList();

   /**
    * Sets the current order and updates the UI components with order details.
    *
    * @param order The order to be displayed.
    */
   public void setOrder(Order order){
      this.currentOrder = order;
      nameLabel.setText(order.getName());
      descriptionLabel.setText(order.getDescription());
      dateLabel.setText(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(order.getCreatedAt()));

      statusLabel.setText(String.valueOf(order.getOrderStatus()));

      fillDTOs();
      updateTotal();
   }

   /**
    * Fills the DTOs with order items and updates the table view.
    * This method retrieves the order items from the current order,
    * converts them to DTOs, and populates the table view.
    */
   private void fillDTOs(){
      orderItemDTOs.clear();
      for (OrderItem item : currentOrder.getOrderItems()) {
         Autopart autopart = item.getAutopart();
         OrderItemDTO dto = new OrderItemDTO(
            item.getId(),
            autopart.getName(),
            autopart.getPrice(),
            autopart.getDescription(),
            item.getQuantity(),
            item.getDiscount(),
            orderService.calculateItemTotal(item)
         );
         orderItemDTOs.add(dto);
      }
      autopartTable.setItems(orderItemDTOs);
      autopartTable.refresh();
   }

   /**
    * Updates the total price label with the calculated total for the current order.
    * This method retrieves the total price from the order service and formats it for display.
    */
   public void updateTotal(){
      double total = orderService.calculateOrderTotal(currentOrder.getId());
      totalPriceLabel.setText(String.format("%.2f", total));
   }

   /**
    * Initializes the controller by configuring the table and setting up event handlers.
    * This method is called automatically by JavaFX when the FXML file is loaded.
    */
   @FXML
   public void initialize() {
      configureTable();

      changeDiscount.setOnAction(event -> onChangeDiscount());
      viewDetails.setOnAction(event -> onViewDetails());
   }

   /**
    * Configures the table view columns to display order item details.
    * This method sets the cell value factories for each column to bind them to the corresponding properties in the DTO.
    */
   private void configureTable() {
      autopartColumn.setCellValueFactory(new PropertyValueFactory<>("autopartName"));
      priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
      descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
      quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
      discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));

      totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
      totalColumn.setCellFactory(
            col -> new TableCell<OrderItemDTO, Double>(){
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

      autopartTable.setItems(orderItemDTOs);
   }

   /**
    * Handles the action of changing the discount for a selected order item.
    * This method opens a dialog to input the new discount value and updates the order item if confirmed.
    */
   @FXML
   private void onChangeDiscount() {
      OrderItemDTO selectedItem = autopartTable.getSelectionModel().getSelectedItem();

      if (selectedItem == null) {
         showWarningMessage();
         return;
      }

      try {
         // Load the discount dialog FXML
         FXMLLoader loader = springFXMLLoader.getLoader("/fxml/discount_dialog.fxml");
         Parent root = loader.load();
         DiscountDialogController controller = loader.getController();

         // Create the dialog Stage
         Stage dialogStage = new Stage();
         dialogStage.setTitle("Change Discount");
         dialogStage.initModality(Modality.APPLICATION_MODAL);
         dialogStage.setScene(new Scene(root));
         dialogStage.setResizable(false);
         controller.setDialogStage(dialogStage);

         // Show the dialog and wait for response
         dialogStage.showAndWait();

         if (controller.isConfirmed()) {
            double newDiscount = controller.getDiscountValue();
            orderService.updateOrderItemDiscount(selectedItem.getId(), newDiscount);

            this.currentOrder = orderService.findByIdWithItems(currentOrder.getId()).get();

            updateTotal();
            fillDTOs();
            configureTable();
            showSuccessMessage();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * Handles the action of viewing details for a selected order item.
    * This method opens a dialog to display detailed information about the selected autopart.
    */
   @FXML
   private void onViewDetails() {
      OrderItemDTO selectedItem = autopartTable.getSelectionModel().getSelectedItem();

      if (selectedItem == null) {
         showWarningMessage();
         return;
      }

      try {
         // Load the discount dialog FXML
         FXMLLoader loader = springFXMLLoader.getLoader("/fxml/autopart_details.fxml");
         Parent root = loader.load();
         AutopartDetailsController  controller = loader.getController();

         // Create the dialog Stage
         Autopart autopart = autopartService.findByIdWithItems(
               orderService.findOrderItemById(selectedItem.getId())
                     .orElseThrow(() -> new RuntimeException("Order item not found"))
                     .getAutopart().getId())
               .orElseThrow(()-> new RuntimeException("Autopart not found"));
         controller.setAutopart(autopart);

         Stage stage = new Stage();
         stage.setTitle("Autopart Details");
         stage.initModality(Modality.APPLICATION_MODAL);
         stage.setScene(new Scene(root));
         stage.show();

         Stage currentStage = (Stage) viewDetails.getScene().getWindow();
         currentStage.close();

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * Displays a success message to the user.
    * This method makes the success label visible and auto-hides it after 5 seconds.
    */
   private void showSuccessMessage() {
      successLabel.setVisible(true);

      // Auto-hide after 3 seconds
      new java.util.Timer().schedule(
            new java.util.TimerTask() {
               @Override
               public void run() {
                  javafx.application.Platform.runLater(() ->
                        successLabel.setVisible(false));
               }
            },
            5000
      );
   }

   /**
    * Displays a warning message to the user.
    * This method makes the warning label visible and auto-hides it after 3 seconds.
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
