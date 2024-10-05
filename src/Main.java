//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
  public static void main(String[] args) {
    String crossword =
        "1+_=6   _  / /   +_-_=3 _ 2* = = / =_ 1 _*4=_=     = 8 5-_=1 3  + +   *8/_=_   1  = =   "
            + "=  _ 8-_=_";
    String parsedCrossword;
    parsedCrossword = MathCrossword.parseCrossword(crossword);

    System.out.println(parsedCrossword);
  }
}