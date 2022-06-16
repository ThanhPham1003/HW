import java.util.*;
import java.util.List;
public class HW5 {
    private static final Map<Character,List<String>> rule = new HashMap<>();
    private static final Map<Character,Integer> map = new HashMap<>();
    private static final int[][] matrix = new int[][]{
            {-1,-1,-1,-1,-1,0,0,-1,-1,-1,-1,2,2},
            {-1,-1,-1,-1,-1,2,2,0,0,-1,-1,2,2},
            {-1,-1,-1,-1,-1,2,2,2,2,-1,-1,2,2},
            {-1,-1,-1,-1,-1,2,2,-1,-1,-1,-1,2,2},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,2},
            {-1,1,1,0,-1,-1,-1,-1,-1,1,1,-1,2},
            {-1,1,1,0,-1,-1,-1,-1,-1,1,1,-1,2},
            {-1,-1,0,-1,-1,-1,-1,-1,-1,1,1,-1,2},
            {-1,-1,0,-1,-1,-1,-1,-1,-1,1,1,-1,2},
            {-1,-1,-1,-1,-1,2,2,2,2,-1,-1,2,2},
            {1,1,1,1,0,-1,-1,-1,-1,1,1,-1,2},
            {-1,-1,-1,-1,-1,2,2,2,2,-1,-1,2,2},
            {1,1,1,1,1,1,1,1,1,1,1,1,-1}
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.printf("%-20s%-20s%-20s%-20s%s\n","Stack","Precedence","Input","Rule","Note");
        System.out.println("Result : " + (check(input) ? "TRUE" : "FALSE"));
    }

    private static boolean check(String s){
        if(s.length() == 0)
            return false;

        rule.put('A', List.of("A+D","A-D","D"));
        rule.put('B', List.of("B*C","B/C","C"));
        rule.put('C', List.of("a","(E)"));
        rule.put('D', List.of("B"));
        rule.put('E', List.of("A"));
        map.put('A', 0);
        map.put('B', 1);
        map.put('C', 2);
        map.put('D', 3);
        map.put('E', 4);
        map.put('+', 5);
        map.put('-', 6);
        map.put('*', 7);
        map.put('/', 8);
        map.put('a', 9);
        map.put('(', 10);
        map.put(')', 11);
        map.put('#', 12);


        char[] array = s.toCharArray();
        s+="#";
        char[] input = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        stack.add('#');
        boolean res = true;
        for (int i = 0 ; i < array.length ; i++ ) {
            char c = array[i];
            StringBuilder inp = new StringBuilder();
            for(int j = i ; j < input.length; j++)
                inp.append(input[j]);
            if (!map.containsKey(c)) {
                System.out.printf("%-20s%-20s%-20s%-20s%s\n",stack,"----------",inp,"----",c+" doesn't exist");
                res = false;
                break;
            }
            int operator = matrix[map.get(stack.peek())][map.get(c)];
            if (operator == -1) {
                System.out.printf("%-20s%-20s%-20s%-20s%s\n",stack,"----------",inp,"Rule not found","----");
                res = false;
                break;
            } else if (operator == 0 || operator == 1){
                String precedence;
                if(operator == 0){
                    precedence = "=";
                }else {
                    precedence = "<";
                }
                System.out.printf("%-20s%-20s%-20s%-20s%s\n",stack,"     "+precedence,inp,"----","----");
                stack.push(c);
            }
            else {
                System.out.printf("%-20s%-20s%-20s%-20s%s\n",stack,"     >",inp,"----","----");
                StringBuilder str = new StringBuilder();
                int opt;
                boolean find = true;
                do {
                    char character;
                    String rules = null;
                    do {
                        character = stack.pop();
                        str.append(character);
                    } while (matrix[map.get(stack.peek())][map.get(character)] == 0 || matrix[map.get(stack.peek())][map.get(character)] == 2);
                    char tail = stack.peek();
                    for (Map.Entry<Character, List<String>> entry : rule.entrySet()) {
                        if (entry.getValue().contains(str.reverse().toString())) {
                            stack.push(entry.getKey());
                            find = true;
                            rules = entry.getKey() + "->"+ str;
                            str = new StringBuilder();
                            break;
                        } else
                            find = false;
                    }
                    opt = matrix[map.get(tail)][map.get(stack.peek())];
                    System.out.printf("%-20s%-20s%-20s%-20s%s\n",stack,"     >",inp,rules,"----");
                } while (opt == 0 || opt == 2);
                Character c1 = stack.pop();
                for (Map.Entry<Character, List<String>> entry : rule.entrySet()) {
                    if (entry.getValue().contains(String.valueOf(c1))) {
                        stack.push(entry.getKey());
                        System.out.printf("%-20s%-20s%-20s%-20s%s\n",stack,"     <",inp,entry.getKey()+"->"+c1,"----");
                        break;
                    }
                }
                if (!find)
                    res = false;
                stack.push(c);
            }
        }
        if(!res)
            return false;
        System.out.printf("%-20s%-20s%-20s%-20s%s\n",stack,"     >","#","----","----");
        while (true){
            char character;
            StringBuilder str = new StringBuilder();
            int opt;
            boolean find = true;
            character = stack.pop();
            str.append(character);
            opt = matrix[map.get(stack.peek())][map.get(character)];
            if(opt == -1)
                break;
            while (opt == 0 || opt ==2){
                character = stack.pop();
                str.append(character);
                opt = matrix[map.get(stack.peek())][map.get(character)];
            }
            str.reverse();
            for (Map.Entry<Character, List<String>> entry : rule.entrySet()) {
                if (entry.getValue().contains(str.toString())) {
                    stack.push(entry.getKey());
                    find = true;
                    System.out.printf("%-20s%-20s%-20s%-20s%s\n",stack,"     >","#",stack.peek()+ "->" +str,"----");
                    break;
                }
                else{
                    find = false;
                }
            }
            if(!find || (stack.size() == 2 && stack.peek().equals('A')) ){
                break;
            }
        }
        StringBuilder str = new StringBuilder();
        while ( stack.size() != 0 ){
            str.append(stack.pop());
        }
        return str.reverse().toString().equals("#A");
    }
}
