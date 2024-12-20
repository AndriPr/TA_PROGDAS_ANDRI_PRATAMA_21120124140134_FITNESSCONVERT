package fitnessconvert;

import database.JSONDatabase;  
import org.json.JSONArray;
import org.json.JSONObject;

import fitnessconvert.SignUpFrame;
import controller.UserController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        JLabel lblTitle = new JLabel("Login", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(new Color(34, 112, 147));

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField(20);
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField(20);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(34, 112, 147));
        btnLogin.setForeground(Color.WHITE);

        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.setBackground(new Color(200, 200, 200));
        btnSignUp.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(lblUsername, gbc);

        gbc.gridx++;
        add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(lblPassword, gbc);

        gbc.gridx++;
        add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(btnLogin, gbc);

        gbc.gridy++;
        add(btnSignUp, gbc);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                JSONObject db = JSONDatabase.loadDatabase(); 
                JSONArray users = db.getJSONArray("users");

                boolean loginSuccess = false;
                for (Object obj : users) {
                    JSONObject user = (JSONObject) obj;
                    if (user.getString("username").equals(username) &&
                        user.getString("password").equals(password)) {
                        loginSuccess = true;
                        break;
                    }
                }

                if (loginSuccess) {
                    JOptionPane.showMessageDialog(this, "Login berhasil!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new PersonalizationFrame(username).setVisible(true);
                    this.dispose();
                    this.dispose(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Username atau password salah.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal membaca data pengguna.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSignUp.addActionListener(e -> {
            new SignUpFrame().setVisible(true);
            this.dispose();
        });
    }
}

