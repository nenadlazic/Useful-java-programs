import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.nio.ByteBuffer;

public final class SmallBinaryFiles {

    public static void main(String[] args) throws IOException {

	if(args.length < 1){
	    log("File name is missed!!");
	    return;
	}
	
        Path path = Paths.get(args[0]);
	byte[] bytes = Files.readAllBytes(path);
		
	log("FILE NAME: "+args[0]);
	log("FILE SIZE :" + bytes.length);

	byte[] startSequenceTFDT = new byte[4];
        startSequenceTFDT[0] = (byte)0x74;
        startSequenceTFDT[1] = (byte)0x66;
	startSequenceTFDT[2] = (byte)0x64;
	startSequenceTFDT[3] = (byte)0x74;

	int beginingPosition = indexOf(bytes, startSequenceTFDT);

	log("START POSITION MOOF: "+beginingPosition);


	byte [] version = Arrays.copyOfRange(bytes, beginingPosition+4, beginingPosition+8);

	byte b1 = version[0];
	int i1 = b1;

	log("VERSION INT: "+i1);	

	if(i1 == 0){
	    byte [] baseMediaDecodeTime = Arrays.copyOfRange(bytes, beginingPosition+8, beginingPosition+12);
	} else {
	    byte [] baseMediaDecodeTime = Arrays.copyOfRange(bytes, beginingPosition+8, beginingPosition+16);
	    ByteBuffer bb = ByteBuffer.wrap(baseMediaDecodeTime);
	    long l = bb.getLong();
	    log("baseMediaDecodeTime: "+l);
	}

	//StringBuilder sb = new StringBuilder();
	//for (byte b : version) {
        //    sb.append(String.format("%02X ", b));
        //}

	//log("HEX VERSION: "+sb.toString());

	//ByteBuffer wrapped = ByteBuffer.wrap(version);
	//int intVersion = wrapped.getInt();
	
	//log("VERSION: "+intVersion);

	//binary.writeSmallBinaryFile(bytes, OUTPUT_FILE_NAME);
    }

    final static String FILE_NAME = "ffs.m4s";
    final static String OUTPUT_FILE_NAME = "output.m4s";

    byte[] readSmallBinaryFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        return Files.readAllBytes(path);
    }

    void writeSmallBinaryFile(byte[] bytes, String fileName) throws IOException {
        Path path = Paths.get(fileName);
        Files.write(path, bytes); //creates, overwrites
    }

    private static void log(Object msg){
        System.out.println(String.valueOf(msg));
    }

    public static int indexOf(byte[] outerArray, byte[] smallerArray) {
        for(int i = 0; i < outerArray.length - smallerArray.length+1; ++i) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i+j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;
    }
}



