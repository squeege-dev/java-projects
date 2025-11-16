import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PatientManagement {
    static Random generate = new Random();
    static LinkedList<PatientRecord> patientRecords = new LinkedList<>();
    static int idNumber = 1000;

    /*
     * ANSI escape codes for colors
     * \u001B[31m = Red
     * \u001B[32m = Green
     * \u001B[33m = Yellow
     * \u001B[34m = Blue
     * \u001B[35m = Purple
     * \u001B[36m = Cyan
     * \u001B[37m = White/Gray
     * \u001B[0m = Reset
     */

    public static void patientManagementMenu() {
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================");
        System.out.println("|==========\u001B[37m" + "Patient Management Menu" + "\u001B[34m=========|");
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================\u001B[0m");
        System.out.println("\u001B[34m1. \u001B[0mRegister New Patient");
        System.out.println("\u001B[34m2. \u001B[0mView Patients");
        System.out.println("\u001B[34m3. \u001B[0mReturn to Main Menu");
        System.out.println("\u001B[37m-------------------------------------------\u001B[0m");
        return;
    }

    public static void registerPatient() {
        LinkedList<String> allergies = new LinkedList<>();
        LinkedList<String> medications = new LinkedList<>();
        LinkedList<String> medicalHistory = new LinkedList<>();

        boolean proceed = false;
        String dob = "", fullName = "";
        int age = 0;
        String sex = "";
        String bloodType = "";
        String phoneNumber = "";

        System.out.println(
                "<<\u001B[34m=======================\u001B[33mRegistration Form for Patients\u001B[34m======================\u001B[0m>>");
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
            for (PatientRecord pr : patientRecords) {
                if (tempFullName.equalsIgnoreCase(pr.getFullName())) {
                    newName = false;
                    break;
                }
            }
            if (newName) {
                fullName = fName + " " + lName;
                break;
            } else {
                System.out.println("\u001B[33m>> Name already Registered. Try Again.");
            }
        }

        // get date of birth & age
        while (true) {
            dob = ValidateInputs.checkInput("\u001B[33m>>\u001B[0m Enter Date of Birth (YYYY-MM-DD): ");
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

        // get sex
        while (true) {
            sex = ValidateInputs.checkInput("\u001B[33m>>\u001B[0m Enter assigned Sex (Male/Female): ").toLowerCase();
            if (sex.equals("0")) {
                System.out.println("\u001B[31m>> Registration Cancelled. \u001B[0m");
                return;
            }
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

        // get blood type
        while (true) {
            bloodType = ValidateInputs.checkInput("\u001B[33m>>\u001B[0m Enter Blood Type: ");
            if (bloodType.equals("0")) {
                System.out.println("\u001B[31m>> Registration Cancelled. \u001B[0m");
                return;
            }
            boolean validBL = false;

            String[] bloodTypes = { "A", "A-", "B", "B-", "O", "O-", "AB", "AB-" };
            for (int i = 0; i < bloodTypes.length; i++) {
                if (bloodType.equals(bloodTypes[i])) {
                    validBL = true;
                }
            }

            if (validBL) {
                break;
            } else {
                System.out.println("---->>! BloodType does not exist. Try Again.");
            }
        }
        String address = ValidateInputs.checkInput("\u001B[33m>>\u001B[0m  Enter Address (Province): ");
        if (address.equals("0")) {
            System.out.println("\u001B[31m>> Registration Cancelled. \u001B[0m");
            return;
        }

        // get phone number
        while (true) {
            System.out.print("\u001B[33m>>\u001B[0m Enter Phone Number: ");
            phoneNumber = Main.in.nextLine();
            if (phoneNumber.equals("0")) {
                System.out.println("\u001B[31m>> Registration Cancelled. \u001B[0m");
                return;
            }

            if (phoneNumber.isEmpty()) {
                System.out.println("\u001B[31m---->>! Phone Number cannot be empty. Try Again.\u001B[0m");
            } else if (phoneNumber.length() != 11) {
                System.out.println("\u001B[31m---->>! Invalid phone number. Must be 11 digits (e.g., 09171234567). \u001B[0m");
            } else {
                boolean notDigitFound = false;

                for (int i = 0; i < phoneNumber.length(); i++) {
                    if (!Character.isDigit(phoneNumber.charAt(i))) {
                        notDigitFound = true;
                        break;
                    }
                }
                if (notDigitFound) {
                    System.out.println("\u001B[31m---->>! Phone Number must only contain numbers (0-9).\u001B[0m");
                } else {
                    break;
                }
            }
        }
        
        // get allergies
        while (true) {
            String yesOrNo = ValidateInputs
                    .checkInput("\u001B[33m>>\u001B[0m Does the patient have any Allergies? (Yes/No): ").toLowerCase();
            if (yesOrNo.equals("yes")) {
                System.out.println("<<------------Please provide the allergies below------------>>");
                System.out.println("            Type and Enter \"Done\" when finished.");
                int pos = 1;
                while (true) {
                    String provideAllergies = ValidateInputs.checkInput(pos++ + ": ").toLowerCase();
                    if (provideAllergies.equals("done")) {
                        System.out.println("<<------------------------------>>");
                        break;
                    } else {
                        allergies.add(provideAllergies);
                    }
                }
            } else if (yesOrNo.equals("no")) {
                allergies.add("N/A");
                break;
            } else {
                System.out.println("\u001B[31m---->>! Invalid Input. Try Again.\u001B[0m");
            }
        }

        // get medications
        while (true) {
            String yesOrNo = ValidateInputs
                    .checkInput("\u001B[33m>>\u001B[0m Does the patient have any current Medications? (Yes/No): ")
                    .toLowerCase();
            if (yesOrNo.equals("yes")) {
                System.out.println("<<------------Please provide the medications below------------>>");
                System.out.println("            Type and Enter \"Done\" when finished.");
                int pos = 1;
                while (true) {
                    String provideMed = ValidateInputs.checkInput(pos++ + ": ").toLowerCase();
                    if (provideMed.equals("done")) {
                        System.out.println("<<------------------------------>>");
                        break;
                    } else {
                        medications.add(provideMed);
                    }
                }
                break;
            } else if (yesOrNo.equals("no")) {
                medications.add("N/A");
                break;
            } else {
                System.out.println("\u001B[31m---->>! Invalid Input. Try Again.\u001B[0m");
            }
        }

        // get medical history
        while (true) {
            String yesOrNo = ValidateInputs
                    .checkInput("\u001B[33m>>\u001B[0m Does the patient have any medical history? (Yes/No): ")
                    .toLowerCase();
            if (yesOrNo.equals("yes")) {
                while (true) {

                    String illness = ValidateInputs.checkInput(">> Enter Illness/Condition: ");

                    String treatment = ValidateInputs.checkInput(">> Enter Treatment or Medication Given: ");

                    System.out.print(">> Enter Date of History (YYYY-MM-DD): ");
                    String date = Main.in.nextLine();

                    String record = String.format("Illness: %s | Treatment: %s | Date: %s", illness, treatment, date);
                    medicalHistory.add(record);

                    System.out.print(">> Add another medical history? (Yes/No): ");
                    String more = Main.in.nextLine().toLowerCase();
                    if (more.equals("no"))
                        break;
                }
                break;

            } else if (yesOrNo.equals("no")) {
                medicalHistory.add("N/A");
                break;

            } else {
                System.out.println("---->>! Invalid Input. Try Again.");
            }
        }

        String patientID = "PAT-" + ++idNumber;

        System.out.print("Processing");
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.print(".");
        }

        System.out.println("\n \u001B[32mPatient Registered Successfully! \u001B[35m >>>\u001B[0m Assigned Patient ID: "
                + patientID);
        PatientRecord newPatient = new PatientRecord(patientID, fullName, dob, age, sex, bloodType, address,
                phoneNumber, allergies, medications, medicalHistory, dateRegistered);
        patientRecords.add(newPatient);
    }

    public static void patientLists() {
        int pageSize = 5;
        int start = 0;
        boolean viewing = true;
        int pages = (patientRecords.size() % pageSize == 0) ? (patientRecords.size() / pageSize)
                : (patientRecords.size() / pageSize) + 1;

        int page = 1;
        while (viewing) {

            int end = start + pageSize;
            if (end > patientRecords.size()) {
                end = patientRecords.size();
            }
            System.out.println(
                    "\u001B[34m<<------------------------------------------------------------------------------------------->>");
            System.out.printf("%67s%n", "\u001B[33mViewing Patient List(s)\u001B[0m");
            System.out.printf("%58s%n ", "Displaying Page " + page + " of " + pages + ".");

            for (int i = start; i < end; i++) {
                patientRecords.get(i).displayPatient(i + 1);
            }
            System.out.println(
                    "<<------------------------------------------------------------------------------------------->>");
            System.out.println(
                    ">> Options: [1] Next, [2] Previous, [3] Select/Find a Patient, [4] Exit.");
            System.out.print(">> Please Select between [ 1 - 4]: ");
            int choice = Main.in.nextInt();
            Main.in.nextLine();
            switch (choice) {
                case 1:
                    if (end < patientRecords.size()) {
                        if (page < pages) {
                            page++;
                        }
                        start += pageSize;

                    } else {
                        System.out.println("\u001B[31m---->>! You are already at the end of the list.\u001B[0m");
                    }
                    break;
                case 2:
                    if (start >= pageSize) {
                        if (page > 1) {
                            page--;
                        }
                        start -= pageSize;
                    } else {
                        System.out.println("\u001B[31m---->>! You are already at the beginning of the list.\u001B[0m");
                    }
                    break;
                case 3:
                    while (true) {
                        Main.in.nextLine();
                        String searchPatient = ValidateInputs
                                .checkInput(">> Enter Patient Name or ID to Select/Search: ");
                        boolean isPatientID = searchPatient.length() >= 4
                                && searchPatient.substring(0, 4).equalsIgnoreCase("PAT-");

                        if (isPatientID) {
                            Collections.sort(patientRecords, Comparator.comparing(PatientRecord::getPatientID));
                            long startTime = System.nanoTime();
                            int low = 0;
                            int high = patientRecords.size() - 1;
                            boolean found = false;
                            int index = 0;
                            while (low <= high) {
                                int mid = (low + high) / 2;
                                String midID = patientRecords.get(mid).getPatientID();

                                int compare = midID.compareToIgnoreCase(searchPatient);

                                if (compare == 0) {
                                    patientRecords.get(mid).displayPatientDetails();
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
                                        .println(">> Options: [1] Update Details, [2] View Medical History, [3] Exit.");
                                int options = ValidateInputs
                                        .checkIntInput(">> What would you like to do next (choose between [1 - 3]): ");
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
                                                                    + patientRecords.get(index).getPhoneNum());
                                                    String newPhoneNum = ValidateInputs
                                                            .checkInput(">> Enter new Phone Number: ");

                                                    if (ValidateInputs.phoneNumValidation(newPhoneNum)) {
                                                        patientRecords.get(index).setPhoneNum(newPhoneNum);
                                                        System.out.println(
                                                                "\u001B[32m>> Phone Number Updated Successfully.\u001B[0m");
                                                        break;
                                                    } else {
                                                        System.out
                                                                .println(
                                                                        "\u001B[31m>> Invalid Phone Number. Try Again.\u001B[0m");
                                                    }
                                                }
                                                patientRecords.get(index).updateModified();
                                                break;

                                            case 2:
                                                System.out.println("---->>> Updating Address <<<---");
                                                System.out.println(">> Current Address: "
                                                        + patientRecords.get(index).getAddress());
                                                String newAddress = ValidateInputs
                                                        .checkInput(">> Enter new Address (Province): ");

                                                patientRecords.get(index).setAddress(newAddress);
                                                System.out
                                                        .println("\u001B[32m>> Address Updated Successfully.\u001B[0m");
                                                patientRecords.get(index).updateModified();
                                                break;

                                            case 3:
                                                System.out.println("---->>> Updating Status <<<---");
                                                boolean currentStatus = patientRecords.get(index).getActiveStatus();
                                                System.out.println(">> Current Status: "
                                                        + (currentStatus ? "Active" : "Inactive"));

                                                int statusChoice = ValidateInputs
                                                        .checkIntInput(">> Choose [1] Set Active | [2] Set Inactive: ");

                                                if (statusChoice == 1 && !currentStatus) {
                                                    patientRecords.get(index).setActiveStatus(true);
                                                    System.out.println("\u001B[32m>> Patient is now ACTIVE.\u001B[0m");
                                                } else if (statusChoice == 2 && currentStatus) {
                                                    patientRecords.get(index).setActiveStatus(false);
                                                    System.out
                                                            .println("\u001B[33m>> Patient is now INACTIVE.\u001B[0m");
                                                } else {
                                                    System.out.println("\u001B[31m>> No change made.\u001B[0m");
                                                }

                                                patientRecords.get(index).updateModified();
                                                break;

                                            default:
                                                System.out.println("Invalid choice. Returning to options.");
                                        }
                                        break;
                                    case 2:
                                        System.out.println("\n\u001B[33m>> Viewing Medical & Treatment History for: "
                                                + patientRecords.get(index).getFullName() + " ("
                                                + patientRecords.get(index).getPatientID() + ")\u001B[0m\n");
                                        patientRecords.get(index).displayMedicalHistory();

                                        System.out.println("\n>> Options: [1] Add Treatment Record, [2] Back");
                                        int histOpt = ValidateInputs.checkIntInput(">> Choose [1-2]: ");

                                        if (histOpt == 1) {
                                            String entry = "";
                                            while (true) {
                                                entry = ValidateInputs.checkInput(
                                                        ">> Enter treatment (format: DIAGNOSIS - MEDICATION/NOTE): ");

                                                if (!entry.contains("-")) {
                                                    System.out.println(
                                                            "\u001B[31m>> Invalid format. You MUST include '-' between fields.\u001B[0m");
                                                    continue;
                                                }
                                                String[] parts = entry.split("-", 2);
                                                if (parts.length < 2 || parts[0].trim().isEmpty()
                                                        || parts[1].trim().isEmpty()) {
                                                    System.out.println(
                                                            "\u001B[31m>> Invalid format. Both sides cannot be empty.\u001B[0m");
                                                    continue;
                                                }
                                                break;
                                            }
                                            patientRecords.get(index).addtoHistory(entry);
                                            System.out.println("\u001B[32m>> Treatment record added.\u001B[0m");
                                        }

                                        break;
                                    case 3:
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
                            for (PatientRecord pr : patientRecords) {
                                if (pr.getFullName().equalsIgnoreCase(searchPatient)) {
                                    pr.displayPatientDetails();
                                    found = true;
                                    index = patientRecords.indexOf(pr);
                                    break;
                                }else if(pr.getFirstName().equalsIgnoreCase(searchPatient) || pr.getLastName().equalsIgnoreCase(searchPatient)){
                                    pr.displayPatientDetails();
                                    found = true;
                                    index = patientRecords.indexOf(pr);
                                    break;

                                }
                            }
                            long endTime = System.nanoTime();
                            double duration = (endTime - startTime) / 1_000_000.0;
                            System.out.println(
                                    "\u001B[32m>> (Linear Search) Search completed in " + duration + " ms.\u001B[0m");
                            if (found) {
                                System.out
                                        .println(">> Options: [1] Update Details, [2] View Medical History, [3] Exit.");
                                int options = ValidateInputs
                                        .checkIntInput(">> What would you like to do next (choose between [1 - 3]): ");
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
                                                                    + patientRecords.get(index).getPhoneNum());
                                                    String newPhoneNum = ValidateInputs
                                                            .checkInput(">> Enter new Phone Number: ");

                                                    if (ValidateInputs.phoneNumValidation(newPhoneNum)) {
                                                        patientRecords.get(index).setPhoneNum(newPhoneNum);
                                                        System.out.println(
                                                                "\u001B[32m>> Phone Number Updated Successfully.\u001B[0m");
                                                        break;
                                                    } else {
                                                        System.out
                                                                .println(
                                                                        "\u001B[31m>> Invalid Phone Number. Try Again.\u001B[0m");
                                                    }
                                                }
                                                patientRecords.get(index).updateModified();
                                                break;

                                            case 2:
                                                System.out.println("---->>> Updating Address <<<---");
                                                System.out.println(">> Current Address: "
                                                        + patientRecords.get(index).getAddress());
                                                String newAddress = ValidateInputs
                                                        .checkInput(">> Enter new Address (Province): ");

                                                patientRecords.get(index).setAddress(newAddress);
                                                System.out
                                                        .println("\u001B[32m>> Address Updated Successfully.\u001B[0m");
                                                patientRecords.get(index).updateModified();
                                                break;

                                            case 3:
                                                System.out.println("---->>> Updating Status <<<---");
                                                boolean currentStatus = patientRecords.get(index).getActiveStatus();
                                                System.out.println(">> Current Status: "
                                                        + (currentStatus ? "Active" : "Inactive"));

                                                int statusChoice = ValidateInputs
                                                        .checkIntInput(">> Choose [1] Set Active | [2] Set Inactive: ");

                                                if (statusChoice == 1 && !currentStatus) {
                                                    patientRecords.get(index).setActiveStatus(true);
                                                    System.out.println("\u001B[32m>> Patient is now ACTIVE.\u001B[0m");
                                                } else if (statusChoice == 2 && currentStatus) {
                                                    patientRecords.get(index).setActiveStatus(false);
                                                    System.out
                                                            .println("\u001B[33m>> Patient is now INACTIVE.\u001B[0m");
                                                } else {
                                                    System.out.println("\u001B[31m>> No change made.\u001B[0m");
                                                }

                                                patientRecords.get(index).updateModified();
                                                break;

                                            default:
                                                System.out.println("Invalid choice. Returning to options.");
                                        }
                                        break;
                                    case 2:
                                        System.out.println("\n\u001B[33m>> Viewing Medical & Treatment History for: "
                                                + patientRecords.get(index).getFullName() + " ("
                                                + patientRecords.get(index).getPatientID() + ")\u001B[0m\n");
                                        patientRecords.get(index).displayMedicalHistory();
                                        System.out.println("\n>> Options: [1] Add Treatment Record, [2] Back");
                                        int histOpt = ValidateInputs.checkIntInput(">> Choose [1-2]: ");
                                        if (histOpt == 1) {
                                            String entry = ValidateInputs
                                                    .checkInput(
                                                            ">> Enter treatment record (diagnosis/medication/note): ");
                                            patientRecords.get(index).addtoHistory(entry);
                                            System.out.println("\u001B[32m>> Treatment record added.\u001B[0m");
                                        }
                                        break;
                                    case 3:
                                        break;
                                    default:
                                }
                            } else {
                                System.out.println("\u001B[31m>> Patient not found.\u001B[0m");
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

    public static void preloadPatients() {
        patientRecords.add(new PatientRecord(
                "PAT-" + ++idNumber, "Juan Dela Cruz", "1999-04-10", 26, "M", "O+", "Manila", "09171234567",
                new LinkedList<>(Arrays.asList("Penicillin")),
                new LinkedList<>(Arrays.asList("Paracetamol")),
                new LinkedList<>(Arrays.asList("Illness: Asthma | Treatment: Salbutamol | Date: 2025-01-05")),
                "2025/10/24 15:47:42"));

        patientRecords.add(new PatientRecord(
                "PAT-" + ++idNumber, "Maria Santos", "2001-06-22", 24, "F", "A+", "Quezon City", "09183456789",
                new LinkedList<>(Arrays.asList("Seafood")),
                new LinkedList<>(Arrays.asList("Amoxicillin")),
                new LinkedList<>(Arrays.asList("Illness: Flu | Treatment: Paracetamol | Date: 2024-02-10",
                        "Illness: Hypertension | Treatment: Losartan | Date: 2025-11-01")),
                "2025/10/24 16:32:21"));

        patientRecords.add(new PatientRecord(
                "PAT-" + ++idNumber, "Carlo Mendoza", "2000-01-08", 25, "M", "B-", "Bulacan", "09098887766",
                new LinkedList<>(Arrays.asList("None")),
                new LinkedList<>(Arrays.asList("Ibuprofen")),
                new LinkedList<>(Arrays.asList("Illness: Allergy | Treatment: Antihistamine | Date: 2025-06-12")),
                "2025/10/24 15:34:50"));

        patientRecords.add(new PatientRecord(
                "PAT-" + ++idNumber, "Angela Lopez", "1997-07-03", 28, "F", "A-", "Bulacan", "09981231234",
                new LinkedList<>(Arrays.asList("Dust")),
                new LinkedList<>(Arrays.asList("Cetirizine")),
                new LinkedList<>(Arrays.asList("Illness: Migraines | Treatment: Ibuprofen | Date: 2025-05-20")),
                "2025/10/24 16:35:41"));

        patientRecords.add(new PatientRecord(
                "PAT-" + ++idNumber, "Paolo Garcia", "1993-08-18", 32, "M", "AB+", "Pampanga", "09987654321",
                new LinkedList<>(Arrays.asList("Peanuts")),
                new LinkedList<>(Arrays.asList("Losartan", "Vitamin D")),
                new LinkedList<>(Arrays.asList("Illness: Diabetes | Treatment: Insulin | Date: 2025-09-18")),
                "2025/10/24 17:13:26"));
    }

}