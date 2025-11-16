import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PatientRecord {
    private String patientID;
    String dob, sex;
    String bloodType;
    int age;
    private String modifiedAt, createdAt;
    String fName, lName;
    String fullName;
    private boolean active;
    String phoneNumber;
    String address;
    
    LinkedList<String> treatmentRecord = new LinkedList<>();
    private int priority;

    LinkedList<String> allergies = new LinkedList<>();
    LinkedList<String> medications = new LinkedList<>();
    LinkedList<String> medicalHistory = new LinkedList<>();

    public PatientRecord(String patientID, String fullName, String dob, int age, String sex,
            String bloodType, String address, String phoneNumber, LinkedList<String> allergies,
            LinkedList<String> medications, LinkedList<String> medicalHistory, String createdAt) {
        this.lName = fullName.split(" ")[fullName.split(" ").length - 1];
        this.fName = fullName.replaceFirst(this.lName, "").trim();
        this.fullName = fullName;
        this.dob = dob;
        this.sex = sex;
        this.age = age;
        this.bloodType = bloodType;
        this.phoneNumber = phoneNumber;
        this.allergies = allergies;
        this.medications = medications;
        this.address = address;
        this.patientID = patientID;
        this.priority = 0;
        this.active = true;
        this.medicalHistory = medicalHistory;
        this.createdAt = createdAt;
        this.modifiedAt = "No Modifications have been made yet.";
    }

    public void displayPatientDetails() {

        System.out.printf("%-15s: %s\n", "ID", this.patientID);
        System.out.printf("%-15s: %-15s | %-15s: %s\n", "Name", this.fullName, "Sex", this.sex);
        System.out.printf("%-15s: %-15s | \n", "Blood Type", this.bloodType);
        System.out.printf("%-15s: %-15s | %-15s: %d\n", "DOB", this.dob, "Age", this.age);
        System.out.printf("%-15s: %-15s | %-15s: %s\n", "Phone", this.phoneNumber, "Address", this.address);

        System.out.print("Allergies: ");
        if (this.allergies.isEmpty() || (this.allergies.size() == 1 && this.allergies.get(0).equalsIgnoreCase("N/A"))) {
            System.out.println("No Allergies Recorded.");
        } else {
            for (int i = 0; i < this.allergies.size(); i++) {
                System.out.print(this.allergies.get(i));
                if (i != this.allergies.size() - 1) {
                    System.out.print(", ");
                } else {
                    System.out.println(".");
                }
            }

        }

        System.out.print("Medications: ");
        if (this.medications.isEmpty()
                || (this.medications.size() == 1 && this.medications.get(0).equalsIgnoreCase("N/A"))) {
            System.out.println("No Medications Recorded.");
        } else {
            for (int i = 0; i < this.medications.size(); i++) {
                System.out.print(this.medications.get(i));
                if (i != this.medications.size() - 1) {
                    System.out.print(", ");
                } else {
                    System.out.println(".");
                }
            }
        }
    }

    public void displayPatient(int position) {
        System.out.printf(
                "\u001B[34m%d.\u001B[0m %-5s: %-15s | %-10s: %-20s | %-5s: %-3d | %-10s: %-3s\n",
                position, "ID", this.patientID,
                "Name", this.fullName,
                "Age", this.age,
                "Sex", this.sex);
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return this.phoneNumber;
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

    public void setActiveStatus(boolean newActiveStatus) {
        this.active = newActiveStatus;
    }

    public String getPatientID() {
        return this.patientID;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getModifiedAt() {
        return this.modifiedAt;
    }

    public void updateModified() {
        this.modifiedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public void addtoHistory(String medHistory) {
        if (medHistory == null || medHistory.trim().isEmpty())
            return;
        this.treatmentRecord.addFirst(medHistory); // newest first
        updateModified();
    }

    public void displayMedicalHistory() {
        System.out.println("---- Medical History ----");
        sortMedicalHistory();
        if (this.medicalHistory == null || this.medicalHistory.isEmpty()
                || (this.medicalHistory.size() == 1 && this.medicalHistory.get(0).equalsIgnoreCase("N/A"))) {
            System.out.println("No medical history recorded.");
        } else {
            for (String h : this.medicalHistory) {
                System.out.println("- " + h);
            }
        }
        System.out.println();
        System.out.println("---- Treatment Records (most recent first) ----");
        if (this.treatmentRecord == null || this.treatmentRecord.isEmpty()) {
            System.out.println("No treatment records yet.");
        } else {
            for (String t : this.treatmentRecord) {
                System.out.println("- " + t);
            }
        }
    }

    public void sortMedicalHistory() {
        Collections.sort(this.medicalHistory, (a, b) -> {
            try {
                String dateA = a.substring(a.lastIndexOf("Date: ") + 6).trim();
                String dateB = b.substring(b.lastIndexOf("Date: ") + 6).trim();

                LocalDateTime dA = LocalDateTime.parse(dateA + "T00:00:00");
                LocalDateTime dB = LocalDateTime.parse(dateB + "T00:00:00");

                return dB.compareTo(dA); 
            } catch (Exception e) {
                return 0;
            }
        });
    }
    public String getFirstName() {
        return this.fName;
    }

    public String getLastName() {
        return this.lName;
    }
    
}
