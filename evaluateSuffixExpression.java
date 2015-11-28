import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class evaluateSuffixExpression {
	
	//匹配无符号double型数
	private static final Pattern UNSIGNED_DOUBLE = Pattern.compile("((\\d+\\.?\\d*)|(\\.\\d+))(([Ee][+-]?)?\\d+)?.*?");
	private static final Pattern OPERATOR = Pattern.compile("[+-/*].*?");

	public static void main(String[] args) {
		System.out.println("Enter an arithmetic expression(supports + - * / operations):");
		
		Scanner in = new Scanner(System.in);
		String expression = in.nextLine();
		in.close();		
		double result = evaluateExpression(transferToSuffixExpression(expression));
		System.out.println("The result of the expression is: " + result);
	}

	public static String transferToSuffixExpression(String expression) {
		Stack<Character> operations = new Stack<>();
		StringBuilder result = new StringBuilder();
		
		//如果表达式不以无符号double型数或左括号开头，抛出异常
		switch (expression.charAt(0)) {
			case ')':
			case '+':
			case '-':
			case '*':
			case '/':
				throw new IllegalArgumentException("Illegal input expression.");
			default:
				break;
		}
	
		final Pattern LEFT_NORMAL = Pattern.compile("\\(.*?");
		final Pattern RIGHT_NORMAL = Pattern.compile("\\).*?");
		
		String next;
		Character character;
		
		Scanner in = new Scanner(expression);
		 
			while (in.hasNext()) {
				 //读取左括号并压入栈
				 if (in.hasNext(LEFT_NORMAL)) {
					 next = in.findInLine(LEFT_NORMAL);
					 
					 //如果左括号右边是运算符，抛出异常
					 if (in.hasNext(OPERATOR))
						 throw new IllegalArgumentException("Illegal input expression.");
					 
					 character = next.charAt(0);
					 operations.push(character);
				 }
				 //读取无符号double型数并添加到result
				 else if (in.hasNext(UNSIGNED_DOUBLE)) {
					 next = in.findInLine(UNSIGNED_DOUBLE);
					 
					 //如果无符号double型数不位于表达式末尾且右边不是运算符或右括号，抛出异常
					 if (in.hasNext() && (!(in.hasNext(OPERATOR) || (in.hasNext(RIGHT_NORMAL)))))
						 throw new IllegalArgumentException("Illegal input expression.");
					 
					 result.append(next + " ");
				 }
				 //对运算符进行操作
				 else if (in.hasNext(OPERATOR)) {
					 next = in.findInLine(OPERATOR);
					 char ch;
					 
					 //如果运算符右边不是无符号double型数或者左括号，抛出异常
					 if (!(in.hasNext(UNSIGNED_DOUBLE) || in.hasNext(LEFT_NORMAL)))
						 throw new IllegalArgumentException("Illegal input expression.");
					 
					 if ((operations.isEmpty())) {
						 character = next.charAt(0);
						 operations.push(character);
					 }
					 //如果栈中有左括号，需再压入栈，留作之后判断左右括号数量是否相等
					 else if ((ch = operations.pop()) == '(') {
						 operations.push(ch);
						 character = next.charAt(0);
						 operations.push(character);
					 }
					 //因为上面else if 语句即使没有执行语句体部分，栈顶的元素也被弹出了，所以这里直接用ch做参数
					 else if (firstIsLower(ch, next.charAt(0))) {
						 operations.push(ch);
						 character = next.charAt(0);
						 operations.push(character);
					 }
					 //这里直接使用ch与上面的道理相同
					 //上面三个条件都不符合执行此语句
					 else {
						 result.append(ch + " ");
						 
						 character = next.charAt(0);
						 operations.push(character);
					 }
				 }
				 else {
					//不合法表达式，即不是期望的右括号
					 if (!in.hasNext(RIGHT_NORMAL))       
						 throw new IllegalArgumentException("Illegal input expression.");
					 
					 next = in.findInLine(RIGHT_NORMAL);
					 
					 try {
						while ((character = operations.pop()) != '(')
							result.append(character + " ");
					 } catch (EmptyStackException e) {    
						 //右括号数多于左括号数
						 throw new RuntimeException("Illegal input expression.");
					 }		
				}		 
			}	 			
			in.close();
			 
			 while(!operations.isEmpty()) {
				 character = operations.pop();
				//左括号数多于右括号数
				 if (character == '(')                
					 throw new IllegalArgumentException("Illegal input expression.");
				 result.append(character + " ");
			}			
			
			return result.toString(); 
		}
	
	public static double evaluateExpression(String suffixExpression) {
		Stack<Double> numbers = new Stack<>();
		
		Scanner in = new Scanner(suffixExpression);
		
		String next;
		double operand1;
		double operand2;
		 
		while (in.hasNext()) {
			if (in.hasNext(UNSIGNED_DOUBLE)) {
				next = in.findInLine(UNSIGNED_DOUBLE);
				numbers.push(new Double(next));
			}
			else {
				next = in.findInLine(OPERATOR);
				switch (next.charAt(0)) {
					case '+':
						operand2 = numbers.pop();
						operand1 = numbers.pop();
						numbers.push(operand1 + operand2);						
						break;
					case '-':
						operand2 = numbers.pop();
						operand1 = numbers.pop();
						numbers.push(operand1 - operand2);
						break;
					case '*':
						operand2 = numbers.pop();
						operand1 = numbers.pop();
						numbers.push(operand1 * operand2);						
						break;
					case '/':
						operand2 = numbers.pop();
						operand1 = numbers.pop();
						//可能发生除以零的情况，结果可以是常量
						numbers.push(operand1 / operand2);
						break;
				}
			}
		}		
		in.close();
		
		return numbers.pop();
	}
		
	//判断第一个运算符是不是比第二个优先级低
	public static boolean firstIsLower(char first, char second) {
		if (((first == '+') || (first == '-')) && ((second == '*') || (second == '/')))
			return true;
		else 
			return false;
	}
}
