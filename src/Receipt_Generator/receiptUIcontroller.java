package Receipt_Generator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

public class receiptUIcontroller {
    /**
     * This Method Show Receipt Window
     * @throws Exception
     */
    public static void generateReceiptInterface() throws Exception {
        Stage stage = new Stage();
        stage.setTitle("Receipt"); // Set Window Title
        Parent root = FXMLLoader.load(receiptUIcontroller.class.getResource("receiptUI.fxml"));
        root.getStylesheets().add(receiptUIcontroller.class.getResource("/Receipt_Generator/styles/receiptInterface.css").toString()); // Style Sheet
        stage.getIcons().add(new Image(receiptUIcontroller.class.getResourceAsStream("images/titleLogo.png"))); // Set window Icon
        stage.setAlwaysOnTop(true); // Set Window always on top
        stage.setResizable(false); // Set window resize disabled
        stage.initModality(Modality.APPLICATION_MODAL); // Defines a modal window that blocks events from being delivered to any other application window.
        stage.setScene(new Scene(root,600,391));
        stage.show();
        saveAndprintPDF(root); // Invoke saveAndprintPDF method
    }

    @FXML
    private Label dateLabel1;

    @FXML
    private Label nameLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label vTypeLabel;

    @FXML
    private Label bNoLabel;

    @FXML
    private Label dateLabel2;

    /**
     * In this Method Show Date In Receipt Window
     */
    @FXML
    public void initialize() {
        LocalDateTime myDateObj = LocalDateTime.now(); // create new LocalDateTime object
        DateTimeFormatter dayFormat1 = DateTimeFormatter.ofPattern("E MMMM yyyy HH:mm"); // set date string pattern
        String dateLabel1Text = myDateObj.format(dayFormat1);
        dateLabel1.setText(dateLabel1Text); // set string on Label
        DateTimeFormatter dayFormat2 = DateTimeFormatter.ofPattern("dd/MM/yy");
        String dateLabel2Text = myDateObj.format(dayFormat2);
        dateLabel2.setText(dateLabel2Text);
        bodyTextChanger(); // Invoke bodyTextChanger Method
    }

    /**
     * This Method Take user Inputs from Main Window and parse it to Receipt window fields
     */
    void bodyTextChanger() {
        String nameText = mainUIcontroller.getNameFieldText();
        String ageText = mainUIcontroller.getAgeFieldText();
        String cityText = mainUIcontroller.getCityFieldText();
        String idText = mainUIcontroller.getIdFieldText();
        String  vText = mainUIcontroller.getvType();
        String boothText = mainUIcontroller.getBoothNo();
        String vaccinationType = "";
        String booth = "";

        idLabel.setText("NIC/Passport No: " + idText);
        nameLabel.setText("Full Name:            " + nameText);
        ageLabel.setText("Age:                      " + ageText);
        cityLabel.setText("City:                      " + cityText);
        switch (vText) { // detects which booth selected
            case "1" -> {
                vaccinationType = "AstraZeneca";
                if (boothText.equals("0")) {
                    booth = "0";
                } else {
                    booth = "1";
                }
            }
            case "2" -> {
                vaccinationType = "Sinopharm";
                if (boothText.equals("0")) {
                    booth = "2";
                } else {
                    booth = "3";
                }
            }
            case "3" -> {
                vaccinationType = "Pfizer";
                if (boothText.equals("0")) {
                    booth = "4";
                } else {
                    booth = "5";
                }
            }
        }
        vTypeLabel.setText(vaccinationType);
        bNoLabel.setText("#" + booth);
    }

    /**
     * This Method Save Receipt window as an Image and Save it as a PDF
     * @param root Root Node
     */
    private static void saveAndprintPDF(Parent root) {
        WritableImage nodeshot = root.snapshot(new SnapshotParameters(), null);
        File file = new File("receipt.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PDDocument doc    = new PDDocument();
        PDPage page = new PDPage();
        page.setRotation(90);
        PDImageXObject pdimage;
        PDPageContentStream content;
        try {
            pdimage = PDImageXObject.createFromFile("receipt.png",doc);
            content = new PDPageContentStream(doc, page);
            int imgW = pdimage.getWidth();
            int imgH = pdimage.getHeight();
            float offset = 10f;
            content.transform(Matrix.getRotateInstance(Math.toRadians(90), page.getCropBox().getWidth() + page.getCropBox().getLowerLeftX(), 0));
            content.drawImage(pdimage, offset, offset,imgW,imgH);
            content.close();
            doc.addPage(page);
            doc.save("ReceiptPDF.pdf");
            doc.close();
            infoAlert();
        } catch (IOException ex) {
            Logger.getLogger(receiptUIcontroller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This Method shows PDF and Image Save Completion Information Alert Window
     */
    private static void infoAlert() {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION); // Make Alert type as Information
        Stage stage = (Stage) infoAlert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true); // Make alert always on top
        stage.getIcons().add(new Image(Objects.requireNonNull(mainUIcontroller.class.getResourceAsStream("images/titleLogo.png")))); // alert window Icon
        infoAlert.setTitle("Save and Print PDF"); // Alert Window Title
        infoAlert.setHeaderText("Image and PDF Saved Successfully!"); // Set Alert window header text
        infoAlert.showAndWait(); // display alert
    }
}
