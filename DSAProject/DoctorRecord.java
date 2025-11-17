import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DoctorRecord {
    String doctorID, createdAt, modifiedAt;
    String fullName, lastName, firstName;
    String dob, sex;
    int age;

    String phoneNumber;
    String address;
    boolean active;
    String specialization;

    LinkedList <String> workSchedules = new LinkedList<>();
    LinkedList<String> handledCases = new LinkedList<>();

    public DoctorRecord(String doctorID, String firstName,String lastName, String dob, int age, String sex,
            String address, String phoneNumber, String specialization,
            LinkedList<String> workSchedules, String createdAt) {
        this.lastName = lastName.trim();
        this.firstName = firstName.trim();
        this.fullName = firstName + " " + lastName;
        this.dob = dob;
        this.sex = sex;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
        this.workSchedules = workSchedules;
        this.address = address;
        this.doctorID = doctorID;
        this.active = true;
        this.createdAt = createdAt;
        this.modifiedAt = "No Modifications have been made yet.";
    }

    public void displayDoctorDetails() {
        System.out.println();
        System.out.printf("%-15s: %s\n", "ID", this.doctorID);
        System.out.printf("%-15s: %-15s | %-15s: %s\n", "Full Name", this.fullName, "Sex", this.sex);
        System.out.printf("%-15s: %-15s | %-15s: %s\n", "DOB", this.dob, "Age", this.age);
        System.out.printf("%-15s: %-15s | %-15s: %s\n", "Phone", this.phoneNumber, "Address", this.address);
        System.out.printf("%-15s: %s |\n", "Specialization", this.specialization);
    }

    public void displayDoctors(int position) {
        System.out.printf(
                "\u001B[34m%d.\u001B[0m %-5s: %-15s | %-5s: %-20s | %-5s: %-5d | %-5s: %-5s | %-16s: %s\n",
                position, "ID", this.doctorID,
                "Name", this.fullName,
                "Age", this.age,
                "Sex", this.sex, "Specialization", this.specialization);
    }

    public boolean changePhoneNum(String phoneNum) {
        if (phoneNumber.isEmpty()) {
            System.out.println("---->>! Phone Number cannot be empty. Try Again.");
        } else if (phoneNumber.length() != 11) {
            System.out.println("---->>! Invalid phone number. Must be 11 digits (e.g., 09171234567). ");
        } else {
            boolean notDigitFound = false;

            for (int i = 0; i < phoneNumber.length(); i++) {
                if (!Character.isDigit(phoneNumber.charAt(i))) {
                    notDigitFound = true;
                    break;
                }
            }

            if (notDigitFound) {
                System.out.println("---->>! Phone Number must only contain numbers (0-9).");
            } else {
                this.phoneNumber = phoneNum;
                return true;
            }
        }
        return false;
    }

    public String getPhoneNum() {
        return this.phoneNumber;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public void updateModified() {
        this.modifiedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNumber = phoneNum;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getActiveStatus() {
        return this.active;
    }
    public String getDoctorID() {
        return this.doctorID;
    }
    public void setActiveStatus(boolean newActiveStatus) {
        this.active = newActiveStatus;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public String getLastName() {
        return this.lastName;
    }

    public LinkedList<String> getWorkSchedules() {
        return this.workSchedules;
    }
}