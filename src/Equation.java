
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Equation {

  private String resultSide;
  private String operation;
  private String[] operationSide;


  void setResultSide(String resultSide) {
    this.resultSide = resultSide;
  }

  void parseEquation(String equation) {
    //System.out.println("equation: "+equation);
    String[] sides = equation.split("=");
    Pattern pattern = Pattern.compile("[+\\-*/]");
    Matcher matcher;

    for (String side : sides) {

      matcher = pattern.matcher(side);
      if (matcher.find()) {
        setOperationSide(extractVariables(side));
        switch (matcher.group()) {
          case "+":
            setOperation("+");
            break;
          case "-":
            setOperation("-");
            break;
          case "*":
            setOperation("*");
            break;
          case "/":
            setOperation("/");
            break;
          default:
            break;
        }
      } else {
        setResultSide(side);
      }

    }
  }

  private String[] extractVariables(String side) {
    String[] variables = side.split("[+\\-*/]");
    return variables;
  }

  public String toString() {
    return operationSide[0] + operation + operationSide[1] + "=" + resultSide;
  }


  public String getOperation() {

    return new String(operation);
  }

  void setOperation(String operation) {
    this.operation = operation;
  }

  public String[] getOperationSide() {
    return operationSide.clone();
  }

  void setOperationSide(String[] operationSide) {
    this.operationSide = new String[operationSide.length];
    System.arraycopy(operationSide, 0, this.operationSide, 0, operationSide.length);
  }

  public String getResultSide() {
    return new String(resultSide);
  }
}
