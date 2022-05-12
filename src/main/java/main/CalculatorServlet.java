package main;

import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet(urlPatterns = {"/calc/*"})
public class CalculatorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getGlobal();

    private static final String EXPRESSION_TEXT = "expression";
    private static final String RESULT_TEXT = "result";
    private static final String OPERATIONS_LIST = "+-/*";
    private static final String EMPTY_STRING = "";
    private static final int MIN_VALUES_RANGE = -10000;
    private static final int MAX_VALUES_RANGE = 10000;
    private static final int SC_CREATED = 201;
    private static final int SC_NO_CONTENT = 204;
    private static final int SC_BAD_REQUEST = 400;
    private static final int SC_FORBIDDEN = 403;
    private static final int SC_CONFLICT = 409;

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String requestArgument = new StringBuilder(request.getPathInfo()).substring(1);

        if (session.getAttribute(requestArgument) == null) {
            response.setStatus(SC_CREATED);
        }

        if (requestArgument.equals(EXPRESSION_TEXT)) {
            checkingExpression(request, response, requestArgument);
        } else {
            checkingValueSize(request, response, requestArgument);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String variableForDelete = new StringBuilder(request.getPathInfo()).substring(1);
        session.setAttribute(variableForDelete, null);
        response.setStatus(SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Calculator calculator = new Calculator();
        EquationCreator equation = new EquationCreator();
        try {
            List<String> tokens = equation.sortEquationElements(session.getAttribute(EXPRESSION_TEXT).toString());
            Map<String, String> childVariableMap = new HashMap<>();
            Enumeration<String> allNamesAttributes = session.getAttributeNames();
            Map<String, Integer> argumentsMap = attributeParse(session, allNamesAttributes, childVariableMap);
            for (Map.Entry<String, String> keyVariable : childVariableMap.entrySet()) {
                Integer valueVariable = argumentsMap.get(childVariableMap.get(keyVariable.getKey()));
                argumentsMap.put(keyVariable.getKey(), valueVariable);
            }

            int result = calculator.calculate(tokens, argumentsMap);
            session.setAttribute(RESULT_TEXT, result);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            response.setStatus(SC_CONFLICT);
        }

        try {
            response.getWriter().print(session.getAttribute(RESULT_TEXT));
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage());
        }
    }

    private void checkingExpression(HttpServletRequest request, HttpServletResponse response, String keyInfo) {
        String valueData = readALineOfText(request);
        if (valueData.chars().mapToObj(i->(char)i).noneMatch(i -> OPERATIONS_LIST.contains(String.valueOf(i)))) {
            response.setStatus(SC_BAD_REQUEST);
            return;
        }
        HttpSession session = request.getSession();
        session.setAttribute(keyInfo, valueData);
    }

    private void checkingValueSize(HttpServletRequest request, HttpServletResponse response, String keyInfo) {
        HttpSession session = request.getSession();
        String valueData = EMPTY_STRING;
        try {
            valueData = readALineOfText(request);
            int number = Integer.parseInt(valueData);
            if (number < MIN_VALUES_RANGE || number > MAX_VALUES_RANGE) {
                response.setStatus(SC_FORBIDDEN);
                return;
            }
            session.setAttribute(keyInfo, valueData);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            session.setAttribute(keyInfo, valueData);
            response.setStatus(SC_BAD_REQUEST);
        }
    }

    private Map<String, Integer> attributeParse(HttpSession session, Enumeration<String> allNamesAttributes,
                                Map<String, String> childVariableMap) {
        Map<String, Integer> argumentMap = new HashMap<>();
        while (allNamesAttributes.hasMoreElements()) {
            String attributeName = allNamesAttributes.nextElement();
            if (attributeName.length() != 1) {
                continue;
            }
            putAttributesInArgs(session, argumentMap, childVariableMap, attributeName);
        }
        return argumentMap;
    }

    private void putAttributesInArgs(HttpSession session, Map<String, Integer> args,
                                     Map<String, String> variablesHavingNameOfAnotherVariable, String attributeName) {
        String valueOfAttribute = session.getAttribute(attributeName).toString();
        try {
            args.put(attributeName, Integer.parseInt(valueOfAttribute));
        } catch (NumberFormatException e) {
            variablesHavingNameOfAnotherVariable.put(attributeName, valueOfAttribute);
        }
    }

    private String readALineOfText(ServletRequest request) {
        String valueData = EMPTY_STRING;
        try {
            valueData = request.getReader().readLine();
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage());
        }
        return valueData;
    }




}