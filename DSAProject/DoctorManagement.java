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
        System.out.println("\u001B[34m-------------------------------------------\u001B[0m");
        return;
    }

    public static LinkedList<String> inputWeeklySchedule() {
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        LinkedList<String> scheduleMap = new LinkedList<>();

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
            for (int i = 0; i < scheduleMap.size(); i++) {
                if (scheduleMap.get(i).equalsIgnoreCase(dayInput)) {
                    System.out.println("\u001B[31m---->> Day already has a schedule.\u001B[0m");
                    day = null;
                    break;
                }
            }

            System.out.println("\n---->> Choose Availability:");
            System.out.println("  [M] Morning   (7am-12pm)");
            System.out.println("  [A] Afternoon (1pm-5pm)");
            System.out.println("  [E] Evening   (6pm-9pm)");
            System.out.println("  [W] Whole Day (7am-5pm)");

            String choice;
            while (true) {
                choice = ValidateInputs.checkInput(">> Enter availability for " + dayInput + " [M/A/E/W]: ")
                        .trim().toUpperCase();
                switch (choice) {
                    case "M":
                        dayInput += "-Morning";
                        break;
                    case "A":
                        dayInput += "-Afternoon";
                        break;
                    case "E":
                        dayInput += "-Evening";
                        break;
                    case "W":
                        dayInput += "-Whole Day";
                        break;
                    default:
                        System.out.println("\u001B[31m---->> Invalid input.\u001B[0m");
                        continue;
                }
                break;
            }

            scheduleMap.add(dayInput);
            System.out.println("---->> Schedule added for " + dayInput + ".");
        }
        return scheduleMap;
    }

    public static void addDoctor() {
        String dob = "";
        String fullName = "", fName = "", lName = "";
        String sex = "";
        int age = 0;

        System.out.println(
                "<<\u001B[34m=======================\u001B[33mRegistration Form for Doctors\u001B[34m======================\u001B[0m>>");
        System.out.printf("\u001B[31m%67s \u001B[0m%n", "You can enter zero [0] to cancel registraion anytime. ");
        String dateRegistered = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        while (true) {
            fName = ValidateInputs.checkInput("\u001B[33m>>\u001B[0m Enter First Name: ");
            if (fName.equals("0")) {
                System.out.println("\u001B[31m>> Registration Cancelled. \u001B[0m");
                return;
            }

            lName = ValidateInputs.checkInput("\u001B[33m>>\u001B[0m Enter Last Name: ");
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

        LinkedList<String> scheduleList = inputWeeklySchedule();
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
        DoctorRecord newDoctor = new DoctorRecord(doctorID, fName, lName, dob, age, sex, address, phoneNumber,
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
                    "\u001B[34m<<------------------------------------------------------------------------------------------->>");
            System.out.println(
                    "\u001B[33m>>\u001B[0m \u001B[35mOptions:\u001B[0m [1] Previous, [2] Next, [3] Select/Find a Patient, [4] Exit.");
            System.out.print("\u001B[33m>>\u001B[0m Please Select between [ 1 - 4 ]: ");
            int choice = ValidateInputs.checkIntInput("\u001B[33m>>\u001B[0m Please Select between [ 1 - 4 ]: ");
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
                    if (end < doctorRecords.size()) {
                        if (page < pages) {
                            page++;
                        }
                        start += pageSize;

                    } else {
                        System.out.println("---->>! You are already at the end of the list.");
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

                            provideOptions(isDoctorID, index);
                            break;
                        }
                        // linear search
                        else {
                            long startTime = System.nanoTime();
                            boolean found = false;
                            int index = 0;
                            LinkedList<DoctorRecord> tempList = new LinkedList<>();
                            for (DoctorRecord dr : doctorRecords) {
                                boolean fullNameMatch = dr.getFullName().equalsIgnoreCase(searchDoctor);
                                boolean firstNameMatch = dr.getFirstName().equalsIgnoreCase(searchDoctor);
                                boolean lastNameMatch = dr.getLastName().equalsIgnoreCase(searchDoctor);

                                if (fullNameMatch || firstNameMatch || lastNameMatch) {
                                    tempList.add(dr);
                                }
                            }

                            if (!tempList.isEmpty()) {
                                if (tempList.size() == 1) {
                                    tempList.get(0).displayDoctorDetails();
                                    provideOptions(true, doctorRecords.indexOf(tempList.get(0)));
                                } else {
                                    System.out.println("\u001B[34m>> Multiple Matches Found: \u001B[0m");
                                    for (int i = 0; i < tempList.size(); i++) {
                                        tempList.get(i).displayDoctors(i + 1);
                                    }

                                    provideOptions(isDoctorID, index);
                                }
                            }
                            long endTime = System.nanoTime();
                            double elapsedTime = (endTime - startTime) / 1_000_000.0;
                            System.out.println(
                                    "\u001B[32m>> (Linear Search) Search completed in " + elapsedTime
                                            + " ms.\u001B[0m");

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

    public static void updateDrPN(int index) {
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
    }

    static void provideOptions(boolean tof, int index) {
        if (tof) {
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
                            updateDrPN(index);
                            break;
                        case 2:
                            updateAddress(index);
                            break;

                        case 3:
                            updateActiveStatus(index);
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
    }

    public static void updateAddress(int index) {
        System.out.println("---->>> Updating Address <<<---");
        System.out.println(">> Current Address: "
                + doctorRecords.get(index).getAddress());
        String newAddress = ValidateInputs
                .checkInput(">> Enter new Address : ");

        doctorRecords.get(index).setAddress(newAddress);
        System.out
                .println("\u001B[32m>> Address Updated Successfully.\u001B[0m");
        doctorRecords.get(index).updateModified();
    }

    public static void updateActiveStatus(int index) {
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
    }

    public static void preloadDoctors() {
        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Samuel", "Rivera",
                "1978-05-12", 47, "Male",
                "Quezon City",
                "09171234501",
                "Internal Medicine",
                new LinkedList<>(Arrays.asList(
                        "Monday-Morning",
                        "Thursday-Afternoon")),
                "2025/11/15 09:00:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Alicia", "Santos",
                "1984-09-21", 41, "Female",
                "Manila City",
                "09982345678",
                "Pediatrics",
                new LinkedList<>(Arrays.asList(
                        "Tuesday-Morning",
                        "Wednesday-Morning",
                        "Friday-Afternoon")),
                "2025/11/15 09:05:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Dominic", "Tan",
                "1975-03-18", 50, "Male",
                "Pasig City",
                "09195551234",
                "Cardiology",
                new LinkedList<>(Arrays.asList(
                        "Monday-Afternoon",
                        "Friday-Morning")),
                "2025/11/15 09:10:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Jasmine", "Lopez",
                "1982-02-27", 43, "Female",
                "Makati City",
                "09173456789",
                "Neurology",
                new LinkedList<>(Arrays.asList(
                        "Monday-Morning",
                        "Wednesday-Afternoon",
                        "Friday-Morning")),
                "2025/11/15 09:15:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Karen", "Velasco",
                "1990-07-03", 35, "Female",
                "Caloocan City",
                "09991239871",
                "Dermatology",
                new LinkedList<>(Arrays.asList(
                        "Monday-Afternoon", "Wednesday-Morning")),
                "2025/11/15 09:20:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Miguel", "Reyes",
                "1970-11-15", 55, "Male",
                "Taguig City",
                "09184561234",
                "Surgery",
                new LinkedList<>(Arrays.asList(
                        "Tuesday-Afternoon",
                        "Thursday-Morning",
                        "Friday-Evening")),
                "2025/11/15 09:25:00"));

        doctorRecords.add(new DoctorRecord(
                "DOC-" + ++idNumber,
                "Brandon", "Chua",
                "1986-01-30", 39, "Male",
                "Las Piñas City",
                "09174561289",
                "Orthopedics",
                new LinkedList<>(Arrays.asList(
                        "Monday-Morning",
                        "Thursday-Morning")),
                "2025/11/15 09:30:00"));

    }
}