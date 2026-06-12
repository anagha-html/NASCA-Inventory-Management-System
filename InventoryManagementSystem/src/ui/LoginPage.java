package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import service.LoginService;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private LoginService loginService = new LoginService();

    public LoginPage() {
        setTitle("Inventory Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

     // LEFT SIDE
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(null);

        ImageIcon warehouseIcon = new ImageIcon("images/warehouse4.jpg");

        JLabel warehouseLabel = new JLabel(warehouseIcon);
        warehouseLabel.setBounds(0, 0, 600, 700);
        warehouseLabel.setLayout(null); // IMPORTANT

        leftPanel.add(warehouseLabel);

        // Transparent overlay
        JPanel overlay = new JPanel();
        overlay.setLayout(null);
        
        overlay.setOpaque(false);
        // IMPORTANT
        overlay.setBounds(0, 0, 600, 700);

        JLabel title1 = new JLabel("Smart Inventory,");
        title1.setFont(new Font("Serif", Font.BOLD, 40));
        title1.setForeground(Color.WHITE);
        title1.setBounds(50, 120, 500, 50);

        JLabel title2 = new JLabel("Smarter Business.");
        title2.setFont(new Font("Serif", Font.BOLD, 40));
        title2.setForeground(new Color(144, 238, 144));
        title2.setBounds(50, 175, 500, 50);

        JLabel desc = new JLabel(
            "<html>Manage your stock, suppliers, and<br>" +
            "transactions — all in one place.</html>"
        );
        desc.setForeground(Color.WHITE);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        desc.setBounds(50, 260, 450, 80);

        overlay.add(title1);
        overlay.add(title2);
        overlay.add(desc);

        warehouseLabel.add(overlay);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(255, 253, 247));
        rightPanel.setLayout(null);


        // Logo
        ImageIcon logoIcon = new ImageIcon("images/logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(180, 30, 250, 150);
        rightPanel.add(logoLabel);

        // Welcome text
        JLabel welcome = new JLabel("Welcome Back");
        welcome.setFont(new Font("Georgia", Font.BOLD, 36));
        welcome.setBounds(150, 180, 350, 50);
        welcome.setForeground(new Color(17, 24, 39));
        rightPanel.add(welcome);

        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(Color.GRAY);
        subtitle.setBounds(180, 225, 250, 30);
        rightPanel.add(subtitle);

        //username label
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(80, 80, 80));
        userLabel.setBounds(100, 275, 200, 20);
        rightPanel.add(userLabel);

        //username feld 
        usernameField = new JTextField();
        usernameField.setBounds(100, 295, 400, 50);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        usernameField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        rightPanel.add(usernameField);

        //pass
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passLabel.setForeground(new Color(80, 80, 80));
        passLabel.setBounds(100, 355, 200, 20);
        rightPanel.add(passLabel);

        //password entry
        passwordField = new JPasswordField();
        passwordField.setBounds(100, 375, 400, 50);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        rightPanel.add(passwordField);

        //error 
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        errorLabel.setForeground(new Color(200, 40, 40));
        errorLabel.setBounds(100, 432, 400, 20);
        rightPanel.add(errorLabel);

        // checkbox for remember me
        JCheckBox remember = new JCheckBox("Remember Me");
        remember.setBounds(100, 458, 150, 30);
        remember.setBackground(new Color(255, 253, 247));
        remember.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rightPanel.add(remember);

        JLabel forgot = new JLabel("Forgot Password?");
        forgot.setBounds(340, 458, 160, 30);
        forgot.setForeground(new Color(6, 95, 70));
        forgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightPanel.add(forgot);

        // Login button
        JButton loginButton = new JButton("LOGIN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()
                    ? new Color(4, 120, 87)
                    : new Color(6, 95, 70));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        loginButton.setBounds(100, 505, 400, 55);
        loginButton.setBackground(new Color(6, 95, 70));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBorder(new EmptyBorder(10, 10, 10, 10));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightPanel.add(loginButton);

        // Loading label
        JLabel loadingLabel = new JLabel("");
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadingLabel.setForeground(new Color(6, 95, 70));
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingLabel.setBounds(100, 565, 400, 20);
        rightPanel.add(loadingLabel);

        JLabel footer = new JLabel("© NASCA Inventory Management System");
        footer.setBounds(130, 630, 350, 30);
        footer.setForeground(Color.GRAY);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rightPanel.add(footer);

        // login butt acton
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // Basic validation
            if (username.isEmpty() || password.isEmpty()) {
                showError("Please enter both username and password.");
                return;
            }

            // Disable button and show loading
            loginButton.setEnabled(false);
            loadingLabel.setText("Signing in...");
            errorLabel.setText("");

            // Run login on background thread so UI doesn't freeze
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() {
                    return loginService.login(username, password);
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        loadingLabel.setText("");
                        loginButton.setEnabled(true);

                        if (success) {
                            //to open the dash if login is success
                            dispose();
                            new InventoryDashboard(username).setVisible(true);
                        } else {
                            showError("Invalid username or password.");
                            passwordField.setText("");
                            passwordField.requestFocus();
                        }
                    } catch (Exception ex) {
                        loadingLabel.setText("");
                        loginButton.setEnabled(true);
                        showError("Connection error. Check database.");
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute();
        });

        
        passwordField.addActionListener(e -> loginButton.doClick());
        usernameField.addActionListener(e -> passwordField.requestFocus());

        
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);
        setVisible(true);
    }

    private void showError(String message) {
        errorLabel.setText("⚠  " + message);
        usernameField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 40, 40), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        passwordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 40, 40), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}