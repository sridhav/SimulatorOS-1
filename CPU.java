
import java.io.IOException;
import java.util.Scanner;

/*
f. The Core of the Operating System. Executes the 1 address and 0 address Instructions. Errors like
Invalid Opcode, Arithematic Divide by zero are handled. Warmings like Infinite loop and invalid input
value are handled in here.

g. Implemented as Specified. More methods are seen here as there are many blocks used multiple times i thought
it would better to use methods.
*/

public class CPU {
    String mem_val;
    String operation;
    int indexed;
    int DADDR;
    String opName="";
    Memory m=new Memory();
    
    /*
    CPU the PC and CPU_TRACE_FLAG are added
    */
    
    public CPU(int X,int Y) throws IOException,Exception{
        Variables.PC=X;
        Variables.CPU_TRACE=Y;
        /*
        While Condition is run until a HLT operation is observed
        PC is Incremented for Every operation.
        FETCHES INSTRUCTION
        */
        while(!opName.equals("HLT")){
            Variables.IR=m.readMemory(Variables.PC);
            mem_val=getMemValue(Variables.IR); 
            /*
            Previous Stack and Memory values for trace files
            */
            Variables.prev_tos=Variables._STACK.size();
            if(Variables._STACK.isEmpty()){
                Variables.prev_stack_val="EMPTY";
            }
            else{
                int val=(int) Variables._STACK.pop();
                Variables.prev_stack_val=Variables.toHex(val);
                Variables._STACK.push(val);
            }
            Variables.prev_ea=Variables.EA;
            
            splitInstruction(mem_val);
            
            Variables.PC++;
            
            if(Y==1){
                Variables.writeToTraceFile();
            }
        }
        
    }
    
    /*
    DECODES THE INSTRUCTION
    */
    
    private void splitInstruction(String mem_val) throws InterruptedException, IOException {
        if(getType(mem_val)==1){
            operation=mem_val.substring(1,6);
            indexed=Integer.parseInt(mem_val.charAt(6)+"");
            String temp=mem_val.substring(9,mem_val.length());
            DADDR=Integer.parseInt(temp,2);
            if(indexed==1){
                Variables.EA=DADDR+Variables._MEM[(int)Variables._STACK.pop()];
                String EAVal="000000000000000000000000000"+Integer.toBinaryString(Variables.EA);
                EAVal=EAVal.substring(EAVal.length()-7,EAVal.length());
                Variables.EA=Integer.parseInt(EAVal,2);
            }
            else{
                Variables.EA=DADDR;
                
            }
            runInstruction16();
        }
        if(getType(mem_val)==0){
            operation = mem_val.substring(3,8);
            runInstruction8();
            if(!opName.equals("RTN")){
                operation=mem_val.substring(11,mem_val.length());
                runInstruction8();
            }
        }
    }
    
    /*
    EXECUTES THE INSTRUCTION - LONG -16 BIT INSTRUCTIONS(ONE ADRESS INSTRUCTIONS)
    
    CYCLOMATIC COMPLEXITY = 33 as all instructions are written under switch.
    Many decisions to make.
    */
    
    private void runInstruction16() throws InterruptedException, IOException {
        
        switch(operation){
            
            case "00001": opName="OR";
            Variables._STACK.push((int)Variables._STACK.pop() | m.readMemory(Variables.EA));
            break;
                
            case "00010": opName="AND";
            Variables._STACK.push((int)Variables._STACK.pop() & m.readMemory(Variables.EA));
            break;
                               
            case "00100": opName="XOR";
            Variables._STACK.push((int)Variables._STACK.pop() ^ m.readMemory(Variables.EA));
            break;
                
            case "00101": opName="ADD";
            Variables._STACK.push((int)Variables._STACK.pop() + m.readMemory(Variables.EA));
            break;
                
            case "00110": opName="SUB";
            Variables._STACK.push((int)Variables._STACK.pop() - m.readMemory(Variables.EA));
            break;
                
            case "00111": opName="MUL";
            int temp=(int)Variables._STACK.pop() * m.readMemory(Variables.EA);
            if(temp>32767 || temp<-32768){
                ErrorHandler.throwWarning(ErrorHandler.WR_VALUE_OUT_OF_RANGE);
                temp=0;
            }
            Variables._STACK.push(temp);
            break;
                
            case "01000": opName="DIV";
            int x=m.readMemory(Variables.EA);
            if(x==0){
                ErrorHandler.throwError(ErrorHandler.ER_ARI_DIVIDE_BY_ZERO);
            }
            Variables._STACK.push((int)Variables._STACK.pop() / m.readMemory(Variables.EA));
            break;
                
            case "01001": opName="MOD";
            x=m.readMemory(Variables.EA);
            if(x==0){
                ErrorHandler.throwError(ErrorHandler.ER_ARI_DIVIDE_BY_ZERO);
            }
            Variables._STACK.push((int)Variables._STACK.pop() % m.readMemory(Variables.EA));
            break;
                
            case "01100": 
                /*
                Infinite Loop Check: The index Variable is checked Everytime
                with the previous value; If both are the same for 5 times. The
                Warning Infinite Loop is issued. I have considered Infinite Loop as
                warning because it might be users interest to run a job infinitely.
                The same has been represented in CPL and CPE
                */
                opName="CPG";
                int val=(int)Variables._STACK.pop();
                Variables._STACK.push(val);
                
                Variables.now_index_value=val;
                if(Variables.prev_index_value==Variables.now_index_value){
                    Variables.index_count++;
                    if(Variables.index_count==5){
                        ErrorHandler.throwWarning(ErrorHandler.WR_INF_LOOP);
                    }
                }
                Variables.prev_index_value=Variables.now_index_value;
                
                if(val>m.readMemory(Variables.EA)){
                    Variables._STACK.push(1);
                }
                else{
                    Variables._STACK.push(0);
                }
                break;
                
            case "01101": opName="CPL";
            val=(int)Variables._STACK.pop();
            Variables._STACK.push(val);
            Variables.now_index_value=val;
            if(Variables.prev_index_value==Variables.now_index_value){
                Variables.index_count++;
            }
            if(Variables.index_count==5){
                ErrorHandler.throwWarning(ErrorHandler.WR_INF_LOOP);
            }
            Variables.prev_index_value=Variables.now_index_value;
            
            if(val<m.readMemory(Variables.EA)){
                Variables._STACK.push(1);
            }
            else{
                Variables._STACK.push(0);
            }
            break;
                
            case "01110": opName="CPE";
            val=(int)Variables._STACK.pop();
            Variables._STACK.push(val);
            Variables.now_index_value=val;
            if(Variables.prev_index_value==Variables.now_index_value){
                Variables.index_count++;
                if(Variables.index_count==5){
                    ErrorHandler.throwWarning(ErrorHandler.WR_INF_LOOP);
                }
            }
            Variables.prev_index_value=Variables.now_index_value;
            if(val==m.readMemory(Variables.EA)){
                Variables._STACK.push(1);
            }
            else{
                Variables._STACK.push(0);
            }
            break;
                
            case "01111": opName="BR";
            Variables.PC=Variables.EA-1;
            break;
                
            case "10000": opName="BRT";
            if((int)Variables._STACK.pop()==1){
                Variables.PC=Variables.EA-1;
            }
            break;
                
            case "10001": opName="BRF";
            if((int)Variables._STACK.pop()==0){
                Variables.PC=Variables.EA-1;
            }
            break;
                
            case "10010": opName="CALL";
            Variables._STACK.push(Variables.PC);
            Variables.PC=Variables.EA-1;
            break;
          
            case "10110": opName="PUSH";
            Variables._STACK.push(m.readMemory(Variables.EA));
            break;
                
            case "10111": opName="POP";
            m.writeMemory(Variables.EA,(int)Variables._STACK.pop());
            break;
                
            default : ErrorHandler.throwError(ErrorHandler.ER_CPU_INVALID_OPCODE);
            break;
        }
        Variables._CLOCK+=4;
        
    }
    
    /*
    EXECUTES THE INSTRUCTION - Short-8 BIT INSTRUCTIONS(ZERO ADRESS INSTRUCTIONS)
    CYCLOMATIC COMPLEXITY = 33 as all instructions are written under switch.
    Many decisions to make.
    */
    
    private void runInstruction8() throws InterruptedException, IOException {
        
        switch(operation){
            case "00000": opName="NOP";
            break;
                
            case "00001": opName="OR";
            Variables._STACK.push((int)Variables._STACK.pop() | (int)Variables._STACK.pop());
            break;
                
            case "00010": opName="AND";
            Variables._STACK.push((int)Variables._STACK.pop() & (int)Variables._STACK.pop());
            break;
                
            case "00011": opName="NOT";
            Variables._STACK.push(~((int)Variables._STACK.pop()));
            break;
                
            case "00100": opName="XOR";
            Variables._STACK.push((int)Variables._STACK.pop() ^ (int)Variables._STACK.pop());
            break;
                
            case "00101": opName="ADD";
            Variables._STACK.push((int)Variables._STACK.pop() + (int)Variables._STACK.pop());
            break;
                
            case "00110": opName="SUB";
            Variables._STACK.push((int)Variables._STACK.pop() - (int)Variables._STACK.pop());
            break;
                
            case "00111": opName="MUL";
            int temp=(int)Variables._STACK.pop() * (int)Variables._STACK.pop();
            if(temp>32767 || temp<-32768){
                ErrorHandler.throwWarning(ErrorHandler.WR_VALUE_OUT_OF_RANGE);
                temp=0;
            }
            Variables._STACK.push(temp);
            break;
                
            case "01000": opName="DIV";
            int x=(int) Variables._STACK.pop();
            if(x==0){
                ErrorHandler.throwError(ErrorHandler.ER_ARI_DIVIDE_BY_ZERO);
            }
            Variables._STACK.push((int)Variables._STACK.pop() / x);
            break;
                
            case "01001": opName="MOD";
            x=(int) Variables._STACK.pop();
            if(x==0){
                ErrorHandler.throwError(ErrorHandler.ER_ARI_DIVIDE_BY_ZERO);
            }
            Variables._STACK.push((int)Variables._STACK.pop() % x);
            break;
            
            case "01010": opName="SL";
            Variables._STACK.push((int)Variables._STACK.pop()<<1);
            break;
                
            case "01011": opName="SR";
            Variables._STACK.push((int)Variables._STACK.pop()>>1);
            break;
                
            case "01100": /*
                Infinite Loop Check: The index Variable is checked Everytime
                with the previous value; If both are the same for 5 times. The
                Warning Infinite Loop is issued. I have considered Infinite Loop as
                warning because it might be users interest to run a job infinitely.
                The same has been represented in CPL and CPE
                */
                opName="CPG";
                int val=(int)Variables._STACK.pop();
                int val11=(int)Variables._STACK.pop();
                Variables._STACK.push(val11);
                Variables._STACK.push(val);
                Variables.now_index_value=val;
                if(Variables.prev_index_value==Variables.now_index_value){
                    Variables.index_count++;
                    if(Variables.index_count==5){
                        ErrorHandler.throwWarning(ErrorHandler.WR_INF_LOOP);
                    }
                }
                Variables.prev_index_value=Variables.now_index_value;
                if(val11>val){
                    Variables._STACK.push(1);
                    
                }
                else{
                    Variables._STACK.push(0);
                }
                break;
                
            case "01101": opName="CPL";
            val=(int)Variables._STACK.pop();
            val11=(int)Variables._STACK.pop();
            Variables._STACK.push(val11);
            Variables._STACK.push(val);
            Variables.now_index_value=val;
            if(Variables.prev_index_value==Variables.now_index_value){
                Variables.index_count++;
                if(Variables.index_count==5){
                    ErrorHandler.throwWarning(ErrorHandler.WR_INF_LOOP);
                }
            }
            Variables.prev_index_value=Variables.now_index_value;
            if(val11<val){
                Variables._STACK.push(1);
                
            }
            else{
                Variables._STACK.push(0);
            }
            break;
                
            case "01110": opName="CPE";
            val=(int)Variables._STACK.pop();
            val11=(int)Variables._STACK.pop();
            
            Variables._STACK.push(val11);
            Variables._STACK.push(val);
            Variables.now_index_value=val;
            if(Variables.prev_index_value==Variables.now_index_value){
                Variables.index_count++;
                if(Variables.index_count==5){
                    ErrorHandler.throwWarning(ErrorHandler.WR_INF_LOOP);
                }
            }
            Variables.prev_index_value=Variables.now_index_value;
            if(val11==val){
                Variables._STACK.push(1);
                
            }
            else{
                Variables._STACK.push(0);
            }
            break;
               
            case "10011": opName="RD";
            readVals();
            break;
                
            case "10100": opName="WR";
            val=(int) Variables._STACK.pop();
            System.out.println("OUTPUT:"+val);
            Variables.OUTPUT=val+"";
            Variables._CLOCK=Variables._CLOCK+14;
            Variables.IO_CLOCK=Variables.IO_CLOCK+14;
            break;
                
            case "10101": opName="RTN";
            Variables.PC=(int) Variables._STACK.pop();
            break;
            
            case "11000":opName="HLT";
            Variables.writeToOutputFile();
            break;
                
            default : ErrorHandler.throwError(ErrorHandler.ER_CPU_INVALID_OPCODE);
            break;
        }
        
        Variables._CLOCK++;
         
    }
    
    /*
    Due to the complexity of code the Read Operation is declared HERE
    RD Operation. Throws an Warning if values are more than 16bits
    */
    
    private void readVals() throws InterruptedException, IOException {
        System.out.println("ENTER INPUT:");
        Scanner sc=new Scanner(System.in);
        int temp=sc.nextInt();
        if(temp>32767 || temp<-32768){
            ErrorHandler.throwWarning(ErrorHandler.WR_VALUE_OUT_OF_RANGE);
            temp=0;
        }
        Variables._STACK.push(temp);
        Variables._CLOCK=Variables._CLOCK+14;
        Variables.IO_CLOCK=Variables.IO_CLOCK+14;
    }
    
    /*
    OTHER METHODS (MISC)
    USED TO PAD THE BINARY BITS
    */
    
    private String getMemValue(int i) {
        String temp;
        temp=Integer.toBinaryString(i);
        temp="00000000000000000"+temp;
        temp=temp.substring(temp.length()-16,temp.length());
        return temp;
    }
    
    /*
    OTHER METHODS (MISC)
    TO GET THE TYPE OF INSTRUCTION BY GETTING THE FIRST BIT
    */
    
    private int getType(String mem_val) {
        return Integer.parseInt(mem_val.charAt(0)+"");
    }
    
    /*
    Experimental MEthods (MISC)
    */
    
    private int notOperation(int x) {
        int temp;
        String temp2=getMemValue(x);
        char[] temp3=temp2.toCharArray();
        for(int i=0;i<temp2.length();i++){
            if(temp3[i]=='0'){
                temp3[i]='1';
            }
            else{
                temp3[i]='0';
            }
        }
        temp2=new String(temp3);
        temp2=temp2.substring(temp2.length()-16, temp2.length());
        temp=Integer.parseInt(temp2, 2);
        return temp;
    }
    
    /*
    Experimental MEthods (MISC)
    */
    
    
    private int AdditionVals(int pop, int x) {
        //int val1=checkVals(pop);
        //int val2=checkVals(x);
        
        int res=pop+x;
        
        return res;
    }
    
    /*
    Experimental MEthods (MISC)
    */
    
    private int checkVals(int pop) {
        String temp="0000000000000000000000000000000"+Integer.toBinaryString(pop);
        String val1=temp.substring(temp.length()-16,temp.length());
        int value1;
        temp=val1.substring(1,val1.length());
        
        if(val1.charAt(0)=='1'){
            value1=-Integer.parseInt(temp,2);
        }
        else{
            value1=Integer.parseInt(temp);
        }
        return value1;
    }
    
}
