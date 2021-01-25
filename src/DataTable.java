import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class DataTable extends JTable {
    private String file;

    private DefaultTableModel model;
    private final DataLoader perda = new DataLoader();

    public JTextArea strResult = null;

    public DataTable() {
        super(new DefaultTableModel());
        model = (DefaultTableModel)this.getModel();

        model.addColumn("Surname");
        model.addColumn("Name");
        model.addColumn("Patronymic");
        model.addColumn("Birthday");
        int colCount = model.getColumnCount();
    }

    public int loadPersonalData(String sFile) {
        int RC;

        file = sFile;
        perda.strResult = this.strResult;

        WriteMsg("loadData:");

        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        RC = perda.open(file);
        if (RC < 0) {
            WriteMsg("loadData: perda.open error: " + RC);
            return RC;
        }
        WriteMsg("loadData: perda.open OK");

        Map<String, PersonalItem> mapPersons = perda.getPersonMap();
        for (Map.Entry<String, PersonalItem> entry : perda.getPersonMap().entrySet()) {
            model.addRow(new Object[] {
                    ((PersonalItem)entry.getValue()).getSurname(),
                    ((PersonalItem)entry.getValue()).getName(),
                    ((PersonalItem)entry.getValue()).getPatronymic(),
                    ((PersonalItem)entry.getValue()).getBirthday()
            });
        }
        return 0;
    }


    private void WriteMsg(String sMes){
        if (this.strResult == null)
            return;
        strResult.setText(this.strResult.getText() + "\n" + sMes);
    }
}
