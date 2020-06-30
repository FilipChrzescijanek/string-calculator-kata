import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntegerStringCalculator implements StringCalculator<Integer> {

    @Override
    public Integer add(final String numbers) {
        final IntPredicate notBiggerThanAThousandFilter = n -> n <= 1000;
        final var componentsSupplier = parseNumbers(numbers);
        checkForNegativeNumbers(componentsSupplier);
        return componentsSupplier.get()
                .filter(notBiggerThanAThousandFilter)
                .sum();
    }

    private void checkForNegativeNumbers(final Supplier<IntStream> componentsSupplier) {
        final IntPredicate negativeNumbersFilter = n -> n < 0;
        final Supplier<IntStream> negativeNumbersSupplier = () -> componentsSupplier.get()
                .filter(negativeNumbersFilter);
        final var negativeNumbers = negativeNumbersSupplier.get();
        if (negativeNumbers.findAny().isPresent()) {
            throw new NegativesNotAllowedException(negativeNumbersSupplier);
        }
    }

    private Supplier<IntStream> parseNumbers(final String numbers) {
        if (overwritesDefaultDelimiter(numbers)) {
            final var inputs = numbers.split("\n", 2);
            final var delimiterHeader = inputs[0];
            final var components = inputs[1];
            final var delimiterPattern = parseDelimiter(delimiterHeader);
            return splitComponentsUsingPattern(components, delimiterPattern);
        } else {
            return splitComponentsUsingPattern(numbers, ",|\n");
        }
    }

    private boolean overwritesDefaultDelimiter(final String numbers) {
        return numbers.startsWith("//");
    }

    private String parseDelimiter(final String delimiterHeader) {
        final var delimiterDefinition = delimiterHeader.substring("//".length());
        final var delimiters = containsMulticharacterDelimiter(delimiterDefinition) ?
                processMultipleDelimiters(delimiterDefinition)
                : Collections.singletonList(delimiterDefinition);
        return quoteAndJoinDelimiters(delimiters);
    }

    private boolean containsMulticharacterDelimiter(final String delimiterDefinition) {
        return delimiterDefinition.startsWith("[");
    }

    private List<String> processMultipleDelimiters(final String delimiterDefinition) {
        return Arrays.asList(delimiterDefinition
                .substring("[".length(), delimiterDefinition.length() - "]".length())
                .split("]\\["));
    }

    private String quoteAndJoinDelimiters(final List<String> delimiters) {
        return delimiters.stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));
    }

    private Supplier<IntStream> splitComponentsUsingPattern(final String components, final String delimiterPattern) {
        if (components.isEmpty()) {
            return IntStream::empty;
        } else {
            return () -> Stream.of(components.split(delimiterPattern, -1))
                    .mapToInt(Integer::parseInt);
        }
    }

}
