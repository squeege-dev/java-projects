import java.util.*;

public class Appointments {
    String queueID;
    String patientID;
    String patientName;
    String doctorName;
    String date;
    String timeSlot;
    int priority;

    Appointments(String queueID, String patientID, String patientName,
            String doctorName, String date, String timeSlot, int priority) {
        this.queueID = queueID;
        this.patientID = patientID;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.date = date;
        this.timeSlot = timeSlot;
        this.priority = priority;
    }

    void displayAppt() {
        System.out.println("[" + queueID + "] " + patientName + " (" + patientID + ") "
                + date + " " + timeSlot + " " + (priority == 1 ? "PRIORITY" : "NORMAL")
                );
    }

}
