package app.views.partial;

import app.model.BaseModel;
import app.model.Ticket;
import app.views.BaseListView;
import app.views.windows.Form_Ticket;
import app.views.windows.MainWindow;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TicketListView extends BaseListView {

    private MainWindow mainWindow;

    public TicketListView(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.generateTable();
        this.fillTableWithData(Filters.regex("incident", ".*", "i"));

        // header title and search box
        Label heading = this.addHeaders("Tickets");
        TextField filterTable = new TextField();
        filterTable.setMaxWidth(200);
        filterTable.setPromptText("Enter something...");
        filterTable.textProperty().addListener((observable, oldValue, newValue) -> {
            Bson filter = Filters.regex("incident", ".*" + newValue + ".*", "i");
            table.getItems().clear();
            fillTableWithData(filter);
        }); // add listener to text field property, when changed, adjust tableview data on filter

        //setCellValueFactory in BaseListView (tableview fills table with property's of ticket)
        String[] columnNames = {"reported", "incident", "type", "user_id", "priority", "deadline", "description"};
        this.generateData(columnNames);

        HBox menu = this.createCrudButtons("add Ticket", "edit Ticket", "Delete Ticket");

        getChildren().addAll(heading, filterTable, table, menu); // add all
    }

    protected void fillTableWithData(Bson filter) {
        ObservableList<Ticket> tableList = FXCollections.observableArrayList();
        //SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Document doc : db.findMany(filter, "Tickets")) {
            try {
                tableList.add(new Ticket(
                        dateFormat.parse(doc.get("Reported").toString()),
                        doc.get("incident").toString(),
                        doc.get("type").toString(),
                        doc.get("user_id").toString(),
                        doc.get("priority").toString(),
                        dateFormat.parse(doc.get("deadline").toString()),
                        doc.get("description").toString()
                ));
            } catch (ParseException e) {
                System.out.println(e.toString());
            }
        }
        for (BaseModel item : tableList) {
            table.getItems().add(item);
        }
    }

    // open empty form to add a ticket
    protected void handleCreateBtnClick() {
        new Form_Ticket(null).getStage().show();
        this.mainWindow.getStage().close();
    }

    // when there is an item selected, create form with all ticket properties filled
    protected void handleEditBtnClick() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            new Form_Ticket((Ticket) table.getSelectionModel().getSelectedItem()).getStage().show();
            this.mainWindow.getStage().close();
        }
    }

    protected void handleDeleteBtnClick() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            // alert user about his action
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete item");
            alert.setHeaderText("Item is about to be deleted");
            alert.setContentText("Are you sure you want to delete this item?");

            // delete when button ok is pressed
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    Ticket t = (Ticket) table.getSelectionModel().getSelectedItem();
                    Bson filter = Filters.eq("incident", t.getIncident());
                    db.deleteOne(filter, "Tickets");
                }
            });
        }
    }
}
