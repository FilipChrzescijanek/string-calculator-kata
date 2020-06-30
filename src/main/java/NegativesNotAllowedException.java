import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NegativesNotAllowedException extends IllegalArgumentException {

    public NegativesNotAllowedException(final Supplier<IntStream> negativeNumbersSupplier) {
        super(String.format(
                "Negatives not allowed: %s",
                negativeNumbersSupplier.get()
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(", "))));
    }

}
