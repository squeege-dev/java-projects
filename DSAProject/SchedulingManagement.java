import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.*;

public class SchedulingManagement {
    static LinkedList<DoctorSchedule> doctorSchedules = new LinkedList<>();
    static int queueNumberGenerator = 1000;

    public static void schedulingManagementMenu() {
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================");
        System.out.println("|==========\u001B[37m" + "Scheduling Management Menu" + "\u001B[34m=========|");
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================\u001B[0m");
        System.out.println("\u001B[34m1. \u001B[0mSchedule Appointment ");
        System.out.println("\u001B[34m2. \u001B[0mView Queues/Schedules ");
        System.out.println("\u001B[34m3. \u001B[0mReturn to Main Menu ");
        System.out.println("\u001B[34m-------------------------------------------\u001B[0m");
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
            int choice = ValidateInputs.checkIntInput(">> Please Select between [ 1 - 3 ]: ");
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
            if (checkName) {
                System.out.println("---->>! Doctor Name not found. Please try again.");
            }
        }
        LinkedList<String> doctorSchedule = new LinkedList<>();
        for (DoctorRecord dr : DoctorManagement.doctorRecords) {
            if (dr.getFullName().equalsIgnoreCase(doctorName)) {
                doctorSchedule = dr.getWorkSchedules();
                System.out.println(doctorName + "'s  Day Schedule(s): " + doctorSchedule);
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

        String dayOfWeek = LocalDate.parse(date).getDayOfWeek().toString();
        String formattedDay = dayOfWeek.charAt(0) + dayOfWeek.substring(1).toLowerCase();
        String shift = null;
        for (String schedule : doctorSchedule) {
            String[] parts = schedule.split("-");
            if (parts.length >= 2 && parts[0].equalsIgnoreCase(formattedDay)) {
                shift = parts[1]; // Get the shift type (Morning, Afternoon, etc.)
                break;
            }
        }

        if (shift == null) {
            System.out.println(">> Doctor is NOT available on " + formattedDay);
            return;
        }

        // Generate time slots based on shift
        LinkedList<String> slotTimes;
        switch (shift) {
            case "Morning":
                slotTimes = new LinkedList<>(Arrays.asList("07:00-08:00", "08:00-09:00", "09:00-10:00",
                        "10:00-11:00", "11:00-12:00"));
                break;
            case "Afternoon":
                slotTimes = new LinkedList<>(Arrays.asList("13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00"));
                break;
            case "Evening":
                slotTimes = new LinkedList<>(Arrays.asList("18:00-19:00", "19:00-20:00", "20:00-21:00"));
                break;
            case "Whole Day":
                slotTimes = new LinkedList<>(Arrays.asList(
                        "07:00-08:00", "08:00-09:00", "09:00-10:00",
                        "10:00-11:00", "11:00-12:00",
                        "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00"));
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
        for (doctorQueues s : ds.queues) {
            System.out.print(idx++ + ". " + s.time);
            if (s.confirmed != null)
                System.out.print(" [TAKEN:" + s.confirmed.queueID + "]");
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
        if (slotChoice < 1 || slotChoice > ds.queues.size()) {
            System.out.println("Invalid slot selection.");
            return;
        }

        doctorQueues chosen = ds.queues.get(slotChoice - 1);
        String queueID = "Q" + ++queueNumberGenerator;
        Appointments appt = new Appointments(queueID, foundPatient.getPatientID(),
                foundPatient.getFullName(), doctorName, date, chosen.time, priority);

        if (chosen.confirmed == null) {
            chosen.confirmed = appt;
            System.out.println("\u001B[32m>> Appointment CONFIRMED. Queue#: " + appt.queueID + "\u001B[0m");
        } else {
            if (priority == 1) {
                chosen.priorityQueue.addLast(appt);
                System.out.println("\u001B[33m>> Slot is taken. Added to PRIORITY waiting list. Queue#: "
                        + appt.queueID + "\u001B[0m");
            } else {
                chosen.normalQueue.addLast(appt);
                System.out.println("\u001B[33m>> Slot is taken. Added to NORMAL waiting list. Queue#: "
                        + appt.queueID + "\u001B[0m");
            }

            boolean anyFree = false;
            for (doctorQueues s : ds.queues) {
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

        for (doctorQueues s : ds.queues) {
            if (!s.priorityQueue.isEmpty()) {
                Appointments serve = s.priorityQueue.pollFirst();
                System.out.println("\u001B[32m>> Served (PRIORITY waiting):\u001B[0m");
                serve.displayAppt();
                return;
            }
            if (s.confirmed != null) {
                Appointments serve = s.confirmed;
                System.out.println("\u001B[32m>> Served (CONFIRMED):\u001B[0m");
                serve.displayAppt();
                if (!s.priorityQueue.isEmpty()) {
                    s.confirmed = s.priorityQueue.pollFirst();
                    System.out.println(">> Promoted from PRIORITY waiting to CONFIRMED: " + s.confirmed.queueID);
                } else if (!s.normalQueue.isEmpty()) {
                    s.confirmed = s.normalQueue.pollFirst();
                    System.out.println(">> Promoted from NORMAL waiting to CONFIRMED: " + s.confirmed.queueID);
                } else {
                    s.confirmed = null;
                }
                return;
            }

            if (s.confirmed == null && !s.normalQueue.isEmpty()) {
                Appointments appt = s.normalQueue.pollFirst();
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
            boolean hasAppointments = false;
            for (doctorQueues s : ds.queues) {
                if (s.confirmed != null || !s.priorityQueue.isEmpty() || !s.normalQueue.isEmpty()) {
                    hasAppointments = true;
                    break;
                }
            }

            if (!hasAppointments) {
                continue;
            }

            System.out.println("=== Dr. " + ds.doctorName + " | Date: " + ds.date + " ===");

            boolean displayedAny = false;
            for (doctorQueues s : ds.queues) {
                if (s.confirmed != null) {
                    s.confirmed.displayAppt();
                    displayedAny = true;
                }
                if (!s.priorityQueue.isEmpty()) {
                    System.out.println("  Priority waiting:");
                    for (Appointments a : s.priorityQueue) {
                        a.displayAppt();
                        displayedAny = true;
                    }
                }
                if (!s.normalQueue.isEmpty()) {
                    System.out.println("  Normal waiting:");
                    for (Appointments a : s.normalQueue) {
                        a.displayAppt();
                        displayedAny = true;
                    }
                }
            }

            if (displayedAny) {
                int choose = ValidateInputs.checkIntInput(
                        ">> Options: [1] Serve Next Appointment, [2] Cancel Appointment, [3] Find Appointment by Queue Number");
                switch (choose) {
                    case 1:
                        serveNext(ds.doctorName, ds.date);
                        break;
                    case 2:
                        String queueNum = ValidateInputs.checkInput(">> Enter Queue Number to Cancel: ");
                        cancelAppointment(queueNum);
                        break;
                    case 3: 
                        String queueNumFind = ValidateInputs.checkInput(">> Enter Queue Number to Find: ");
                        Appointments found = findAppointmentByQueue(queueNumFind);
                        if (found != null) {
                            System.out.println("Appointment found:");
                            found.displayAppt();
                        } else {
                            System.out.println("Appointment with Queue Number " + queueNumFind + " not found.");
                        }
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } else {
                System.out.println("No appointments scheduled for this date.");
            }
            System.out.println("----------------------------------------");
        }
    }

    static void cancelAppointment(String queueNum) {
        for (DoctorSchedule ds : doctorSchedules) {
            for (doctorQueues s : ds.queues) {
                if (s.confirmed != null && s.confirmed.queueID.equals(queueNum)) {
                    System.out.println("Cancelled confirmed appointment: " + queueNum);

                    if (!s.priorityQueue.isEmpty()) {
                        s.confirmed = s.priorityQueue.poll();
                        System.out.println("Promoted priority to confirmed: " + s.confirmed.queueID);
                    } else if (!s.normalQueue.isEmpty()) {
                        s.confirmed = s.normalQueue.poll();
                        System.out.println("Promoted normal to confirmed: " + s.confirmed.queueID);
                    } else {
                        s.confirmed = null;
                    }
                    return;
                }

                Appointments removed = removeFromQueueList(s.priorityQueue, queueNum);
                if (removed != null) {
                    System.out.println("Cancelled priority waiting appointment: " + queueNum);
                    return;
                }
                removed = removeFromQueueList(s.normalQueue, queueNum);
                if (removed != null) {
                    System.out.println("Cancelled normal waiting appointment: " + queueNum);
                    return;
                }
            }
        }
        System.out.println("Queue number not found: " + queueNum);
    }

    static Appointments removeFromQueueList(LinkedList<Appointments> list, String queueNum) {
        for (Appointments a : list) {
            if (a.queueID.equals(queueNum)) {
                list.remove(a);
                return a;
            }
        }
        return null;
    }

    static Appointments findAppointmentByQueue(String queueNumber) {
        for (DoctorSchedule ds : doctorSchedules) {
            for (doctorQueues s : ds.queues) {
                if (s.confirmed != null && s.confirmed.queueID.equals(queueNumber))
                    return s.confirmed;
                for (Appointments a : s.priorityQueue)
                    if (a.queueID.equals(queueNumber))
                        return a;
                for (Appointments a : s.normalQueue)
                    if (a.queueID.equals(queueNumber))
                        return a;
            }
        }
        return null;
    }

    static class doctorQueues {
        String time;
        Appointments confirmed;
        LinkedList<Appointments> priorityQueue = new LinkedList<>();
        LinkedList<Appointments> normalQueue = new LinkedList<>();

        doctorQueues(String time) {
            this.time = time;
        }
    }

    static class DoctorSchedule {
        String doctorName;
        String date;
        LinkedList<doctorQueues> queues = new LinkedList<>();

        DoctorSchedule(String doctorName, String date, List<String> slotTimes) {
            this.doctorName = doctorName;
            this.date = date;
            for (String t : slotTimes)
                queues.add(new doctorQueues(t));
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
}
