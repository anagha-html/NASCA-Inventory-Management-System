package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import database.DBConnection;

public class SuppliersPanel extends JPanel {

    private static final Color DARK       = new Color(22, 30, 26);
    private static final Color GREEN      = new Color(30, 80, 55);
    private static final Color GREEN_DARK = new Color(34, 90, 60);
    private static final Color BG         = new Color(240, 242, 240);
    private static final Color WHITE      = Color.WHITE;
    private static final Color TEXT       = new Color(20, 30, 25);
    private static final Color MUTED      = new Color(110, 130, 120);
    private static final Color BORDER     = new Color(220, 228, 224);

    private DefaultTableModel tableModel;
    private JTable table;
    private String username;

    public SuppliersPanel(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        setBackground(BG);
        add(buildTopBar(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        loadSuppliers();
    }

    // top bar

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(WHITE);
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER),
            new EmptyBorder(12, 20, 12, 20)
        ));
        JLabel title = new JLabel("Supplier Management");
        title.setFont(new Font("Georgia", Font.BOLD, 15));
        title.setForeground(TEXT);
        JLabel user = new JLabel("👤  " + username + "  ▾");
        user.setFont(new Font("SansSerif", Font.PLAIN, 13));
        user.setForeground(TEXT);
        bar.add(title, BorderLayout.WEST);
        bar.add(user,  BorderLayout.EAST);
        return bar;
    }

    // content

    private JPanel buildContent() {
        JPanel content = new JPanel();
        content.setBackground(BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("Suppliers");
        heading.setFont(new Font("Georgia", Font.BOLD, 22));
        heading.setForeground(TEXT);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        heading.setBorder(new EmptyBorder(0, 0, 16, 0));
        content.add(heading);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        // header titles and buttons 
        JPanel header = new JPanel(new BorderLayout(8, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel tableTitle = new JLabel("All Suppliers");
        tableTitle.setFont(new Font("Georgia", Font.BOLD, 14));
        tableTitle.setForeground(TEXT);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        buttons.setOpaque(false);

        JButton addBtn    = makeButton("+ Add");
        JButton updateBtn = makeButton("✏ Update");
        JButton deleteBtn = makeButton("🗑 Delete");

        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);

        header.add(tableTitle, BorderLayout.WEST);
        header.add(buttons,    BorderLayout.EAST);

        // table
        String[] cols = {"ID", "Supplier Name", "Contact Number", "Email", "Address"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setBackground(WHITE);
        table.setSelectionBackground(new Color(235, 248, 240));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("SansSerif", Font.BOLD, 11));
        th.setBackground(new Color(245, 248, 246));
        th.setForeground(MUTED);
        th.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        th.setReorderingAllowed(false);

        // Right-click context menu
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuUpdate = new JMenuItem("✏  Update Supplier");
        JMenuItem menuDelete = new JMenuItem("🗑  Delete Supplier");
        menuUpdate.addActionListener(e -> updateSelectedSupplier());
        menuDelete.addActionListener(e -> deleteSelectedSupplier());
        menu.add(menuUpdate);
        menu.add(menuDelete);
        table.setComponentPopupMenu(menu);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) table.setRowSelectionInterval(row, row);
            }
        });

        // Button actions
        addBtn.addActionListener(e    -> showAddDialog());
        updateBtn.addActionListener(e -> updateSelectedSupplier());
        deleteBtn.addActionListener(e -> deleteSelectedSupplier());

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);

        card.add(header, BorderLayout.NORTH);
        card.add(sp,     BorderLayout.CENTER);

        content.add(card);
        return content;
    }

    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(GREEN);
        b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(6, 12, 6, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return b;
    }

    // load suppliers

    private void loadSuppliers() {
        new SwingWorker<Void, Void>() {
            java.util.List<Object[]> rows = new java.util.ArrayList<>();

            @Override protected Void doInBackground() {
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(
                        "SELECT SupplierID, SupplierName, ContactNumber, Email, Address " +
                        "FROM supplierdetails ORDER BY SupplierID DESC");
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rows.add(new Object[]{
                            rs.getInt("SupplierID"),
                            rs.getString("SupplierName"),
                            rs.getString("ContactNumber"),
                            rs.getString("Email"),
                            rs.getString("Address")
                        });
                    }
                } catch (Exception e) { e.printStackTrace(); }
                return null;
            }

            @Override protected void done() {
                tableModel.setRowCount(0);
                rows.forEach(tableModel::addRow);
            }
        }.execute();
    }

    //add suppliers 

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Supplier", true);
        dialog.setSize(400, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(WHITE);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 12));
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(24, 24, 10, 24));

        JTextField fName    = new JTextField();
        JTextField fContact = new JTextField();
        JTextField fEmail   = new JTextField();
        JTextField fAddress = new JTextField();

        form.add(new JLabel("Supplier Name:"));  form.add(fName);
        form.add(new JLabel("Contact Number:")); form.add(fContact);
        form.add(new JLabel("Email:"));           form.add(fEmail);
        form.add(new JLabel("Address:"));         form.add(fAddress);

        JLabel statusLbl = new JLabel("", SwingConstants.CENTER);
        statusLbl.setForeground(Color.RED);
        statusLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton save = new JButton("Save Supplier");
        save.setBackground(GREEN); save.setForeground(WHITE);
        save.setFocusPainted(false);
        save.setBorder(new EmptyBorder(10, 20, 10, 20));
        save.addActionListener(e -> {
            String name    = fName.getText().trim();
            String contact = fContact.getText().trim();
            String email   = fEmail.getText().trim();
            String address = fAddress.getText().trim();

            if (name.isEmpty()) { statusLbl.setText("Supplier Name is required."); return; }

            new SwingWorker<Boolean, Void>() {
                @Override protected Boolean doInBackground() throws Exception {
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO supplierdetails(SupplierName, ContactNumber, Email, Address) VALUES(?,?,?,?)")) {
                        ps.setString(1, name);
                        ps.setString(2, contact.isEmpty() ? null : contact);
                        ps.setString(3, email.isEmpty()   ? null : email);
                        ps.setString(4, address.isEmpty() ? null : address);
                        return ps.executeUpdate() > 0;
                    }
                }
                @Override protected void done() {
                    try {
                        if (get()) {
                            dialog.dispose();
                            loadSuppliers();
                            JOptionPane.showMessageDialog(SuppliersPanel.this,
                                "Supplier added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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

    // update

    private void updateSelectedSupplier() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a supplier to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int    id      = (int)    tableModel.getValueAt(row, 0);
        String name    = nullSafe(tableModel.getValueAt(row, 1));
        String contact = nullSafe(tableModel.getValueAt(row, 2));
        String email   = nullSafe(tableModel.getValueAt(row, 3));
        String address = nullSafe(tableModel.getValueAt(row, 4));

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Update Supplier — " + name, true);
        dialog.setSize(400, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(WHITE);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 12));
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(24, 24, 10, 24));

        // Pre-fill current values
        JTextField fName    = new JTextField(name);
        JTextField fContact = new JTextField(contact);
        JTextField fEmail   = new JTextField(email);
        JTextField fAddress = new JTextField(address);

        form.add(new JLabel("Supplier Name:"));  form.add(fName);
        form.add(new JLabel("Contact Number:")); form.add(fContact);
        form.add(new JLabel("Email:"));           form.add(fEmail);
        form.add(new JLabel("Address:"));         form.add(fAddress);

        JLabel statusLbl = new JLabel("", SwingConstants.CENTER);
        statusLbl.setForeground(Color.RED);
        statusLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton save = new JButton("Update Supplier");
        save.setBackground(GREEN); save.setForeground(WHITE);
        save.setFocusPainted(false);
        save.setBorder(new EmptyBorder(10, 20, 10, 20));
        save.addActionListener(e -> {
            String newName    = fName.getText().trim();
            String newContact = fContact.getText().trim();
            String newEmail   = fEmail.getText().trim();
            String newAddress = fAddress.getText().trim();

            if (newName.isEmpty()) { statusLbl.setText("Supplier Name is required."); return; }

            new SwingWorker<Boolean, Void>() {
                @Override protected Boolean doInBackground() throws Exception {
                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(
                            "UPDATE supplierdetails SET SupplierName=?, ContactNumber=?, Email=?, Address=? WHERE SupplierID=?")) {
                        ps.setString(1, newName);
                        ps.setString(2, newContact.isEmpty() ? null : newContact);
                        ps.setString(3, newEmail.isEmpty()   ? null : newEmail);
                        ps.setString(4, newAddress.isEmpty() ? null : newAddress);
                        ps.setInt(5, id);
                        return ps.executeUpdate() > 0;
                    }
                }
                @Override protected void done() {
                    try {
                        if (get()) {
                            dialog.dispose();
                            loadSuppliers();
                            JOptionPane.showMessageDialog(SuppliersPanel.this,
                                "Supplier updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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

    // delete supplier

    private void deleteSelectedSupplier() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a supplier to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int    id   = (int)    tableModel.getValueAt(row, 0);
        String name = nullSafe(tableModel.getValueAt(row, 1));

        if (JOptionPane.showConfirmDialog(this,
                "Delete supplier \"" + name + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception {
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM supplierdetails WHERE SupplierID = ?")) {
                    ps.setInt(1, id);
                    return ps.executeUpdate() > 0;
                }
            }
            @Override protected void done() {
                try { if (get()) loadSuppliers(); }
                catch (Exception ex) { ex.printStackTrace(); }
            }
        }.execute();
    }

    // helper
    private String nullSafe(Object val) {
        return val != null ? val.toString() : "";
    }
}
