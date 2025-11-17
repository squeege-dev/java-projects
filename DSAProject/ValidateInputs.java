import java.time.LocalDate;
import java.util.*;

public class ValidateInputs {

    public static boolean checkPatientId(String id, LinkedList<PatientRecord> patientRecords) {
        for (PatientRecord pr : patientRecords) {
            if (pr.getPatientID().equals(id)) {
                return false;
            }
        }
        return true;
    }

    public static String checkInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = Main.in.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("---->>! Input cannot be empty. Please try again.");
                continue;
            }
            return input;
        }
    }

    public static boolean phoneNumValidation(String phoneNum) {
        if (phoneNum.length() != 11) {
            System.out.println("---->>! Invalid phone number. Must be 11 digits (e.g., 09171234567). ");
        } else {
            boolean notDigitFound = false;

            for (int i = 0; i < phoneNum.length(); i++) {
                if (!Character.isDigit(phoneNum.charAt(i))) {
                    notDigitFound = true;
                    break;
                }
            }
            if (notDigitFound) {
                System.out.println("---->>! Phone Number must only contain numbers (0-9).");
            } else {
                return true;
            }
        }
        return false;
    }

    public static int checkIntInput(String prompt) {
        int number;
        while (true) {
            try {
                System.out.print(prompt);
                number = Main.in.nextInt();
                Main.in.nextLine();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
        return number;
    }

    public static boolean checkDoctorId(String id, LinkedList<DoctorRecord> doctorRecords) {
        for (DoctorRecord dr : doctorRecords) {
            if (dr.doctorID.equals(id)) {
                return false;
            }
        }
        return true;
    }


}
