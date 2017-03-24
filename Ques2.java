package ques2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author: Samarth Bharti | Adita Kapoor |  Amitesh
 * @SNU ID:   1410110358   |  1410110017  | 1410110044
 * 
 */
public class Ques2 {
    Component cmp;
    static Stack brackets = new Stack();
    
    public static void main(String[] args) throws IOException {
     
        String language;
        BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));

        /*Accept the language*/
        System.out.print("Enter the language: ");
        language = reader.readLine();
        String expression = stackable(language);
        System.out.println("The expression in a stack readable form is: " + expression);
        
        /*Convert staclable ecpression to NFA*/
        Component nfa = eToNfa(expression);
        Component temp=nfa;
        ArrayList<State> print=new ArrayList<State>();
        print=nfa.getOut();
        for(int h=0;h<print.size();h++){
            System.out.println(print.get(h).toString());
        }
        /*Check if the input string matches the language of the designed NFA*/
        System.out.println("\nEnter a string for simulation: ");
        String str = reader.readLine();

        Boolean check = Check.compare(nfa.getStart(), str);
        if(check)
           System.out.println("Not a match");
        else
           System.out.println("It's a match");
    } 
    
    
    /*Function converts the postfix expression into an equivalent NFA*/
    public static Component eToNfa(String postfix){
   
        Component top=null; //Pops topmost element of the stack
        Component second=null;  //Pops second element of the stack when operator is a binary operator
        Component NFA=null; //Returns the final NFA
        Stack <Component> stck=new Stack<Component>();  //Used to store intermidiatry components
        State combine=new State();  //State stores the result when two components are combined over an operator
        int len=postfix.length();
        for(int i=0;i<len;i++){
            char item=postfix.charAt(i);
            //Or operator
            if(item=='|'){
                top=stck.pop();
                second=stck.pop();
                combine=combine.combineState(top.getStart(), second.getStart());
                ArrayList<State> combArrows=new ArrayList<State>();

                for (int k = 0; k < top.getOut().size(); k++){
                    combArrows.add(top.getOut().get(k));
                }
                for (int l = 0; l < second.getOut().size(); l++){
                    combArrows.add(second.getOut().get(l));
                }
                   Component newComponent= new Component(combine,combArrows);
                   stck.push(newComponent);
            }
            //Astriks operator
            else if(item=='*'){
                top=stck.pop();
                combine=combine.combineState(top.getStart(),null);
                ADD(top, combine);
                stck.push(new Component(combine,combine));
            }
            //Union operator
            else if(item=='+'){
                top=stck.pop();
                combine=combine.combineState(top.getStart(),null);
                ADD(top, combine);
                stck.push(new Component(combine,combine));  
            }
            //The encoutered item is an alphbet
            else{
                State charState = new State(item);
                stck.push(new Component(charState, charState));
            }
        }
        NFA = stck.pop();   //Pops the final NFA
        ADD(NFA, new State());
        return NFA;
    }
    
    /*Function adds a state to the component*/
    private static void ADD(Component cmp, State s){
        ArrayList<State> toBePatched = cmp.getOut();
        for (int i = 0; i < toBePatched.size(); i++){
            State openarrows = toBePatched.get(i);
            openarrows.patch(s);
        }
    }
    
    /* The function converts expression into a stackable (post-fix) format in order to convert it into a NFA*/
    public static String stackable(String expression){    
        String stackRead = "";
        for(int i=0;i<expression.length();i++){
            //Stores each charachter of the infix expression iteratively
            char incoming= expression.charAt(i);
            //If charachter is an alphabet. the append it to the postfix string
            if(Character.isLetter(incoming))    
               stackRead = stackRead+incoming;
            //If a open bracket is encounterd, add it to the stack
            else if(incoming == '('){
                brackets.push(incoming);
            }
            //If a clossing bracket is encountered, pop the stack until the first open bracket is encounterd
            else if (incoming==')')
            {
                if(!brackets.isEmpty()){
                while ((char)brackets.peek() != '(')
                {
                        stackRead = stackRead + brackets.pop();
                }
                //Pop out the open bracket itself
                brackets.pop();
                }
            }
            //If operator is encounterd
            else
            {
                int precedenceVal=0;
                //If stack is empty, the opation is not possible
                if(!brackets.isEmpty()){
                    try{
                        /*Check if the priority of the incoming charachter is higher, 
                        or lower than topmost stack element*/
                        precedenceVal = hasHigherprecedence(incoming, (char)brackets.peek());
                    }catch(EmptyStackException ex){
                        System.out.println("Stack is empty, illegal expression!");
                        }
                }
                /*If the precedence of the topmost stack element is higher, then keep popping elements
                until the stack is not empty, or open bracket is not encountered
                */
                while (!brackets.isEmpty() && !((char)brackets.peek()=='(') && precedenceVal<=0)
                    stackRead = stackRead + brackets.pop();
                brackets.push(incoming);
                
            }
            
        }
        /*Pop the remaining elements*/
        while (!brackets.isEmpty())
            stackRead = stackRead + brackets.pop();
	
        return stackRead;
        
    }
    /*  0-> Same precedence**************************
        1-> Incoming operator has higher precedence**
       -1-> Stack top has higher precedence**********
    */
   private static int hasHigherprecedence(char incomingOperator, char topofStack){
       //+ and * have highest precedence, hence t-1 is never returned if either operators is + or *
        if(incomingOperator=='*'||incomingOperator=='+'){
            if(topofStack=='*'||topofStack=='+')
                return 0;
            else
                return 1;
        }
        // Or operator has the lowest precedence
        else if(incomingOperator=='|'){
            if(incomingOperator == topofStack)
                return 0;
            else return -1;
        }
        return -2;
   }
}

class Check {
    /*Funtion compares if the final state is achieved by the user input*/
    public static boolean compare(State begin, String input){
    
        ArrayList <State> character = new ArrayList<State> ();
        ArrayList <State> non = new ArrayList<State> ();
        int listID = 0;
        begin.setLastlist(listID);
        /* If there are two paths from the current state,
                and begin state is not null or we haven't reached the last state*/
        
        if(begin.isSplit()&& (begin != null && begin.getLastlist() != listID)){
            addState(character, begin.getOut1(), listID);
            addState(character, begin.getOut2(), listID);
        }
        //Add the start state to the stack otherwise
        else{
            character.add(begin);
        }
        for (int i = 0; i < input.length(); i++){
            char c = input.charAt(i);
            listID = current(character, c, non, listID);
            character = non;
            non = new ArrayList<State>();
        }
        //Flag to check if a match occurs
        boolean flag=false;
        for (int i = 0; i < character.size(); i++){
            State state = character.get(i);
            if (state.isMatch()){
                //A match has occured
                flag=true;
                break;
            }
        }
       return flag;   
    }
    /*Function to return the ID of the current state*/
    private static int current(ArrayList<State> currentList, char c, ArrayList<State> nextList, int listID){
        listID++;
        for (int i = 0; i < currentList.size(); i++){
            State s = currentList.get(i);
            if(c == s.getChar()){
                addState(nextList, s.getOut1(), listID);
            }
        }
        return listID;
    }
    /*Function to add a state s to the arraylist*/ 
    private static void addState(ArrayList<State> list, State s, int listID){
        /*If we have reached the last state, or the state to be added is a null state; return*/
        if (s == null || s.getLastlist() == listID)
            return;
        s.setLastlist(listID);
        /*If there is a split, take both branches and add the state*/
        if(s.isSplit()){
            addState(list, s.getOut1(), listID);
            addState(list, s.getOut2(), listID);
            return;
        }
        list.add(s);
    }
}
/*State class is to define states of the NFA*/
class State {
    
    private char character;
    private State FArrow;   //The first outward arrow
    private State SArrow;   //If split occurs, this stores the second outward arrow
    private boolean diversion;  //True if there is a split
    private boolean match;
    private boolean isChar;     //Checks if it is a char, true if it is
    private int lastlist;   //Checks if it is the last element of the list
    
    //Initializes a new match state with no outgoing transitions
    public State(){
        
        this.FArrow = null;
        this.SArrow = null;
        this.isChar = false;
        this.diversion = false;
        this.match = false;
         this.lastlist = -1;
    }
    /*Constructor with character defined*/
    public State(char character){
        this.character = character;
        this.FArrow = null;
        this.SArrow = null;
        this.isChar = true;
        this.diversion = false;
        this.match = false;
        this.lastlist = -1;
    }
    /*Constructor to initialize by combinig two states*/
    public State(State out1, State out2){
        this.FArrow = out1;
        this.SArrow = out2;
        this.isChar = false;
        this.diversion = true;
        this.match = false;
        this.lastlist = -1;
    }
    /*Function combines two states*/
    public State combineState(State out1, State out2){
        State temp=new State();
        temp.FArrow = out1;
        temp.SArrow = out2;
        temp.isChar = false;
        temp.diversion = true;
        temp.match = false;
        temp.lastlist = -1;
        return temp;
    }
    public char getChar(){
        return character;
    }
    public boolean isLiteral(){
        return isChar;
    }
    public State getOut1(){
        return FArrow;
    }
    
     public State getOut2(){
        return SArrow;
    
    }
     
     public boolean isSplit(){
        return diversion;
    }
    
    public boolean isMatch(){
        return match;
    }
    public int getLastlist(){
        return lastlist;
    }
    public void setLastlist(int listindex){
        lastlist = listindex;
    }
    public void patch(State s){
        if(FArrow == null)
            FArrow = s;
        if(SArrow == null && diversion)
            SArrow = s;
    }
}
/*Stores intermidiate constructs during the construction of the NFA*/
class Component {
    
    private State start;
    private ArrayList<State> out;

    public Component(){
        this.out = null;
        this.start = null;
    }
    //Initializes a new Component with starting state and one outwards-pointing state
    public Component(State start, State out){
        this.out = new ArrayList<State>();
        this.out.add(out);
        this.start = start;
    }
    //Initializes a new Component with starting state and an array of outwards-pointing states
    public Component(State start, ArrayList outArrows){
        this.out = outArrows;
        this.start = start;
    }
    public State getStart() {
        return start;
    }
    public ArrayList<State> getOut() {
        return out;
    }
    @Override
    public String toString() {
        return "\nstartState: "+this.start+" outArrowList: "+this.out;
    }  
}

