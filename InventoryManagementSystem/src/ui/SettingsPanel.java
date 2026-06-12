package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class SettingsPanel extends JPanel {

    private static final Color BG         = new Color(240, 242, 240);
    private static final Color WHITE      = Color.WHITE;
    private static final Color TEXT       = new Color(20, 30, 25);
    private static final Color MUTED      = new Color(110, 130, 120);
    private static final Color BORDER     = new Color(220, 228, 224);
    private static final Color GREEN      = new Color(30, 80, 55);
    private static final Color GREEN_DARK = new Color(34, 90, 60);
    private static final Color RED        = new Color(180, 40, 40);
    private static final Color RED_DARK   = new Color(220, 50, 50);

    private String username;
    private JFrame parentFrame;

    // Dark mode colors
    private static final Color DARK_BG      = new Color(18, 24, 22);
    private static final Color DARK_CARD    = new Color(28, 36, 32);
    private static final Color DARK_TEXT    = new Color(220, 230, 225);
    private static final Color DARK_MUTED   = new Color(120, 150, 135);
    private static final Color DARK_BORDER  = new Color(45, 60, 50);

    // Track dark mode state 
    public static boolean darkMode = false;

    public SettingsPanel(String username, JFrame parentFrame) {
        this.username    = username;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(darkMode ? DARK_BG : BG);
        add(buildTopBar(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    // top

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(darkMode ? DARK_CARD : WHITE);
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, darkMode ? DARK_BORDER : BORDER),
            new EmptyBorder(12, 20, 12, 20)
        ));
        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Georgia", Font.BOLD, 15));
        title.setForeground(darkMode ? DARK_TEXT : TEXT);

        JLabel user = new JLabel("👤  " + username + "  ▾");
        user.setFont(new Font("SansSerif", Font.PLAIN, 13));
        user.setForeground(darkMode ? DARK_TEXT : TEXT);

        bar.add(title, BorderLayout.WEST);
        bar.add(user,  BorderLayout.EAST);
        return bar;
    }

    // content
    private JPanel buildContent() {
        JPanel scroll = new JPanel();
        scroll.setBackground(darkMode ? DARK_BG : BG);
        scroll.setLayout(new BoxLayout(scroll, BoxLayout.Y_AXIS));
        scroll.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel pageTitle = new JLabel("Settings");
        pageTitle.setFont(new Font("Georgia", Font.BOLD, 22));
        pageTitle.setForeground(darkMode ? DARK_TEXT : TEXT);
        pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        pageTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        scroll.add(pageTitle);

        // account
        scroll.add(sectionLabel("Account"));
        scroll.add(Box.createVerticalStrut(8));

        JPanel accountCard = buildCard();
        accountCard.setLayout(new BoxLayout(accountCard, BoxLayout.Y_AXIS));

        // Logged in as
        accountCard.add(settingRow(
            "👤", "Logged in as",
            username,
            null, null
        ));
        accountCard.add(rowDivider());

        // Change password
        JButton changePwdBtn = smallButton("Change");
        changePwdBtn.addActionListener(e -> showChangePasswordDialog());
        accountCard.add(settingRow(
            "🔑", "Change Password",
            "Update your login password",
            changePwdBtn, null
        ));
        accountCard.add(rowDivider());

        // Logout
        JButton logoutBtn = smallButton("Logout");
        logoutBtn.setBackground(RED);
        logoutBtn.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(
                parentFrame, "Are you sure you want to logout?",
                "Logout", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                parentFrame.dispose();
                new LoginPage().setVisible(true);
            }
        });
        accountCard.add(settingRow(
            "🚪", "Logout",
            "Sign out of your account",
            logoutBtn, null
        ));

        scroll.add(accountCard);
        scroll.add(Box.createVerticalStrut(20));

        // apperence section
        scroll.add(sectionLabel("Appearance"));
        scroll.add(Box.createVerticalStrut(8));

        JPanel appearanceCard = buildCard();
        appearanceCard.setLayout(new BoxLayout(appearanceCard, BoxLayout.Y_AXIS));

        // Dark mode toggle
        JToggleButton darkToggle = buildToggle(darkMode);
        darkToggle.addActionListener(e -> {
            darkMode = darkToggle.isSelected();
            JOptionPane.showMessageDialog(parentFrame,
                "Dark mode " + (darkMode ? "enabled" : "disabled") + ".\nPlease reopen the Settings page to see changes.",
                "Appearance", JOptionPane.INFORMATION_MESSAGE);
        });
        appearanceCard.add(settingRow(
            "🌙", "Dark Mode",
            "Switch to a darker colour scheme",
            null, darkToggle
        ));
        appearanceCard.add(rowDivider());

        // Font size
        String[] sizes = {"Small", "Medium (Default)", "Large"};
        JComboBox<String> fontSizeBox = new JComboBox<>(sizes);
        fontSizeBox.setSelectedIndex(1);
        fontSizeBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        fontSizeBox.setMaximumSize(new Dimension(160, 30));
        fontSizeBox.setBackground(WHITE);
        appearanceCard.add(settingRow(
            "🔤", "Font Size",
            "Adjust the display font size",
            null, fontSizeBox
        ));

        scroll.add(appearanceCard);
        scroll.add(Box.createVerticalStrut(20));

        // ── Section: System ──
        scroll.add(sectionLabel("System"));
        scroll.add(Box.createVerticalStrut(8));

        JPanel systemCard = buildCard();
        systemCard.setLayout(new BoxLayout(systemCard, BoxLayout.Y_AXIS));

        // Low stock threshold
        JSpinner thresholdSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        thresholdSpinner.setMaximumSize(new Dimension(80, 30));
        thresholdSpinner.setFont(new Font("SansSerif", Font.PLAIN, 12));
        systemCard.add(settingRow(
            "⚠", "Low Stock Threshold",
            "Alert when quantity falls below this value",
            null, thresholdSpinner
        ));
        systemCard.add(rowDivider());

        // Auto-refresh toggle
        JToggleButton autoRefresh = buildToggle(true);
        systemCard.add(settingRow(
            "⟳", "Auto Refresh Dashboard",
            "Automatically reload dashboard data",
            null, autoRefresh
        ));
        systemCard.add(rowDivider());

        // Export data
        JButton exportBtn = smallButton("Export CSV");
        exportBtn.addActionListener(e -> JOptionPane.showMessageDialog(parentFrame,
            "Export feature coming soon!", "Export", JOptionPane.INFORMATION_MESSAGE));
        systemCard.add(settingRow(
            "📥", "Export Inventory Data",
            "Download inventory as a CSV file",
            exportBtn, null
        ));

        scroll.add(systemCard);
        scroll.add(Box.createVerticalStrut(20));

        // about section
        scroll.add(sectionLabel("About"));
        scroll.add(Box.createVerticalStrut(8));

        JPanel aboutCard = buildCard();
        aboutCard.setLayout(new BoxLayout(aboutCard, BoxLayout.Y_AXIS));
        aboutCard.add(settingRow("📦", "Application",   "NASCA Inventory Management System", null, null));
        aboutCard.add(rowDivider());
        aboutCard.add(settingRow("🔖", "Version",       "1.0.0",                              null, null));
        aboutCard.add(rowDivider());
        aboutCard.add(settingRow("👩‍💻", "Developed by", "Shreelekshmi, Chaithanya, Nrithika, Ashna & Anagha",                              null, null));

        scroll.add(aboutCard);
        scroll.add(Box.createVerticalStrut(20));

        JScrollPane sp = new JScrollPane(scroll);
        sp.setBorder(null);
        sp.setBackground(darkMode ? DARK_BG : BG);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(darkMode ? DARK_BG : BG);
        wrapper.add(sp, BorderLayout.CENTER);
        return wrapper;
    }

    //password chnager

    private void showChangePasswordDialog() {
        JDialog dialog = new JDialog(parentFrame, "Change Password", true);
        dialog.setSize(360, 260);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(WHITE);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 12));
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(24, 24, 10, 24));

        JPasswordField fCurrent = new JPasswordField();
        JPasswordField fNew     = new JPasswordField();
        JPasswordField fConfirm = new JPasswordField();

        form.add(new JLabel("Current Password:")); form.add(fCurrent);
        form.add(new JLabel("New Password:"));     form.add(fNew);
        form.add(new JLabel("Confirm Password:")); form.add(fConfirm);

        JLabel statusLbl = new JLabel("", SwingConstants.CENTER);
        statusLbl.setForeground(Color.RED);
        statusLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton save = new JButton("Update Password");
        save.setBackground(GREEN);
        save.setForeground(WHITE);
        save.setFocusPainted(false);
        save.setBorder(new EmptyBorder(10, 20, 10, 20));
        save.addActionListener(e -> {
            String current = new String(fCurrent.getPassword()).trim();
            String newPwd  = new String(fNew.getPassword()).trim();
            String confirm = new String(fConfirm.getPassword()).trim();

            if (current.isEmpty() || newPwd.isEmpty() || confirm.isEmpty()) {
                statusLbl.setText("All fields are required."); return;
            }
            if (!newPwd.equals(confirm)) {
                statusLbl.setText("New passwords do not match."); return;
            }
            if (newPwd.length() < 6) {
                statusLbl.setText("Password must be at least 6 characters."); return;
            }

            // Update in DB
            new SwingWorker<Boolean, Void>() {
                @Override protected Boolean doInBackground() throws Exception {
                    try (java.sql.Connection con = database.DBConnection.getConnection();
                         java.sql.PreparedStatement check = con.prepareStatement(
                            "SELECT * FROM user WHERE username=? AND password=?")) {
                        check.setString(1, username);
                        check.setString(2, current);
                        java.sql.ResultSet rs = check.executeQuery();
                        if (!rs.next()) return false; // wrong current password

                        try (java.sql.PreparedStatement update = con.prepareStatement(
                            "UPDATE user SET password=? WHERE username=?")) {
                            update.setString(1, newPwd);
                            update.setString(2, username);
                            return update.executeUpdate() > 0;
                        }
                    }
                }
                @Override protected void done() {
                    try {
                        if (get()) {
                            dialog.dispose();
                            JOptionPane.showMessageDialog(parentFrame,
                                "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            statusLbl.setText("Current password is incorrect.");
                        }
                    } catch (Exception ex) { statusLbl.setText("Error: " + ex.getMessage()); }
                }
            }.execute();
        });

        JPanel bottom = new JPanel(new BorderLayout(0, 6));
        bottom.setBackground(WHITE);
        bottom.setBorder(new EmptyBorder(0, 24, 20, 24));
        bottom.add(statusLbl, BorderLayout.NORTH);
        bottom.add(save,      BorderLayout.CENTER);

        dialog.add(form,   BorderLayout.CENTER);
        dialog.add(bottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    //builders

    
    private JPanel settingRow(String icon, String title, String subtitle,
                               JButton btn, JComponent control) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(12, 16, 12, 16));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        // Left: icon + text
        JPanel left = new JPanel(new BorderLayout(10, 0));
        left.setOpaque(false);

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 18));
        iconLbl.setPreferredSize(new Dimension(30, 30));

        JPanel textBlock = new JPanel();
        textBlock.setOpaque(false);
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLbl.setForeground(darkMode ? DARK_TEXT : TEXT);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subLbl.setForeground(darkMode ? DARK_MUTED : MUTED);

        textBlock.add(titleLbl);
        textBlock.add(subLbl);

        left.add(iconLbl,   BorderLayout.WEST);
        left.add(textBlock, BorderLayout.CENTER);

        row.add(left, BorderLayout.CENTER);

        // Right: button or control
        if (btn != null) {
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            right.setOpaque(false);
            right.add(btn);
            row.add(right, BorderLayout.EAST);
        } else if (control != null) {
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            right.setOpaque(false);
            right.add(control);
            row.add(right, BorderLayout.EAST);
        }

        return row;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setBackground(darkMode ? DARK_CARD : WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(new CompoundBorder(
            new LineBorder(darkMode ? DARK_BORDER : BORDER, 1, true),
            new EmptyBorder(4, 0, 4, 0)
        ));
        return card;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(darkMode ? DARK_MUTED : MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 4, 0, 0));
        return l;
    }

    private JSeparator rowDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(darkMode ? DARK_BORDER : BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private JButton smallButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(GREEN);
        b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return b;
    }

    private JToggleButton buildToggle(boolean initialState) {
        JToggleButton toggle = new JToggleButton(initialState ? "ON" : "OFF", initialState) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isSelected() ? GREEN : new Color(180, 190, 185));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(WHITE);
                int cx = isSelected() ? getWidth() - getHeight() + 4 : 4;
                g2.fillOval(cx, 4, getHeight() - 8, getHeight() - 8);
            }
        };
        toggle.setPreferredSize(new Dimension(56, 26));
        toggle.setContentAreaFilled(false);
        toggle.setBorderPainted(false);
        toggle.setFocusPainted(false);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.addActionListener(e -> toggle.setText(toggle.isSelected() ? "ON" : "OFF"));
        toggle.setForeground(new Color(0,0,0,0)); // hide default text, drawn in paint
        return toggle;
    }
}
