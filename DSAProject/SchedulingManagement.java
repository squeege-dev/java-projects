import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.*;

public class SchedulingManagement {
    static LinkedList<DoctorSchedule> doctorSchedules = new LinkedList<>();
    static int queueNumberGenerator = 1000;

    static class Slot {
        String time;
        Scheduling confirmed;
        LinkedList<Scheduling> priorityQueue = new LinkedList<>();
        LinkedList<Scheduling> normalQueue = new LinkedList<>();

        Slot(String time) {
            this.time = time;
        }
    }

    static class DoctorSchedule {
        String doctorName;
        String date;
        LinkedList<Slot> slots = new LinkedList<>();

        DoctorSchedule(String doctorName, String date, List<String> slotTimes) {
            this.doctorName = doctorName;
            this.date = date;
            for (String t : slotTimes)
                slots.add(new Slot(t));
        }

    }

    static DoctorSchedule findDoctorSchedule(String doctorName, String date) {
        for (DoctorSchedule ds : doctorSchedules) {
            if (ds.doctorName.equalsIgnoreCase(doctorName) && ds.date.equals(date)) {
                return ds;
            }
        }
        return null;
    }

    private static DoctorSchedule createDoctorSchedule(String doctorName, String date, List<String> slotTimes) {
        DoctorSchedule ds = new DoctorSchedule(doctorName, date, slotTimes);
        doctorSchedules.add(ds);
        return ds;
    }

    public static void addScheduling() {
        String date;
        String doctorName = "";
        int priority = 0;
        System.out.println(">> What type of appointment do you want to schedule? [N] Normal / [E] Emergency)");
        String type = ValidateInputs.checkInput(">> Enter appointment type: ").toLowerCase();
        if (type.equals("n") || type.equals("normal")) {
            priority = 0;
        } else if (type.equals("e") || type.equals("emergency")) {
            priority = 1;
        } else {
            System.out.println("\u001B[31mInvalid appointment type selected.\u001B[0m");
            return;
        }

        String patientID = ValidateInputs.checkInput(">> Enter patient ID to schedule (ex. PAT-0000): ").trim();
        PatientRecord foundPatient = null;
        for (PatientRecord p : PatientManagement.patientRecords) {
            if (p.getPatientID().equalsIgnoreCase(patientID)) {
                foundPatient = p;
                break;
            }
        }
        if (foundPatient == null) {
            System.out.println("\u001B[31m---->>! Patient ID not found.\u001B[0m");
            return;
        }

        int pageSize = 5;
        int start = 0;
        boolean viewing = true;
        int pages = (DoctorManagement.doctorRecords.size() % pageSize == 0)
                ? (DoctorManagement.doctorRecords.size() / pageSize)
                : (DoctorManagement.doctorRecords.size() / pageSize) + 1;
        int page = 1;
        while (viewing) {

            int end = start + pageSize;
            if (end > DoctorManagement.doctorRecords.size()) {
                end = DoctorManagement.doctorRecords.size();
            }
            System.out.println(
                    "\u001B[34m<<------------------------------------------------------------------------------------------->>");
            System.out.printf("%67s%n", "\u001B[33mViewing Doctor List(s)\u001B[0m");
            System.out.printf("%58s%n ", "Displaying Page " + page + " of " + pages + ".");

            for (int i = start; i < end; i++) {
                DoctorManagement.doctorRecords.get(i).displayDoctors(i + 1);
            }
            System.out.println(
                    "<<------------------------------------------------------------------------------------------->>");
            System.out.println(
                    ">> Options: [1] Previous [2] Next, [3] Select a Doctor.");
            System.out.print(">> Please Select between [ 1 - 3 ]: ");
            int choice = Main.in.nextInt();

            switch (choice) {
                case 1:
                    if (start >= pageSize) {
                        if (page > 1) {
                            page--;
                        }
                        start -= pageSize;
                    } else {
                        System.out.println("---->>! You are already at the beginning of the list.");
                    }
                    break;
                case 2:
                    if (end < DoctorManagement.doctorRecords.size()) {
                        if (page < pages) {
                            page++;
                        }
                        start += pageSize;

                    } else {
                        System.out.println("---->>! You are already at the end of the list.");
                    }
                    break;
                case 3:
                    viewing = false;
                    break;
            }
        }
        Main.in.nextLine();
        boolean checkName = true;

        while (checkName) {
            doctorName = ValidateInputs.checkInput(">> Enter Doctor Name: ").trim();
            for (DoctorRecord dr : DoctorManagement.doctorRecords) {
                if (dr.fullName.equalsIgnoreCase(doctorName)) {
                    doctorName = dr.fullName;
                    checkName = false;
                    break;
                } else if (dr.getFirstName().equalsIgnoreCase(doctorName)) {
                    doctorName = dr.fullName;
                    checkName = false;
                    break;
                } else if (dr.getLastName().equalsIgnoreCase(doctorName)) {
                    doctorName = dr.fullName;
                    checkName = false;
                    break;
                }
            }
        }
        HashMap<String, String> days = new HashMap<>();
        for (DoctorRecord dr : DoctorManagement.doctorRecords) {
            if (dr.getFullName().equalsIgnoreCase(doctorName)) {
                System.out.println("Day: " + dr.getWorkSchedules().keySet());
                days.putAll(dr.getWorkSchedules());
                break;
            }
        }
        while (true) {
            date = ValidateInputs.checkInput(">> Enter Date (YYYY-MM-DD): ").trim();
            try {
                LocalDate checkDate = LocalDate.parse(date);
                if (checkDate.isBefore(LocalDate.now())) {
                    System.out.println("---->>! Invalid Date Input. Date cannot be in the past.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("---->>! Invalid date format. Please try again (YYYY-MM-DD)");
            }
        }

        // GET DAY OF WEEK FROM THE DATE
        String dayOfWeek = LocalDate.parse(date).getDayOfWeek().name();
        String formattedDay = dayOfWeek.charAt(0) + dayOfWeek.substring(1).toLowerCase();

        // CHECK IF DOCTOR WORKS THAT DAY
        if (!days.containsKey(formattedDay)) {
            System.out.println(">> Doctor is NOT available on " + formattedDay);
            return;
        }

        // GET SHIFT
        String shift = days.get(formattedDay);

        // CHECK FOR NOT AVAILABLE
        if (shift.equals("N")) {
            System.out.println(">> Doctor is NOT available on " + formattedDay + " (Not Available)");
            return;
        }

        // GENERATE SLOT TIMES
        List<String> slotTimes = new ArrayList<>();

        switch (shift) {
            case "M":
                Collections.addAll(slotTimes,
                        "07:00-08:00", "08:00-09:00", "09:00-10:00",
                        "10:00-11:00", "11:00-12:00");
                break;
            case "A":
                Collections.addAll(slotTimes,
                        "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00");
                break;
            case "E":
                Collections.addAll(slotTimes,
                        "18:00-19:00", "19:00-20:00", "20:00-21:00");
                break;
            case "W":
                Collections.addAll(slotTimes,
                        "07:00-08:00", "08:00-09:00", "09:00-10:00",
                        "10:00-11:00", "11:00-12:00",
                        "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00");
                break;
            default:
                System.out.println("Unknown shift type.");
                return;
        }

        DoctorSchedule ds = findDoctorSchedule(doctorName, date);
        if (ds == null) {
            ds = createDoctorSchedule(doctorName, date, slotTimes);
        }

        System.out.println("Available time slots for Dr. " + doctorName + " on " + date + ":");
        int idx = 1;
        for (Slot s : ds.slots) {
            System.out.print(idx++ + ". " + s.time);
            if (s.confirmed != null)
                System.out.print(" [TAKEN:" + s.confirmed.queueNumber + "]");
            int waiting = s.priorityQueue.size() + s.normalQueue.size();
            if (waiting > 0)
                System.out.print(" [Waiting:" + waiting + "]");
            System.out.println();
        }
        System.out.println("Choose slot number: ");
        String slotChoiceStr = Main.in.nextLine().trim();
        int slotChoice;
        try {
            slotChoice = Integer.parseInt(slotChoiceStr);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid slot selection.");
            return;
        }
        if (slotChoice < 1 || slotChoice > ds.slots.size()) {
            System.out.println("Invalid slot selection.");
            return;
        }
        Slot chosen = ds.slots.get(slotChoice - 1);

        int qnum = ++queueNumberGenerator;
        Scheduling appt = new Scheduling(qnum, foundPatient.getPatientID(),
                foundPatient.getFullName(), doctorName, date, chosen.time, priority);

        if (chosen.confirmed == null) {
            chosen.confirmed = appt;
            System.out.println("\u001B[32m>> Appointment CONFIRMED. Queue#: " + appt.queueNumber + "\u001B[0m");
        } else {
            if (priority == 1) {
                chosen.priorityQueue.addLast(appt);
                System.out.println("\u001B[33m>> Slot is taken. Added to PRIORITY waiting list. Queue#: "
                        + appt.queueNumber + "\u001B[0m");
            } else {
                chosen.normalQueue.addLast(appt);
                System.out.println("\u001B[33m>> Slot is taken. Added to NORMAL waiting list. Queue#: "
                        + appt.queueNumber + "\u001B[0m");
            }

            boolean anyFree = false;
            for (Slot s : ds.slots) {
                if (s.confirmed == null) {
                    anyFree = true;
                    break;
                }
            }
            if (!anyFree && priority == 0) {
                System.out.println(
                        "\u001B[31m>> All slots for this date are taken. Consider another date or ask for priority/earlier slot.\u001B[0m");
            }
        }
    }

    public static void serveNext(String doctorName, String date) {
        DoctorSchedule ds = findDoctorSchedule(doctorName, date);
        if (ds == null) {
            System.out.println("No schedule found for Dr. " + doctorName + " on " + date);
            return;
        }

        for (Slot s : ds.slots) {
            if (!s.priorityQueue.isEmpty()) {
                Scheduling serve = s.priorityQueue.pollFirst();
                System.out.println("\u001B[32m>> Served (PRIORITY waiting):\u001B[0m");
                serve.displayAppt();
                return;
            }
            if (s.confirmed != null) {
                Scheduling serve = s.confirmed;
                System.out.println("\u001B[32m>> Served (CONFIRMED):\u001B[0m");
                serve.displayAppt();
                if (!s.priorityQueue.isEmpty()) {
                    s.confirmed = s.priorityQueue.pollFirst();
                    System.out.println(">> Promoted from PRIORITY waiting to CONFIRMED: " + s.confirmed.queueNumber);
                } else if (!s.normalQueue.isEmpty()) {
                    s.confirmed = s.normalQueue.pollFirst();
                    System.out.println(">> Promoted from NORMAL waiting to CONFIRMED: " + s.confirmed.queueNumber);
                } else {
                    s.confirmed = null;
                }
                return;
            }

            if (s.confirmed == null && !s.normalQueue.isEmpty()) {
                Scheduling appt = s.normalQueue.pollFirst();
                System.out.println("\u001B[32m>> Served (NORMAL waiting):\u001B[0m");
                appt.displayAppt();
                return;
            }
        }
        System.out.println("No appointments to serve for Dr. " + doctorName + " on " + date);
    }

    public static void viewQueues() {
        if (doctorSchedules.isEmpty()) {
            System.out.println("No doctor schedules yet.");
            return;
        }
        for (DoctorSchedule ds : doctorSchedules) {
            System.out.println("=== Dr. " + ds.doctorName + " | Date: " + ds.date + " ===");

            for (Slot s : ds.slots) {
                if (!s.priorityQueue.isEmpty()) {
                    System.out.println("  Priority waiting:");
                    for (Scheduling a : s.priorityQueue)
                        a.displayAppt();
                }
                if (!s.normalQueue.isEmpty()) {
                    System.out.println("  Normal waiting:");
                    for (Scheduling a : s.normalQueue)
                        a.displayAppt();
                }
                if (s.confirmed != null) {
                    s.confirmed.displayAppt();
                } else {
                    continue;
                }
            }
            System.out.println("----------------------------------------");
        }
    }

    public void cancelAppointment(int queueNumber) {
        for (DoctorSchedule ds : doctorSchedules) {
            for (Slot s : ds.slots) {
                if (s.confirmed != null && s.confirmed.queueNumber == queueNumber) {
                    System.out.println("Cancelled confirmed appointment: " + queueNumber);

                    if (!s.priorityQueue.isEmpty()) {
                        s.confirmed = s.priorityQueue.pollFirst();
                        System.out.println("Promoted priority to confirmed: " + s.confirmed.queueNumber);
                    } else if (!s.normalQueue.isEmpty()) {
                        s.confirmed = s.normalQueue.pollFirst();
                        System.out.println("Promoted normal to confirmed: " + s.confirmed.queueNumber);
                    } else {
                        s.confirmed = null;
                    }
                    return;
                }
                Scheduling removed = removeFromQueueList(s.priorityQueue, queueNumber);
                if (removed != null) {
                    System.out.println("Cancelled priority waiting appointment: " + queueNumber);
                    return;
                }
                removed = removeFromQueueList(s.normalQueue, queueNumber);
                if (removed != null) {
                    System.out.println("Cancelled normal waiting appointment: " + queueNumber);
                    return;
                }
            }
        }
        System.out.println("Queue number not found: " + queueNumber);
    }

    private Scheduling removeFromQueueList(LinkedList<Scheduling> list, int queueNumber) {
        Iterator<Scheduling> it = list.iterator();
        while (it.hasNext()) {
            Scheduling a = it.next();
            if (a.queueNumber == queueNumber) {
                it.remove();
                return a;
            }
        }
        return null;
    }

    public static void schedulingManagementMenu() {
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================");
        System.out.println("|==========\u001B[37m" + "Scheduling Management Menu" + "\u001B[34m=========|");
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================\u001B[0m");
        System.out.println("\u001B[34m1. \u001B[0mSchedule Appointment ");
        System.out.println("\u001B[34m2. \u001B[0mView Queues/Schedules ");
        System.out.println("\u001B[34m3. \u001B[0mReturn to Main Menu ");
        System.out.println("\u001B[37m-------------------------------------------\u001B[0m");
    }

    public Scheduling findAppointmentByQueue(int queueNumber) {
        for (DoctorSchedule ds : doctorSchedules) {
            for (Slot s : ds.slots) {
                if (s.confirmed != null && s.confirmed.queueNumber == queueNumber)
                    return s.confirmed;
                for (Scheduling a : s.priorityQueue)
                    if (a.queueNumber == queueNumber)
                        return a;
                for (Scheduling a : s.normalQueue)
                    if (a.queueNumber == queueNumber)
                        return a;
            }
        }
        return null;
    }

}
