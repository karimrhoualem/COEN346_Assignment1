import multi_threading.DefectiveThread;

import java.io.IOException;

import static administrator.FileReader.GetBulbArray;

public class Program {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args[0] == null) {
            System.out.println("Missing file specified at command line. Program terminating.");
            return;
        }

        int[] bulbArray = GetBulbArray(args[0]);

        if(bulbArray == null) {
            System.out.println("Error generating bulb array. Program terminating.");
            return;
        }

        DefectiveThread defectiveThread = new DefectiveThread(bulbArray, "Root Thread (#1)", 0, bulbArray.length);
        defectiveThread.start();

        //TODO: CHANGE THIS FOR SOMETHING MORE DYNAMIC (WAITS FOR ALL THREADS TO TERMINATE)
        Thread.sleep(1000);
//        defectiveThread.join();
        defectiveThread.get_totalNumOfThreads();
        defectiveThread.get_defectiveBulbs();
    }
}
