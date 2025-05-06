import java.io.*;
import java.util.*;

public class PDA {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/input_pda.txt"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/output_pda.txt"));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("1")) {
                    new Problem1PDA(br, bw);
                } else if (line.equals("2")) {
                    new Problem2PDA(br, bw);
                } else if (line.equals("3")) {
                    new Problem3PDA(br, bw);
                } else if (line.equals("4")) {
                    new Problem4PDA(br, bw);
                } else if (line.equals("5")) {
                    new Problem5PDA(br, bw);
                }
            }

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class TransitionKey {
    private int currentState;
    private char input;
    private char stackTop;

    TransitionKey(int currentState, char input, char stackTop) {
        this.currentState = currentState;
        this.input = input;
        this.stackTop = stackTop;
    }

    public int getCurrentState() {
        return currentState;
    }

    public char getInput() {
        return input;
    }

    public char getStackTop() {
        return stackTop;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransitionKey that = (TransitionKey) o;
        return currentState == that.currentState &&
                input == that.input &&
                stackTop == that.stackTop;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentState, input, stackTop);
    }


}

class TransitionValue {
    private int nextState;
    private String stackPush;

    TransitionValue(int nextState, String stackPush) {
        this.nextState = nextState;
        this.stackPush = stackPush;
    }

    public int getNextState() {
        return nextState;
    }

    public String getStackPush() {
        return stackPush;
    }

}

class TransitionFunction {
    private Map<TransitionKey, List<TransitionValue>> transitionMap = new HashMap<>();


    public TransitionFunction() {

    }

    public void addTransition(int currentState, char input, char stackTop, int nextState, String stackPush) {
        TransitionKey key = new TransitionKey(currentState, input, stackTop);
        TransitionValue value = new TransitionValue(nextState, stackPush);
        transitionMap.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public List<TransitionValue> getTransitions(int currentState, char input, char stackTop) {
        TransitionKey key = new TransitionKey(currentState, input, stackTop);
        return transitionMap.getOrDefault(key, new ArrayList<>());
    }


}

class PDAClass {
    private ArrayList<Integer> states;
    private ArrayList<Character> inputAlphabet;
    private ArrayList<Character> stackAlphabet;
    private TransitionFunction transitionFunction;
    private int startState;
    private ArrayList<Integer> finalStates;
    private char stackInitial;

    PDAClass(ArrayList<Integer> states, ArrayList<Character> inputAlphabet,
             ArrayList<Character> stackAlphabet, TransitionFunction transitionFunction,
             int startState, ArrayList<Integer> finalStates, char stackInitial) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.stackAlphabet = stackAlphabet;
        this.transitionFunction = transitionFunction;
        this.startState = startState;
        this.finalStates = finalStates;
        this.stackInitial = stackInitial;
    }

    public boolean isAccepted(String input) {
        // Check if input is empty and start state is a final state
        if (input.isEmpty() && finalStates.contains(startState)) {
            return true;
        }

        Stack<Character> stack = new Stack<>();
        stack.push(stackInitial);
        // Start DFS from the state 1 as the first state will push the stack initial
        return dfs(1, 0, input, stack);
    }


    private boolean dfs(int currentState, int inputIndex, String input, Stack<Character> stack) {
        // Base case
        // Check if we are at the end of the input + 1 and the current state is a final state
        // input + 1 because if we read the last character will pass the index + 1
        if (inputIndex == input.length() + 1 && finalStates.contains(currentState)) {
            return true;
        }


        // Check if we are at the end of the input + 1
        if(inputIndex > input.length()+1) {
            return false;
        }



        // Get the current input character and stack top
        char inputChar = (inputIndex < input.length()) ? input.charAt(inputIndex) : 'ε';
        char stackTop = stack.isEmpty() ? 'ε' : stack.peek();

        // if the input character is 'ε' and the stack top is 'ε', we can skip this transition
        if(inputChar == 'ε' && stackTop == 'ε') {
            return false;
        }

        // Get all possible transitions
        List<TransitionValue> transitions = new ArrayList<>();
        transitions.addAll(transitionFunction.getTransitions(currentState, inputChar, stackTop));
        transitions.addAll(transitionFunction.getTransitions(currentState, inputChar, 'ε'));
        transitions.addAll(transitionFunction.getTransitions(currentState, 'ε', stackTop));
        transitions.addAll(transitionFunction.getTransitions(currentState, 'ε', 'ε'));

        // loop through all the transitions
        for (TransitionValue transition : transitions) {

            // get the next state and stack push
            int nextState = transition.getNextState();
            String stackPush = transition.getStackPush();

            Stack<Character> newStack = new Stack<>();
            newStack.addAll(stack);

            // Pop if needed when the stack top is not 'ε' and the current transition will pop the stackTop
            if ((stackTop != 'ε') && newStack.peek() == stackTop &&(
                    (transitionFunction.getTransitions(currentState, inputChar, stackTop).contains(transition))
                            ||   (transitionFunction.getTransitions(currentState, 'ε', stackTop).contains(transition))
                    )) {
                newStack.pop();
            }




            // Push if needed when the stack push is not 'ε'
            if (!stackPush.equals("ε")) {
                    newStack.push(stackPush.charAt(0));

            }

            // Check if the next input index is valid
            // if the current transition read char then increment the input index
            int nextInputIndex = (transitionFunction.getTransitions(currentState, inputChar, stackTop).contains(transition))
                    ||   (transitionFunction.getTransitions(currentState, inputChar, 'ε').contains(transition))
                    ? inputIndex + 1
                    : inputIndex;


            // Recursively call dfs with the next state, next input index, and new stack
            if (dfs(nextState, nextInputIndex, input, newStack)) {
                return true;
            }
        }



        return false;
    }


    public void solveProblem(BufferedReader br, BufferedWriter bw) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.equals("end")) {
            boolean result = isAccepted(line);
            bw.write(result ? "accepted" : "not accepted");
            bw.newLine();
        }
        bw.write("x");
        bw.newLine();
    }
}

class Problem1PDA {
    public Problem1PDA(BufferedReader br, BufferedWriter bw) throws IOException {
        bw.write("1");
        bw.newLine();

        ArrayList<Integer> states = new ArrayList<Integer>(Arrays.asList(0,1,2 ,3 ,4 , 5 ,6));
        ArrayList<Integer> finalStates = new ArrayList<Integer>(Arrays.asList(0 , 2 , 6));
        ArrayList<Character> inputAlphabet = new ArrayList<Character>(Arrays.asList('a','b' , 'c'));
        ArrayList<Character> stackAlphabet = new ArrayList<Character>(Arrays.asList('$','a'));
        int startState = 0;
        Character stackInitial = '$';
        TransitionFunction transitionFunction = new TransitionFunction();
        transitionFunction.addTransition(0,'ε','ε',1, "$");
        transitionFunction.addTransition(1,'ε','ε',3, "ε");
        transitionFunction.addTransition(1,'ε','ε',2, "ε");
        transitionFunction.addTransition(2,'b','ε',2, "ε");
        transitionFunction.addTransition(3,'a','ε',3, "a");
        transitionFunction.addTransition(3,'c','a',4, "ε");
        transitionFunction.addTransition(3,'b','ε',5, "ε");
        transitionFunction.addTransition(4,'c','a',4, "ε");
        transitionFunction.addTransition(4,'ε','$',6, "ε");
        transitionFunction.addTransition(5,'b','ε',5, "ε");
        transitionFunction.addTransition(5,'c','a',4, "ε");

        // add the rest of transitions.
        PDAClass pda = new PDAClass(states,inputAlphabet,stackAlphabet,transitionFunction,startState,finalStates,stackInitial);

        pda.solveProblem(br, bw);
    }
}

class Problem2PDA {
    public Problem2PDA(BufferedReader br, BufferedWriter bw) throws IOException {
        bw.write("2");
        bw.newLine();

        ArrayList<Integer> states = new ArrayList<Integer>(Arrays.asList(0,1,2));
        ArrayList<Integer> finalStates = new ArrayList<Integer>(Arrays.asList(2));
        ArrayList<Character> inputAlphabet = new ArrayList<Character>(Arrays.asList('a','b'));
        ArrayList<Character> stackAlphabet = new ArrayList<Character>(Arrays.asList('$','a'));
        int startState = 0;
        Character stackInitial = '$';
        TransitionFunction transitionFunction = new TransitionFunction();
        transitionFunction.addTransition(1,'ε','ε',2, "$");
        TransitionFunction productionRules = new TransitionFunction();
        // add the rest of transitions.
        PDAClass pda = new PDAClass(states,inputAlphabet,stackAlphabet,productionRules,startState,finalStates,stackInitial);

        pda.solveProblem(br, bw);
    }
}

class Problem3PDA {
    public Problem3PDA(BufferedReader br, BufferedWriter bw) throws IOException {
        bw.write("3");
        bw.newLine();

        ArrayList<Integer> states = new ArrayList<Integer>(Arrays.asList(0,1,2));
        ArrayList<Integer> finalStates = new ArrayList<Integer>(Arrays.asList(2));
        ArrayList<Character> inputAlphabet = new ArrayList<Character>(Arrays.asList('a','b'));
        ArrayList<Character> stackAlphabet = new ArrayList<Character>(Arrays.asList('$','a'));
        int startState = 0;
        Character stackInitial = '$';
        TransitionFunction transitionFunction = new TransitionFunction();
        transitionFunction.addTransition(1,'ε','ε',2, "$");
        TransitionFunction productionRules = new TransitionFunction();
        // add the rest of transitions.
        PDAClass pda = new PDAClass(states,inputAlphabet,stackAlphabet,productionRules,startState,finalStates,stackInitial);

        pda.solveProblem(br, bw);
    }
}

class Problem4PDA {
    public Problem4PDA(BufferedReader br, BufferedWriter bw) throws IOException {
        bw.write("4");
        bw.newLine();

        ArrayList<Integer> states = new ArrayList<Integer>(Arrays.asList(0,1,2));
        ArrayList<Integer> finalStates = new ArrayList<Integer>(Arrays.asList(2));
        ArrayList<Character> inputAlphabet = new ArrayList<Character>(Arrays.asList('a','b'));
        ArrayList<Character> stackAlphabet = new ArrayList<Character>(Arrays.asList('$','a'));
        int startState = 0;
        Character stackInitial = '$';
        TransitionFunction transitionFunction = new TransitionFunction();
        transitionFunction.addTransition(1,'ε','ε',2, "$");
        TransitionFunction productionRules = new TransitionFunction();
        // add the rest of transitions.
        PDAClass pda = new PDAClass(states,inputAlphabet,stackAlphabet,productionRules,startState,finalStates,stackInitial);

        pda.solveProblem(br, bw);
    }
}

class Problem5PDA {
    public Problem5PDA(BufferedReader br, BufferedWriter bw) throws IOException {
        ArrayList<Integer> states = new ArrayList<Integer>(Arrays.asList(0,1,2));
        ArrayList<Integer> finalStates = new ArrayList<Integer>(Arrays.asList(2));
        ArrayList<Character> inputAlphabet = new ArrayList<Character>(Arrays.asList('a','b'));
        ArrayList<Character> stackAlphabet = new ArrayList<Character>(Arrays.asList('$','a'));
        int startState = 0;
        Character stackInitial = '$';
        TransitionFunction transitionFunction = new TransitionFunction();
        transitionFunction.addTransition(1,'ε','ε',2, "$");
        TransitionFunction productionRules = new TransitionFunction();
        // add the rest of transitions.
        PDAClass pda = new PDAClass(states,inputAlphabet,stackAlphabet,productionRules,startState,finalStates,stackInitial);
        pda.solveProblem(br, bw);

    }
}