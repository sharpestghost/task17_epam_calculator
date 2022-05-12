package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class EquationCreator {
    private static final Logger LOGGER = Logger.getGlobal();
    private static final String CLOSING_PARENTHESIS = ")";
    private static final String OPENING_PARENTHESIS = "(";
    private static final String OPERATIONS_AND_PARENTHESIS = "()+-/*";
    private static final String OPERATIONS_LIST = "+-/*";
    private static final String MULTIPLY_AND_DIVIDE = "/*";
    private static final String PLUS = "+";
    private static final String SUBTRACT = "-";
    private static final String MULTIPLY = "*";
    private static final String DIVIDE = "/";
    private static final String WHITESPACE_SYMBOL = " ";
    private static final String EMPTY_STRING = "";

    public synchronized List<String> sortEquationElements(String equation) {
        List<String> tokensList = new ArrayList<>();
        Deque<String> symobolsStack = new ArrayDeque<>();
        List<String> equationElementsNotation = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(deleteWhitespaceInEquation(equation),
                OPERATIONS_AND_PARENTHESIS, true);
        while (tokenizer.hasMoreElements()) {
            tokensList.add(tokenizer.nextToken());
        }
        addEquationTokens(tokensList, symobolsStack, equationElementsNotation);
        addRemainingTokens(symobolsStack, equationElementsNotation);
        return equationElementsNotation;
    }

    private void addEquationTokens(List<String> tokens, Deque<String> stackSymbols,
                                   List<String> elementsList) {
        for (String token : tokens) {
            try {
                switch (token) {
                    case PLUS:
                    case SUBTRACT:
                    case CLOSING_PARENTHESIS:
                        addAllSymbols(stackSymbols, elementsList, token);
                        break;
                    case MULTIPLY:
                    case DIVIDE:
                        addMultiplicationAndDivisionSymbols(stackSymbols, elementsList, token);
                        break;
                    case OPENING_PARENTHESIS:
                        stackSymbols.addFirst(token);
                        break;
                    default:
                        elementsList.add(token);
                }
            } catch (NoSuchElementException exception) {
                LOGGER.info(exception.getMessage());
                if (!token.equals(CLOSING_PARENTHESIS)) {
                    stackSymbols.addFirst(token);
                }
            }
        }
    }

    private void addRemainingTokens(Deque<String> symbolsStack, List<String> equationElementsNotation) {
        while (!symbolsStack.isEmpty()) {
            if (symbolsStack.getFirst().equals(OPENING_PARENTHESIS)) {
                return;
            }
            equationElementsNotation.add(symbolsStack.removeFirst());
        }
    }

    private String deleteWhitespaceInEquation(String equation) {
        return equation.replace(WHITESPACE_SYMBOL, EMPTY_STRING);
    }

    private void addAllSymbols(Deque<String> stackSymbols, List<String> elementsList, String token) {
        while (OPERATIONS_LIST.contains(stackSymbols.getFirst())) {
            elementsList.add(stackSymbols.removeFirst());
        }
        if (token.equals(CLOSING_PARENTHESIS)) {
            stackSymbols.removeFirst();
        } else {
            stackSymbols.addFirst(token);
        }
    }

    private void addMultiplicationAndDivisionSymbols(Deque<String> stackSymbols, List<String> equationElementsNotation,
                                                     String token) {
        while (MULTIPLY_AND_DIVIDE.contains(stackSymbols.getFirst())) {
            equationElementsNotation.add(stackSymbols.removeFirst());
        }
        stackSymbols.addFirst(token);
    }
}
