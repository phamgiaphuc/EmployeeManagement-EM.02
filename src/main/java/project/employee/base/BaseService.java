package project.employee.base;

import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BaseService {
    /**
     * Checking Stages
     */
    // Checking if there is any empty Text Field in the stage - filling the empty Text Field with "null"
    public String checkInfo(String info) {
        if (info.equals("")) {
            info = "null";
        }
        return info;
    }

    // Checking if ID or AGE is an integer
    public int checkInteger(String check) {
        try {
            Integer.parseInt(check);
            return 0;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Checking if the AGE is valid
    public int checkAgeRange(String check) {
        int age = Integer.parseInt(check);
        if (age >= 0 && age <= 100) {
            return 0;
        } else {
            return -1;
        }
    }

    // Checking if the ID is already existed in the data
    public int checkId(String id) {
        int check = 0;
        File file = new File(ConstVariables.EMPLOYEE_DATA_PATH);
        if (file.length() == 0) {
            return check;
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    String trimmedLine = currentLine.trim();
                    String[] array = trimmedLine.split("@");
                    if (array[0].equals(id)) {
                        check = -1;
                        System.out.println("This ID " + "'" + id + "'" + " is already had. Please try another id." + "\n");
                        break;
                    }
                }
                reader.close();
                return check;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return check;
    }

    // Suggesting ID
    public int suggestId() throws IOException {
        int num = 0;
        File file = new File(ConstVariables.EMPLOYEE_DATA_PATH);
        String currentLine;
        if (file.length() == 0) {
            num = 1;
        } else {
            Set<Integer> set = new HashSet<>();
            BufferedReader reader = new BufferedReader(new FileReader(ConstVariables.EMPLOYEE_DATA_PATH));
            while ((currentLine = reader.readLine()) != null) {
                String[] array = currentLine.split("@");
                set.add(Integer.parseInt(array[0]));
            }
            reader.close();
            // Return the biggest number in the set
            int length = Collections.max(set);
            if (length == set.size()) {
                num = length + 1;
            } else {
                for (int i = 1; i <= length; i++) {
                    if (!set.contains(i)) {
                        num = i;
                        return num;
                    }
                }
            }
        }
        return num;
    }

    // Edit employee information
    public void editAndAdd(String data, String id) throws IOException {
        String[] new_data = data.split("@");
        File file = new File(ConstVariables.EMPLOYEE_DATA_PATH);
        File temp = new File("temp.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
        String curentLine;
        while ((curentLine = reader.readLine()) != null) {
            String[] array = curentLine.split("@");
            if (array[0].equals(id)) {
                writer.write(new_data[0] + "@" + new_data[1] + "@" + new_data[2] + "@" + new_data[3] + "@" + new_data[4] + "@" + new_data[5] + "\n");
            } else {
                writer.write(curentLine + "\n");
            }
        }
        writer.close();
        reader.close();
        if (temp.renameTo(file)) {
            System.out.println("Change successful!");
        }
    }

    // Delete employee information
    public void delete(String id) throws IOException {
        File inputF = new File(ConstVariables.EMPLOYEE_DATA_PATH);
        File tempF = new File("temp.txt");
        BufferedReader reader = new BufferedReader(new FileReader(inputF));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempF));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            String[] array = trimmedLine.split("@");
            if (array[0].equals(id)) {
                continue;
            }
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        if (tempF.renameTo(inputF)) {
            System.out.println("Delete successful!" + "\n");
        }
    }

}
