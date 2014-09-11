
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class System2 {
    /*
    a. Sridhar Vemula
    b. CS
    c. A Simple Batch System(Step 1)
    d. 2/23/2014
    e. All the global Variables(which are static) are declared in Variables.java
    f. The System Routine is the Driver for the simulation. It has the objects for Loader and CPU class.
    the Loader loads data from input device(file) to main memory. After the program is loaded
    it needs to be executed. The CPU object executes the Instructions.
    g. As the Object Oriented model I divided every module to a individual Class. So in order to have the
    global Variables to be distributed among these classes I have used Variables class and declared some static
    public variables which can be used by all Classes. The imp Global Variables like PC,IR, CLOCK, MEM etc., are
    declared here. And In Java it doesnt allow me to pass Value by Reference. So In order to do this rather than
    seding X, Y values am sending the static Variables that are Saved(Its the same. But As am not Using the exact
    specified variables thought to give a note)
    */
    
    public static void main(String[] args) throws IOException, Exception {
        File f=new File("trace_file.txt");
        if(f.exists()){
            f.delete();
        }
        f=new File("output_file.txt");
        if(f.exists()){
            f.delete();
        }
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter File Name");
        Variables.loadFile=sc.next();
        Loader x=new Loader(0,1);
        CPU m=new CPU(Variables.IPC,Variables.CPU_TRACE);
    }
    
}