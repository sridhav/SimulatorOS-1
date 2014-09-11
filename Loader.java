
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
f. the Loader is responsible for loading the data from input(file) to Memory. The hex file is loaded
and the Loader calls the Memory Module to write to memory. It Handles two errors Invalid Trace Flag
error and Invalid Loader Format Error.

g. The Loader Job accurately implements the Specification.

*/
public class Loader {
    private String store="";
    private String data="";
    private Memory memory;
    public Loader(int X,int Y) throws IOException, Exception{
        
        Variables.CPU_TRACE=Y;
        readLoader(Variables.loadFile);
    }
    
    /*
    The readLoader Method takes the data from the file and writes them into the Memory.
    The Initial PC, Trace flag, mem_size, Load Address and JOB_ID gets stored in global variables
    which are given in the file.
    
    */
    
    private void readLoader(String load) throws FileNotFoundException, IOException, Exception {
        BufferedReader br=new BufferedReader(new FileReader(load));
        String temp;
        while((temp=br.readLine())!=null){
            store=store+temp+"\n";
        }        
        String lines[]=store.split("\\r?\\n");
        for(int i=1;i<lines.length;i++){
            data=data+lines[i];
        } 
        String[] top=lines[0].split("\\s");
        
        Variables.JOB_ID=Byte.parseByte(top[0],16);
        Variables.LA=Byte.parseByte(top[1],16);
        Variables.IPC=Byte.parseByte(top[2],16);
        Variables.mem_size=Byte.parseByte(top[3],16);
        Variables.CPU_TRACE=Byte.parseByte(top[4],16);
        
        int j=0;
        Variables.BR=Variables.LA;
        
        for(int i=0;i<Variables.mem_size;i++){
            ErrorHandler.checkLoaderFormat(data.substring(j,j+4));
            String X="WRITE";
            int Y=i;
            int Z=Integer.parseInt(data.substring(j,j+4),16);
            memory=new Memory();
            memory.writeMemory(Y, Z);
            ErrorHandler.checkMemoryAdressBound(i);
            j=j+4;
        } 
    }
 
}
