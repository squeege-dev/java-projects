import java.util.*;

public class Main {
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = adminLogin();
        PatientManagement.preloadPatients();
        DoctorManagement.preloadDoctors();
        while (running) {
            mainMenu();
            System.out.print("Select an option to proceed: ");
            String option = in.nextLine();
            switch (option) {
                case "1":
                    boolean ptMngtLoop = true;
                    while (ptMngtLoop) {
                        
                        PatientManagement.patientManagementMenu();
                        System.out.print("Select an option to proceed: ");
                        String pmOption = in.nextLine();
                        switch (pmOption) {
                            case "1":
                                PatientManagement.registerPatient();
                                break;
                            case "2":
                                PatientManagement.patientLists();
                                break;
                            case "3":
                                ptMngtLoop = false;
                                break;
                            default:
                                System.out.println("Invalid Option Selected.");
                        }
                    }
                    break;
                case "2":
                    while (true) {
                        DoctorManagement.doctorMangementMenu();
                        System.out.print("Select an option to proceed: ");
                        String dmOption = in.nextLine();
                        switch (dmOption) {
                            case "1":
                                DoctorManagement.addDoctor();
                                break;
                            case "2":
                                DoctorManagement.viewDoctorsList();
                                break;
                            case "3":
                                dmOption = "exit";
                                break;
                            default:
                                System.out.println("Invalid Option Selected.");
                        }
                        if (dmOption.equals("exit")) {
                            break;
                        }
                    }
                    break;
                case "3":
                    while (true) {
                        SchedulingManagement.schedulingManagementMenu();
                        System.out.print("Select an option to proceed: ");
                        String smOption = in.nextLine();
                        switch (smOption) {
                            case "1":
                                SchedulingManagement.addScheduling();
                                break;
                            case "2":
                                SchedulingManagement.viewQueues();
                                break;
                            case "3":
                                smOption = "exit";
                                break;
                            default:
                                System.out.println("Invalid Option Selected.");
                        }
                        if (smOption.equals("exit")) {
                            break;
                        }
                    }
                    break;
                case "4":
                    System.out.print(">> Exiting the System");
                    for (int i = 0; i < 3; i++) {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.print(".");
                    }
                    System.out.println(
                            "\n\u001B[34m>>! \u001B[0mThank you for Using the Hospital Management System. Goodbye!\u001B[34m!<< \u001B[0m");

                    running = false;
                    break;
                default:
                    System.out.println("Invalid Option Selected.");
                    break;
            }
        }
    }

    static void mainMenu() {
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================");
        System.out.println("|========\u001B[37m" + "Hospital Management System" + "\u001B[34m========|");
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================\u001B[0m");
        System.out.println("\u001B[34m1. \u001B[0mPatient Management.");
        System.out.println("\u001B[34m2. \u001B[0mDoctor Management.");
        System.out.println("\u001B[34m3. \u001B[0mAppointment Scheduling.");
        System.out.println("\u001B[34m4. \u001B[0mExit.");
        System.out.println("\u001B[34m-------------------------------------------\u001B[0m");
    }

    static boolean adminLogin() {
        boolean bool = false;

        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================");
        System.out.println("|========\u001B[37m" + "Hospital Management System" + "\u001B[34m========|");
        System.out.println("\u001B[34m=================\u001B[37m==========\u001B[34m=================\u001B[0m");
        System.out.println("    Welcome to Hospital Management System!");
        System.out.println("          Please login to continue.");

        while (true) {
            System.out.println("\u001B[34m-------------------------------------------\u001B[0m");
            System.out.print("\u001B[33m>> \u001B[0mEnter Admin Username: ");
            String username = in.nextLine();
            System.out.print("\u001B[33m>> \u001B[0mEnter Admin Password: ");
            String password = in.nextLine();

            if (!password.equals("admin123") && !username.equals("admin")) {
                System.out.println("\u001B[31m>>! Invalid Credentials. Access Denied. \u001B[0m");

            } else if (password.equals("admin123")) {
                if (username.equals("admin")) {
                    System.out.println("\u001B[32m>>! Login Successful! Welcome, Admin.\u001B[0m");
                    bool = true;
                    break;
                } else {
                    System.out.println("\u001B[31m>>! Invalid Username. Access Denied. \u001B[0m");
                }
            } else {
                System.out.println("\u001B[31m>>! Invalid Password. Access Denied. \u001B[0m");
            }
        }
        return bool;
    }
}