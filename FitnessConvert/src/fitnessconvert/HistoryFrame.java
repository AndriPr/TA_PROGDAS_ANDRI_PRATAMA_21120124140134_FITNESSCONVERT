package fitnessconvert;

import database.JSONDatabase;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;

public class HistoryFrame extends JFrame {
    private final String username;

    public HistoryFrame(String username) {
        this.username = username;

        setTitle("Riwayat Konversi");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255)); 

        JLabel lblTitle = new JLabel("Riwayat Konversi", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(new Color(34, 112, 147));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(lblTitle, BorderLayout.NORTH);

        String[] columnNames = {"Jarak (km)", "Kecepatan", "Langkah", "Kalori"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(25);
        table.setGridColor(new Color(200, 200, 200));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBackground(new Color(34, 112, 147));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        JButton btnBack = new JButton("Kembali");
        btnBack.setBackground(new Color(34, 112, 147));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 16));
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        JPanel panelButton = new JPanel();
        panelButton.setBackground(new Color(240, 248, 255));
        panelButton.add(btnBack);
        add(panelButton, BorderLayout.SOUTH);

        try {
            loadHistory(tableModel);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat riwayat.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        btnBack.addActionListener(e -> {
            new ConverterFrame(username).setVisible(true);
            this.dispose();
        });
    }

    private void loadHistory(DefaultTableModel tableModel) throws IOException {
        JSONObject db = JSONDatabase.loadDatabase();
        JSONArray history = db.getJSONArray("history");

        for (Object obj : history) {
            JSONObject record = (JSONObject) obj;
            if (record.getString("username").equals(username)) {
                Object[] row = {
                    record.getDouble("distance"),
                    record.getString("speed"),
                    record.getInt("steps"),
                    record.getDouble("calories")
                };
                tableModel.addRow(row);
            }
        }
    }
}
