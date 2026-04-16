import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class Controller {

    private TableView<Expense> table;
    private ObservableList<Expense> expenseList;

    private TextField amountField;
    private ComboBox<String> categoryBox;
    private DatePicker datePicker;
    private Label totalLabel;

    private TextField limitField;
    private double expenseLimit = 0;

    private ProgressBar progressBar;
    private Label warningLabel;
    private Label insightLabel;

    private PieChart pieChart;
    private ObservableList<PieChart.Data> pieData;

    private VBox root;

    public Controller() {
        expenseList = FXCollections.observableArrayList();
        pieData = FXCollections.observableArrayList();

        // ===== TITLE =====
        Label title = new Label("Expense Tracker");
        title.setFont(new Font("Arial", 30));
        title.setStyle("-fx-text-fill: #0d47a1; -fx-font-weight: bold;");

        // ===== INPUTS =====
        amountField = new TextField();
        amountField.setPromptText("Amount");

        categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Food", "Travel", "Shopping", "Bills");
        categoryBox.getSelectionModel().selectFirst();

        datePicker = new DatePicker();

        // ===== LIMIT =====
        limitField = new TextField();
        limitField.setPromptText("Set Limit");

        Button setLimitBtn = new Button("Set Limit");

        setLimitBtn.setOnAction(e -> {
            try {
                expenseLimit = Double.parseDouble(limitField.getText());
                showAlert("Limit set to Rs " + expenseLimit);
            } catch (Exception ex) {
                showAlert("Enter valid limit!");
            }
        });

        Button addBtn = new Button("Add");
        Button deleteBtn = new Button("Delete");

        totalLabel = new Label("Total: Rs 0");

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);

        warningLabel = new Label("");
        warningLabel.setStyle("-fx-text-fill: red;");

        insightLabel = new Label("Insights will appear here");

        // ===== TABLE =====
        table = new TableView<>();

        TableColumn<Expense, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Expense, Double> amtCol = new TableColumn<>("Amount");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Expense, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(catCol, amtCol, dateCol);
        table.setItems(expenseList);
        table.setPrefWidth(400);
        table.setPrefHeight(250);

        // ===== PIE CHART =====
        pieChart = new PieChart(pieData);
        pieChart.setPrefSize(350, 250);

        // ===== ACTIONS =====
        addBtn.setOnAction(e -> addExpense());
        deleteBtn.setOnAction(e -> deleteExpense());

        // ===== TOP FORM =====
        HBox form = new HBox(10, amountField, categoryBox, datePicker, addBtn);
        form.setAlignment(Pos.CENTER);

        HBox limitBox = new HBox(10, limitField, setLimitBtn);
        limitBox.setAlignment(Pos.CENTER);

        VBox topSection = new VBox(10, form, limitBox, deleteBtn);
        topSection.setAlignment(Pos.CENTER);

        // ===== MAIN CONTENT (SIDE BY SIDE) =====
        HBox mainContent = new HBox(30, table, pieChart);
        mainContent.setAlignment(Pos.CENTER);

        // ===== ROOT =====
        root = new VBox(20,
                title,
                topSection,
                mainContent,
                totalLabel,
                progressBar,
                warningLabel,
                insightLabel
        );

        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // LIGHT BACKGROUND
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");
    }

    public VBox getView() {
        return root;
    }

    private void addExpense() {
        try {
            if (amountField.getText().isEmpty() || datePicker.getValue() == null) {
                showAlert("Fill all fields!");
                return;
            }

            double amount = Double.parseDouble(amountField.getText());
            String category = categoryBox.getValue();
            String date = datePicker.getValue().toString();

            Expense exp = new Expense(category, amount, date);
            expenseList.add(exp);

            updateTotal();
            updateChart();
            updateInsights();

            amountField.clear();

        } catch (Exception e) {
            showAlert("Invalid input!");
        }
    }

    private void updateTotal() {
        double total = 0;
        for (Expense e : expenseList) {
            total += e.getAmount();
        }

        totalLabel.setText("Total: Rs " + total);

        updateProgress(total);
        checkLimit(total);
    }

    private void updateProgress(double total) {
        if (expenseLimit > 0) {
            double progress = total / expenseLimit;
            progressBar.setProgress(progress);

            if (progress > 1) {
                warningLabel.setText("Limit exceeded!");
            } else if (progress > 0.8) {
                warningLabel.setText("Near limit!");
            } else {
                warningLabel.setText("");
            }
        }
    }

    private void checkLimit(double total) {
        if (expenseLimit > 0 && total > expenseLimit) {
            showAlert("Limit exceeded! You spent Rs " + total);
        }
    }

    private void updateInsights() {
        if (expenseList.isEmpty()) return;

        double food = 0, travel = 0, shopping = 0, bills = 0;

        for (Expense e : expenseList) {
            switch (e.getCategory()) {
                case "Food": food += e.getAmount(); break;
                case "Travel": travel += e.getAmount(); break;
                case "Shopping": shopping += e.getAmount(); break;
                case "Bills": bills += e.getAmount(); break;
            }
        }

        double max = Math.max(Math.max(food, travel), Math.max(shopping, bills));
        String top = (max == food) ? "Food" :
                     (max == travel) ? "Travel" :
                     (max == shopping) ? "Shopping" : "Bills";

        insightLabel.setText("Top Category: " + top + " | Transactions: " + expenseList.size());
    }

    private void deleteExpense() {
        Expense selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            expenseList.remove(selected);
            updateTotal();
            updateChart();
            updateInsights();
        }
    }

    private void updateChart() {
        pieData.clear();

        double food = 0, travel = 0, shopping = 0, bills = 0;

        for (Expense e : expenseList) {
            switch (e.getCategory()) {
                case "Food": food += e.getAmount(); break;
                case "Travel": travel += e.getAmount(); break;
                case "Shopping": shopping += e.getAmount(); break;
                case "Bills": bills += e.getAmount(); break;
            }
        }

        if (food > 0) pieData.add(new PieChart.Data("Food", food));
        if (travel > 0) pieData.add(new PieChart.Data("Travel", travel));
        if (shopping > 0) pieData.add(new PieChart.Data("Shopping", shopping));
        if (bills > 0) pieData.add(new PieChart.Data("Bills", bills));
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}