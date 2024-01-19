import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

class LogAnalyzer{
    public static void main(String[] args) {
        try {
            FileReader objr=new FileReader("TestLog.txt");
            Scanner read = new Scanner (objr);
            while(read.hasNextLine()){
                System.out.println(read.nextLine());                
            }
            // System.out.println("Reading Done.. :)");
            read.close();
        }
        catch (FileNotFoundException e){
            System.out.println("I/O Issues. Something Went Wrong :(");
        }
    }
}