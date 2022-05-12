package main;

import java.util.logging.Logger;

public interface CalcInterface {
    Logger LOGGER = Logger.getGlobal();
    String EXPRESSION_TEXT = "expression";
    String RESULT_TEXT = "result";
    String CLOSING_PARENTHESIS = ")";
    String OPENING_PARENTHESIS = "(";
    String OPERATIONS_AND_PARENTHESIS = "()+-/*";
    String OPERATIONS_LIST = "+-/*";
    String MULTIPLY_AND_DIVIDE = "/*";
    String PLUS = "+";
    String SUBTRACT = "-";
    String MULTIPLY = "*";
    String DIVIDE = "/";
    String WHITESPACE_SYMBOL = " ";
    String EMPTY_STRING = "";
    int MIN_VALUES_RANGE = -10000;
    int MAX_VALUES_RANGE = 10000;
    int SC_CREATED = 201;
    int SC_NO_CONTENT = 204;
    int SC_BAD_REQUEST = 400;
    int SC_FORBIDDEN = 403;
    int SC_CONFLICT = 409;
}
