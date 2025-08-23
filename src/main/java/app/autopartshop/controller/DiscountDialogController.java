package app.autopartshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

/**
 * Controller for the discount dialog in the AutoPart Shop application.
 * This dialog allows users to input a discount percentage for an order.
 */
@Controller
public class DiscountDialogController {

   @FXML private TextField discountField;
   @FXML private Label errorLabel;
   @FXML private Button okButton;
   @FXML private Button cancelButton;

   private boolean confirmed = false;
   private Stage dialogStage;

   /**
    * Initializes the controller class.
    * This method is called after the FXML file has been loaded.
    */
   @FXML
   public void initialize() {
      errorLabel.setVisible(false);

      // Set up button actions
      okButton.setOnAction(event -> onOk());
      cancelButton.setOnAction(event -> onCancel());
   }

   /**
    * Sets the stage for this dialog.
    *
    * @param dialogStage the stage to set
    */
   public void setDialogStage(Stage dialogStage) {
      this.dialogStage = dialogStage;
   }

   /**
    * Checks if the dialog was confirmed.
    *
    * @return true if confirmed, false otherwise
    */
   public boolean isConfirmed() {
      return confirmed;
   }

   /**
    * Retrieves the discount value entered by the user.
    *
    * @return the discount value as a double, or -1 if the input is invalid
    */
   public double getDiscountValue() {
      try {
         return Double.parseDouble(discountField.getText());
      } catch (NumberFormatException e) {
         return -1; // Indicates invalid input
      }
   }

   /**
    * Sets the current discount value in the dialog.
    *
    * @param discount the discount value to set
    */
   public void setCurrentDiscount(double discount) {
      discountField.setText(String.valueOf(discount));
   }

   /**
    * Handles the OK button action.
    * Validates the discount input and closes the dialog if valid.
    */
   @FXML
   private void onOk() {
      try {
         double discount = Double.parseDouble(discountField.getText());
         if (discount < 0 || discount > 100) {
            errorLabel.setVisible(true);
            return;
         }
         confirmed = true;
         dialogStage.close();
      } catch (NumberFormatException e) {
         errorLabel.setVisible(true);
      }
   }

   /**
    * Handles the Cancel button action.
    * Closes the dialog without confirming the discount.
    */
   @FXML
   private void onCancel() {
      confirmed = false;
      dialogStage.close();
   }
}