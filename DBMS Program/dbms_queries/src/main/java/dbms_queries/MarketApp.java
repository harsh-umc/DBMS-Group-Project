package dbms_queries;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

/** Plain, square-cornered JavaFX desktop UI. */
public class MarketApp extends Application {
    private final TableView<Queries.Row> table = new TableView<>();
    private final Label status = new Label("Ready");
    private final TextField search = new TextField();
    private final VBox body = new VBox(10);
    private int selectedMarket = -1;
    private int selectedProduct = -1, selectedVendor = -1;
    private String currentTab = "Markets";

    @Override public void start(Stage stage) {
        Label title = new Label("Farmers Market Manager");
        title.setStyle("-fx-font-size: 19px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox header = new HBox(title);
        header.setPadding(new Insets(16));
        header.setStyle("-fx-background-color: #36563d;");
        HBox nav = new HBox(6);
        for (String tab : new String[]{"Markets", "Vendors", "Products", "Pre-Orders"}) {
            Button b = new Button(tab);
            b.setOnAction(e -> showTab(tab));
            nav.getChildren().add(b);
        }
        search.setPromptText("Search markets or products");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> showTab(currentTab));
        HBox toolbar = new HBox(8, search, searchButton);
        HBox.setHgrow(search, Priority.ALWAYS);
        body.setPadding(new Insets(12));
        VBox.setVgrow(body, Priority.ALWAYS);
        VBox root = new VBox(10, header, nav, toolbar, body, status);
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: #e8e8e8; -fx-font-family: Arial; -fx-font-size: 13px;");
        Scene scene = new Scene(root, 950, 620);
        scene.getStylesheets().add(getClass().getResource("/dbms_queries/app.css").toExternalForm());
        stage.setScene(scene); stage.setTitle("Farmers Market Manager"); stage.show();
        showTab("Markets");
    }
    private void showTab(String tab) {
        currentTab = tab;
        body.getChildren().clear();
        try {
            switch (tab) {
                case "Markets" -> showMarkets();
                case "Vendors" -> showVendors();
                case "Products" -> showProducts();
                case "Pre-Orders" -> showOrderForm();
            }
        } catch (SQLException ex) {
            status.setText("Database error: " + ex.getMessage());
            body.getChildren().add(new Label("Could not load data. Check MySQL and connection settings."));
        }
    }
    private void columns(String... names) {
        table.getColumns().clear();
        for (int i = 0; i < names.length; i++) {
            final int index = i;
            TableColumn<Queries.Row, Object> col = new TableColumn<>(names[i]);
            col.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().cells()[index]));
            col.setPrefWidth(i == 1 ? 200 : 135);
            table.getColumns().add(col);
        }
    }
    private void display(List<Queries.Row> rows) {
        table.getItems().setAll(rows);
        VBox.setVgrow(table, Priority.ALWAYS);
        body.getChildren().add(table);
        status.setText(rows.size() + " row(s) loaded");
    }
    private void showMarkets() throws SQLException {
        body.getChildren().add(new Label("Farmers Markets — select a market to view its vendors"));
        columns("Market ID", "Market Name", "Address");
        display(Queries.showAllMarkets(search.getText()));
        Button view = new Button("View Vendors");
        view.setOnAction(e -> {
            Queries.Row r = table.getSelectionModel().getSelectedItem();
            if (r == null) { status.setText("Select a market first."); return; }
            selectedMarket = ((Number)r.cells()[0]).intValue();
            showTab("Vendors");
        });
        body.getChildren().add(view);
    }
    private void showVendors() throws SQLException {
        if (selectedMarket < 0) {
            body.getChildren().add(new Label("Select a market from the Markets tab first."));
            return;
        }
        body.getChildren().add(new Label("Vendors at market #" + selectedMarket));
        columns("Vendor ID", "Vendor Name", "Website", "Stall");
        display(Queries.showVendorsAtMarket(selectedMarket));
    }
    private void showProducts() throws SQLException {
        body.getChildren().add(new Label("Products — select an item to place a pre-order"));
        columns("Product ID", "Product", "Category", "Vendor ID", "Vendor", "Price", "Stock");
        display(Queries.showAllAvailableProducts(search.getText()));
        Button order = new Button("Pre-Order Selected Product");
        order.setOnAction(e -> {
            Queries.Row r = table.getSelectionModel().getSelectedItem();
            if (r == null) { status.setText("Select a product first."); return; }
            selectedProduct = ((Number)r.cells()[0]).intValue();
            selectedVendor = ((Number)r.cells()[3]).intValue();
            showTab("Pre-Orders");
        });
        body.getChildren().add(order);
    }
    private void showOrderForm() {
        body.getChildren().add(new Label("Place a Pre-Order"));
        if (selectedProduct < 0) {
            body.getChildren().add(new Label("Select a product from the Products tab first."));
            return;
        }
        body.getChildren().add(new Label("Product #" + selectedProduct + " — Vendor #" + selectedVendor));
        TextField customerID = new TextField(), address = new TextField(), qty = new TextField("1");
        customerID.setPromptText("Existing Customer ID");
        address.setPromptText("Delivery address");
        qty.setPromptText("Quantity");
        for (TextField f : new TextField[]{customerID, address, qty}) f.setMaxWidth(360);
        Button submit = new Button("Place Order");
        submit.setOnAction(e -> {
            try {
                int id = Queries.order(selectedProduct, selectedVendor,
                    Integer.parseInt(customerID.getText().trim()),
                    address.getText().trim(), Integer.parseInt(qty.getText().trim()));
                new Alert(Alert.AlertType.INFORMATION, "Order #" + id + " placed successfully.").showAndWait();
                status.setText("Order #" + id + " committed to database");
            } catch (SQLException | IllegalArgumentException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });
        body.getChildren().addAll(new Label("Use an existing Customer ID from the database."),
            customerID, address, qty, submit);
    }
    public static void main(String[] args) { launch(args); }
}
