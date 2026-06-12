package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import database.DBConnection;


public class TransactionAndReportsPanel extends JPanel {

    private static final Color GREEN      = new Color(30, 80, 55);
    private static final Color GREEN_DARK = new Color(34, 90, 60);
    private static final Color BG         = new Color(240, 242, 240);
    private static final Color WHITE      = Color.WHITE;
    private static final Color TEXT       = new Color(20, 30, 25);
    private static final Color MUTED      = new Color(110, 130, 120);
    private static final Color BORDER     = new Color(220, 228, 224);
    private static final Color GREEN_BG   = new Color(220, 252, 231);
    private static final Color GREEN_FG   = new Color(22, 101, 52);
    private static final Color AMBER_BG   = new Color(254, 243, 199);
    private static final Color AMBER_FG   = new Color(146, 64, 14);
    private static final Color RED_BG     = new Color(254, 226, 226);
    private static final Color RED_FG     = new Color(153, 27, 27);

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final String username;

    private DefaultTableModel logModel;
    private DefaultTableModel reportModel;

    public TransactionAndReportsPanel(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        setBackground(BG);

        add(topBar(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 12));
        tabs.addTab("➕ Add Transaction", wrap(buildAddTab()));
        tabs.addTab("🧾 Transaction Log", wrap(buildLogTab()));
        tabs.addTab("📊 Reports",        wrap(buildReportsTab()));

        add(tabs, BorderLayout.CENTER);
    }

    // top bar

    private JPanel topBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(WHITE);
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER), new EmptyBorder(12, 20, 12, 20)));
        JLabel title = new JLabel("Transaction & Report Management");
        title.setFont(new Font("Georgia", Font.BOLD, 15));
        title.setForeground(TEXT);
        JLabel user = new JLabel("👤  " + username + "  ▾");
        user.setFont(new Font("SansSerif", Font.PLAIN, 13));
        user.setForeground(TEXT);
        bar.add(title, BorderLayout.WEST);
        bar.add(user,  BorderLayout.EAST);
        return bar;
    }

    private JScrollPane wrap(JComponent content) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(16, 16, 16, 16));
        outer.add(content, BorderLayout.CENTER);
        JScrollPane sp = new JScrollPane(outer);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    // add trans

    private JPanel buildAddTab() {
        JPanel card = card("Add Transaction");

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 12));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(12, 0, 16, 0));

        JComboBox<Item> itemCombo = itemComboBox();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{
            "Stock In", "Stock Out", "Sale", "Purchase"
        });
        styleCombo(typeCombo);

        JTextField fQty   = tf();
        JTextField fPrice = tf();
        JTextField fDate  = tf(LocalDate.now().format(DATE_FMT));
        JTextField fRemark = tf();

        // Auto-fill price from selected item
        itemCombo.addActionListener(e -> {
            Item sel = (Item) itemCombo.getSelectedItem();
            if (sel != null) fPrice.setText(String.format("%.2f", sel.price));
        });
        if (itemCombo.getItemCount() > 0) {
            Item sel = (Item) itemCombo.getSelectedItem();
            if (sel != null) fPrice.setText(String.format("%.2f", sel.price));
        }

        form.add(lbl("Item:"));              form.add(itemCombo);
        form.add(lbl("Transaction Type:"));  form.add(typeCombo);
        form.add(lbl("Quantity:"));          form.add(fQty);
        form.add(lbl("Price:"));             form.add(fPrice);
        form.add(lbl("Date (yyyy-MM-dd):")); form.add(fDate);
        form.add(lbl("Remarks:"));           form.add(fRemark);

        JLabel statusLbl = errorLbl();
        JButton saveBtn = primaryBtn("Save Transaction");

        saveBtn.addActionListener(e -> {
            Item item = (Item) itemCombo.getSelectedItem();
            if (item == null) { setError(statusLbl, "No item selected."); return; }
            String type = (String) typeCombo.getSelectedItem();

            int qty;
            double price;
            LocalDate date;
            try {
                qty = Integer.parseInt(fQty.getText().trim());
                price = Double.parseDouble(fPrice.getText().trim());
                date = LocalDate.parse(fDate.getText().trim(), DATE_FMT);
            } catch (Exception ex) {
                setError(statusLbl, "Quantity/Price must be numeric and Date must be yyyy-MM-dd.");
                return;
            }
            if (qty <= 0) { setError(statusLbl, "Quantity must be greater than zero."); return; }

            boolean increase = type.equals("Stock In") || type.equals("Purchase");
            // Stock Out / Sale decrease stock
            if (!increase && qty > item.qty) {
                setError(statusLbl, "Not enough stock. Available: " + item.qty);
                return;
            }

            saveBtn.setEnabled(false);
            new SwingWorker<Boolean, Void>() {
                @Override protected Boolean doInBackground() throws Exception {
                    try (Connection con = DBConnection.getConnection()) {
                        if (con == null) return false;
                        con.setAutoCommit(false);
                        try {
                            try (PreparedStatement ps = con.prepareStatement(
                                    "INSERT INTO Transactions(ItemID,TransactionType,Quantity,Price,TransactionDate,Remarks) VALUES(?,?,?,?,?,?)")) {
                                ps.setInt(1, item.id);
                                ps.setString(2, type);
                                ps.setInt(3, qty);
                                ps.setDouble(4, price);
                                ps.setTimestamp(5, Timestamp.valueOf(date.atStartOfDay()));
                                ps.setString(6, fRemark.getText().trim());
                                ps.executeUpdate();
                            }
                            String sql = increase
                                ? "UPDATE InventoryItem SET QuantityAvailable = QuantityAvailable + ? WHERE ItemID=?"
                                : "UPDATE InventoryItem SET QuantityAvailable = QuantityAvailable - ? WHERE ItemID=?";
                            try (PreparedStatement ps = con.prepareStatement(sql)) {
                                ps.setInt(1, qty);
                                ps.setInt(2, item.id);
                                ps.executeUpdate();
                            }
                            con.commit();
                            return true;
                        } catch (Exception ex) {
                            con.rollback();
                            throw ex;
                        }
                    }
                }
                @Override protected void done() {
                    saveBtn.setEnabled(true);
                    try {
                        if (get()) {
                            setSuccess(statusLbl, "✓ Transaction saved.");
                            fQty.setText("");
                            fRemark.setText("");
                            reloadItemCombo(itemCombo, item.id);
                            if (logModel != null) loadLog();
                        } else {
                            setError(statusLbl, "Failed to save (no DB connection).");
                        }
                    } catch (Exception ex) {
                        setError(statusLbl, "Error: " + rootMsg(ex));
                    }
                }
            }.execute();
        });

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(form, BorderLayout.NORTH);
        content.add(saveBtn, BorderLayout.CENTER);
        content.add(statusLbl, BorderLayout.SOUTH);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    // ── 2. TRANSACTION LOG ───────────────────────────────────────────────────

    private JPanel buildLogTab() {
        JPanel card = card("Transaction Log");

        logModel = new DefaultTableModel(
            new String[]{"ID","Date","Item","Type","Qty","Price","Total","Remarks"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = styledTable(logModel);
        table.getColumnModel().getColumn(3).setCellRenderer(typeRenderer());

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setBorder(new EmptyBorder(0, 0, 10, 0));
        JButton refreshBtn = secondaryBtn("⟳ Refresh");
        refreshBtn.addActionListener(e -> loadLog());
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(refreshBtn);
        topRow.add(right, BorderLayout.EAST);

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(topRow, BorderLayout.NORTH);
        content.add(scrollTable(table, 380), BorderLayout.CENTER);

        card.add(content, BorderLayout.CENTER);
        loadLog();
        return card;
    }

    private void loadLog() {
        new SwingWorker<java.util.List<Object[]>, Void>() {
            @Override protected java.util.List<Object[]> doInBackground() {
                java.util.List<Object[]> rows = new java.util.ArrayList<>();
                String sql = "SELECT t.TransactionID, t.TransactionDate, i.ItemName, t.TransactionType, " +
                             "t.Quantity, t.Price, (t.Quantity*t.Price), t.Remarks " +
                             "FROM Transactions t JOIN InventoryItem i ON t.ItemID=i.ItemID " +
                             "ORDER BY t.TransactionDate DESC, t.TransactionID DESC";
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rows.add(new Object[]{
                            rs.getInt(1), fmtDate(rs.getTimestamp(2)), rs.getString(3), rs.getString(4),
                            rs.getInt(5), money(rs.getDouble(6)), money(rs.getDouble(7)), rs.getString(8)
                        });
                    }
                } catch (Exception e) { e.printStackTrace(); }
                return rows;
            }
            @Override protected void done() {
                try {
                    logModel.setRowCount(0);
                    get().forEach(logModel::addRow);
                } catch (Exception ignored) {}
            }
        }.execute();
    }

    // ── 3. REPORTS ───────────────────────────────────────────────────────────

    private JPanel buildReportsTab() {
        JPanel card = card("Reports");

        JComboBox<String> reportCombo = new JComboBox<>(new String[]{
            "Stock Report", "Sales Report", "Purchase Report", "Low Stock Report"
        });
        styleCombo(reportCombo);

        JButton genBtn = primaryBtn("Generate");

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        controls.setOpaque(false);
        controls.add(lbl("Select Report:"));
        controls.add(reportCombo);
        controls.add(genBtn);

        JLabel summaryLbl = new JLabel(" ");
        summaryLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        summaryLbl.setForeground(GREEN_DARK);
        summaryLbl.setBorder(new EmptyBorder(0, 0, 8, 0));

        reportModel = new DefaultTableModel();
        JTable table = styledTable(reportModel);

        genBtn.addActionListener(e -> generateReport((String) reportCombo.getSelectedItem(), table, summaryLbl));

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(controls, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(summaryLbl, BorderLayout.NORTH);
        center.add(scrollTable(table, 360), BorderLayout.CENTER);
        content.add(center, BorderLayout.CENTER);

        card.add(content, BorderLayout.CENTER);

        // Load default report on open
        generateReport("Stock Report", table, summaryLbl);
        return card;
    }

    private void generateReport(String reportType, JTable table, JLabel summaryLbl) {
        summaryLbl.setText("Loading…");
        switch (reportType) {
            case "Stock Report"      -> loadStockReport(table, summaryLbl);
            case "Sales Report"      -> loadTypeReport("Sale", table, summaryLbl, GREEN_FG);
            case "Purchase Report"   -> loadTypeReport("Purchase", table, summaryLbl, new Color(70,70,120));
            case "Low Stock Report"  -> loadLowStockReport(table, summaryLbl);
        }
    }

    private void loadStockReport(JTable table, JLabel summaryLbl) {
        new SwingWorker<Void, Void>() {
            DefaultTableModel model;
            double totalValue = 0;
            int totalUnits = 0;

            @Override protected Void doInBackground() {
                model = new DefaultTableModel(
                    new String[]{"Item","Category","Qty","Price","Stock Value","Status"}, 0) {
                    @Override public boolean isCellEditable(int r, int c) { return false; }
                };
                String sql = "SELECT ItemName, Category, QuantityAvailable, MinimumStockLevel, Price FROM InventoryItem ORDER BY ItemName";
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int qty = rs.getInt(3);
                        int min = rs.getInt(4);
                        double price = rs.getDouble(5);
                        double value = qty * price;
                        String status = qty == 0 ? "Out of Stock" : qty <= min ? "Low Stock" : "In Stock";
                        model.addRow(new Object[]{ rs.getString(1), rs.getString(2), qty, money(price), money(value), status });
                        totalValue += value;
                        totalUnits += qty;
                    }
                } catch (Exception e) { e.printStackTrace(); }
                return null;
            }
            @Override protected void done() {
                table.setModel(model);
                table.getColumnModel().getColumn(5).setCellRenderer(statusRenderer());
                summaryLbl.setForeground(GREEN_DARK);
                summaryLbl.setText(String.format("Total Items: %d   |   Total Units: %d   |   Total Stock Value: %s",
                    model.getRowCount(), totalUnits, money(totalValue)));
            }
        }.execute();
    }

    private void loadTypeReport(String type, JTable table, JLabel summaryLbl, Color color) {
        new SwingWorker<Void, Void>() {
            DefaultTableModel model;
            double totalAmount = 0;
            int totalQty = 0;

            @Override protected Void doInBackground() {
                model = new DefaultTableModel(
                    new String[]{"Date","Item","Qty","Price","Total"}, 0) {
                    @Override public boolean isCellEditable(int r, int c) { return false; }
                };
                String sql = "SELECT t.TransactionDate, i.ItemName, t.Quantity, t.Price, (t.Quantity*t.Price) " +
                             "FROM Transactions t JOIN InventoryItem i ON t.ItemID=i.ItemID " +
                             "WHERE t.TransactionType=? ORDER BY t.TransactionDate DESC";
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, type);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int qty = rs.getInt(3);
                            double total = rs.getDouble(5);
                            model.addRow(new Object[]{ fmtDate(rs.getTimestamp(1)), rs.getString(2), qty, money(rs.getDouble(4)), money(total) });
                            totalQty += qty;
                            totalAmount += total;
                        }
                    }
                } catch (Exception e) { e.printStackTrace(); }
                return null;
            }
            @Override protected void done() {
                table.setModel(model);
                summaryLbl.setForeground(color);
                summaryLbl.setText(String.format("%d transaction(s)   |   Total Qty: %d   |   Total Amount: %s",
                    model.getRowCount(), totalQty, money(totalAmount)));
            }
        }.execute();
    }

    private void loadLowStockReport(JTable table, JLabel summaryLbl) {
        new SwingWorker<Void, Void>() {
            DefaultTableModel model;

            @Override protected Void doInBackground() {
                model = new DefaultTableModel(
                    new String[]{"Item","Category","Qty","Min Level","Status"}, 0) {
                    @Override public boolean isCellEditable(int r, int c) { return false; }
                };
                String sql = "SELECT ItemName, Category, QuantityAvailable, MinimumStockLevel FROM InventoryItem " +
                             "WHERE QuantityAvailable <= MinimumStockLevel ORDER BY (MinimumStockLevel-QuantityAvailable) DESC";
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int qty = rs.getInt(3);
                        int min = rs.getInt(4);
                        String status = qty == 0 ? "Out of Stock" : "Low Stock";
                        model.addRow(new Object[]{ rs.getString(1), rs.getString(2), qty, min, status });
                    }
                } catch (Exception e) { e.printStackTrace(); }
                return null;
            }
            @Override protected void done() {
                table.setModel(model);
                table.getColumnModel().getColumn(4).setCellRenderer(statusRenderer());
                summaryLbl.setForeground(model.getRowCount() == 0 ? GREEN_FG : AMBER_FG);
                summaryLbl.setText(model.getRowCount() == 0
                    ? "✓ All items are above their minimum stock level."
                    : "⚠ " + model.getRowCount() + " item(s) at or below minimum stock level.");
            }
        }.execute();
    }

    // ── SHARED UI HELPERS ────────────────────────────────────────────────────

    private JPanel card(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(BORDER,1,true), new EmptyBorder(18,20,18,20)));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Georgia", Font.BOLD, 15));
        heading.setForeground(TEXT);
        heading.setBorder(new EmptyBorder(0, 0, 4, 0));
        card.add(heading, BorderLayout.NORTH);
        return card;
    }

    private JScrollPane scrollTable(JTable table, int height) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(BORDER, 1, true));
        sp.setPreferredSize(new Dimension(10, height));
        sp.getViewport().setBackground(WHITE);
        return sp;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setBackground(WHITE);
        table.setSelectionBackground(new Color(235, 248, 240));
        table.setSelectionForeground(TEXT);
        table.setIntercellSpacing(new Dimension(0, 0));
        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("SansSerif", Font.BOLD, 11));
        th.setBackground(new Color(245, 248, 246));
        th.setForeground(MUTED);
        th.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        th.setReorderingAllowed(false);
        return table;
    }

    private DefaultTableCellRenderer statusRenderer() {
        return new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = new JLabel(v != null ? v.toString() : "", SwingConstants.CENTER);
                l.setFont(new Font("SansSerif", Font.BOLD, 10));
                l.setOpaque(true);
                switch (v != null ? v.toString() : "") {
                    case "In Stock"  -> { l.setBackground(GREEN_BG); l.setForeground(GREEN_FG); }
                    case "Low Stock" -> { l.setBackground(AMBER_BG); l.setForeground(AMBER_FG); }
                    default          -> { l.setBackground(RED_BG); l.setForeground(RED_FG); }
                }
                l.setBorder(new EmptyBorder(2, 8, 2, 8));
                return l;
            }
        };
    }

    private DefaultTableCellRenderer typeRenderer() {
        return new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = new JLabel(v != null ? v.toString() : "", SwingConstants.CENTER);
                l.setFont(new Font("SansSerif", Font.BOLD, 10));
                l.setOpaque(true);
                switch (v != null ? v.toString() : "") {
                    case "Stock In", "Purchase" -> { l.setBackground(GREEN_BG); l.setForeground(GREEN_FG); }
                    case "Stock Out", "Sale"    -> { l.setBackground(AMBER_BG); l.setForeground(AMBER_FG); }
                    default                     -> { l.setBackground(new Color(230,230,250)); l.setForeground(new Color(70,70,120)); }
                }
                l.setBorder(new EmptyBorder(2, 8, 2, 8));
                return l;
            }
        };
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }

    private JLabel errorLbl() {
        JLabel l = new JLabel(" ");
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }
    private void setError(JLabel l, String msg)   { l.setForeground(RED_FG);   l.setText(msg); }
    private void setSuccess(JLabel l, String msg) { l.setForeground(GREEN_FG); l.setText(msg); }

    private JTextField tf() { return tf(""); }
    private JTextField tf(String v) {
        JTextField f = new JTextField(v);
        f.setBorder(new CompoundBorder(new LineBorder(BORDER,1,true), new EmptyBorder(6,8,6,8)));
        f.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return f;
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        combo.setBackground(WHITE);
        combo.setBorder(new CompoundBorder(new LineBorder(BORDER,1,true), new EmptyBorder(2,4,2,4)));
    }

    private JButton primaryBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(GREEN);
        b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        return b;
    }

    private JButton secondaryBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(GREEN_DARK);
        b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return b;
    }

    private static String money(double d) { return String.format("%.2f", d); }

    private static String fmtDate(Timestamp ts) {
        if (ts == null) return "";
        return ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private static String rootMsg(Throwable t) {
        Throwable cause = t;
        while (cause.getCause() != null) cause = cause.getCause();
        return cause.getMessage() != null ? cause.getMessage() : cause.toString();
    }

    // ── ITEM COMBO BOX ───────────────────────────────────────────────────────

    private static class Item {
        final int id; final String name; final int qty; final double price;
        Item(int id, String name, int qty, double price) {
            this.id = id; this.name = name; this.qty = qty; this.price = price;
        }
        @Override public String toString() { return name + "  (Stock: " + qty + ")"; }
    }

    private JComboBox<Item> itemComboBox() {
        JComboBox<Item> combo = new JComboBox<>();
        styleCombo(combo);
        reloadItemCombo(combo, -1);
        return combo;
    }

    private void reloadItemCombo(JComboBox<Item> combo, int preserveId) {
        new SwingWorker<java.util.List<Item>, Void>() {
            @Override protected java.util.List<Item> doInBackground() {
                java.util.List<Item> items = new java.util.ArrayList<>();
                String sql = "SELECT ItemID, ItemName, QuantityAvailable, Price FROM InventoryItem ORDER BY ItemName";
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) items.add(new Item(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4)));
                } catch (Exception e) { e.printStackTrace(); }
                return items;
            }
            @Override protected void done() {
                try {
                    java.util.List<Item> items = get();
                    combo.removeAllItems();
                    int selectIndex = 0;
                    for (int i = 0; i < items.size(); i++) {
                        combo.addItem(items.get(i));
                        if (items.get(i).id == preserveId) selectIndex = i;
                    }
                    if (combo.getItemCount() > 0) combo.setSelectedIndex(selectIndex);
                } catch (Exception ignored) {}
            }
        }.execute();
    }
}