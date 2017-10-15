import java.io.*;
import java.util.Arrays;

/**
 * Author: Nenad Lazic
 *
 * Finite state machine for extracting useful data between start and stop sequences.
 * Binary data is obtained in parts and may contain unnecessary data and these parts can be cut off anywhere.
 * Description:
 * Initial state 0
 * Transition function: f
 * f(0, startSequence) = 1 f(0, endSequence) = 0 f(0, otherSequence) = 0
 * f(1, endSequence) = 2 f(1, startSequence) = 1(clear buffer) f(1, otherSequence) = buffering
 * f(2, startSequence) = 1 f(2, otherSequence) = 0 f(2, endSequence) = 0
 */
public class FiniteStateMachine {

	private int currentState = 0;
    
   

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Please provide input file(binary file)");
            System.exit(0);
        }

        String inputFile = args[0];
        byte[] retval = null;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		try (
			InputStream is = new FileInputStream(inputFile);
	    ){
		    int nRead;
		    byte[] data = new byte[128];

		    while ((nRead = is.read(data, 0, data.length)) != -1) {
		        buffer.write(data, 0, nRead);
		    }

		    buffer.flush();

		    //System.out.println(new String(buffer.toByteArray()));
		} catch (IOException ex) {
	            ex.printStackTrace();
		}

	    System.out.println("-------------------------------------------------------------------------");
	    System.out.println("---------------Useful data parsed from binary file-----------------");
	    System.out.println("-------------------------------------------------------------------------");

	    byte[] data = buffer.toByteArray();
	    try{
			//byte[] retVal = feedData(data);
			//System.out.println(new String(retVal));
	    }catch (Exception ex) {
	        ex.printStackTrace();
        }
    }

    //function from TextEngine(purpose of preprocessing raw data)
	public byte[] feedData(byte[] data) throws InterruptedException {

		System.out.println("CurrentState: "+currentState);

         //useful data starts with sequence (in this case start sequence ">\n<tt "")
        byte[] startSequence = new byte[6];
        startSequence[0] = (byte)0x3e;
        startSequence[1] = (byte)0x0a;
        startSequence[2] = (byte)0x3c;
        startSequence[3] = (byte)0x74;
        startSequence[4] = (byte)0x74;
        startSequence[5] = (byte)0x20;
    
        //useful data ends with sequence ()
        byte[] endSequence = new byte[5];
        endSequence[0] = (byte)0x3c;
        endSequence[1] = (byte)0x2f;
        endSequence[2] = (byte)0x74;
        endSequence[3] = (byte)0x74;
        endSequence[4] = (byte)0x3e;

	    int startSequenceLength = startSequence.length;
	    int startPositionUsefulData = -1;
		int endPositionUsefulData = -1;
	    

        switch (currentState) {
            case 0:  
                System.out.println("Current state 0");
                break;
            case 1:  
                System.out.println("Current state 1");
                break;
            case 2:  
                System.out.println("Current state 2");
                break;
            default:
                System.out.println("Undefined state");
                break;
        }


	    int i = 0;
	    int dataLength = data.length;
	    for(i = 0; i < dataLength - startSequenceLength; i++) {
	        int j = 0;
        	for(j = 0; j < startSequenceLength; j++) {
        	    if(data[i+j] != startSequence[j]) {
        	        break;
        	    }
        	}

        	//start sequence found
            if(j == startSequenceLength ) {
                startPositionUsefulData = i + 4;
        		break;
        	}
        }

	    byte[] slice = null;
       	if(i < dataLength - startSequenceLength) {
       		//slice data from position startPositionUsefulData to the end
        	slice = Arrays.copyOfRange(data, startPositionUsefulData, dataLength);
        	return slice;
		}
       	return slice;
    }
}
