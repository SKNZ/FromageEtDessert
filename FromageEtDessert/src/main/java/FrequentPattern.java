import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by sknz on 4/2/15.
 */
public class FrequentPattern {
    private int count;
    private List<Integer> items = new ArrayList<>();

    public List<Integer> getItems() {
        return items;
    }

    public int getCount() {
        return count;
    }

    private static Pattern
            regexPattern =
            Pattern.compile("(.*)\\(([0-9]+)\\)");

    public static FrequentPattern fromString(String s) throws FedEx {
        FrequentPattern pattern = new FrequentPattern();
        Matcher regex = regexPattern.matcher(s);

        regex.reset(s);
        if (!regex.find()) {
            throw new FedEx("Bad file format");
        }

        if (regex.group(1) != null && !regex.group(1).isEmpty()) {
            pattern.items.addAll(
                    Arrays.asList(regex.group(1).split("\\s+"))
                            .stream()
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()));
        }

        String freq = regex.group(2).trim();
        pattern.count = Integer.parseInt(freq);

        if (pattern.items.isEmpty())
            return null;

        return pattern;
    }

    public static FrequentPattern substract(FrequentPattern x,
                                            FrequentPattern z) {
        FrequentPattern pattern = new FrequentPattern();
        pattern.items.addAll(z.items);
        pattern.items.removeAll(x.items);

        return pattern;
    }

    public boolean contains(FrequentPattern fp) {
        return items.containsAll(fp.items) && items.size() > fp.items.size();
    }

    @Override
    public String toString() {
        return "{" +
                +count +
                ", " + items +
                '}';
    }

    public static void main(String[] args) {
        FrequentPattern a = new FrequentPattern();
        a.items = Arrays.asList(1, 2, 3);

        FrequentPattern b = new FrequentPattern();
        b.items = Arrays.asList(2, 3);

        FrequentPattern c = FrequentPattern.substract(b, a);
        System.out.println(c);
    }

    public String textSerialize() {
        return items
                .stream()
                .map(Object::toString)
                .reduce((a, b) -> a + " " + b)
                + " (" + this.count + ")";
    }
}
