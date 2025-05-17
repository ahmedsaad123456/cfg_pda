import java.io.*;
import java.util.*;

public class CFG {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/input_cfg.txt"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/output_cfg.txt"));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.equals("1")) {
                    new Problem1CFG(br, bw);
                } else if (line.equals("2")) {
                    new Problem2CFG(br, bw);
                } else if (line.equals("3")) {
                    new Problem3CFG(br, bw);
                } else if (line.equals("4")) {
                    new Problem4CFG(br, bw);
                } else if (line.equals("5")) {
                    new Problem5CFG(br, bw);
                }
            }

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class CFGClass {
    private ArrayList<Character> terminals;
    private ArrayList<Character> nonTerminals;
    private Character startSymbol;
    private Map<Character, ArrayList<String>> productions;

    CFGClass(ArrayList<Character> terminals, ArrayList<Character> nonTerminals,
             Character startSymbol, Map<Character, ArrayList<String>> productions) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.startSymbol = startSymbol;
        this.productions = productions;
    }

    public boolean derive(String currentDerivation, String text) {

        if (currentDerivation.equals(text)) {
            return true;
        }

        int len = 0;
        for (int i = 0; i < currentDerivation.length(); i++) {
            char currentChar = currentDerivation.charAt(i);
            if (terminals.contains(currentChar)) {
                len++;
            }
        }

        if (len > text.length()) {
            return false;
        }



        boolean hasDerivation = false;
        for (int i = 0; i < currentDerivation.length(); i++) {
            char currentChar = currentDerivation.charAt(i);
            if (nonTerminals.contains(currentChar)) {
                ArrayList<String> productionRules = productions.get(currentChar);
                for (String rule : productionRules) {
                    if(rule.equals("ε")){
                        String newDerivation = currentDerivation.substring(0, i) + currentDerivation.substring(i + 1);
                        hasDerivation = derive(newDerivation, text);
                        if (hasDerivation) {
                            return true;
                        }
                        continue;
                    }
                    String newDerivation = currentDerivation.substring(0, i) + rule + currentDerivation.substring(i + 1);
                    hasDerivation = derive(newDerivation, text);
                    if (hasDerivation) {
                        return true;
                    }
                }
                return false;
            }
        }

       return false;




    }


    public void solveProblem(BufferedReader br, BufferedWriter bw) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.equals("end")) {
            System.out.println(line);
            boolean result = derive(startSymbol.toString(), line);
            bw.write(result ? "accepted" : "not accepted");
            bw.newLine();
        }
        bw.write("x");
        bw.newLine();
    }
}

class Problem1CFG {

    public Problem1CFG(BufferedReader br, BufferedWriter bw) throws IOException {
        bw.write("1");
        bw.newLine();

        ArrayList<Character> terminals = new ArrayList<Character>(Arrays.asList('a','b'));
        ArrayList<Character> nonTerminals = new ArrayList<Character>(Arrays.asList('S'));
        Character startSymbol = 'S';
        Map<Character,ArrayList<String>> productionRules = new HashMap<Character,ArrayList<String>>();
        ArrayList<String> production_S = new ArrayList<>(Arrays.asList("aSbS", "bSaS" ,"ε"));
        productionRules.put('S', production_S);

        CFGClass cfg = new CFGClass(terminals,nonTerminals,startSymbol,productionRules);

        cfg.solveProblem(br, bw);
    }
}

class Problem2CFG {
    public Problem2CFG(BufferedReader br, BufferedWriter bw) throws IOException {
        bw.write("2");
        bw.newLine();


        ArrayList<Character> terminals = new ArrayList<Character>(Arrays.asList('a','b'));
        ArrayList<Character> nonTerminals = new ArrayList<Character>(Arrays.asList('S'));
        Character startSymbol = 'S';
        Map<Character,ArrayList<String>> productionRules = new HashMap<Character,ArrayList<String>>();
        ArrayList<String> production_S = new ArrayList<>(Arrays.asList("aSbSbS", "bSaSbS","bSbSaS","ε"));
        productionRules.put('S', production_S);


        CFGClass cfg = new CFGClass(terminals,nonTerminals,startSymbol,productionRules);

        cfg.solveProblem(br, bw);
    }
}

class Problem3CFG {
    public Problem3CFG(BufferedReader br, BufferedWriter bw) throws IOException {
        bw.write("3");
        bw.newLine();

        ArrayList<Character> terminals = new ArrayList<Character>(Arrays.asList('a', 'b'));
        ArrayList<Character> nonTerminals = new ArrayList<Character>(Arrays.asList('S', 'A'));
        Character startSymbol = 'S';
        Map<Character, ArrayList<String>> productionRules = new HashMap<Character, ArrayList<String>>();
        
        ArrayList<String> production_S = new ArrayList<>(Arrays.asList("aSa", "bSb","aAb","bAa"));
        ArrayList<String> production_A = new ArrayList<>(Arrays.asList("aA", "bA","ε"));
        
        productionRules.put('S', production_S);
        productionRules.put('A', production_A);

        CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules);

        cfg.solveProblem(br, bw);
    }
}

class Problem4CFG {
    public Problem4CFG(BufferedReader br, BufferedWriter bw) throws IOException {
        bw.write("4");
        bw.newLine();

        ArrayList<Character> terminals = new ArrayList<Character>(Arrays.asList('a','b'));
        ArrayList<Character> nonTerminals = new ArrayList<Character>(Arrays.asList('S','T'));
        Character startSymbol = 'S';
        Map<Character,ArrayList<String>> productionRules = new HashMap<Character,ArrayList<String>>();
        ArrayList<String> production_S = new ArrayList<>(Arrays.asList("aaSb","aaa"));
        productionRules.put('S', production_S);
        CFGClass cfg = new CFGClass(terminals,nonTerminals,startSymbol,productionRules);

        cfg.solveProblem(br, bw);
    }
}

class Problem5CFG {
    public Problem5CFG(BufferedReader br, BufferedWriter bw) throws IOException {
        bw.write("5");
        bw.newLine();

        ArrayList<Character> terminals = new ArrayList<Character>(Arrays.asList('a','b'));
        ArrayList<Character> nonTerminals = new ArrayList<Character>(Arrays.asList('S'));
        Character startSymbol = 'S';
        Map<Character,ArrayList<String>> productionRules = new HashMap<Character,ArrayList<String>>();
        ArrayList<String> production_S = new ArrayList<>(Arrays.asList("aS","aSb","a"));
        productionRules.put('S', production_S);
        CFGClass cfg = new CFGClass(terminals,nonTerminals,startSymbol,productionRules);

        cfg.solveProblem(br, bw);
    }
}