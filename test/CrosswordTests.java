import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class CrosswordTests {

  private final String testInput =
      "1+_=6   _" + "  / /   +" + "_-_=3 _ 2" + "* = = / =" + "_ 1 _*4=_" + "=     =  " +
          "8 5-_=1 3" + "  + +   *" + "8/_=_   1" + "  = =   =" + "  _ 8-_=_";
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

    Method replaceUnderscoresMethod = Crossword.class.getDeclaredMethod("replaceUnderscores");
    replaceUnderscoresMethod.setAccessible(true);
    replaceUnderscoresMethod.invoke(testCrossword);

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

  @Test
  @DisplayName("testAddHorizontalEquations")
  void testParseHorizontalEquations() throws Exception {
    Crossword testCrossword = new Crossword(testInput);

    Method replaceUnderscoresMethod = Crossword.class.getDeclaredMethod("replaceUnderscores");
    replaceUnderscoresMethod.setAccessible(true);
    replaceUnderscoresMethod.invoke(testCrossword);

    Method parseHorizontalEquationsMethod =
        Crossword.class.getDeclaredMethod("parseHorizontalEquations", Pattern.class);
    parseHorizontalEquationsMethod.setAccessible(true);
    parseHorizontalEquationsMethod.invoke(testCrossword, pattern);

    List<String> expectedEquations = Arrays.asList(
        "1+x_1=6", "x_3-x_4=3", "x_7*4=x_8", "5-x_9=1", "8/x_10=x_11", "8-x_13=x_14"
    );

    // Verify that the list of equations matches the expected horizontal equations
    assertEquals(expectedEquations, testCrossword.getEquations());
  }

  @Test
  @DisplayName("testAddVerticalEquations")
  void testAddVerticalEquations() throws Exception {
    Crossword testCrossword = new Crossword(testInput);

    Method replaceUnderscoresMethod = Crossword.class.getDeclaredMethod("replaceUnderscores");
    replaceUnderscoresMethod.setAccessible(true);
    replaceUnderscoresMethod.invoke(testCrossword);

    Method parseVerticalEquationsMethod =
        Crossword.class.getDeclaredMethod("parseVerticalEquations", Pattern.class);
    parseVerticalEquationsMethod.setAccessible(true);
    parseVerticalEquationsMethod.invoke(testCrossword, pattern);


    List<String> expectedEquations = Arrays.asList(
        "x_3*x_6=8", "x_1/x_4=1", "5+x_10=x_12", "6/3=x_7", "x_9+x_11=8", "x_5/4=1", "x_2+2=x_8", "3*1=x_14"
    );

    assertEquals(expectedEquations, testCrossword.getEquations());
  }

  @Test
  @DisplayName("testGetEquations")
  void testGetEquations() {
    // Arrange
    String testInput = "1+_=6   _" + "  / /   +" + "_-_=3 _ 2" + "* = = / =" + "_ 1 _*4=_" + "=     =  " +
        "8 5-_=1 3" + "  + +   *" + "8/_=_   1" + "  = =   =" + "  _ 8-_=_";
    Crossword testCrossword = new Crossword(testInput);

    // Act
    List<Equation> equations = testCrossword.getEquations();

    // Assert
    List<String> expectedEquations = Arrays.asList(
        "1+x_1=6", "x_3-x_4=3", "x_7*4=x_8", "5-x_9=1", "8/x_10=x_11", "8-x_13=x_14",
        "x_3*x_6=8", "x_1/x_4=1", "5+x_10=x_12", "6/3=x_7", "x_9+x_11=8", "x_5/4=1", "x_2+2=x_8", "3*1=x_14"
    );


    List<String> actualEquations = equations.stream()
        .map(Equation::toString)
        .collect(Collectors.toList());

    assertEquals(expectedEquations, actualEquations);
  }


}