package app.views;

import app.model.User;
import app.views.windows.MainWindow;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BaseForm {

    private User currentLoggedInUser;
    protected Stage stage;
    protected VBox layout;
    protected Button dashboardButton;
    protected Button userButton;
    protected Button ticketButton;
    protected VBox nav_bar = this.createNavBar();
    public Stage getStage() {
        return stage;
    }

    protected void setLoggedInUser(User user){
        currentLoggedInUser = user;
    }

    protected GridPane form = this.createGrid();

    public BaseForm() {
        // create a new Stage (window)
        stage = new Stage();

        // set up the global layout, menu on the left, sub scene on the right
         layout = new VBox();

        // --MENU-- //


        // add menu to layout
        layout.getChildren().addAll(nav_bar);
    }

    protected VBox createNavBar(){
        VBox container = new VBox();
        VBox header = new VBox();
        HBox nav_bar = new HBox();

        // labels for title and description
        Label title = new Label("NoDesk");
        Label description = new Label("Licensed to: The Garden Group");
        title.setFont(Font.font("Verdana", 30));
        description.setFont(Font.font("Verdana", 20));
        header.setAlignment(Pos.TOP_RIGHT);
        header.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
        header.getChildren().addAll(title, description);

        // buttons
        dashboardButton = new Button("Dashboard");
        userButton = new Button("User");
        ticketButton = new Button("Ticket");
        dashboardButton.setMinWidth(395);
        dashboardButton.setMinHeight(40);
        ticketButton.setMinWidth(395);
        ticketButton.setMinHeight(40);
        userButton.setMinWidth(395);
        userButton.setMinHeight(40);

        // add all children and set alignment to right
        nav_bar.setAlignment(Pos.CENTER);
        nav_bar.getChildren().addAll(dashboardButton, ticketButton, userButton);
        //container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(header, nav_bar);
        return container;
    }

    protected GridPane createGrid(){
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 80, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(150, 150, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(150,150, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        // Add Column Constraints
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }


    protected DatePicker generateDatePicker(String title, int placement){
        Label label = new Label(title);
        this.form.add(label, 0,placement);

        DatePicker date = new DatePicker();
        date.setPrefHeight(20);
        date.setPrefWidth(400);
        this.form.add(date, 1, placement);

        return date;
    }

    protected ComboBox<String> generateComboBox(String title, String[] comboBoxItems, int placement){
        Label label = new Label(title);
        this.form.add(label, 0, placement);

        ComboBox<String> comboBox = new ComboBox<String>();
        comboBox.setPrefWidth(400);
        comboBox.getItems().addAll(comboBoxItems);
        this.form.add(comboBox, 1, placement);

        return comboBox;
    }

    protected TextField generateTextField(String title, int placement){
        Label label = new Label(title);
        this.form.add(label, 0, placement);

        // Add description Field
        TextField field = new TextField();
        field.setPrefHeight(20);
        field.setPrefWidth(400);
        field.setMaxWidth(400);
        this.form.add(field, 1, placement);

        return field;
    }

    protected PasswordField generatePasswordField(String title, int placement){
        Label label = new Label(title);
        this.form.add(label, 0, placement);

        // Add description Field
        PasswordField field = new PasswordField();
        field.setPrefHeight(20);
        field.setPrefWidth(400);
        field.setMaxWidth(400);
        this.form.add(field, 1, placement);

        return field;
    }

    protected Button generateFormBtn(String btnTitle, int placement){
        Button btn = new Button(btnTitle);
        btn.setPrefHeight(40);
        btn.setDefaultButton(true);
        btn.setPrefWidth(100);
        this.form.add(btn, placement, 8, 2, 1);
        GridPane.setMargin(btn, new Insets(20, 0,20,0));

        return btn;
    }

    // --Open main window and close this one
    protected void openMainAndClose(ActionEvent actionEvent, String option){
        MainWindow mainWindow = new MainWindow();
        mainWindow.setTableView(option);
        mainWindow.getStage().show();

        // close this window
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }
}