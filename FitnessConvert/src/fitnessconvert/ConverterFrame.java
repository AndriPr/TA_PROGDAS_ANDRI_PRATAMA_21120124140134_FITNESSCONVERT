package fitnessconvert;

import database.JSONDatabase;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ConverterFrame extends JFrame {
    private final String username;
    private int weight;

    public ConverterFrame(String username) {
        this.username = username;

        setTitle("Fitness Converter");
        setSize(500, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel lblTitle = new JLabel("Fitness Converter", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(new Color(34, 112, 147));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);

        JLabel lblDistance = new JLabel("Jarak (km):");
        lblDistance.setFont(new Font("Arial", Font.PLAIN, 18));
        JTextField txtDistance = new JTextField(10);
        JLabel lblSpeed = new JLabel("Kecepatan:");
        lblSpeed.setFont(new Font("Arial", Font.PLAIN, 18));
        JComboBox<String> cmbSpeed = new JComboBox<>(new String[]{"Rendah", "Sedang", "Tinggi"});

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(lblDistance, gbc);
        gbc.gridx = 1;
        add(txtDistance, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(lblSpeed, gbc);
        gbc.gridx = 1;
        add(cmbSpeed, gbc);

        JButton btnConvert = new JButton("Hitung");
        btnConvert.setBackground(new Color(34, 112, 147));
        btnConvert.setForeground(Color.WHITE);
        btnConvert.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(btnConvert, gbc);

        JLabel lblResult = new JLabel("Hasil: ", JLabel.CENTER);
        lblResult.setFont(new Font("Arial", Font.PLAIN, 20));
        lblResult.setForeground(new Color(0, 128, 0));
        gbc.gridy++;
        add(lblResult, gbc);

        JButton btnHistory = new JButton("Lihat Riwayat");
        btnHistory.setBackground(new Color(100, 149, 237));
        btnHistory.setForeground(Color.WHITE);
        btnHistory.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy++;
        add(btnHistory, gbc);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelButtons.setBackground(new Color(240, 248, 255));

        JButton btnBack = new JButton("Kembali");
        btnBack.setBackground(new Color(100, 149, 237)); 
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnLogout = new JButton("Log Out");
        btnLogout.setBackground(new Color(255, 99, 71)); 
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));

        panelButtons.add(btnBack);
        panelButtons.add(btnLogout);

        gbc.gridy++;
        add(panelButtons, gbc);

        loadUserWeight();

        btnConvert.addActionListener(e -> {
            try {
                double distance = Double.parseDouble(txtDistance.getText());
                String speed = cmbSpeed.getSelectedItem().toString();

                if (distance <= 0) {
                    lblResult.setText("Masukkan jarak yang valid.");
                    return;
                }
                double[] results = calculateStepsAndCalories(distance, speed);
                int steps = (int) results[0];
                double calories = results[1];

                lblResult.setText(String.format("Langkah: %d | Kalori: %.2f", steps, calories));

                saveToHistory(distance, speed, steps, calories);

            } catch (NumberFormatException ex) {
                lblResult.setText("Masukkan angka yang valid untuk jarak.");
            } catch (IOException ex) {
                lblResult.setText("Gagal menyimpan riwayat.");
            }
        });

        btnHistory.addActionListener(e -> {
            new HistoryFrame(username).setVisible(true);
            this.dispose();
        });

        btnBack.addActionListener(e -> {
            new PersonalizationFrame(username).setVisible(true);
            this.dispose();
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
    }

    private void loadUserWeight() {
        try {
            JSONObject db = JSONDatabase.loadDatabase();
            JSONArray users = db.getJSONArray("users");
            for (Object obj : users) {
                JSONObject user = (JSONObject) obj;
                if (user.getString("username").equals(username)) {
                    weight = user.getInt("weight");
                    return;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data pengguna.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double[] calculateStepsAndCalories(double distance, String speed) {
        double stepFactor;
        switch (speed) {
            case "Rendah":
                stepFactor = 1200;
                break;
            case "Sedang":
                stepFactor = 1500;
                break;
            case "Tinggi":
                stepFactor = 1800;
                break;
            default:
                stepFactor = 0;
        }
        int steps = (int) (distance * stepFactor);
        double calories = steps * weight * 0.0005;
        return new double[]{steps, calories};
    }

    private void saveToHistory(double distance, String speed, int steps, double calories) throws IOException {
        JSONObject db = JSONDatabase.loadDatabase();
        JSONArray history = db.getJSONArray("history");

        JSONObject record = new JSONObject();
        record.put("username", username);
        record.put("distance", distance);
        record.put("speed", speed);
        record.put("steps", steps);
        record.put("calories", calories);

        history.put(record);
        JSONDatabase.saveDatabase(db);
    }
}
