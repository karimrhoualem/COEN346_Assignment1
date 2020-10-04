/**
 * @author Karim Rhoualem
 * @course COEN 346 - Programming Assignment #1
 * @version 1.0.0.0
 * @since 2020-10-03
 */
import java.io.IOException;

/**
 * Main command-line entry point that fetches the initial bulb array and finds the defective bulbs by using
 * a multi-threaded, recursive approach.
 */
public class Program {
    public static void main(String[] args) throws IOException, InterruptedException {
        /**
         * Verifies whether a .txt file was specified as an argument at the command line.
         */
        if (args[0] == null) {
            System.out.println("Missing file specified at command line. Program terminating.");
            return;
        }

        /**
         * Calls the GetBulbArray method from the FileReader class and loads the bulbArray
         * with the inputs read from the .txt file specified as an argument at the command line.
         */
        int[] bulbArray = FileReader.GetBulbArray(args[0]);

        /**
         * Verifies whether the bulbArray is empty from its call to GetBulbArray.
         */
        if(bulbArray == null) {
            System.out.println("Error generating bulb array. Program terminating.");
            return;
        }

        /**
         * Starts the first recursive thread that searches for the defective bulbs in the bulbArray.
         */
        DefectiveThread defectiveThread = new DefectiveThread(bulbArray, "Root Thread (#1)", 0, bulbArray.length);
        defectiveThread.start();

        /**
         * Puts the main thread temporarily to sleep to let the dynamically created threads complete their operations.
         */
        //TODO: CHANGE THIS FOR SOMETHING MORE DYNAMIC (WAITS FOR ALL THREADS TO TERMINATE)
        Thread.sleep(1000);
//        defectiveThread.join();

        /**
         * Displays the total number of defective bulbs and the indices of the defectives from the original array.
         */
        defectiveThread.get_totalNumOfThreads();
        defectiveThread.get_defectiveBulbs();
    }
}
