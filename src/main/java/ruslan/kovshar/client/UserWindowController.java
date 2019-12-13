package ruslan.kovshar.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class UserWindowController implements Initializable {

    @FXML
    private TextField textField;

    @FXML
    private TextFlow textFlow;

    @FXML
    private Button sendBtn;


    public TextField getTextField() {
        return textField;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public TextFlow getTextFlow() {
        return textFlow;
    }

    public void setTextFlow(TextFlow textFlow) {
        this.textFlow = textFlow;
    }

    public Button getSendBtn() {
        System.out.println(sendBtn);
        return sendBtn;
    }

    public void setSendBtn(Button sendBtn) {
        this.sendBtn = sendBtn;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Start initialization");/*
        sendBtn.setOnAction(actionEvent -> sendMessage());

        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().getName().equals("Enter")) {
                sendMessage();
            }
        });*/

        System.out.println("End initialization");
    }
}
