package Receipt_Generator;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class mainUIcontroller {
    private static String NameFieldText;
    private static String ageFieldText;
    private static String cityFieldText;
    private static String idFieldText;
    private static String vType;
    private static String boothNo;

    @FXML
    private TextField fNameField;

    @FXML
    private TextField sNameField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField idField;

    @FXML
    private ToggleGroup vaccinationType;

    @FXML
    private ToggleGroup VT1;

    @FXML
    private ToggleGroup VT2;

    @FXML
    private ToggleGroup VT3;

    @FXML
    private Label dateTimeLabel;

    @FXML
    private RadioButton booth0;

    @FXML
    private RadioButton booth1;

    @FXML
    private RadioButton booth2;

    @FXML
    private RadioButton booth3;

    @FXML
    private RadioButton booth4;

    @FXML
    private RadioButton booth5;

    public static String getNameFieldText() {
        return NameFieldText;
    }

    public static String getAgeFieldText() {
        return ageFieldText;
    }

    public static String getCityFieldText() {
        return cityFieldText;
    }

    public static String getIdFieldText() {
        return idFieldText;
    }

    public static String  getvType() {
        return vType;
    }

    public static String getBoothNo() {
        return boothNo;
    }

    @FXML
    public void initialize() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            dateTimeLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    @FXML
    void disableSet1() {
        booth0.setDisable(false);
        booth1.setDisable(false);
        booth2.setDisable(true);
        booth2.setSelected(false);
        booth3.setDisable(true);
        booth3.setSelected(false);
        booth4.setDisable(true);
        booth4.setSelected(false);
        booth5.setDisable(true);
        booth5.setSelected(false);
    }

    @FXML
    void  disableSet2() {
        booth0.setDisable(true);
        booth0.setSelected(false);
        booth1.setDisable(true);
        booth1.setSelected(false);
        booth2.setDisable(false);
        booth3.setDisable(false);
        booth4.setDisable(true);
        booth4.setSelected(false);
        booth5.setDisable(true);
        booth5.setSelected(false);
    }

    @FXML
    void  disableSet3() {
        booth0.setDisable(true);
        booth0.setSelected(false);
        booth1.setDisable(true);
        booth1.setSelected(false);
        booth2.setDisable(true);
        booth2.setSelected(false);
        booth3.setDisable(true);
        booth3.setSelected(false);
        booth4.setDisable(false);
        booth5.setDisable(false);
    }

    @FXML
    void clearAllInputs(ActionEvent e) throws IOException {
            Parent parent = FXMLLoader.load(getClass().getResource("mainUI.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((javafx.scene.Node)e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
    }

    @FXML
    void generateReceipt() throws Exception {
        boolean isValid = inputValidator();
        if (isValid) {
            receiptUIcontroller.generateReceiptInterface();
        }
    }

    boolean inputValidator() {
        boolean radInput1 = false;
        boolean radInput2 = false;

        ArrayList<String> messages = new ArrayList<String>();
        StringBuilder errorMessage = new StringBuilder();

        Object[] vTypes = vaccinationType.getToggles().toArray();
        Object[] vt1 = VT1.getToggles().toArray();
        Object[] vt2 = VT2.getToggles().toArray();
        Object[] vt3 = VT3.getToggles().toArray();

        if (fNameField.getText().equals("")) {
            messages.add("First Name Input Field is Empty!");
        }
        if (sNameField.getText().equals("")) {
            messages.add("Second Name Input Field is Empty!");
        }
        if (ageField.getText().equals("")) {
            messages.add("Age Input Field is Empty!");
        } else if (!(ageField.getText().matches("^[0-9]*$"))) {
            messages.add("Non Numeric Age Field Input Detected!");
        } else if (Integer.parseInt(ageField.getText()) <= 0 || Integer.parseInt(ageField.getText()) >= 120) {
            messages.add("Out of Range Age Field Input Detected!");
        }
        if (cityField.getText().equals("")) {
            messages.add("City Input Field is Empty!");
        }
        if (idField.getText().equals("")) {
            messages.add("NIC/Passport Input Field is Empty!");
        }

        for (int i = 0; i < vTypes.length; i++) {
            if (vaccinationType.getSelectedToggle() == vTypes[i]) {
                vType = Integer.toString(i + 1);
                radInput1 = true;
                break;
            }
        }

        for (int i = 0; i < 2; i++) {
            if (VT1.getSelectedToggle() == vt1[i] || VT2.getSelectedToggle() == vt2[i] || VT3.getSelectedToggle() == vt3[i]) {
                boothNo = Integer.toString(i);
                radInput2 = true;
                break;
            }
        }
        if (!radInput1) {
            messages.add("Vaccine Request Not Selected!");
        }
        if (!radInput2) {
            messages.add("Vaccinating Booth Not Selected!");
        }

        if (messages.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(mainUIcontroller.class.getResourceAsStream("images/titleLogo.png"))));
            alert.setTitle("Error in Inputs");
            for (int i = 0; i < messages.size(); i++) {
                errorMessage.append(messages.get(i)).append("\n");
            }
            alert.setHeaderText(null);
            alert.setContentText(String.valueOf(errorMessage));
            alert.showAndWait();
            return false;
        } else {
            NameFieldText = fNameField.getText() + " " + sNameField.getText();
            ageFieldText = ageField.getText();
            cityFieldText = cityField.getText();
            idFieldText = idField.getText();
            return true;
        }
    }
}
