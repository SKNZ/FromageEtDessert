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
        double count = AssociationRules.FREQUENT_PATTERNS.stream().filter(fp -> fp.getItems().equals(y.getItems())).findFirst().get().getCount();
        double tot = AssociationRules.FREQUENT_PATTERNS.get(0).getCount();
//        System.out.println(count);
//        System.out.println(tot);
        System.out.println(count / tot + " " + y.getItems());
        return getConf() / (z.getCount() / tot);
    }
}
