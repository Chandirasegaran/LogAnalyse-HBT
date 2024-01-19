import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class LogAnalyze {
    static List<String> overuniqueusers = new ArrayList<>();

    void LogParse(List<String> logs) {
        System.out.println("Log Parse:");
        System.out.println("-----------------------------------------------------------");

        for (String log : logs) {
            String[] parts = log.split(" ");
            if (parts.length > 3) {
                String logLevel = parts[2];
                int startingOfMessage = (log.indexOf(logLevel) + logLevel.length());
                String message = log.substring(startingOfMessage).trim();
                System.out.println("Date: " + parts[0]);
                System.out.println("Time: " + parts[1]);
                System.out.println("Log Level: " + logLevel.substring(0, logLevel.length() - 1));
                System.out.println("Message: " + message);
                System.out.println();
            }
        }
    }

    void countOccurence(List<String> logs) {

        System.out.println("Count the occurrence of unique error messages in the log: ");
        System.out.println("----------------------------------------------------------");

        List<String> errMsg = new ArrayList<>();
        for (String log : logs) {
            String[] parts = log.split(" ");
            if (parts.length > 3) {

                String logLevel = parts[2];
                String logMess = logLevel.substring(0, logLevel.length() - 1);
                int startingOfMessage = (log.indexOf(logLevel) + logLevel.length());
                String message = log.substring(startingOfMessage).trim();
                String mess = message.substring(0, message.length() - 1);
                if (logMess.equals("ERROR")) {

                    errMsg.add(mess);
                }

            }
        }

        List<String> uniqueItems = new ArrayList<>();

        for (String item : errMsg) {
            if (!uniqueItems.contains(item)) {
                uniqueItems.add(item);
                int count = 1;
                for (int j = errMsg.indexOf(item) + 1; j < errMsg.size(); j++) {
                    if (item.equals(errMsg.get(j))) {
                        count++;
                    }
                }
                System.out.println(item + " : Error Occurs " + count + " times");
            }
        }
    }

    void Activitiy(List<String> logs) {
        System.out.println("Activity of Users:");
        List<String> uniqueusers = new ArrayList<>();
        List<String> users = new ArrayList<>();
        String[][] userdata = new String[logs.size()][3];
        String[][] userActCount = new String[logs.size()][2];
        int iter = 0;
        for (String log : logs) {
            String[] parts = log.split(" ");
            if (parts.length > 3) {
                String logLevel = parts[2];
                String logMess = logLevel.substring(0, logLevel.length() - 1);
                String datetime = (parts[0] + " " + parts[1]);
                String user = parts[4];
                int startingOfMessage = (log.indexOf(user) + user.length());
                String message = log.substring(startingOfMessage).trim();
                if (logMess.equals("INFO")) {
                    users.add((parts[4]));
                    userdata[iter][0] = datetime;
                    userdata[iter][1] = user;
                    userdata[iter][2] = message;
                    iter++;
                }

            }
        }

        List<Integer> UserActivityCount = new ArrayList<>();
        for (String item : users) {
            if (!uniqueusers.contains(item)) {
                uniqueusers.add(item);
                overuniqueusers.add(item);
                System.out.println("-----------------------------------------------------");
                System.out.println();
                System.out.println("User Name: " + item.replace("'", ""));
                System.out.println();
                int count = 0, i = 0, j = 0;
                for (String log : logs) {
                    if (userdata[i][1] != null) {
                        String ouruser = userdata[i][1];
                        String uuser = item;
                        if (ouruser.equals(uuser)) {
                            System.out.println("Time Stamp: " + userdata[i][0] + " | " + " Action:" + userdata[i][2]);
                            count++;
                        }
                        ;
                        i++;
                    }
                }
                UserActivityCount.add(count);
                userActCount[j][0] = item;
                userActCount[j][1] = String.valueOf(count);
            }
        }
        int mostActiveIndex = 0;
        int leastActiveIndex = 0;
        for (int i = 1; i < UserActivityCount.size(); i++) {
            if (UserActivityCount.get(i) > UserActivityCount.get(mostActiveIndex)) {
                mostActiveIndex = i;
            }
            if (UserActivityCount.get(i) < UserActivityCount.get(leastActiveIndex)) {
                leastActiveIndex = i;
            }
        }
        System.out.println("Most Active User: " + users.get(mostActiveIndex).replace("'", ""));
        System.out.println("Number of Activities: " + UserActivityCount.get(mostActiveIndex));
        System.out.println();
        System.out.println("Least Active User: " + users.get(leastActiveIndex).replace("'", ""));
        System.out.println("Number of Activities: " + UserActivityCount.get(leastActiveIndex));
    }

    void TimePattern(List<String> logs) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        String[][] userdata = new String[overuniqueusers.size()][2];
        System.out.println("Time Pattern of Users:");
        System.out.println();

        for (String user : overuniqueusers) {
            long usertotaltime = 0L;
            LocalTime loginTime = null;
            LocalTime logoutTime = null;
            boolean loggedin = false;

            for (String log : logs) {
                String[] parts = log.split(" ");
                if (parts.length > 3) {
                    String logMess = parts[2].substring(0, parts[2].length() - 1);
                    if (logMess.equals("INFO") && log.contains(user)) {
                        String time = parts[1];
                        int startingOfMessage = (log.indexOf(user) + user.length());
                        // System.out.println(log);
                        String message = log.substring(startingOfMessage).trim();

                        if (loggedin == false) {
                            if (message.contains("logged in")) {
                                try {
                                    loginTime = LocalTime.parse(time, timeFormat);
                                    loggedin = true;
                                } catch (Exception e) {
                                    System.out.println("Can't Parse Date");
                                }
                            } else if (message.contains("logged out")) {
                                System.out.println("User " + user + " is Loggin Out without Login.");
                            } else {
                                System.out.println("User " + user + " is performing Action without Login.");
                            }
                        } else if (loggedin == true) {
                            if (message.contains("logged out")) {
                                try {
                                    logoutTime = LocalTime.parse(time, timeFormat);
                                    loggedin = false;

                                    Duration duration = Duration.between(loginTime, logoutTime);
                                    usertotaltime += duration.toMinutes();
                                } catch (Exception e) {
                                    System.out.println("Can't Parse Date");
                                }
                            }
                        }

                    }
                }
            }
            userdata[overuniqueusers.indexOf(user)][0] = user;
            userdata[overuniqueusers.indexOf(user)][1] = String.valueOf(usertotaltime);
        }

        System.out.println("User Time Pattern: ");
        for (String[] userTime : userdata) {
            System.out.println("User: " + userTime[0] + " Logged in for " + userTime[1] + " minutes");
        }


        String TotalCount[] = new String[logs.size()];
        int totalCountIndex = 0; // Track valid indices for TotalCount

        for (int iter = 1; iter < logs.size(); iter++) {
            String log = logs.get(iter);
            String[] parts = log.split(" ");
            if (parts.length > 3) {
                String logLevel = parts[2];
                String logMess = logLevel.substring(0, logLevel.length() - 1);
                if (logMess.equals("ERROR") || logMess.equals("WARN")) {
                    String logg = logs.get(iter - 1);
                    String[] part = logg.split(" ");
                    if (part.length > 3) {
                        String llevel = part[2];
                        String lMess = llevel.substring(0, llevel.length() - 1);
                        String user = part[4];
                        if (lMess.equals("INFO")) {
                            TotalCount[totalCountIndex++] = user;
                        }
                    }
                }
            }
        }

        int maxError = 0;
        String maxErrorUser = null;
 
        for (int i = 0; i < TotalCount.length; i++) {
            String current = TotalCount[i];
            int currentCount = 1;

            // Count occurrences of the current element
            for (int j = i + 1; j < TotalCount.length; j++) {
                if (TotalCount[j] != null && current.equals(TotalCount[j])) {
                    currentCount++;
                }
            }

            // Update maxRepeatedValue if needed
            if (currentCount > maxError) {
                maxError = currentCount;
                maxErrorUser = current;
            }
        }

        // Print the result
        if (maxErrorUser != null) {
            System.out.println();
            System.out.println("Maximum Error and Warning are made by: " + maxErrorUser);
            System.out.println("Count: " + maxError);
            System.out.println();
        } else {
            System.out.println("No Error made by any user.");
        }

    }

}

class LogAnalyzer {
    public static void main(String[] args) {
        List<String> logList = new ArrayList<>();

        try {
            FileReader objr = new FileReader("TestLog.txt");
            Scanner read = new Scanner(objr);
            while (read.hasNextLine()) {
                logList.add(read.nextLine());
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.out.println("I/O Issues. Something Went Wrong :(");
        }

        LogAnalyze object = new LogAnalyze();
        System.out.println();
        // object.LogParse(logList);
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
        // object.countOccurence(logList);
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
        object.Activitiy(logList);
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
        object.TimePattern(logList);

    }
}