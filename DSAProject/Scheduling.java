import java.util.*;

public class Scheduling {
    int queueNumber;
    String patientID;
    String patientName;
    String doctorName;
    String date;
    String timeSlot;
    int priority;

    Scheduling(int queueNumber, String patientID, String patientName,
            String doctorName, String date, String timeSlot, int priority) {
        this.queueNumber = queueNumber;
        this.patientID = patientID;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.date = date;
        this.timeSlot = timeSlot;
        this.priority = priority;
    }

    void displayAppt() {
        System.out.println("[" + queueNumber + "] " + patientName + " (" + patientID + ") "
                + date + " " + timeSlot + " " + (priority == 1 ? "PRIORITY" : "NORMAL")
                );
    }

}
