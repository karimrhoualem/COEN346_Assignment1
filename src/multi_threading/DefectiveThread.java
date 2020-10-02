package multi_threading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefectiveThread extends Thread {
    private int _startIndex;
    private int _endIndex;
    private int[] _bulbArray;
    private String _threadName;

    private static int _numOfDefectiveBulbs = 0;
    private static int _numOfThreads = 1;
    private static List<Integer> _defectiveBulbs = new ArrayList<Integer>();

    private static final Object _numOfDefectiveBulbsCountLock = new Object();
    private static final Object _numOfThreadsCountLock = new Object();
    private static final Object _defectiveBulbsAddLock = new Object();

    public DefectiveThread(int[] bulbArray, String threadName, int startIndex, int endIndex) {
        _bulbArray = bulbArray;
        _threadName = threadName;
        _startIndex = startIndex;
        _endIndex = endIndex;
        System.out.println("Creating Thread - " + _threadName + " " + _bulbArray.length); //TODO: remove _bulbArray.length
    }

    @Override
    public void run() {
        try {
            FindDefective(_bulbArray);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void FindDefective(int[] bulbArray) throws InterruptedException {
        int length = bulbArray.length;

        if (length <= 0) {
            return;
        }

        //Check if the sub-array has at least one bulb set to 0. If it does, continue sub-array division. If it doesn't, return.
        for (int i = 0; i < length; i++) {
            if (bulbArray[i] == 0) {
                break;
            }
            if (i == length-1) {
                return;
            }
        }

        //Base case: if length of sub-array is reduced to 1, check if bulb has value of 0 or 1 and then return
        if (length == 1) {
            //If the bulb has a value of zero, add its position from the original array to the defectives array.
            if (bulbArray[0] == 0) {
                synchronized (_numOfDefectiveBulbsCountLock) {
                    _numOfDefectiveBulbs++;
                }
                synchronized (_defectiveBulbsAddLock) {
                    _defectiveBulbs.add(_startIndex+1);
                }
            }
            return;
        }

        //Select a pivot point in the middle of the array
        int pivot = (int) Math.ceil(((double) length) / 2);

        //Split the array into two sub arrays
        int[] leftArray = Arrays.copyOfRange(bulbArray,0, pivot);
        int[] rightArray = Arrays.copyOfRange(bulbArray,pivot, length);

        //Recursively call FindDefective() on each sub-array.
        synchronized (_numOfThreadsCountLock) {
            _numOfThreads++;
        }
        DefectiveThread leftArrayThread = new DefectiveThread(leftArray, "#" + _numOfThreads, _startIndex, _startIndex+pivot-1);
        leftArrayThread.start();
//        leftArrayThread.join();

        synchronized (_numOfThreadsCountLock) {
            _numOfThreads++;
        }
        DefectiveThread rightArrayThread = new DefectiveThread(rightArray, "#" + _numOfThreads, _startIndex+pivot, _endIndex);
        rightArrayThread.start();
//        rightArrayThread.join();
    }

    public void get_totalNumOfThreads() {
        System.out.println();
        System.out.println("Total number of threads used: " + _numOfThreads);
    }

    public void get_defectiveBulbs() {
        System.out.print("multi_threading.DefectiveThread bulbs are: ");
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
