package application.employeemanagement;

import javafx.fxml.Initializable;
import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    private void update(HashMap<String, Integer> employees_status) {
        String employee_data_path = ConstVariables.EMPLOYEE_DATA_PATH;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(employee_data_path));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] array = currentLine.split(",");
                String area = array[1].substring(0, 2);
                employees_status.put(area, employees_status.getOrDefault(area, 0) + 1);
            }
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HashMap<String, Integer> employees_status = new HashMap<>();
        update(employees_status);
        System.out.println(employees_status);
    }
}
