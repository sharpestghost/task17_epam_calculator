package main;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class Calculator  {
    private static final String PLUS = "+";
    private static final String SUBTRACT = "-";
    private static final String MULTIPLY = "*";
    private static final String DIVIDE = "/";

    public synchronized int calculate(Iterable<String> elementsList, Map<String, Integer> args) {
        Deque<Integer> stackOfNumbers = new ArrayDeque<>();
        for (String token : elementsList) {
            switch (token) {
                case PLUS:
                    stackOfNumbers.add(stackOfNumbers.removeFirst() + stackOfNumbers.removeFirst());
                    break;
                case SUBTRACT:
                    stackOfNumbers.add(- stackOfNumbers.removeFirst() + stackOfNumbers.removeFirst());
                    break;
                case MULTIPLY:
                    stackOfNumbers.add(stackOfNumbers.removeFirst() * stackOfNumbers.removeFirst());
                    break;
                case DIVIDE:
                    divideTwoNumbers(stackOfNumbers);
                    break;
                default:
                    addToStack(args, stackOfNumbers, token);
            }
        }
        return stackOfNumbers.removeFirst();
    }

    private void divideTwoNumbers(Deque<Integer> stackOfNumbers) {
        int firstNumber = stackOfNumbers.removeFirst();
        int secondNumber = stackOfNumbers.removeFirst();
        stackOfNumbers.add(secondNumber / firstNumber);
    }

    private void addToStack(Map<String, Integer> args, Deque<Integer> stackOfNumbers, String token) {
        if (args.containsKey(token)) {
            stackOfNumbers.addFirst(args.get(token));
        } else {
            int arg = Integer.parseInt(token);
            stackOfNumbers.addFirst(arg);
        }
    }

}
