import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class CrosswordTests {

  private final String testInput =
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
  private Crossword testCrossword = new Crossword(testInput);
  private String regex =
      "([0-9]|x_[1-9][0-9]*)(\\+|\\-|\\*|\\/)([0-9]|x_[1-9][0-9]*)=([0-9]|x_[1-9][0-9]*)";
  private Pattern pattern = Pattern.compile(regex);

  @Test
  @DisplayName("formatExampleCrossword")
  void testFormatCrossword() throws Exception {

    String[][] expected = {
        {"1", "+", "_", "=", "6", " ", " ", " ", "_"},
        {" ", " ", "/", " ", "/", " ", " ", " ", "+"},
        {"_", "-", "_", "=", "3", " ", "_", " ", "2"},
        {"*", " ", "=", " ", "=", " ", "/", " ", "="},
        {"_", " ", "1", " ", "_", "*", "4", "=", "_"},
        {"=", " ", " ", " ", " ", " ", "=", " ", " "},
        {"8", " ", "5", "-", "_", "=", "1", " ", "3"},
        {" ", " ", "+", " ", "+", " ", " ", " ", "*"},
        {"8", "/", "_", "=", "_", " ", " ", " ", "1"},
        {" ", " ", "=", " ", "=", " ", " ", " ", "="},
        {" ", " ", "_", " ", "8", "-", "_", "=", "_"}
    };

    String[][] actual = testCrossword.getCrossword();
    assertArrayEquals(expected, actual);
  }

  @Test
  @DisplayName("testReplaceUnderscoresWithVariables")
  void testParseVariables() throws Exception {

    Method parseVariablesMethod = Crossword.class.getDeclaredMethod("parseVariables");
    parseVariablesMethod.setAccessible(true);
    parseVariablesMethod.invoke(testCrossword);

    String[][] expected = {
        {"1", "+", "x_1", "=", "6", " ", " ", " ", "x_2"},
        {" ", " ", "/", " ", "/", " ", " ", " ", "+"},
        {"x_3", "-", "x_4", "=", "3", " ", "x_5", " ", "2"},
        {"*", " ", "=", " ", "=", " ", "/", " ", "="},
        {"x_6", " ", "1", " ", "x_7", "*", "4", "=", "x_8"},
        {"=", " ", " ", " ", " ", " ", "=", " ", " "},
        {"8", " ", "5", "-", "x_9", "=", "1", " ", "3"},
        {" ", " ", "+", " ", "+", " ", " ", " ", "*"},
        {"8", "/", "x_10", "=", "x_11", " ", " ", " ", "1"},
        {" ", " ", "=", " ", "=", " ", " ", " ", "="},
        {" ", " ", "x_12", " ", "8", "-", "x_13", "=", "x_14"}
    };

    String[][] actual = testCrossword.getCrossword();

    assertArrayEquals(expected, actual);
  }

}