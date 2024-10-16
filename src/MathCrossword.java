import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sosy_lab.common.ShutdownManager;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.BasicLogManager;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.BooleanFormulaManager;
import org.sosy_lab.java_smt.api.FormulaManager;
import org.sosy_lab.java_smt.api.IntegerFormulaManager;
import org.sosy_lab.java_smt.api.Model;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverException;

/**
 * This program solves the given math crossword using an SMT solver via the JavaSMT API.
 *
 * <p>You are allowed to use JavaSMT and all packages/dependencies present in JavaSMT (e.g. Guava).
 * You can import this file into the examples folder of JavaSMT if you want.
 * Then you only need to install JavaSMT and no further setup is required.
 * Please adhere to good software development practices!
 * Also, please document how another user can execute your code!
 *
 * <p>The goal of this task is to parse the crossword
 * (you may expect different crosswords, but all of them in the given size),
 * create formulas based on the layout of the
 * crossword, let an arbitrary SMT solver create a solution (model) and replace all _ characters
 * with a (correct) number. All numbers should only range from 0 to 9.
 *
 * <p>The crossword should be copied as a single String and should be formatted as follows
 * (without the * and leading whitespaces):
 *
 * <pre>
 * 1+_=6   _
 *   / /   +
 * _-_=3 _ 2
 * * = = / =
 * _ 1 _*4=_
 * =     =
 * 8 5-_=1 3
 *   + +   *
 * 8/_=_   1
 *   = =   =
 *   _ 8-_=_
 * </pre>
 *
 * <p>The solution will then be printed on StdOut simillarly to the input,
 * but with all _ replaced by numbers. Example (not to be used!) below:
 *
 * <pre>
 * 1+5=6   6
 *   / /   +
 * 8-5=3 4 2
 * * = = / =
 * 1 1 2*4=8
 * =     =
 * 8 5-4=1 3
 *   + +   *
 * 8/2=4   1
 *   = =   =
 *   7 8-5=3
 * </pre>
 *
 * <p> Bonus question: Do all solvers return the same model?
 * What differences can be observed if they don't?
 * Why are some solvers not usable and what would you need to change to be able to use them? (Theory question only. No code required.)
 * <ul>
 *  <li>Some solvers do not support some operations. SMTInterpol does not support multiplication for variables as an example.
 *  In this case multiplication through multiple additions can solve the problem.</li>
 * </ul>
 *
 * <p>To use this code, it is important to make sure the selected solver supports the operations '+' '-' '*' '/'.
 * The classes Crossword and Equation help identify equations and variables for later processing to create IntegerFormulas and Boolean Formulas.
 * Executing the code will solve a crossword given in the problem description using the Princess solver.
 *
 * @see Crossword
 * @see Equation
 */
public class MathCrossword {
  public static final String input =
      "1+_=6   _\n"
          + "  / /   +\n"
          + "_-_=3 _ 2\n"
          + "* = = / =\n"
          + "_ 1 _*4=_\n"
          + "=     =  \n"
          + "8 5-_=1 3\n"
          + "  + +   *\n"
          + "8/_=_   1\n"
          + "  = =   =\n"
          + "  _ 8-_=_";
  private static Map<String, IntegerFormula> variables = new HashMap<>();
  private static List<BooleanFormula> booleanFormulas = new ArrayList<>();

  public static void main(String[] args) throws InvalidConfigurationException {

    Configuration config = Configuration.fromCmdLineArguments(args);
    LogManager logger = BasicLogManager.create(config);
    ShutdownManager shutdown = ShutdownManager.create();

    // SolverContext is a class wrapping a solver context.
    // Solver can be selected either using an argument or a configuration option
    // inside `config`.
    SolverContext context = SolverContextFactory.createSolverContext(
        config, logger, shutdown.getNotifier(), SolverContextFactory.Solvers.PRINCESS);

    Crossword crossword = new Crossword(input);
    System.out.println(crossword.toString());
    System.out.println("--------------");
    crossword.parseCrossword();

    FormulaManager fmgr = context.getFormulaManager();

    BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
    IntegerFormulaManager imgr = fmgr.getIntegerFormulaManager();

    generateBooleanFormulas(crossword, imgr);
    BooleanFormula constraint = bmgr.and(booleanFormulas);

    try (ProverEnvironment prover = context.newProverEnvironment(
        SolverContext.ProverOptions.GENERATE_MODELS)) {
      prover.addConstraint(constraint);

      for (IntegerFormula var : variables.values()) {
        prover.addConstraint(imgr.greaterOrEquals(var, imgr.makeNumber(0)));
        prover.addConstraint(imgr.lessOrEquals(var, imgr.makeNumber(9)));
      }

      boolean isUnsat = prover.isUnsat();
      if (!isUnsat) {
        Model model = prover.getModel();
        Map<String, Integer> variableValues = new HashMap<>();
        for (Map.Entry<String, IntegerFormula> entry : variables.entrySet()) {
          String variableName = entry.getKey();
          IntegerFormula variable = entry.getValue();
          int value = model.evaluate(variable).intValue();
          variableValues.put(variableName, value);
        }
        crossword.updateVariables(variableValues);
        crossword.replaceVariablesWithValues();
        System.out.println(crossword.toString());
      } else {
        System.out.println("Unsatisfiable");
      }
    } catch (SolverException | InterruptedException e) {
      e.printStackTrace();
    }


  }

  /**
   * Generates the boolean formulas for the given crossword and integer formula manager.
   *
   * @param crossword the crossword object containing the equations and variables
   * @param imgr      the integer formula manager used to create integer formulas
   */
  private static void generateBooleanFormulas(Crossword crossword,
                                              IntegerFormulaManager imgr) {
    Map<String, Integer> variablesMap = crossword.getVariables();
    List<String> variablesNames = new ArrayList<>(variablesMap.keySet());
    List<Equation> equations = crossword.getEquations();

    generateVariables(variablesNames, imgr);

    for (Equation equation : equations) {
      String operation = equation.getOperation();
      String[] operationSide = equation.getOperationSide();

      switch (operation) {
        case "+":
          booleanFormulas.add(
              imgr.equal(
                  imgr.add(
                      variables.get(operationSide[0]),
                      variables.get(operationSide[1])
                  ), variables.get(equation.getResultSide())
              ));
          break;
        case "-":
          booleanFormulas.add(
              imgr.equal(
                  imgr.subtract(
                      variables.get(operationSide[0]),
                      variables.get(operationSide[1])
                  ), variables.get(equation.getResultSide())
              ));
          break;
        case "*":
          booleanFormulas.add(
              imgr.equal(
                  imgr.multiply(
                      variables.get(operationSide[0]),
                      variables.get(operationSide[1])
                  ), variables.get(equation.getResultSide())
              ));
          break;
        case "/":
          booleanFormulas.add(
              imgr.equal(
                  imgr.divide(
                      variables.get(operationSide[0]),
                      variables.get(operationSide[1])
                  ), variables.get(equation.getResultSide())
              ));
          break;
        default:
          break;
      }
    }
  }

  /**
   * Generates the variables for the given list of variable names and integer formula manager.
   *
   * @param variablesNames the list of variable names to generate
   * @param imgr           the integer formula manager used to create integer formulas
   */
  private static void generateVariables(List<String> variablesNames, IntegerFormulaManager
      imgr) {
    for (String variable : variablesNames) {
      if (variable.startsWith("x_")) {
        variables.put(variable, imgr.makeVariable(variable));
      } else {
        variables.put(variable, imgr.makeNumber(Integer.parseInt(variable)));
      }

    }
  }

}
