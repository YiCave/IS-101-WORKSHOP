import java.sql.*;
import java.util.Scanner;

public class FSKTMMiniMarket {

    static final String DB_URL  = "---";
    static final String DB_USER = "---";
    static final String DB_PASS = "---";

    static final Scanner sc = new Scanner(System.in);

    static final int CART_CAPACITY = 20;
    static int[] cartProductIds = new int[CART_CAPACITY];
    static int[] cartQtys       = new int[CART_CAPACITY];
    static int cartCount = 0;

    public static void main(String[] args) {
        System.out.println("=== Welcome To FSKTM Mini Market ===");
        System.out.println("Buy Goods Here!");
        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            if ("1".equals(choice)) {
                loginFlow();
            } else if ("2".equals(choice)) {
                registerFlow();
            } else if ("3".equals(choice)) {
                System.out.println("Bye!");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    static void registerFlow() {
        try (Connection conn = getConnection()) {
            System.out.println("\n=== Register ===");
            System.out.print("First name: ");
            String first = sc.nextLine().trim();
            System.out.print("Last name: ");
            String last = sc.nextLine().trim();
            System.out.print("Email: ");
            String email = sc.nextLine().trim();
            System.out.print("Password: ");
            String pass = sc.nextLine().trim();

            System.out.println("Are you registering as staff?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print("Choose: ");
            String s = sc.nextLine().trim();
            boolean isStaff = "1".equals(s);

            int nextId = generateNextConsumerId(conn);

            String sql = "INSERT INTO consumers (consumer_id, first_name, last_name, email, password, created_at, is_management) "
                       + "VALUES (?, ?, ?, ?, ?, NOW(), ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, nextId);
                ps.setString(2, first);
                ps.setString(3, last);
                ps.setString(4, email);
                ps.setString(5, pass);
                ps.setBoolean(6, isStaff);
                ps.executeUpdate();
            }

            System.out.println("Registration successful. Your ID: " + nextId);
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    static int generateNextConsumerId(Connection conn) throws SQLException {
        String q = "SELECT COALESCE(MAX(consumer_id), 0) + 1 FROM consumers";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(q)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }

    static void loginFlow() {
        System.out.println("\nAre you a FSKTM mini market staff?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("Choose: ");
        String ch = sc.nextLine().trim();
        boolean expectStaff = "1".equals(ch);

        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Password: ");
        String pass = sc.nextLine().trim();

        try (Connection conn = getConnection()) {
            String sql = "SELECT consumer_id, first_name, is_management FROM consumers WHERE email = ? AND password = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, pass);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int consumerId = rs.getInt("consumer_id");
                        String first = rs.getString("first_name");
                        boolean isMgmt = rs.getBoolean("is_management");

                        if (expectStaff && !isMgmt) {
                            System.out.println("This account is not staff.");
                            return;
                        }

                        System.out.println("Welcome, " + first + "!");
                        if (isMgmt && expectStaff) {
                            staffMenu();
                        } else {
                            consumerShopFlow(consumerId);
                        }
                    } else {
                        System.out.println("Invalid email/password.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    static void staffMenu() {
        while (true) {
            System.out.println("\n=== Staff Menu ===");
            System.out.println("1. View Items");
            System.out.println("2. Restock Item");
            System.out.println("3. Check Sales (sum of transactions)");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();

            if ("1".equals(c)) {
                showItems();
            } else if ("2".equals(c)) {
                restockItem();
            } else if ("3".equals(c)) {
                checkSales();
            } else if ("4".equals(c)) {
                System.out.println("Logged out from staff.");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    static void showItems() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT product_id, item_name, item_price, quantity_left FROM items ORDER BY product_id")) {

            System.out.println("\n+-----------+----------------------+-----------+--------------+");
            System.out.println("| ProductID | Item Name            | Price(RM) | Qty In Stock |");
            System.out.println("+-----------+----------------------+-----------+--------------+");
            while (rs.next()) {
                int pid   = rs.getInt("product_id");
                String nm = rs.getString("item_name");
                double pr = rs.getDouble("item_price");
                int qty   = rs.getInt("quantity_left");
                System.out.printf("| %9d | %-20s | %9.2f | %12d |\n", pid, nm, pr, qty);
            }
            System.out.println("+-----------+----------------------+-----------+--------------+");
        } catch (Exception e) {
            System.out.println("Error showing items: " + e.getMessage());
        }
    }

    static void restockItem() {
        try (Connection conn = getConnection()) {
            System.out.print("Enter item name to restock (exact): ");
            String name = sc.nextLine().trim();
            System.out.print("Add quantity: ");
            int addQty = Integer.parseInt(sc.nextLine().trim());

            String sql = "UPDATE items SET quantity_left = quantity_left + ? WHERE item_name = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, addQty);
                ps.setString(2, name);
                int updated = ps.executeUpdate();
                if (updated > 0) {
                    System.out.println("Restocked successfully.");
                } else {
                    System.out.println("Item not found.");
                }
            }
        } catch (Exception e) {
            System.out.println("Restock error: " + e.getMessage());
        }
    }

    static void checkSales() {
        String sql = "SELECT COALESCE(SUM(t.quantity * i.item_price), 0) AS total_sales " +
                     "FROM transactions t JOIN items i ON t.product_id = i.product_id";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                double total = rs.getDouble("total_sales");
                System.out.printf("Total Sales: RM %.2f\n", total);
            }
        } catch (Exception e) {
            System.out.println("Sales check error: " + e.getMessage());
        }
    }

    static void consumerShopFlow(int consumerId) {
        cartCount = 0;
        autoRestockIfAllZero();

        while (true) {
            showItems();

            System.out.println("\n=== Shop Menu ===");
            System.out.println("1. Add to Cart");
            System.out.println("2. View Cart");
            System.out.println("3. Checkout");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();

            if ("1".equals(c)) {
                addToCartFlow();
            } else if ("2".equals(c)) {
                showCart();
            } else if ("3".equals(c)) {
                checkout(consumerId);
            } else if ("4".equals(c)) {
                System.out.println("Logged out.");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    static void addToCartFlow() {
        try (Connection conn = getConnection()) {
            System.out.print("Enter Product ID: ");
            int pid = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Quantity: ");
            int qty = Integer.parseInt(sc.nextLine().trim());

            int stock = getStock(conn, pid);
            if (stock < 0) {
                System.out.println("Product not found.");
                return;
            }
            if (stock == 0) {
                System.out.println("Sorry, stock is 0 for this item.");
                if (allItemsZero(conn)) {
                    System.out.println("All items are out of stock. Auto-restocking...");
                    autoRestock(conn);
                }
                return;
            }
            if (qty <= 0) {
                System.out.println("Quantity must be > 0.");
                return;
            }
            if (qty > stock) {
                System.out.println("Not enough stock. Available: " + stock);
                return;
            }

            int idx = findInCart(pid);
            if (idx >= 0) {
                cartQtys[idx] += qty;
            } else {
                if (cartCount >= CART_CAPACITY) {
                    System.out.println("Cart is full.");
                    return;
                }
                cartProductIds[cartCount] = pid;
                cartQtys[cartCount]       = qty;
                cartCount++;
            }
            System.out.println("Added to cart.");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number.");
        } catch (Exception e) {
            System.out.println("Add-to-cart error: " + e.getMessage());
        }
    }

    static int findInCart(int pid) {
        for (int i = 0; i < cartCount; i++) {
            if (cartProductIds[i] == pid) return i;
        }
        return -1;
    }

    static int getStock(Connection conn, int pid) throws SQLException {
        String q = "SELECT quantity_left FROM items WHERE product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, pid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    static boolean allItemsZero(Connection conn) throws SQLException {
        String q = "SELECT COUNT(*) FROM items WHERE quantity_left > 0";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(q)) {
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        }
        return false;
    }

    static void autoRestockIfAllZero() {
        try (Connection conn = getConnection()) {
            if (allItemsZero(conn)) {
                System.out.println("All items are out of stock. Auto-restocking...");
                autoRestock(conn);
            }
        } catch (Exception e) {
            System.out.println("Auto-restock check error: " + e.getMessage());
        }
    }

    static void autoRestock(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("UPDATE items SET quantity_left = quantity_left + 5");
        }
        System.out.println("Auto-restock done (+5 to all items).");
    }

    static void showCart() {
        if (cartCount == 0) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("\nYour Cart:");
        System.out.println("+-----------+----------+");
        System.out.println("| ProductID | Qty      |");
        System.out.println("+-----------+----------+");
        for (int i = 0; i < cartCount; i++) {
            System.out.printf("| %9d | %8d |\n", cartProductIds[i], cartQtys[i]);
        }
        System.out.println("+-----------+----------+");
    }

    static void checkout(int consumerId) {
        if (cartCount == 0) {
            System.out.println("Cart is empty.");
            return;
        }
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            for (int i = 0; i < cartCount; i++) {
                int pid = cartProductIds[i];
                int qty = cartQtys[i];

                int stock = getStock(conn, pid);
                if (stock < qty) {
                    conn.rollback();
                    System.out.println("Checkout failed. Not enough stock for product: " + pid + " (left " + stock + ")");
                    conn.setAutoCommit(true);
                    return;
                }

                String upd = "UPDATE items SET quantity_left = quantity_left - ? WHERE product_id = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(upd)) {
                    ps2.setInt(1, qty);
                    ps2.setInt(2, pid);
                    int done = ps2.executeUpdate();
                    if (done == 0) {
                        conn.rollback();
                        System.out.println("Checkout failed during stock update for product: " + pid);
                        conn.setAutoCommit(true);
                        return;
                    }
                }

                String ins = "INSERT INTO transactions (consumer_id, product_id, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(ins)) {
                    ps.setInt(1, consumerId);
                    ps.setInt(2, pid);
                    ps.setInt(3, qty);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("Checkout successful! Thank you.");
            
            for (int i = 0; i < cartCount; i++) {
                cartProductIds[i] = 0;
                cartQtys[i] = 0;
            }
            cartCount = 0;

            if (allItemsZero(conn)) {
                System.out.println("All items sold out after checkout. Auto-restocking...");
                autoRestock(conn);
            }
        } catch (Exception e) {
            System.out.println("Checkout error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
