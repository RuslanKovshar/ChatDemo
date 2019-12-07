package ruslan.kovshar.client;

public class ChatClient {
    public static void main(String[] args) {
        UserWindow userWindow = new UserWindow();
        userWindow.launchApplication(args);
        userWindow.finishWork();
    }
}
