
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
g. So in order to have the global Variables to be distributed among these classes I have used Variables class
and declared some static public variables which can be used by all Classes. The imp Global Variables like
PC,IR, CLOCK, MEM etc., are declared here.
*/

public class Variables {
    
    public static int TOS;
    public static int PC;
    public static int IR;
    public static int BR;
    public static int EA;
    
    public static Stack _STACK=new Stack();
    public static int _MEM[]=new int[256];
    
    /*
    USED FOR CHECKING AN INFINITE LOOP
    */
    
    public static int prev_index_value=0;
    public static int now_index_value=0;
    public static int index_count=0;
    
    /*
    LOADER OBTAINED VARIABLES FROM INPUT FILE.
    */
    
    public static int JOB_ID;
    public static int LA;
    public static int IPC;
    public static int CPU_TRACE=0;
    public static int mem_size;
    
    
    public static long _CLOCK=0;
    public static long IO_CLOCK=0;
   
    public static int prev_tos=0;
    public static int prev_ea=0;
    public static String prev_stack_val;
    
    /*
    LOADER INPUT FILE
    */
    
    public static String loadFile;
   
    /*
    OUTPUT FROM WRITE. Trace date to be written into Trace file
    */

    public static String OUTPUT="";
    public static String traceData="";
   
    /*
    Method to write to trace_file.txt
    */
    
    public static void writeToTraceFile() throws IOException,NoSuchElementException{
        FileWriter fw=new FileWriter("./trace_file.txt",true);
        String temp="";
        String Stack_val;
        if(Variables._STACK.isEmpty()){
            Stack_val="EMPTY";
        }
        else{
            int val=(int) Variables._STACK.pop();
            Stack_val=toHex(val);
            Variables._STACK.push(val);
        }
        
        temp=temp+""+toHex(Variables.PC)+"\t"+toHex(Variables.BR)+"\t"+toHex(Variables.IR)+"\t"+toHex(Variables.prev_tos)+"\t"+prev_stack_val+"\t"+toHex(prev_ea)+"\t"+toHex(_MEM[prev_ea])+"\t"+toHex(Variables._STACK.size())+"\t"+Stack_val+"\t"+toHex(EA)+"\t"+toHex(Variables._MEM[EA]);
        temp=temp+"\n";
        fw.write(temp);
        fw.close();
    }
    
    /*
    Method to write to output_file.txt
    */
    
    public static void writeToOutputFile() throws IOException,NoSuchElementException{
        
        FileWriter fw=new FileWriter("./output_file.txt",false);
        String temp="JOB ID :";
        temp=temp+JOB_ID+"\n";
        temp=temp+ErrorHandler.Error;
        temp=temp+ErrorHandler.stack_trace;
        temp=temp+ErrorHandler.Warning;
        if(ErrorHandler.NATURE==1){
            temp=temp+"NORMAL EXECUTION\n";
        }
        if(ErrorHandler.NATURE==0){
            temp=temp+"ABNORMAL EXECUTION\n";
            temp=temp+writeMemoryDump();
        }
        temp=temp+OUTPUT+"\n";
        temp=temp+"CLOCK:"+toHex((int) _CLOCK)+"\n";
        temp=temp+"Run Time:"+(long)(_CLOCK)+"\n";
        temp=temp+"Execution Time:"+(long)(_CLOCK-IO_CLOCK)+"\n";
        temp=temp+"I/O Time:"+(long)IO_CLOCK+"\n";
        fw.write(temp);
        fw.close();
    }
    
    /*
    Write to Memory Dump
    */
    
    private static String writeMemoryDump() {
        String temp2="MEMORY DUMP\n";
        for(int i=0;i<_MEM.length;i++){
            int x=_MEM[i];
            String temp="000000"+Integer.toHexString(x);
            temp2=temp.substring(temp.length()-4,temp.length())+"\t"+Integer.toBinaryString(x)+"\n";
        }
        return temp2;
    }
    
    /*
    converts to Hex values;
    */
    
    public static String toHex(int val) {
        String temp="000000000"+Integer.toHexString(val);
        temp=temp.substring(temp.length()-4,temp.length());
        return temp;
    }
    
    /*
    Debug Methods while coding & Testing
    Displays all global Variables,Mem & Stack
    */
    
    public static void display(){
        System.out.format("Top of Stack :%-30d",TOS);
        System.out.format("Program Counter:%-30d",PC);
        System.out.format("Instruction Register:%-30d",IR);
        System.out.format("Base Register:%-30d",BR);
        System.out.format("Effective Address:%-30d",EA);
        System.out.format("Job ID:%-30d", JOB_ID);
        System.out.format("Load Address:%-30d", LA);
        System.out.format("Initial Program Counter:%-30d", IPC);
        System.out.format("Memory Size:%-30d",mem_size);
        System.out.format("Clock:%-30d",_CLOCK);
        displayMem();
        displayStack();
    }
    
    /*
    Debug Methods while coding & Testing
    Displays Memory
    */
    
    public static void displayMem() {
        System.out.println("\n\n#######MEMORY#########");
        for(int i=0;i<255;i++){
            String hex=Integer.toHexString(_MEM[i]);
            String bin=Integer.toBinaryString(_MEM[i]);
            hex="000000000"+hex;
            hex=hex.substring(hex.length()-4, hex.length());
            bin="0000000000000000000000000000000000000000"+bin;
            bin=bin.substring(bin.length()-16, bin.length());
            hex=String.format("%4s",hex).replace(' ', '0');
            bin=String.format("%32s",bin).replace(' ', '0');
            System.out.println(_MEM[i]+"\t"+hex+"\t"+bin);
         
        }
    }
    
    /*
    Debug Methods while coding & Testing
    Displays Stack Vals
    */
    
    public static void displayStack() {
        System.out.println("#########STACK###########");
        Iterator m=_STACK.iterator();
        while(m.hasNext()){
            int val=(int) m.next();
            String hex=Integer.toHexString(val);
            String bin=Integer.toBinaryString(val);
            hex="000000000"+hex;
            hex=hex.substring(hex.length()-4, hex.length());
            bin="0000000000000000000000000000000000000000"+bin;
            bin=bin.substring(bin.length()-16, bin.length());
            hex=String.format("%4s", hex).replace(' ', '0');
            bin=String.format("%32s",bin).replace(' ','0');
            System.out.println(val+"\t"+hex+"\t"+bin);
        }
    }
  
}
