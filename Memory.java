
import java.io.IOException;

/*

f. The Memory Routine reads and writes data to the Memory. Its the sole job of Memory
An Error Handler for MemoryAddressBound is checked. If error is present it terminates.
MemoryAddressBound -> If EA is greater than 256 | EA is less than 0

g. As Java doesnot allow pass by reference. I could not use the Procedure MEMORY(X,Y,Z).
So I need to create to other Methods readMemory to Read From Memory, and writeMemory
to write to memory. The overall functionality doesnot change except for the Specification

*/

public class Memory {
    
   public int readMemory(int Y) throws IOException{
        ErrorHandler.checkMemoryAdressBound(Variables.BR+Y);
        Integer x=new Integer(Variables._MEM[Variables.BR+Y]);
        short y=x.shortValue();
        x=(int)y;
        return x;
    } 
    
    public void writeMemory(int Y,int Z) throws IOException{
        ErrorHandler.checkMemoryAdressBound(Variables.BR+Y);
        Variables._MEM[Variables.BR+Y]=Z;
    }
    
}
