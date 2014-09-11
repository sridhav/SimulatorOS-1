
import java.io.IOException;
import java.util.Scanner;

/*
f.All the Errors and Warining are given Different integer values.
The Error Handler catches the Errors and displays the Reason for errors.

Most Runtime errors are caught here
g. Error handler is given a Separate class in order for modularity. For code Understandability
I preffered this.
*/

public class ErrorHandler {
    /*
    ERROR CODES
    */
    
    public final static int ER_LOADER_INVALID_FORMAT=1;
    public final static int WR_LOADER_INVALID_FLAG=2;
    public final static int ER_LOADER_INVALID_IPC=3;
    public final static int ER_LOADER_INVALID_SIZE=4;
    
    public final static int ER_STACK_OVERFLOW=11;
    public final static int ER_STACK_UNDERFLOW=12;
    
    public final static int ER_MEM_OUT_OF_RANGE=21;
    
    public final static int ER_ARI_DIVIDE_BY_ZERO=31;
    
    public final static int WR_INF_LOOP=101;
    public static final int ER_CPU_INVALID_OPCODE=41;
    public final static int WR_CPU_INVALID_FLAG=42;
    public final static int WR_VALUE_OUT_OF_RANGE=51;
    
    public static int NATURE=1;
    
    
    public static String Error="";
    public static String stack_trace="";
    public static String Warning="";
    
    /*
    Warnings are Caught and the program execution completes but a Warning
    Appears on the output file
    */
    
    public static void throwWarning(int err) throws InterruptedException, IOException {
        Warning=Warning+"WARNINGS\n";
        switch(err){
            case WR_CPU_INVALID_FLAG: Warning=Warning+"Warning : Invalid CPU Trace Flag. Default 0 is taken\n";
            break;
            case WR_LOADER_INVALID_FLAG:Warning=Warning+"Warning : Invalid Loader Trace Flag. Default 0 is taken\n";
            break;
            case WR_VALUE_OUT_OF_RANGE:Warning=Warning+"Warning : Invalid Input. Input Ranges between -32768 to 32767\n";
            break;
            case WR_INF_LOOP:Warning=Warning+"Warning : Possibility of Infinite Loop\n";
            System.out.println("WARNING : Possibility of Infinite Loop. Press 1 to exit");
            Scanner sc=new Scanner(System.in);
            int inp=sc.nextInt();
            if(inp==1){
                Variables.writeToOutputFile();
            }
            System.exit(0);
            break;
                
        }
        Warning=Warning+"\n";
    }
    
    /*
    Erros are Caught and the program execution terminates with abnormal behaviour
    Error Appears on the output file. A Stack Trace is also shown if any syntax errors
    */
    
    public static void throwError(int err) throws IOException{
        Error=Error+"ERRORS :\n";
        switch(err){
            case ER_LOADER_INVALID_FORMAT: Error=Error+"Error : INVALID LOADER FORMAT";
            NATURE=0;
            break;
            case ER_LOADER_INVALID_IPC : Error=Error+"Error : INVALID LOADER IPC";
            NATURE=0;
            break;
            case ER_LOADER_INVALID_SIZE : Error=Error+"Error : INVALID LOADER SIZE VALUE";
            NATURE=0;
            break;
            case ER_STACK_OVERFLOW:Error=Error+"Error : STACK OVERFLOW";
            NATURE=0;
            break;
            case ER_STACK_UNDERFLOW:Error=Error+"STACK UNDERFLOW";
            NATURE=0;
            break;
            case ER_MEM_OUT_OF_RANGE:Error=Error+"Error: MEMORY OUT OF RANGE";
            NATURE=0;
            break;
            case ER_ARI_DIVIDE_BY_ZERO:Error=Error+"Error: ARITHEMATIC DIVIDE BY ZERO";
            NATURE=0;
            break;
            case ER_CPU_INVALID_OPCODE: Error=Error+"Error : INVALID OPCODE";
            NATURE=0;
            break;
        }
        Error=Error+"\n";
        PrintStackTrace();
        Variables.writeToOutputFile();
        System.exit(1);
    }
    
    /*
    Prints Stack Trace when there is an Error
    */
    
    private static void PrintStackTrace() {
        stack_trace=stack_trace+"STACK TRACE";
        StackTraceElement[] st=Thread.currentThread().getStackTrace();
        for(int i=1;i<st.length;i++){
            stack_trace=stack_trace+"Class Name :"+st[i].getFileName()+" Method Name :"+st[i].getMethodName()+" Line Number:"+st[i].getLineNumber()+"\n";
        }
    }
    
    /*
    The Error Check Methods are also declared here. These are declared static
    So these can be used in all the classes in the simulation
    Checks the Loader format. If Loader has characters like z,x which are not hex values
    Error is returned.
    */
    
    public static void checkLoaderFormat(String str) throws IOException {
        str=str.toUpperCase();
        if(!str.matches("[A-F0-9]{4}")){
            System.out.println(str);
            ErrorHandler.throwError(ErrorHandler.ER_LOADER_INVALID_FORMAT);
        }
    }
    
    /*
    Checks the address if Memory address thats being asked to check is greater
    than 256 or less than 0 error is caught
    */
    
    public static void checkMemoryAdressBound(int i) throws IOException {
        if(i>=256 | i<0){
            ErrorHandler.throwError(ErrorHandler.ER_MEM_OUT_OF_RANGE);
        }
    }
    
}
