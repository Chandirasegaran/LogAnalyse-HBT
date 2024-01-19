import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class LogAnalyze {
    void LogParse(List<String> logs) {
        System.out.println("Log Parse:");
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
        for (int i=1;i<UserActivityCount.size();i++){
            if(UserActivityCount.get(i)>UserActivityCount.get(mostActiveIndex)){
                mostActiveIndex=i;
            }
            if(UserActivityCount.get(i)<UserActivityCount.get(leastActiveIndex)){
                leastActiveIndex=i;
            }
        }
        System.out.println("Most Active User: " + users.get(mostActiveIndex).replace("'", ""));
        System.out.println("Number of Activities: " + UserActivityCount.get(mostActiveIndex));
        System.out.println();
        System.out.println("Least Active User: " + users.get(leastActiveIndex).replace("'", ""));
        System.out.println("Number of Activities: " + UserActivityCount.get(leastActiveIndex));
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
    }
}