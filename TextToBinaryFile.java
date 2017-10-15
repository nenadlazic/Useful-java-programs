import java.io.*;
import java.util.Arrays;

/**
 * Author: Nenad Lazic
 *
 *	This java program convert text file to binary file
 */

public class TextToBinaryFile {
    public static void main(String[] args) {
        
        BufferedReader br = null;
        String everything = "";
        FileOutputStream fop = null;
        File file;
        Boolean debug = false;

        if (args.length < 2) {
            System.out.println("Please provide input text file and output file(for bynary output of input)");
            System.exit(0);
        }
        try {
            br = new BufferedReader(new FileReader(args[0]));        	
        } catch (FileNotFoundException ex)  
        {
            System.out.println("Input file can not found");
        } 

        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
            System.out.println("The input file was successfully readed...");
            if(debug){
                System.out.println(everything);
            }
        } catch (IOException ie){
            System.out.println("IO exception");
        } finally {
            try {
                br.close();
            } catch (IOException ie){
                System.out.println("IO exception2");
            }
        }

        if(debug){
            byte[] b = everything.getBytes();
            System.out.println(Arrays.toString(b));
        }

        try {
            file = new File(args[1]);
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = everything.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("The output file was successfully writed...");

            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
