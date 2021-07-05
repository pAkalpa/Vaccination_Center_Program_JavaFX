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
    public static void generateReceiptInterface() throws Exception {
        Stage stage = new Stage();
        stage.setTitle("Receipt");
        Parent root = FXMLLoader.load(receiptUIcontroller.class.getResource("receiptUI.fxml"));
        root.getStylesheets().add(receiptUIcontroller.class.getResource("/Receipt_Generator/styles/receiptInterface.css").toString());
        stage.getIcons().add(new Image(receiptUIcontroller.class.getResourceAsStream("images/titleLogo.png")));
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root,600,391));
        stage.show();
        saveAndprintPDF(root);
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

    @FXML
    public void initialize() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter dayFormat1 = DateTimeFormatter.ofPattern("E MMMM yyyy");
        String dateLabel1Text = myDateObj.format(dayFormat1);
        dateLabel1.setText(dateLabel1Text);
        DateTimeFormatter dayFormat2 = DateTimeFormatter.ofPattern("dd/MM/yy");
        String dateLabel2Text = myDateObj.format(dayFormat2);
        dateLabel2.setText(dateLabel2Text);
        bodyTextChanger();
    }

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
        switch (vText) {
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

    private static void infoAlert() {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) infoAlert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.getIcons().add(new Image(Objects.requireNonNull(mainUIcontroller.class.getResourceAsStream("images/titleLogo.png"))));
        infoAlert.setTitle("Save and Print PDF");
        infoAlert.setHeaderText("Image and PDF Saved Successfully!");
        infoAlert.showAndWait();
    }
}
