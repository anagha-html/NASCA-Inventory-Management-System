package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;

import database.DBConnection;

public class InventoryDashboard extends JFrame {

    private static final Color DARK       = new Color(22, 30, 26);
    private static final Color GREEN      = new Color(30, 80, 55);
    private static final Color GREEN_DARK = new Color(34, 90, 60);
    private static final Color BG         = new Color(240, 242, 240);
    private static final Color WHITE      = Color.WHITE;
    private static final Color TEXT       = new Color(20, 30, 25);
    private static final Color MUTED      = new Color(110, 130, 120);
    private static final Color BORDER     = new Color(220, 228, 224);
    private static final Color AMBER      = new Color(217, 119, 6);

    private JLabel lblTotalProducts = makeStatValue("...");
    private JLabel lblLowStock      = makeStatValue("...");
    private JLabel lblSuppliers     = makeStatValue("...");
    private JLabel lblTransactions  = makeStatValue("...");

    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel alertsPanel;
    private String username;

    private CardLayout cardLayout   = new CardLayout();
    private JPanel     contentCards = new JPanel(cardLayout);

    private JPanel navDashboard;
    private JPanel navSuppliers;
    private JPanel navTransactions;
    private JPanel navSettings;

    public InventoryDashboard(String username) {
        this.username = username;
        setTitle("NASCA – Inventory Management System");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(buildSidebar(),  BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);
        loadData();
    }

    public InventoryDashboard() { this("Admin"); }

    // ── SIDEBAR ──────────────────────────────────────────────────────────────

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(DARK);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("  NASCA");
        logo.setFont(new Font("Georgia", Font.BOLD, 18));
        logo.setForeground(WHITE);
        logo.setBorder(new EmptyBorder(24, 16, 8, 16));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("  Inventory System");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        sub.setForeground(new Color(150, 180, 160));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(sub);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(makeDivider());

        navDashboard = navItem("⌂  Dashboard", true);
        navSuppliers = navItem("⊡  Suppliers",  false);
        navTransactions = navItem("⇄  Transactions & Reports", false);
        navSettings  = navItem("⚙  Settings",   false);

        navDashboard.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentCards, "dashboard");
                setActive(navDashboard, navSuppliers, navTransactions, navSettings);
            }
        });
        navSuppliers.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentCards, "suppliers");
                setActive(navSuppliers, navDashboard, navTransactions, navSettings);
            }
        });
        navTransactions.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentCards, "transactions");
                setActive(navTransactions, navDashboard, navSuppliers, navSettings);
            }
        });
        navSettings.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                contentCards.remove(contentCards.getComponentCount() - 1);
                contentCards.add(new SettingsPanel(username, InventoryDashboard.this), "settings");
                cardLayout.show(contentCards, "settings");
                setActive(navSettings, navDashboard, navSuppliers, navTransactions);
            }
        });

        sidebar.add(navDashboard);
        sidebar.add(navSuppliers);
        sidebar.add(navTransactions);
        
        sidebar.add(navSettings);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(makeDivider());

        JPanel logout = navItem("→  Logout", false);
        logout.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int ok = JOptionPane.showConfirmDialog(
                    InventoryDashboard.this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) { dispose(); new LoginPage().setVisible(true); }
            }
        });
        sidebar.add(logout);
        return sidebar;
    }

    private void setActive(JPanel on, JPanel... offs) {
        on.setBackground(GREEN_DARK);
        for (Component c : on.getComponents())
            if (c instanceof JLabel l) { l.setFont(new Font("SansSerif", Font.BOLD, 13)); l.setForeground(WHITE); }
        for (JPanel off : offs) {
            off.setBackground(DARK);
            for (Component c : off.getComponents())
                if (c instanceof JLabel l) { l.setFont(new Font("SansSerif", Font.PLAIN, 13)); l.setForeground(new Color(160,190,170)); }
        }
    }

    private JPanel navItem(String text, boolean active) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(active ? GREEN_DARK : DARK);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        p.setBorder(new EmptyBorder(10, 16, 10, 16));
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 13));
        lbl.setForeground(active ? WHITE : new Color(160, 190, 170));
        p.add(lbl, BorderLayout.CENTER);
        if (!active) p.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { p.setBackground(new Color(40,60,48)); }
            @Override public void mouseExited(MouseEvent e)  {
                if (!p.getBackground().equals(GREEN_DARK)) p.setBackground(DARK);
            }
        });
        return p;
    }

    private JSeparator makeDivider() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(50, 65, 55));
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }

    // ── MAIN AREA ─────────────────────────────────────────────────────────────

    private JPanel buildMainArea() {
        contentCards.setBackground(BG);
        contentCards.add(buildDashboardPage(), "dashboard");
        contentCards.add(new SuppliersPanel(username), "suppliers");
        contentCards.add(new TransactionAndReportsPanel(username), "transactions");
        contentCards.add(new SettingsPanel(username, this), "settings");
        return contentCards;
    }

    private JPanel buildDashboardPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG);
        page.add(buildTopBar(), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(buildDashboard());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(BG);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(WHITE);
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER),
            new EmptyBorder(12, 20, 12, 20)
        ));
        JLabel title = new JLabel("Inventory Management System");
        title.setFont(new Font("Georgia", Font.BOLD, 15));
        title.setForeground(TEXT);
        JLabel user = new JLabel("👤  " + username + "  ▾");
        user.setFont(new Font("SansSerif", Font.PLAIN, 13));
        user.setForeground(TEXT);
        bar.add(title, BorderLayout.WEST);
        bar.add(user,  BorderLayout.EAST);
        return bar;
    }

    private JPanel buildDashboard() {
        JPanel dash = new JPanel();
        dash.setBackground(BG);
        dash.setLayout(new BoxLayout(dash, BoxLayout.Y_AXIS));
        dash.setBorder(new EmptyBorder(20, 20, 20, 20));
        dash.add(buildBanner());
        dash.add(Box.createVerticalStrut(16));
        dash.add(buildStatCards());
        dash.add(Box.createVerticalStrut(16));
        dash.add(buildBottom());
        dash.add(Box.createVerticalStrut(20));
        return dash;
    }

    // ── BANNER ────────────────────────────────────────────────────────────────

    private JPanel buildBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(new Color(15, 20, 18));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        banner.setPreferredSize(new Dimension(0, 120));
        banner.setBorder(new CompoundBorder(
            new LineBorder(GREEN_DARK, 1, true),
            new EmptyBorder(24, 28, 24, 28)
        ));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome Back, " + username + "!");
        welcome.setFont(new Font("Georgia", Font.BOLD, 24));
        welcome.setForeground(WHITE);

        JLabel sub = new JLabel("Manage inventory efficiently and accurately.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(new Color(160, 200, 180));

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMM yyyy"));
        JLabel dateLbl = new JLabel("📅  " + date);
        dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        dateLbl.setForeground(new Color(180, 210, 195));

        left.add(welcome);
        left.add(Box.createVerticalStrut(4));
        left.add(sub);
        left.add(Box.createVerticalStrut(8));
        left.add(dateLbl);

        JButton refresh = new JButton("⟳ Refresh");
        refresh.setBackground(GREEN);
        refresh.setForeground(WHITE);
        refresh.setFocusPainted(false);
        refresh.setBorder(new EmptyBorder(8, 16, 8, 16));
        refresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refresh.addActionListener(e -> loadData());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(refresh);

        banner.add(left,  BorderLayout.WEST);
        banner.add(right, BorderLayout.EAST);
        return banner;
    }

    // ── STAT CARDS ────────────────────────────────────────────────────────────

    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        row.add(statCard("Total Products",      "☰",  lblTotalProducts, "view all products →"));
        row.add(statCard("Low Stock Items",      "⚠",  lblLowStock,      "view low stocks →"));
        row.add(statCard("Total Suppliers",      "👥", lblSuppliers,     "view all suppliers →"));
        row.add(statCard("Monthly Transactions", "▦",  lblTransactions,  "view transactions →"));
        return row;
    }

    private JPanel statCard(String title, String icon, JLabel valueLbl, String link) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true), new EmptyBorder(14,14,12,14)));

        JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        iconLbl.setOpaque(true);
        iconLbl.setBackground(GREEN);
        iconLbl.setForeground(WHITE);
        iconLbl.setPreferredSize(new Dimension(40, 40));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(0, 12, 0, 0));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        titleLbl.setForeground(MUTED);

        valueLbl.setFont(new Font("Georgia", Font.BOLD, 22));
        valueLbl.setForeground(TEXT);

        JLabel linkLbl = new JLabel(link);
        linkLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        linkLbl.setForeground(GREEN_DARK);

        info.add(titleLbl); info.add(valueLbl);
        info.add(Box.createVerticalStrut(2)); info.add(linkLbl);
        card.add(iconLbl, BorderLayout.WEST);
        card.add(info,    BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(new LineBorder(GREEN_DARK,1,true), new EmptyBorder(14,14,12,14)));
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBorder(new CompoundBorder(new LineBorder(BORDER,1,true), new EmptyBorder(14,14,12,14)));
            }
        });
        return card;
    }

    // ── BOTTOM ────────────────────────────────────────────────────────────────

    private JPanel buildBottom() {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.add(buildTable(),  BorderLayout.CENTER);
        row.add(buildAlerts(), BorderLayout.EAST);
        return row;
    }

    // ── TABLE ────────────────────────────────────────────────────────────────

    private JPanel buildTable() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER,1,true), new EmptyBorder(16,16,16,16)));

        JPanel header = new JPanel(new BorderLayout(8, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel heading = new JLabel("Inventory Overview");
        heading.setFont(new Font("Georgia", Font.BOLD, 14));
        heading.setForeground(TEXT);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        buttons.setOpaque(false);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(160, 30));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchField.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true), new EmptyBorder(4, 8, 4, 8)));

        JButton searchBtn = makeBtn("🔍 Search");
        JButton addBtn    = makeBtn("+ Add");
        JButton updateBtn = makeBtn("✏ Update");
        JButton deleteBtn = makeBtn("🗑 Delete");

        buttons.add(searchField);
        buttons.add(searchBtn);
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);

        header.add(heading, BorderLayout.WEST);
        header.add(buttons, BorderLayout.EAST);

        String[] cols = {"ID","Item Name","Category","Quantity","Status","Price"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setBackground(WHITE);
        table.setSelectionBackground(new Color(235, 248, 240));
        table.setSelectionForeground(TEXT);
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

        // Status badge renderer
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = new JLabel(v != null ? v.toString() : "", SwingConstants.CENTER);
                l.setFont(new Font("SansSerif", Font.BOLD, 10));
                l.setOpaque(true);
                switch (v != null ? v.toString() : "") {
                    case "In Stock"  -> { l.setBackground(new Color(220,252,231)); l.setForeground(new Color(22,101,52)); }
                    case "Low Stock" -> { l.setBackground(new Color(254,243,199)); l.setForeground(new Color(146,64,14)); }
                    default          -> { l.setBackground(new Color(254,226,226)); l.setForeground(new Color(153,27,27)); }
                }
                l.setBorder(new EmptyBorder(2, 8, 2, 8));
                return l;
            }
        });

        // Right-click menu
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuUpdate = new JMenuItem("✏  Update Item");
        JMenuItem menuDelete = new JMenuItem("🗑  Delete Item");
        menuUpdate.addActionListener(e -> updateSelectedItem());
        menuDelete.addActionListener(e -> deleteSelectedItem());
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
        updateBtn.addActionListener(e -> updateSelectedItem());
        deleteBtn.addActionListener(e -> deleteSelectedItem());
        searchBtn.addActionListener(e -> {
            String kw = searchField.getText().trim();
            if (kw.isEmpty()) loadData(); else searchItems(kw);
        });
        searchField.addActionListener(e -> {
            String kw = searchField.getText().trim();
            if (kw.isEmpty()) loadData(); else searchItems(kw);
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if (searchField.getText().trim().isEmpty()) loadData();
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        sp.getViewport().setBackground(WHITE);

        JButton viewAll = makeBtn("View All Inventory");
        viewAll.addActionListener(e -> { searchField.setText(""); loadData(); });

        JPanel btnRow = new JPanel(new BorderLayout());
        btnRow.setOpaque(false);
        btnRow.setBorder(new EmptyBorder(12, 0, 0, 0));
        btnRow.add(viewAll, BorderLayout.CENTER);

        card.add(header, BorderLayout.NORTH);
        card.add(sp,     BorderLayout.CENTER);
        card.add(btnRow, BorderLayout.SOUTH);
        return card;
    }

    // ── ALERTS ───────────────────────────────────────────────────────────────

    private JPanel buildAlerts() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setPreferredSize(new Dimension(210, 0));
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER,1,true), new EmptyBorder(16,14,16,14)));

        JLabel heading = new JLabel("Low Stocks Alert");
        heading.setFont(new Font("Georgia", Font.BOLD, 14));
        heading.setForeground(TEXT);
        heading.setBorder(new EmptyBorder(0, 0, 12, 0));

        alertsPanel = new JPanel();
        alertsPanel.setBackground(WHITE);
        alertsPanel.setLayout(new BoxLayout(alertsPanel, BoxLayout.Y_AXIS));
        alertsPanel.add(new JLabel("Loading..."));

        card.add(heading,     BorderLayout.NORTH);
        card.add(alertsPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel alertRow(String name, String qty) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(new Color(255, 251, 235));
        row.setOpaque(true);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        row.setBorder(new CompoundBorder(
            new LineBorder(new Color(254,243,199), 1, true),
            new EmptyBorder(6, 8, 6, 8)
        ));
        JLabel icon = new JLabel("⚠");
        icon.setForeground(AMBER);
        JPanel texts = new JPanel();
        texts.setOpaque(false);
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        JLabel n = new JLabel(name);
        n.setFont(new Font("SansSerif", Font.BOLD, 11));
        n.setForeground(TEXT);
        JLabel q = new JLabel(qty);
        q.setFont(new Font("SansSerif", Font.PLAIN, 10));
        q.setForeground(new Color(146, 64, 14));
        texts.add(n); texts.add(q);
        row.add(icon,  BorderLayout.WEST);
        row.add(texts, BorderLayout.CENTER);
        return row;
    }

    // ── LOAD DATA ─────────────────────────────────────────────────────────────

    private void loadData() {
        new SwingWorker<Void, Void>() {
            int total, lowStock, suppliers, transactions;
            java.util.List<Object[]> items    = new java.util.ArrayList<>();
            java.util.List<Object[]> lowItems = new java.util.ArrayList<>();

            @Override protected Void doInBackground() {
                try (Connection con = DBConnection.getConnection()) {
                    if (con == null) return null;
                    total     = queryInt(con, "SELECT COUNT(*) FROM InventoryItem");
                    lowStock  = queryInt(con, "SELECT COUNT(*) FROM InventoryItem WHERE QuantityAvailable <= MinimumStockLevel");
                    suppliers = queryInt(con, "SELECT COUNT(*) FROM supplierdetails");
                    try { transactions = queryInt(con,
                        "SELECT COUNT(*) FROM Transaction WHERE MONTH(TransactionDate)=MONTH(CURDATE()) AND YEAR(TransactionDate)=YEAR(CURDATE())");
                    } catch (Exception ignored) {}

                    try (PreparedStatement ps = con.prepareStatement(
                            "SELECT ItemID,ItemName,Category,QuantityAvailable,MinimumStockLevel,Price FROM InventoryItem ORDER BY ItemID DESC");
                         ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int qty = rs.getInt("QuantityAvailable");
                            int min = rs.getInt("MinimumStockLevel");
                            String status = qty == 0 ? "Out of Stock" : qty <= min ? "Low Stock" : "In Stock";
                            items.add(new Object[]{ rs.getInt("ItemID"), rs.getString("ItemName"),
                                rs.getString("Category"), qty, status,
                                String.format("%.2f", rs.getDouble("Price")) });
                        }
                    }
                    try (PreparedStatement ps = con.prepareStatement(
                            "SELECT ItemName,QuantityAvailable FROM InventoryItem WHERE QuantityAvailable <= MinimumStockLevel ORDER BY QuantityAvailable LIMIT 5");
                         ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int qty = rs.getInt("QuantityAvailable");
                            lowItems.add(new Object[]{ rs.getString("ItemName"),
                                qty == 0 ? "Out of stock" : qty + " units" });
                        }
                    }
                } catch (Exception e) { e.printStackTrace(); }
                return null;
            }

            @Override protected void done() {
                lblTotalProducts.setText(String.valueOf(total));
                lblLowStock.setText(String.valueOf(lowStock));
                lblSuppliers.setText(String.valueOf(suppliers));
                lblTransactions.setText(String.valueOf(transactions));
                tableModel.setRowCount(0);
                items.forEach(tableModel::addRow);
                alertsPanel.removeAll();
                if (lowItems.isEmpty()) {
                    JLabel ok = new JLabel("✓ All items well stocked");
                    ok.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    ok.setForeground(GREEN_DARK);
                    alertsPanel.add(ok);
                } else {
                    for (Object[] item : lowItems) {
                        alertsPanel.add(alertRow(item[0].toString(), item[1].toString()));
                        alertsPanel.add(Box.createVerticalStrut(8));
                    }
                }
                alertsPanel.revalidate();
                alertsPanel.repaint();
            }
        }.execute();
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────

    private void searchItems(String keyword) {
        new SwingWorker<Void, Void>() {
            java.util.List<Object[]> results = new java.util.ArrayList<>();
            @Override protected Void doInBackground() {
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(
                        "SELECT ItemID,ItemName,Category,QuantityAvailable,MinimumStockLevel,Price " +
                        "FROM InventoryItem WHERE ItemName LIKE ? OR Category LIKE ?")) {
                    String p = "%" + keyword + "%";
                    ps.setString(1, p); ps.setString(2, p);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int qty = rs.getInt("QuantityAvailable");
                            int min = rs.getInt("MinimumStockLevel");
                            String status = qty == 0 ? "Out of Stock" : qty <= min ? "Low Stock" : "In Stock";
                            results.add(new Object[]{ rs.getInt("ItemID"), rs.getString("ItemName"),
                                rs.getString("Category"), qty, status,
                                String.format("%.2f", rs.getDouble("Price")) });
                        }
                    }
                } catch (Exception e) { e.printStackTrace(); }
                return null;
            }
            @Override protected void done() {
                tableModel.setRowCount(0);
                if (results.isEmpty())
                    JOptionPane.showMessageDialog(InventoryDashboard.this,
                        "No items found for: \"" + keyword + "\"", "Search", JOptionPane.INFORMATION_MESSAGE);
                else results.forEach(tableModel::addRow);
            }
        }.execute();
    }

    // ── ADD ITEM (with barcode lookup) ────────────────────────────────────────

    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Add New Item", true);
        dialog.setSize(420, 460);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(WHITE);

        // ── Barcode section at top ──────────────────────────────────────────
        JPanel barcodeSection = new JPanel(new BorderLayout(0, 4));
        barcodeSection.setBackground(new Color(245, 250, 247));
        barcodeSection.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER),
            new EmptyBorder(14, 24, 14, 24)
        ));

        JLabel barcodeHeading = new JLabel("📷  Scan Barcode to Auto-fill");
        barcodeHeading.setFont(new Font("SansSerif", Font.BOLD, 11));
        barcodeHeading.setForeground(GREEN_DARK);

        JPanel barcodeRow = new JPanel(new BorderLayout(8, 0));
        barcodeRow.setOpaque(false);

        JTextField fBarcode = new JTextField();
        fBarcode.setFont(new Font("SansSerif", Font.PLAIN, 13));
        fBarcode.setBorder(new CompoundBorder(
            new LineBorder(new Color(180, 210, 195), 1, true),
            new EmptyBorder(6, 8, 6, 8)
        ));
        fBarcode.setToolTipText("Scan or type a barcode and press Enter");

        JButton scanBtn = new JButton("Look up");
        scanBtn.setBackground(GREEN);
        scanBtn.setForeground(WHITE);
        scanBtn.setFocusPainted(false);
        scanBtn.setBorder(new EmptyBorder(6, 14, 6, 14));
        scanBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        scanBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        barcodeRow.add(fBarcode, BorderLayout.CENTER);
        barcodeRow.add(scanBtn,  BorderLayout.EAST);

        // Inline status for barcode lookup result
        JLabel barcodeLbl = new JLabel(" ");
        barcodeLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        barcodeLbl.setForeground(MUTED);

        barcodeSection.add(barcodeHeading, BorderLayout.NORTH);
        barcodeSection.add(barcodeRow,     BorderLayout.CENTER);
        barcodeSection.add(barcodeLbl,     BorderLayout.SOUTH);

        // ── Main form ──────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridLayout(6, 2, 8, 12));
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(16, 24, 10, 24));

        JTextField fName     = tf();
        JTextField fCategory = tf();
        JTextField fQty      = tf();
        JTextField fPrice    = tf();
        JTextField fMin      = tf();
        JTextField fSup      = tf();

        form.add(lbl("Item Name:"));       form.add(fName);
        form.add(lbl("Category:"));        form.add(fCategory);
        form.add(lbl("Quantity:"));        form.add(fQty);
        form.add(lbl("Price:"));           form.add(fPrice);
        form.add(lbl("Min Stock Level:")); form.add(fMin);
        form.add(lbl("Supplier ID:"));     form.add(fSup);

        // ── Barcode lookup logic ───────────────────────────────────────────
        Runnable doLookup = () -> {
            String barcode = fBarcode.getText().trim();
            if (barcode.isEmpty()) return;

            barcodeLbl.setForeground(MUTED);
            barcodeLbl.setText("⏳ Looking up barcode…");
            scanBtn.setEnabled(false);
            fBarcode.setEnabled(false);

            new SwingWorker<String[], Void>() {
                @Override protected String[] doInBackground() {
                    return fetchFromOpenFoodFacts(barcode);
                }

                @Override protected void done() {
                    scanBtn.setEnabled(true);
                    fBarcode.setEnabled(true);
                    try {
                        String[] result = get();
                        if (result == null) {
                            barcodeLbl.setForeground(Color.RED);
                            barcodeLbl.setText("✗ Product not found for barcode: " + barcode);
                        } else {
                            // result: [name, category, quantity_hint]
                            if (!result[0].isEmpty()) fName.setText(result[0]);
                            if (!result[1].isEmpty()) fCategory.setText(result[1]);
                            // quantity_hint is a numeric string when Open Food Facts
                            // returned a net weight/volume that parsed cleanly
                            if (!result[2].isEmpty() && fQty.getText().trim().isEmpty())
                                fQty.setText(result[2]);

                            barcodeLbl.setForeground(new Color(22, 101, 52));
                            barcodeLbl.setText("✓ Details filled from Open Food Facts");
                            fName.requestFocusInWindow();
                        }
                    } catch (Exception ex) {
                        barcodeLbl.setForeground(Color.RED);
                        barcodeLbl.setText("✗ Lookup failed: " + ex.getMessage());
                    }
                }
            }.execute();
        };

        scanBtn.addActionListener(e -> doLookup.run());
        fBarcode.addActionListener(e -> doLookup.run()); // fires on Enter / scanner

        // ── Save logic ────────────────────────────────────────────────────
        JLabel statusLbl = errorLbl();
        JButton save = saveBtn("Save Item");
        save.addActionListener(e -> {
            try {
                String name = fName.getText().trim(), cat = fCategory.getText().trim();
                if (name.isEmpty() || cat.isEmpty()) {
                    statusLbl.setText("Name and Category are required.");
                    return;
                }
                int qty = Integer.parseInt(fQty.getText().trim());
                double price = Double.parseDouble(fPrice.getText().trim());
                int min = Integer.parseInt(fMin.getText().trim());
                String sup = fSup.getText().trim();

                new SwingWorker<Boolean, Void>() {
                    @Override protected Boolean doInBackground() throws Exception {
                        try (Connection con = DBConnection.getConnection();
                             PreparedStatement ps = con.prepareStatement(
                                "INSERT INTO InventoryItem(ItemName,Category,QuantityAvailable,Price,MinimumStockLevel,SupplierID) VALUES(?,?,?,?,?,?)")) {
                            ps.setString(1, name); ps.setString(2, cat);
                            ps.setInt(3, qty); ps.setDouble(4, price); ps.setInt(5, min);
                            if (sup.isEmpty()) ps.setNull(6, Types.INTEGER);
                            else ps.setInt(6, Integer.parseInt(sup));
                            return ps.executeUpdate() > 0;
                        }
                    }
                    @Override protected void done() {
                        try {
                            if (get()) {
                                dialog.dispose();
                                loadData();
                                JOptionPane.showMessageDialog(InventoryDashboard.this,
                                    "Item added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception ex) { statusLbl.setText("Error: " + ex.getMessage()); }
                    }
                }.execute();
            } catch (NumberFormatException ex) {
                statusLbl.setText("Quantity, Price, Min Stock must be numbers.");
            }
        });

        // ── Layout ────────────────────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(WHITE);
        center.add(barcodeSection, BorderLayout.NORTH);
        center.add(form,           BorderLayout.CENTER);

        assembleDialog(dialog, center, statusLbl, save);
        dialog.setVisible(true);
    }

    // ── OPEN FOOD FACTS LOOKUP ────────────────────────────────────────────────
    //
    //  Returns String[3] { productName, category, quantityHint }
    //  or null when the barcode is not found / status != 1.
    //
    private String[] fetchFromOpenFoodFacts(String barcode) {
        try {
            String endpoint = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
            HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("User-Agent", "NASCA-InventorySystem/1.0");

            if (conn.getResponseCode() != 200) return null;

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            JSONObject root = new JSONObject(sb.toString());
            if (root.optInt("status", 0) != 1) return null; // not found

            JSONObject product = root.optJSONObject("product");
            if (product == null) return null;

            // Product name: prefer localized English, fall back to generic
            String name = product.optString("product_name_en", "");
            if (name.isEmpty()) name = product.optString("product_name", "");

            // Category: take the first English category tag, strip the "en:" prefix
            String category = "";
            if (product.has("categories_tags")) {
                var tags = product.getJSONArray("categories_tags");
                for (int i = 0; i < tags.length(); i++) {
                    String tag = tags.getString(i);
                    if (tag.startsWith("en:")) {
                        // Convert "en:breakfast-cereals" → "Breakfast Cereals"
                        category = toTitleCase(tag.substring(3).replace("-", " "));
                        break;
                    }
                }
            }
            if (category.isEmpty())
                category = product.optString("categories", "").split(",")[0].trim();

            // Quantity hint: only populate the qty field when the raw
            // "quantity" string is a plain integer (e.g. "500" not "500 ml")
            String quantityHint = "";
            String rawQty = product.optString("quantity", "").trim();
            if (rawQty.matches("\\d+")) quantityHint = rawQty;

            return new String[]{ name, category, quantityHint };

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** "breakfast cereals" → "Breakfast Cereals" */
    private static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) return input;
        String[] words = input.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty())
                sb.append(Character.toUpperCase(w.charAt(0)))
                  .append(w.substring(1).toLowerCase())
                  .append(" ");
        }
        return sb.toString().trim();
    }

    // ── UPDATE ITEM ───────────────────────────────────────────────────────────

    private void updateSelectedItem() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Select an item first.","No Selection",JOptionPane.WARNING_MESSAGE); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        String itemName = tableModel.getValueAt(row, 1).toString();

        JDialog dialog = new JDialog(this, "Update — " + itemName, true);
        dialog.setSize(380, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(WHITE);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 12));
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(24, 24, 10, 24));

        JTextField fCat   = tf(tableModel.getValueAt(row,2).toString());
        JTextField fQty   = tf(tableModel.getValueAt(row,3).toString());
        JTextField fPrice = tf(tableModel.getValueAt(row,5).toString());
        JTextField fMin   = tf();

        form.add(lbl("Category:"));        form.add(fCat);
        form.add(lbl("New Quantity:"));    form.add(fQty);
        form.add(lbl("New Price:"));       form.add(fPrice);
        form.add(lbl("Min Stock Level:")); form.add(fMin);

        JLabel statusLbl = errorLbl();
        JButton save = saveBtn("Update Item");
        save.addActionListener(e -> {
            try {
                String cat = fCat.getText().trim();
                int qty = Integer.parseInt(fQty.getText().trim());
                double price = Double.parseDouble(fPrice.getText().trim());
                String minTxt = fMin.getText().trim();
                new SwingWorker<Boolean, Void>() {
                    @Override protected Boolean doInBackground() throws Exception {
                        try (Connection con = DBConnection.getConnection()) {
                            String sql = minTxt.isEmpty()
                                ? "UPDATE InventoryItem SET Category=?,QuantityAvailable=?,Price=? WHERE ItemID=?"
                                : "UPDATE InventoryItem SET Category=?,QuantityAvailable=?,Price=?,MinimumStockLevel=? WHERE ItemID=?";
                            PreparedStatement ps = con.prepareStatement(sql);
                            ps.setString(1,cat); ps.setInt(2,qty); ps.setDouble(3,price);
                            if (!minTxt.isEmpty()) { ps.setInt(4,Integer.parseInt(minTxt)); ps.setInt(5,id); }
                            else ps.setInt(4,id);
                            return ps.executeUpdate() > 0;
                        }
                    }
                    @Override protected void done() {
                        try { if (get()) { dialog.dispose(); loadData();
                            JOptionPane.showMessageDialog(InventoryDashboard.this,"Item updated!","Success",JOptionPane.INFORMATION_MESSAGE);
                        }} catch (Exception ex) { statusLbl.setText("Error: "+ex.getMessage()); }
                    }
                }.execute();
            } catch (NumberFormatException ex) { statusLbl.setText("Quantity and Price must be numbers."); }
        });
        assembleDialog(dialog, form, statusLbl, save);
        dialog.setVisible(true);
    }

    // ── DELETE ITEM ───────────────────────────────────────────────────────────

    private void deleteSelectedItem() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Select an item first.","No Selection",JOptionPane.WARNING_MESSAGE); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = tableModel.getValueAt(row, 1).toString();
        if (JOptionPane.showConfirmDialog(this,"Delete \""+name+"\"?","Confirm",JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception {
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement("DELETE FROM InventoryItem WHERE ItemID=?")) {
                    ps.setInt(1, id); return ps.executeUpdate() > 0;
                }
            }
            @Override protected void done() {
                try { if (get()) loadData(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }.execute();
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────

    private void assembleDialog(JDialog d, JPanel form, JLabel status, JButton save) {
        JPanel bottom = new JPanel(new BorderLayout(0, 6));
        bottom.setBackground(WHITE);
        bottom.setBorder(new EmptyBorder(0, 24, 20, 24));
        bottom.add(status, BorderLayout.NORTH);
        bottom.add(save,   BorderLayout.CENTER);
        d.add(form,   BorderLayout.CENTER);
        d.add(bottom, BorderLayout.SOUTH);
    }

    private JTextField tf(String v) { JTextField f = tf(); f.setText(v); return f; }
    private JTextField tf() {
        JTextField f = new JTextField();
        f.setBorder(new CompoundBorder(new LineBorder(BORDER,1,true), new EmptyBorder(4,6,4,6)));
        return f;
    }
    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(TEXT);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }
    private JLabel errorLbl() {
        JLabel l = new JLabel("", SwingConstants.CENTER);
        l.setForeground(Color.RED);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }
    private JButton saveBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(GREEN); b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }
    private JButton makeBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(GREEN); b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(6, 12, 6, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return b;
    }
    private int queryInt(Connection con, String sql) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    private static JLabel makeStatValue(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Georgia", Font.BOLD, 22));
        l.setForeground(new Color(20, 30, 25));
        return l;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryDashboard("Admin").setVisible(true));
    }
}