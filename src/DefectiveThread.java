import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The DefectiveThread class is used as the main entry point into the recursive multi-threading
 * program that Program.java runs. DefectiveThread is an extension of the Thread class. It overrides
 * Thread.Run() to call FindDefective(int[] bulbArray). Doing so triggers a recursive call to
 * FindDefective(int[] bulbArray). The bulbArray is recursively divided into two sub-arrays,
 * and a new thread handles each sub-array until the sub-array cannot be divided further.
 *
 */
public class DefectiveThread extends Thread {
    /**
     * Non-static variables that object-specific and don't require synchronization.
     */
    private int _startIndex;
    private int _endIndex;
    private int[] _bulbArray;
    private String _threadName;

    /**
     * Static variables that are class-specific and require synchronization.
     */
    private static int _numOfDefectiveBulbs = 0;
    private static int _numOfThreads = 1;
    private static List<Integer> _defectiveBulbs = new ArrayList<Integer>();

    /**
     * Locks used to synchronize their representative variables between threads.
     */
    private static final Object _numOfDefectiveBulbsCountLock = new Object();
    private static final Object _numOfThreadsCountLock = new Object();
    private static final Object _defectiveBulbsAddLock = new Object();

    /**
     * DefectiveThread constructor takes a bulbArray, threadName, startIndex, and endIndex as parameters.
     * Constructor will be called recursively through FindDefective() method and print
     * a message to console with the new thread number.
     * @param bulbArray The bulbArray that is recursively inspected.
     * @param threadName Iterative name given to each new thread created by the DefectiveThread method.
     * @param startIndex Starting index used to keep track of the bulb position in the original bulbArray.
     * @param endIndex Ending index used to keep track of the bulb position in the original bulbArray.
     */
    public DefectiveThread(int[] bulbArray, String threadName, int startIndex, int endIndex) {
        _bulbArray = bulbArray;
        _threadName = threadName;
        _startIndex = startIndex;
        _endIndex = endIndex;
        System.out.println("Creating Thread - " + _threadName);
    }

    /**
     * Override of Thread.run() method that will be called through DefectiveThread.Start().
     */
    @Override
    public void run() {
        try {
            FindDefective(_bulbArray);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * FindDefective is called in the main thread and recursively, indirectly by itself. With each iteration,
     * FindDefective creates a new DefectiveThread object. When it starts() the object, it makes a call to the
     * run() override on the thread object, which in turn recursively calls FindDefective on the subset of the
     * original array.
     * @param bulbArray Subset of the original array that is recursively passed as a parameter.
     * @throws InterruptedException
     */
    private void FindDefective(int[] bulbArray) throws InterruptedException {
        int length = bulbArray.length;

        /**
         * As a method of extra precaution, check to see if the bulbArray ever reaches a negative index.
         * If it does, return and do not continue recursive process.
         */
        if (length <= 0) {
            return;
        }

        /**
         * Check if the sub-array has at least one bulb set to 0. If it does, continue sub-array division. If it doesn't, return.
         */
        for (int i = 0; i < length; i++) {
            if (bulbArray[i] == 0) {
                break;
            }
            if (i == length-1) {
                return;
            }
        }

        /**
         * Base case: if length of sub-array is reduced to 1, check if bulb has value of 0 or 1 and then return
         */
        if (length == 1) {
            /**
             * If the bulb has a value of zero, add its position from the original array to the defectives array.
             */
            if (bulbArray[0] == 0) {
                /**
                 * Lock the _numOfDefectiveBulbs variable in order to synchronize its value between threads.
                 */
                synchronized (_numOfDefectiveBulbsCountLock) {
                    _numOfDefectiveBulbs++;
                }

                /**
                 * When a defective bulb has been found, lock the push to the ArrayList in order to avoid
                 * two bulbs potentially overwriting each others values.
                 */
                synchronized (_defectiveBulbsAddLock) {
                    _defectiveBulbs.add(_startIndex+1);
                }
            }
            return;
        }

        /**
         * Consistently select a pivot point in the middle of the sub-array.
         */
        int pivot = (int) Math.ceil(((double) length) / 2);

        /**
         * Split the array into two sub arrays.
         */
        int[] leftArray = Arrays.copyOfRange(bulbArray,0, pivot);
        int[] rightArray = Arrays.copyOfRange(bulbArray,pivot, length);

        /**
         * Create a new DefectiveThread object for each sub-array.
         * Lock the _numOfThreads variables prior to each new object DefectiveThread object creation
         * in order to synchronize the count of the total number of threads between the active threads.
         */
        synchronized (_numOfThreadsCountLock) {
            _numOfThreads++;
        }
        DefectiveThread leftArrayThread = new DefectiveThread(leftArray, "#" + _numOfThreads, _startIndex, _startIndex+pivot-1);

        synchronized (_numOfThreadsCountLock) {
            _numOfThreads++;
        }
        DefectiveThread rightArrayThread = new DefectiveThread(rightArray, "#" + _numOfThreads, _startIndex+pivot, _endIndex);

        /**
         * Call start(), which in turn calls the thread's run() method.
         * This will recursively call FindDefective() on each half sub-array.
         * Then call join() on each sub-array. This will wait for the new thread to return before continuing
         * to the next line.
         */
        leftArrayThread.start();
        rightArrayThread.start();
        rightArrayThread.join();
        leftArrayThread.join();

    }

    /**
     * Get method that displays the final thread count when the program is done executing.
     */
    public void get_totalNumOfThreads() {
        System.out.println();
        System.out.println("Total number of threads used: " + _numOfThreads);
    }

    /**
     * Get method that displays the total number of defective bulbs found in the original array,
     * as well as the original index of each defective bulb.
     */
    public void get_defectiveBulbs() {
        System.out.print("DefectiveThread bulbs are: ");
        for (int i = 0; i < _defectiveBulbs.size(); i++) {
            if (i == _defectiveBulbs.size() - 1) {
                System.out.print(_defectiveBulbs.get(i) + "\n");
            }
            else {
                System.out.print(_defectiveBulbs.get(i) + ", ");
            }
        }
        System.out.println("Total # of defective bulbs: " + _numOfDefectiveBulbs);
    }
}
