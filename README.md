# Math Crossword Solver

This project solves a given math crossword using an SMT solver via the JavaSMT API.

## Overview

This project parses a crossword by creating formulas based on the layout of the crossword, letting an SMT solver create a solution (model), and replacing all `_` characters with a correct number.
JDK 23 was used for this project and tested on Fedora Workstation 40, so no other guarantees are made.
To execute simply run the main function in MathCrossword.java or execute the .jar file MathCrossword found in MathCrossword/out/artifacts/MathCrossword_jar with the java -jar command.