import java.io.*;

public class DefectiveLightbulbs {
    public static void main(String[] args) throws IOException {
        if (args[0] == null) {
            System.out.println("Missing file specified at command line. Program terminating.");
            return;
        }

        int[] defectivesArray;
        int[] bulbArray = GetBulbArray(args[0]);

        if(bulbArray == null) {
            System.out.println("Error generating bulb array. Program terminating.");
            return;
        }

        System.out.println("Program terminated correctly.");


    }

    public void FindDefective() {

    }

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
            //Create file reader
            srcFileName = arg;
            fileInputStream = new FileInputStream(srcFileName);
            reader = new BufferedReader(new InputStreamReader(fileInputStream));

            //Get the first integer for the size of the array
            line = reader.readLine();
            sizeOfArray = Integer.parseInt(line);
            System.out.println("Reading inputs from " + srcFileName);
            System.out.println("Size of array: " + sizeOfArray);

            //Make sure the first integer of the array is a valid integer
            if (!IsValidArraySize(sizeOfArray)) {
                System.out.println("Invalid array size.");
                return null;
            }

            //Initialize the array using the size integer above
            array = new int[sizeOfArray];
            int index = 0;

            line = reader.readLine();
            //Loop through each line in the text file
            while (line != null ) { //(readValue = reader.read()) != -1) {
                value = Integer.parseInt(line);
                //Make sure that the values are either 0 or 1
                if (!IsValidBulbValue(value)) {
                    System.out.println("Invalid bulb value found.");
                    return null;
                }

                //Fill the array with the values from the input text file
                array[index] = value;
                index++;

                line = reader.readLine();
            }

            //Verify that the first integer in the array (size of array) equals to the number
            //of lines read from the input text file.
            if(!SizeMatch(sizeOfArray, index)) {
                System.out.println("Size match error. Number of lines in " + srcFileName + ": " + index + ".");
                return null;
            }

            for (int i = 0; i < sizeOfArray; i++) {
                System.out.println("Bulb " + (i+1) + ": " + array[i] + ".");
            }

            //Return array
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

    private static boolean IsValidBulbValue(int bulbValue) {
        return (bulbValue == 0 || bulbValue == 1);
    }

    private static boolean IsValidArraySize(int value) {
        return value >= 0;
    }

    private static boolean SizeMatch(int sizeOfArray, int numOfLines) {
        return sizeOfArray == numOfLines;
    }
}
