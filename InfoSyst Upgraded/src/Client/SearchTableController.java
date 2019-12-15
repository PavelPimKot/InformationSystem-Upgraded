package Client;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Pair;

public class SearchTableController {

    private ObservableList<Pair<Integer, String>> searchInfo;


    @FXML
    private TableView<Pair<Integer, String>> SearchTable;

    @FXML
    private TableColumn<Pair<Integer, String>, String> ResultColunmn;

    @FXML
    private TableColumn<Pair<Integer, String>, String> PositionColumn;

    public void setSearchInfo(ObservableList<Pair<Integer, String>> searchInfo) {
        this.searchInfo = searchInfo;
        SearchTable.setItems(searchInfo);
    }

    @FXML
    void EditCommit(TableColumn.CellEditEvent event) {
        int index = event.getTableView().getSelectionModel().getFocusedIndex();
        String input = event.getNewValue().toString();
        if (input.length() == 0) {
            searchInfo.remove(index);
        } else {
            Pair<Integer, String> prev = searchInfo.get(index);
            searchInfo.set(index, new Pair<Integer, String>(prev.getKey(), input));
        }
    }


    @FXML
    void initialize() {
        PositionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().toString()));
        ResultColunmn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue()));
        ResultColunmn.setCellFactory(TextFieldTableCell.forTableColumn());
    }
}
