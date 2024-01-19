import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.midi.Soundbank;

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
        object.LogParse(logList);
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
        object.countOccurence(logList);
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
        object.countOccurence(logList);

        System.out.println();
    }
}