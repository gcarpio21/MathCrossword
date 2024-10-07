import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class CrosswordTests {

  @Test
  @DisplayName("formatExampleCrossword")
  void formatCrossword() throws Exception {
    String crossword =
        "1+_=6   _" + "  / /   +" + "_-_=3 _ 2" + "* = = / =" + "_ 1 _*4=_" + "=     =  " +
            "8 5-_=1 3" + "  + +   *" + "8/_=_   1" + "  = =   =" + "  _ 8-_=_" ;

    String[] expected =
        {"1+_=6   _",
            "  / /   +",
            "_-_=3 _ 2",
            "* = = / =",
            "_ 1 _*4=_",
            "=     =  ",
            "8 5-_=1 3",
            "  + +   *",
            "8/_=_   1",
            "  = =   =",
            "  _ 8-_=_"};

    Method method = Crossword.class.getDeclaredMethod("formatCrossword", String.class);
    method.setAccessible(true);

    String[] actual = (String[]) method.invoke(null, crossword);

    assertArrayEquals(expected, actual);
  }
}