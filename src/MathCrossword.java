import org.sosy_lab.common.ShutdownManager;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.BasicLogManager;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.api.BooleanFormulaManager;
import org.sosy_lab.java_smt.api.FormulaManager;
import org.sosy_lab.java_smt.api.IntegerFormulaManager;
import org.sosy_lab.java_smt.api.SolverContext;

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
 *     What differences can be observed if they don't?
 *     Why are some solvers not usable and what would you need to change to be able to use them? (Theory question only. No code required.)
 */

public class MathCrossword {

  public static void main(String[] args) throws InvalidConfigurationException {
    Configuration config = Configuration.fromCmdLineArguments(args);
    LogManager logger = BasicLogManager.create(config);
    ShutdownManager shutdown = ShutdownManager.create();

    // SolverContext is a class wrapping a solver context.
    // Solver can be selected either using an argument or a configuration option
    // inside `config`.
    SolverContext context = SolverContextFactory.createSolverContext(
        config, logger, shutdown.getNotifier(), SolverContextFactory.Solvers.Z3);

    FormulaManager fmgr = context.getFormulaManager();

    BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
    IntegerFormulaManager imgr = fmgr.getIntegerFormulaManager();


  }


}
