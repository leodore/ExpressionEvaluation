public class EvaluateSuffixExpressionTest {
  public static void main(String[] args) {
  		System.out.println("Enter an arithmetic expression(supports + - * / operations):");
  		
  		Scanner in = new Scanner(System.in);
  		String expression = in.nextLine();
  		in.close();		
  		double result = evaluateExpression(transferToSuffixExpression(expression));
  		System.out.println("The result of the expression is: " + result);
  	}
	}
