package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	expr = expr.replaceAll("\\s+", "");
    	char[] chars = expr.toCharArray();
    	
    	for (int i = 0; i < chars.length; i++) 
    	{
    		// Check for variable, if next char is [, its an array
    		if ((chars[i] >= 'a' && chars[i] <= 'z') || (chars[i] >= 'A' && chars[i] <= 'Z')) 
    		{
    			String varName = "";
    			int j = i;
    			
    			while (j < chars.length && ((chars[j] >= 'a' && chars[j] <= 'z') || (chars[j] >= 'A' && chars[j] <= 'Z'))) 
    			{
    				varName += chars[j];
    				j++;
    			}
    			i = j - 1;
    			
    			if (i == chars.length - 1) 
    			{
        			boolean flag = false;
    				for (int k = 0; k < vars.size(); k++) 
        			{
        				if (vars.get(k).name.equals(varName)) 
        				{
        					flag = true;
        				}
        			}
    				if (!flag) 
    				{
    					Variable var = new Variable(varName);
            			vars.add(var);
    				}
    			}
    			else if (i < chars.length - 1) 
    			{
    				if (chars[i + 1] == '[') 
        			{
    					boolean flag = false;
        				for (int k = 0; k < arrays.size(); k++) 
            			{
            				if (arrays.get(k).name.equals(varName)) 
            				{
            					flag = true;
            				}
            			}
        				if (!flag) 
        				{
        					Array arr = new Array(varName);
            				arrays.add(arr);
        				}
        			}
        			else 
        			{
        				boolean flag = false;
        				for (int k = 0; k < vars.size(); k++) 
            			{
            				if (vars.get(k).name.equals(varName)) 
            				{
            					flag = true;
            				}
            			}
        				if (!flag) 
        				{
        					Variable var = new Variable(varName);
                			vars.add(var);
        				}
        			}
    			}
    		}
    		
    	}
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	expr = expr.replaceAll("\\s+", "");
    	char[] chars = expr.toCharArray();
    	
    	Stack<Float> nums = new Stack<Float>();
    	Stack<Character> ops = new Stack<Character>();
    	Stack<String> arraysStack = new Stack<String>();
    	
    	for (int i = 0; i < chars.length; i++) 
    	{
    		// Check for number
    		if (chars[i] >= '0' && chars[i] <= '9') 
    		{
    			String parsedNum = "";
    			int j = i;
    			
    			while (j < chars.length && chars[j] >= '0' && chars[j] <= '9') 
    			{
    				parsedNum += chars[j];
    				j++;
    			}
    			i = j - 1;
    			nums.push((float)Integer.parseInt(parsedNum));
    		}
    		
    		// Check for variable
    		else if ((chars[i] >= 'a' && chars[i] <= 'z') || (chars[i] >= 'A' && chars[i] <= 'Z')) 
    		{
    			String varName = "";
    			int j = i;
    			
    			while (j < chars.length && ((chars[j] >= 'a' && chars[j] <= 'z') || (chars[j] >= 'A' && chars[j] <= 'Z'))) 
    			{
    				varName += chars[j];
    				j++;
    			}
    			i = j - 1;
    			
    			// If its the last index in the array it has to be a variable
    			if (i == chars.length - 1) 
    			{
    				for (int k = 0; k < vars.size(); k++) 
        			{
        				if (varName.equals(vars.get(k).name)) 
    					{
        					nums.push((float)vars.get(k).value);
        					break;
        				}
        			}
    			}
    			// If it doesn't have a [ after the variable its still a variable
    			else if (i <= chars.length - 1 && chars[i + 1] != '[') 
    			{
    				for (int k = 0; k < vars.size(); k++) 
        			{
        				if (varName.equals(vars.get(k).name)) 
    					{
        					nums.push((float)vars.get(k).value);
        					break;
        				}
        			}
    			}
    			// If it has a [ after the variable it has to be an array
    			else if (i <= chars.length - 1 && chars[i + 1] == '[') 
    			{
    				for (int k = 0; k < arrays.size(); k++) 
    				{
    					if (varName.equals(arrays.get(k).name)) 
    					{
    						arraysStack.push(arrays.get(k).name);
    					}
    				}
    			}
    			
    		}
    		// Check for left parenthesis
    		else if (chars[i] == '(') 
    		{
    			ops.push(chars[i]);
    		}
    		
    		// Check for right parenthesis
    		else if (chars[i] == ')') 
    		{
    			while (ops.peek() != '(') 
    			{
    				nums.push(doOperation(ops.pop(), nums.pop(), nums.pop()));
    			}
    			ops.pop();
    		}
    		// Check for left bracket
    		else if (chars[i] == '[') 
    		{
    			ops.push(chars[i]);
    		}
    		
    		// Check for right bracket
    		else if (chars[i] == ']') 
    		{
    			while (ops.peek() != '[') 
    			{
    				nums.push(doOperation(ops.pop(), nums.pop(), nums.pop()));
    			}
    			float temp = nums.pop();
				for (int k = 0; k < arrays.size(); k++) 
				{
					if (arraysStack.peek().equals(arrays.get(k).name)) 
					{
						nums.push((float)arrays.get(k).values[(int) temp]);
						arraysStack.pop();
						break;
					}
				}
				ops.pop();
    		}
    		
    		// Check for operator
    		else if (chars[i] == '+' || chars[i] == '-' || chars[i] == '*' || chars[i] == '/') 
    		{
    			while (!ops.isEmpty() && hasPrecedence(chars[i], ops.peek())) 
    			{
    				nums.push(doOperation(ops.pop(), nums.pop(), nums.pop()));
    			}
    			ops.push(chars[i]);
    		}
    	}
    	
    	while (!ops.isEmpty())
    	{
    		nums.push(doOperation(ops.pop(), nums.pop(), nums.pop()));
    	}
    	
    	return nums.pop();
    }
    private static boolean hasPrecedence(char currentOp, char stackOp) 
    {
    	if (stackOp == '(' || stackOp == ')') 
    	{
    		return false;
    	}
    	if (stackOp == '[' || stackOp == ']') 
    	{
    		return false;
    	}
    	
    	if ((currentOp == '*' || currentOp == '/') && (stackOp == '+' || stackOp == '-')) 
    	{
    		return false;
    	}
    	else 
    	{
    		return true;
    	}
    }
    private static float doOperation(char op, float num2, float num1) 
    {
    	switch(op) 
    	{
    	case '+':
    		return num1 + num2;
    	case '-': 
    		return num1 - num2;
    	case '*':
    		return num1 * num2;
    	case '/':
    		if (num2 == 0) 
    		{
    			throw new UnsupportedOperationException("Can't divide by zero");
    		}
    		return num1 / num2;
    	}
    	return 0;
    }
}
