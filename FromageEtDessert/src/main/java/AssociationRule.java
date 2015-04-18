/**
 * Created by sknz on 4/7/15.
 */
public class AssociationRule {
    private final FrequentPattern x;
    private final FrequentPattern z;
    private FrequentPattern y;

    public AssociationRule(FrequentPattern x, FrequentPattern z) {
        this.x = x;
        this.z = z;
        assert (x.contains(z));
        y = FrequentPattern.substract(x, z);
    }

    @Override
    public String toString() {
        return x + " -> " + z + " conf " + getConf();
    }

    public String textSerialize() {
        return x.textSerialize() + " | " + z.textSerialize() + " | " + getConf() + " | " + getLift();
    }

    public double getConf() {
        return (double) z.getCount() / x.getCount();
    }

    public double getLift() {
        return getConf() / ((double) AssociationRules.FREQUENT_PATTERNS.stream().filter(fp -> fp.getItems().equals(y.getItems())).findFirst().get().getCount() / AssociationRules.FREQUENT_PATTERNS.get(0).getCount());
    }
}
