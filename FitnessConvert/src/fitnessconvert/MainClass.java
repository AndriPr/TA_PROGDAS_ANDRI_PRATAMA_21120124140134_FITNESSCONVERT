package fitnessconvert;

import javax.swing.SwingUtilities;

public class MainClass {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true); 
        });
    }
}
