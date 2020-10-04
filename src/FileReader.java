import java.io.*;

/**
 * FileReader class that is used to read a .txt file from the command line.
 * From the .txt file, the first input is used to define the size of the output array,
 * and each subsequent line serves as an input to be stored in the array.
 */
public class FileReader {
    /**
     * Fills an array of bulbs with inputs of 0 or 1, indicating whether the bulbs
     * are off or on, respectively. The method uses the first input from the .txt file to
     * define the size of the returned array, and each subsequent line in the input file
     * specifies a state of a bulb. Each state is then inserted into the array.
     * @param arg The .txt file argument that is specified at the command line.
     * @return An array of integers with the state of each bulb from the input file.
     * @throws IOException
     */
    public static int[] GetBulbArray(String arg) throws IOException {
        int[] array;
        int sizeOfArray;
        int value;
        String line;
        char c;

        String srcFileName = null;
        FileInputStream fileInputStream = null;
        BufferedReader reader = null;

        try {
            /**
             * Create the file reader.
             */
            srcFileName = arg;
            fileInputStream = new FileInputStream(srcFileName);
            reader = new BufferedReader(new InputStreamReader(fileInputStream));

            /**
             * Get the first integer for the size of the array
             */
            line = reader.readLine();
            sizeOfArray = Integer.parseInt(line);
            System.out.println("Reading inputs from " + srcFileName);
            System.out.println("Size of array: " + sizeOfArray);

            /**
             * Ensure that the first integer of the array is a valid integer.
             */
            if (!IsValidArraySize(sizeOfArray)) {
                System.out.println("Invalid array size.");
                return null;
            }

            /**
             * Initialize the array using the size integer above.
             */
            array = new int[sizeOfArray];
            int index = 0;

            /**
             * Loop through each line in the text file and insert it into the array.
             */
            line = reader.readLine();
            while (line != null ) { //(readValue = reader.read()) != -1) {
                /**
                 * Check if there is a blank line in the input file.
                 */
                if (line.length() > 0) {
                    value = Integer.parseInt(line);

                    /**
                     * Make sure that the values are either 0 or 1.
                     */
                    if (!IsValidBulbValue(value)) {
                        System.out.println("Invalid bulb value found.");
                        return null;
                    }

                    /**
                     * Fill the array with the values from the input text file.
                     */
                    array[index] = value;
                    index++;

                    /**
                     * Read next line from input file and loop again.
                     */
                    line = reader.readLine();
                }
                else {
                    /**
                     * If there is a blank line in the input file, move to the next one and continue looping.
                     */
                    line = reader.readLine();
                }
            }

            /**
             * Verify that the first integer in the array (size of array) equals to the number of lines read
             * from the input text file.
             */
            if(!SizeMatch(sizeOfArray, index)) {
                System.out.println("Size match error. Number of lines in " + srcFileName + ": " + index + ".");
                return null;
            }

            /**
             * Input file has been completely read, now return array.
             */
            return array;
        }
        catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.out.println("fnfe");
            return null;
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            System.out.println("ioe");
            return null;
        }
        catch (NumberFormatException nfe) {
            System.out.println(nfe.getMessage());
            System.out.println("nfe");
            return null;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("e");
            return null;
        }
        finally {
            try {
                reader.close();
                fileInputStream.close();
            }
            catch (NullPointerException npe) {
                if (npe.getMessage() != null) {
                    System.out.println(npe.getMessage());
                }
                System.out.println("NPE");
                return null;
            }
            catch (IOException ioe) {
                if (ioe.getMessage() != null) {
                    System.out.println(ioe.getMessage());
                }
                System.out.println("IOE");
                return null;
            }
            catch(Exception e) {
                if (e.getMessage() != null) {
                    System.out.println(e.getMessage());
                }
                System.out.println("E");
                return null;
            }
        }
    }

    /**
     * Limit bulb values in input file to 0's or 1's.
     * @param bulbValue Bulb value read from the line in the input file.
     * @return
     */
    private static boolean IsValidBulbValue(int bulbValue) {
        return (bulbValue == 0 || bulbValue == 1);
    }

    /**
     * Ensure that the first value in the input file specifies a valid array size greater than zero.
     * @param value Value of array size read from the input file.
     * @return
     */
    private static boolean IsValidArraySize(int value) {
        return value >= 0;
    }

    /**
     * Ensure that the first value in the input file (which is the size of the array)
     * matches the total number of inputs that were subsequently read.
     * @param sizeOfArray Size of array specified in the input file.
     * @param numOfLines Total number of inputs read from the .txt file after the initial array size value.
     * @return
     */
    private static boolean SizeMatch(int sizeOfArray, int numOfLines) {
        return sizeOfArray == numOfLines;
    }
}
