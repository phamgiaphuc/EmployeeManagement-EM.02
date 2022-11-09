package project.employee.worker;

import project.employee.Worker;
import project.employee.base.BaseService;
import utilities.ConstVariables;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WorkerService extends BaseService {
    public void addWorkerToFile(Worker worker) {
        FileWriter myList;
        try {
            if (checkFileExist()) {
                myList = new FileWriter(ConstVariables.EMPLOYEE_DATA_PATH, true);
            } else {
                myList = new FileWriter(ConstVariables.EMPLOYEE_DATA_PATH);
            }
            myList.write(worker.toString() + "\n");
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
