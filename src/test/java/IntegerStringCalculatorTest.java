import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegerStringCalculatorTest {

    @Test
    public void testAddGivenEmptyString() {
        assertEquals(0, new IntegerStringCalculator().add(""));
    }

    @Test
    public void testAddGivenOneOrTwoNumbers() {
        assertEquals(1, new IntegerStringCalculator().add("1"));
        assertEquals(3, new IntegerStringCalculator().add("1,2"));
    }

    @Test
    public void testAddGivenAnUnknownAmountOfNumbers() {
        assertEquals(10, new IntegerStringCalculator().add("1,2,3,4"));
        assertEquals(10, new IntegerStringCalculator().add("4,3,2,1"));
    }

    @Test
    public void testAddGivenNewLinesBetweenNumbers() {
        assertEquals(6, new IntegerStringCalculator().add("1\n2,3"));
        assertEquals(6, new IntegerStringCalculator().add("1\n2\n3"));
    }

    @Test
    public void testAddGivenDifferentDelimiters() {
        assertEquals(3, new IntegerStringCalculator().add("//;\n1;2"));
        assertEquals(3, new IntegerStringCalculator().add("//?\n1?2"));
        assertEquals(3, new IntegerStringCalculator().add("//*\n1*2"));
        assertEquals(3, new IntegerStringCalculator().add("//.\n1.2"));
    }

    @Test
    public void testAddGivenANumberBiggerThanAThousand() {
        assertEquals(2, new IntegerStringCalculator().add("2,1001"));
        assertEquals(1000, new IntegerStringCalculator().add("1001,1000"));
        assertEquals(0, new IntegerStringCalculator().add("1001,1001"));
    }

    @Test
    public void testAddGivenAMulticharacterDelimiter() {
        assertEquals(6, new IntegerStringCalculator().add("//[***]\n1***2***3"));
        assertEquals(6, new IntegerStringCalculator().add("//[.*]\n1.*2.*3"));
        assertEquals(6, new IntegerStringCalculator().add("//[^$]\n1^$2^$3"));
    }

    @Test
    public void testAddGivenMultipleDelimiters() {
        assertEquals(6, new IntegerStringCalculator().add("//[*][%]\n1*2%3"));
        assertEquals(6, new IntegerStringCalculator().add("//[.*][%]\n1.*2%3"));
        assertEquals(6, new IntegerStringCalculator().add("//[...][**][.?]\n1.?2**3"));
        assertEquals(10, new IntegerStringCalculator().add("//[...][**][.?]\n1.?2**3...4"));
        assertEquals(1007, new IntegerStringCalculator().add("//[...][**][.?]\n1000.?1001**3...4"));
    }

    @Test
    public void testAddGivenAnIncorrectDelimiterInInput() {
        assertThrows(NumberFormatException.class, () -> new IntegerStringCalculator().add("1,\n"));
        assertThrows(NumberFormatException.class, () -> new IntegerStringCalculator().add("1,,"));
        assertThrows(NumberFormatException.class, () -> new IntegerStringCalculator().add("//;\n1,2"));
        assertThrows(NumberFormatException.class, () -> new IntegerStringCalculator().add("//;\n1,\n"));
        assertThrows(NumberFormatException.class, () -> new IntegerStringCalculator().add("//[***]\n1,\n"));
        assertThrows(NumberFormatException.class, () -> new IntegerStringCalculator().add("//[*][%]\n1,\n"));
    }

    @Test
    public void testAddGivenANegativeNumber() {
        assertThrows(
                NegativesNotAllowedException.class,
                () -> new IntegerStringCalculator().add("-2,1001"),
                "Negatives not allowed: -2");
        assertThrows(
                NegativesNotAllowedException.class,
                () -> new IntegerStringCalculator().add("1,-2,1001,-3,-5"),
                "Negatives not allowed: -2, -3, -5");
        assertThrows(
                NegativesNotAllowedException.class,
                () -> new IntegerStringCalculator().add("//;\n1;-2;1001;-3;-5"),
                "Negatives not allowed: -2, -3, -5");
        assertThrows(
                NegativesNotAllowedException.class,
                () -> new IntegerStringCalculator().add("//[***]\n1***-2***1001***-3***-5"),
                "Negatives not allowed: -2, -3, -5");
        assertThrows(
                NegativesNotAllowedException.class,
                () -> new IntegerStringCalculator().add("//[*][%]\n1*-2*1001*-3%-5"),
                "Negatives not allowed: -2, -3, -5");
    }

}
