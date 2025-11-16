import java.util.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DoctorManagement {
    static Random generate = new Random();
    static LinkedList<DoctorRecord> doctorRecords = new LinkedList<>();
    static int idNumber = 1000;

    public static void doctorMangementMenu() {
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================");
        System.out.println("|==========\u001B[37m" + "Doctor Management Menu" + "\u001B[34m=========|");
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================\u001B[0m");
        System.out.println("\u001B[34m1. \u001B[0m Register New Doctor");
        System.out.println("\u001B[34m2. \u001B[0mView Doctors");
        System.out.println("\u001B[34m3. \u001B[0mReturn to Main Menu");
        System.out.println("\u001B[37m-------------------------------------------\u001B[0m");
        return;
    }

    public static HashMap<String,String> inputWeeklySchedule() {
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        HashMap<String, String> scheduleMap = new HashMap<>();

        System.out.println("Available Days: Monday, Tuesday, Wednesday, Thursday, Friday");
        System.out.println("Enter a day OR type 'done' to finish.");

        while (true) {
            String dayInput = ValidateInputs.checkInput(">> Enter day: ").trim();

            if (dayInput.equalsIgnoreCase("done") || dayInput.equals("0"))
                break;

            DayOfWeek day = null;
            
            try {
                day = DayOfWeek.valueOf(dayInput.toUpperCase());
            } catch (Exception e) {
                System.out.println("\u001B[31m---->> Invalid day. Try again.\u001B[0m");
                continue;
            }

            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                System.out.println("\u001B[31m---->> Weekends are not allowed.\u001B[0m");
                continue;
            }

            if (scheduleMap.containsKey(day)) {
                System.out.println("\u001B[31m---->> Day already has a schedule.\u001B[0m");
                continue;
            }

            System.out.println("\n---->> Choose Availability:");
            System.out.println("  [M] Morning   (7am–12pm)");
            System.out.println("  [A] Afternoon (1pm–5pm)");
            System.out.println("  [E] Evening   (6pm–9pm)");
            System.out.println("  [W] Whole Day (7am–5pm)");
            System.out.println("  [N] Not Available");

            String choice;
            while (true) {
                choice = ValidateInputs.checkInput(">> Enter availability for " + dayInput + " [M/A/E/W/N]: ")
                        .trim().toUpperCase();
                if (choice.matches("[MAEWN]"))
                    break;
                System.out.println("\u001B[31m---->> Invalid input.\u001B[0m");
            }

            scheduleMap.put(dayInput, choice);
            System.out.println("---->> Schedule added for " + dayInput + ".");
        }
        return scheduleMap;
    }

    public static void addDoctor() {
        String dob = "";
        String fullName = "";
        String sex = "";
        int age = 0;
        
        System.out.println(
                "<<\u001B[34m=======================\u001B[33mRegistration Form for Doctors\u001B[34m======================\u001B[0m>>");
        System.out.printf("\u001B[31m%67s \u001B[0m%n", "You can enter zero [0] to cancel registraion anytime. ");
        String dateRegistered = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        while (true) {
            String fName = ValidateInputs.checkInput("\u001B[33m>>\u001B[0m Enter First Name: ");
            if (fName.equals("0")) {
                System.out.println("\u001B[31m>> Registration Cancelled. \u001B[0m");
                return;
            }

            String lName = ValidateInputs.checkInput("\u001B[33m>>\u001B[0m Enter Last Name: ");
            if (lName.equals("0")) {
                System.out.println("\u001B[31m>> Registration Cancelled. \u001B[0m");
                return;
            }
            String tempFullName = fName + " " + lName;
            boolean newName = true;
            for (DoctorRecord pr : doctorRecords) {
                if (tempFullName.equalsIgnoreCase(pr.getFullName())) {
                    newName = false;
                }
            }
            if (newName) {
                fullName = fName + " " + lName;
                break;
            } else {
                System.out.println("\u001B[33m>> Name already Registered. Try Again.");
            }
        }
        while (true) {
            sex = ValidateInputs.checkInput("\u001B[33m>>\u001B[0m Enter assigned Sex (Male/Female): ").toLowerCase();
            if (sex.equals("0"))
                return;
            if (sex.equals("male")) {
                sex = "Male";
                break;
            } else if (sex.equals("female")) {
                sex = "Female";
                break;
            } else {
                System.out.println("---->>! Please choose between > Male < & > Female <.");
            }
        }

        while (true) {
            dob = ValidateInputs.checkInput("\u001B[33m>> \u001B[0m Enter Date of Birth (YYYY-MM-DD): ");
            if (dob.equals("0")) {
                System.out.println("\u001B[31m>> Registration Cancelled. \u001B[0m");
                return;
            }
            try {
                LocalDate date = LocalDate.parse(dob);
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("---->>! Date of birth cannot be in the future. Try Again.");
                    continue;
                }

                int checkInput = Integer.parseInt(dob.substring(0, 4));

                if (checkInput > 1900 && checkInput <= LocalDateTime.now().getYear()) {

                    checkInput = Integer.parseInt(dob.substring(5, 7));
                    if (checkInput >= 1 && checkInput <= 12) {

                        checkInput = Integer.parseInt(dob.substring(8, 10));
                        if (checkInput >= 1 && checkInput <= 31) {

                            age = Integer.parseInt(dateRegistered.substring(0, 4))
                                    - Integer.parseInt(dob.substring(0, 4));
                            break;
                        }
                    }
                } else {
                    System.out.println("---->>! Invalid Year in Date of Birth. Try Again.");
                }
            } catch (Exception e) {
                System.out.println("---->>! Invalid date format. Please try again (YYYY-MM-DD)");
            }
        }
        String address = ValidateInputs.checkInput("\u001B[33m>> \u001B[0m  Enter Address (Province): ");
        if (address.equals("0"))
            return;

        String phoneNumber = ValidateInputs.checkInput("\u001B[33m>> \u001B[0m Enter Phone Number: ");
        if (phoneNumber.equals("0"))
            return;

        String specialization = ValidateInputs.checkInput("\u001B[33m>> \u001B[0m Enter specialization: ");
        if (specialization.equals("0"))
            return;

        HashMap<String, String> scheduleList = inputWeeklySchedule();
        String doctorID = "DOC-" + ++idNumber;
        System.out.print("Processing");
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.print(".");
        }
        System.out.println("\n \u001B[32mPatient Registered Successfully! \u001B[35m >>>\u001B[0m Assigned Doctor ID: "
                + doctorID);
        DoctorRecord newDoctor = new DoctorRecord(doctorID, fullName, dob, age, sex, address, phoneNumber,
                specialization,
                scheduleList, dateRegistered);
        doctorRecords.add(newDoctor);
    }

    public static void viewDoctorsList() {
        int pageSize = 5;
        int start = 0;
        boolean viewing = true;
        int pages = (doctorRecords.size() % pageSize == 0) ? (doctorRecords.size() / pageSize)
                : (doctorRecords.size() / pageSize) + 1;
        int page = 1;
        while (viewing) {

            int end = start + pageSize;
            if (end > doctorRecords.size()) {
                end = doctorRecords.size();
            }
            System.out.println(
                    "\u001B[34m<<------------------------------------------------------------------------------------------->>");
            System.out.printf("%67s%n", "\u001B[33mViewing Doctor List(s)\u001B[0m");
            System.out.printf("%58s%n ", "Displaying Page " + page + " of " + pages + ".");

            for (int i = start; i < end; i++) {
                doctorRecords.get(i).displayDoctors(i + 1);
            }
            System.out.println(
                    "<<------------------------------------------------------------------------------------------->>");
            System.out.println(
                    ">> Options: [1] Next, [2] Previous, [3] Select/Find a Patient, [4] Exit.");
            System.out.print(">> Please Select between [ 1 - 4 ]: ");
            int choice = Main.in.nextInt();
            Main.in.nextLine();
            switch (choice) {
                case 1:
                    if (end < doctorRecords.size()) {
                        if (page < pages) {
                            page++;
                        }
                        start += pageSize;

                    } else {
                        System.out.println("---->>! You are already at the end of the list.");
                    }
                    break;
                case 2:
                    if (start >= pageSize) {
                        if (page > 1) {
                            page--;
                        }
                        start -= pageSize;
                    } else {
                        System.out.println("---->>! You are already at the beginning of the list.");
                    }
                    break;
                case 3:
                    while (true) {
                        String searchDoctor = ValidateInputs
                                .checkInput(">> Enter Doctor Name or ID to Select/Search: ");
                        boolean isDoctorID = searchDoctor.length() >= 4
                                && searchDoctor.substring(0, 4).equalsIgnoreCase("DOC-");
                        if (isDoctorID) {
                            Collections.sort(doctorRecords, Comparator.comparing(DoctorRecord::getDoctorID));

                            long startTime = System.nanoTime();
                            int low = 0;
                            int high = doctorRecords.size() - 1;
                            boolean found = false;
                            int index = 0;
                            while (low <= high) {
                                int mid = (low + high) / 2;
                                String midID = doctorRecords.get(mid).getDoctorID();

                                int compare = midID.compareToIgnoreCase(searchDoctor);

                                if (compare == 0) {
                                    doctorRecords.get(mid).displayDoctorDetails();
                                    found = true;
                                    index = mid;
                                    break;
                                } else if (compare < 0) {
                                    low = mid + 1;
                                } else {
                                    high = mid - 1;
                                }
                            }
                            long endTime = System.nanoTime();
                            double duration = (endTime - startTime) / 1_000_000.0;
                            System.out.println(
                                    "\u001B[32m>> (Binary Search) Search completed in " + duration + " ms.\u001B[0m");

                            if (found) {
                                System.out
                                        .println(">> Options: [1] Update Details, [2] Exit.");
                                int options = ValidateInputs
                                        .checkIntInput(">> What would you like to do next (choose between [1 / 2]): ");
                                switch (options) {
                                    case 1:
                                        System.out.println(
                                                "---->>! Choose Which Among the Following You Would Like to Change. !<<----");
                                        System.out.println(
                                                "--> Options: [1] Update Contact Number, [2] Update Address, [3] Update Status. <--");
                                        int changeWhat = ValidateInputs.checkIntInput(">> Choose between [1 - 3]: ");

                                        switch (changeWhat) {

                                            case 1:
                                                System.out.println("---->>> Updating Phone Number <<<---");
                                                while (true) {
                                                    System.out.println(
                                                            ">> Current Phone Number: "
                                                                    + doctorRecords.get(index).getPhoneNum());
                                                    String newPhoneNum = ValidateInputs
                                                            .checkInput(">> Enter new Phone Number: ");

                                                    if (ValidateInputs.phoneNumValidation(newPhoneNum)) {
                                                        doctorRecords.get(index).setPhoneNum(newPhoneNum);
                                                        System.out.println(
                                                                "\u001B[32m>> Phone Number Updated Successfully.\u001B[0m");
                                                        break;
                                                    } else {
                                                        System.out
                                                                .println(
                                                                        "\u001B[31m>> Invalid Phone Number. Try Again.\u001B[0m");
                                                    }
                                                }
                                                doctorRecords.get(index).updateModified();
                                                break;

                                            case 2:
                                                System.out.println("---->>> Updating Address <<<---");
                                                System.out.println(">> Current Address: "
                                                        + doctorRecords.get(index).getAddress());
                                                String newAddress = ValidateInputs
                                                        .checkInput(">> Enter new Address : ");

                                                doctorRecords.get(index).setAddress(newAddress);
                                                System.out
                                                        .println("\u001B[32m>> Address Updated Successfully.\u001B[0m");
                                                doctorRecords.get(index).updateModified();
                                                break;

                                            case 3:
                                                System.out.println("---->>> Updating Status <<<---");
                                                boolean currentStatus = doctorRecords.get(index).getActiveStatus();
                                                System.out.println(">> Current Status: "
                                                        + (currentStatus ? "Active" : "Inactive"));

                                                int statusChoice = ValidateInputs
                                                        .checkIntInput(">> Choose [1] Set Active | [2] Set Inactive: ");

                                                if (statusChoice == 1 && !currentStatus) {
                                                    doctorRecords.get(index).setActiveStatus(true);
                                                    System.out.println("\u001B[32m>> Doctor is now ACTIVE.\u001B[0m");
                                                } else if (statusChoice == 2 && currentStatus) {
                                                    doctorRecords.get(index).setActiveStatus(false);
                                                    System.out
                                                            .println("\u001B[33m>> Doctor is now INACTIVE.\u001B[0m");
                                                } else {
                                                    System.out.println("\u001B[31m>> No change made.\u001B[0m");
                                                }

                                                doctorRecords.get(index).updateModified();
                                                break;

                                            default:
                                                System.out.println("Invalid choice. Returning to options.");
                                        }
                                        break;
                                    case 2:
                                        break;
                                    default:
                                }
                            } else {
                                System.out.println("\u001B[31m>> Patient not found.\u001B[0m");
                            }
                            break;
                        }
                        // linear search
                        else {
                            long startTime = System.nanoTime();

                            boolean found = false;
                            int index = 0;
                            for (DoctorRecord dr : doctorRecords) {
                                if (dr.getFullName().equalsIgnoreCase(searchDoctor)) {
                                    dr.displayDoctorDetails();
                                    found = true;
                                    index = doctorRecords.indexOf(dr);
                                    break;
                                }else if(dr.getFirstName().equalsIgnoreCase(searchDoctor) || dr.getLastName().equalsIgnoreCase(searchDoctor)){
                                    dr.displayDoctorDetails();
                                    found = true;
                                    index = doctorRecords.indexOf(dr);
                                    break;
                                }
                            }
                            long endTime = System.nanoTime();
                            double duration = (endTime - startTime) / 1_000_000.0;
                            System.out.println(
                                    "\u001B[32m>> (Linear Search) Search completed in " + duration + " ms.\u001B[0m");
                            if (found) {
                                System.out
                                        .println(">> Options: [1] Update Details, [2] Exit.");
                                int options = ValidateInputs
                                        .checkIntInput(">> What would you like to do next (choose between [1 - 2]): ");
                                switch (options) {
                                    case 1:
                                        System.out.println(
                                                "---->>! Choose Which Among the Following You Would Like to Change. !<<----");
                                        System.out.println(
                                                "--> Options: [1] Update Contact Number, [2] Update Address, [3] Update Status. <--");
                                        int changeWhat = ValidateInputs.checkIntInput(">> Choose between [1 - 3]: ");

                                        switch (changeWhat) {

                                            case 1:
                                                System.out.println("---->>> Updating Phone Number <<<---");
                                                while (true) {
                                                    System.out.println(
                                                            ">> Current Phone Number: "
                                                                    + doctorRecords.get(index).getPhoneNum());
                                                    String newPhoneNum = ValidateInputs
                                                            .checkInput(">> Enter new Phone Number: ");

                                                    if (ValidateInputs.phoneNumValidation(newPhoneNum)) {
                                                        doctorRecords.get(index).setPhoneNum(newPhoneNum);
                                                        System.out.println(
                                                                "\u001B[32m>> Phone Number Updated Successfully.\u001B[0m");
                                                        break;
                                                    } else {
                                                        System.out
                                                                .println(
                                                                        "\u001B[31m>> Invalid Phone Number. Try Again.\u001B[0m");
                                                    }
                                                }
                                                doctorRecords.get(index).updateModified();
                                                break;

                                            case 2:
                                                System.out.println("---->>> Updating Address <<<---");
                                                System.out.println(">> Current Address: "
                                                        + doctorRecords.get(index).getAddress());
                                                String newAddress = ValidateInputs
                                                        .checkInput(">> Enter new Address : ");

                                                doctorRecords.get(index).setAddress(newAddress);
                                                System.out
                                                        .println("\u001B[32m>> Address Updated Successfully.\u001B[0m");
                                                doctorRecords.get(index).updateModified();
                                                break;

                                            case 3:
                                                System.out.println("---->>> Updating Status <<<---");
                                                boolean currentStatus = doctorRecords.get(index).getActiveStatus();
                                                System.out.println(">> Current Status: "
                                                        + (currentStatus ? "Active" : "Inactive"));

                                                int statusChoice = ValidateInputs
                                                        .checkIntInput(">> Choose [1] Set Active | [2] Set Inactive: ");

                                                if (statusChoice == 1 && !currentStatus) {
                                                    doctorRecords.get(index).setActiveStatus(true);
                                                    System.out.println("\u001B[32m>> Doctor is now ACTIVE.\u001B[0m");
                                                } else if (statusChoice == 2 && currentStatus) {
                                                    doctorRecords.get(index).setActiveStatus(false);
                                                    System.out
                                                            .println("\u001B[33m>> Doctor is now INACTIVE.\u001B[0m");
                                                } else {
                                                    System.out.println("\u001B[31m>> No change made.\u001B[0m");
                                                }
                                                doctorRecords.get(index).updateModified();
                                                break;

                                            default:
                                                System.out.println("Invalid choice. Returning to options.");
                                        }
                                        break;
                                    case 2:
                                        break;
                                    default:
                                }
                            } else {
                                System.out.println("\u001B[31m>> Doctor not found.\u001B[0m");
                            }
                            break;
                        }
                    }
                    break;
                case 4:
                    viewing = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void preloadDoctors() {
        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Samuel Rivera",
                "1978-05-12", 47, "Male",
                "Quezon City",
                "09171234501",
                "Internal Medicine",
                new HashMap<>(Map.of(
                        "Monday", "08:00-12:00",
                        "Thursday", "13:00-17:00")),
                "2025/11/15 09:00:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Alicia Santos",
                "1984-09-21", 41, "Female",
                "Manila City",
                "09982345678",
                "Pediatrics",
                new HashMap<>(Map.of(
                        "Tuesday", "08:00-12:00",
                        "Wednesday", "08:00-12:00",
                        "Friday", "13:00-17:00")),
                "2025/11/15 09:05:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Dominic Tan",
                "1975-03-18", 50, "Male",
                "Pasig City",
                "09195551234",
                "Cardiology",
                new HashMap<>(Map.of(
                        "Monday", "13:00-17:00",
                        "Friday", "08:00-12:00")),
                "2025/11/15 09:10:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Jasmine Lopez",
                "1982-02-27", 43, "Female",
                "Makati City",
                "09173456789",
                "Neurology",
                new HashMap<>(Map.of(
                        "Monday", "08:00-12:00",
                        "Wednesday", "13:00-17:00",
                        "Friday", "08:00-11:00")),
                "2025/11/15 09:15:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Karen Velasco",
                "1990-07-03", 35, "Female",
                "Caloocan City",
                "09991239871",
                "Dermatology",
                new HashMap<>(Map.of(
                        "Wednesday", "08:00-12:00",
                        "Monday", "13:00-17:00")),
                "2025/11/15 09:20:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Miguel Reyes",
                "1970-11-15", 55, "Male",
                "Taguig City",
                "09184561234",
                "Surgery",
                new HashMap<>(Map.of(
                        "Tuesday", "13:00-17:00",
                        "Thursday", "08:00-12:00",
                        "Friday", "14:00-18:00")),
                "2025/11/15 09:25:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Brandon Chua",
                "1986-01-30", 39, "Male",
                "Las Piñas City",
                "09174561289",
                "Orthopedics",
                new HashMap<>(Map.of(
                        "Monday", "09:00-12:00",
                        "Thursday", "09:00-12:00")),
                "2025/11/15 09:30:00"));

    }
}