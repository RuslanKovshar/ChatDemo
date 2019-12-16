package ruslan.kovshar.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class UserWindowController implements Initializable {

    @FXML
    public VBox vBox;

    @FXML
    private TextField textField;

    @FXML
    private Button sendBtn;

    @FXML
    private ScrollPane scroll; //this must match the fx:id of the ScrollPane element


    public TextField getTextField() {
        return textField;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

    public Button getSendBtn() {
        return sendBtn;
    }

    public void setSendBtn(Button sendBtn) {
        this.sendBtn = sendBtn;
    }

    public ScrollPane getScroll() {
        return scroll;
    }

    public void setScroll(ScrollPane scroll) {
        this.scroll = scroll;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
