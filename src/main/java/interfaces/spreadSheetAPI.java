package interfaces;

import java.util.ArrayList;

public interface spreadSheetAPI {
    void getRowValueFromSpreadSheet(String sheetId, String range);
    void getExactlyValueFromSpreadSheet(String sheetId, String range);
    void writeValueToSpreadSheet(ArrayList arrayList, String sheetId, String range);
    void updateValueToSpreadSheet(String spreadsheetId, String title, String find, String replace);
}
