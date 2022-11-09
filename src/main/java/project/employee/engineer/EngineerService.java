package project.employee.engineer;

import project.employee.Engineer;
import project.employee.base.BaseService;
import utilities.ConstVariables;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EngineerService extends BaseService {
    public void addEngineerToFile(Engineer engineer) {
        FileWriter myList;
        try {
            if (checkFileExist()) {
                myList = new FileWriter(ConstVariables.EMPLOYEE_DATA_PATH, true);
            } else {
                myList = new FileWriter(ConstVariables.EMPLOYEE_DATA_PATH);
            }
            myList.write(engineer.toString() + "\n");
            myList.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean checkFileExist() {
        File file = new File(ConstVariables.EMPLOYEE_DATA_PATH);
        return file.exists();
    }
}
