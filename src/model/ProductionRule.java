package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProductionRule {
    private final String leftHandSide;
    private final Set<String> rightHandSide;

    public ProductionRule(String leftHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = new HashSet<>();
    }

    public String getLeftHandSide() {
        return leftHandSide;
    }

    public Set<String> getRightHandSide() {
        return Collections.unmodifiableSet(rightHandSide);
    }

    public void setNewRightHandSide(Set<String> rightHandSide) {
        this.rightHandSide.clear();
        this.addAllRightHandSide(rightHandSide);
    }

    public void addRightHandSide (String rightHandSide) {
        this.rightHandSide.add(rightHandSide);
    }

    public void addAllRightHandSide (Set<String> rightHandSideAll) {
        this.rightHandSide.addAll(rightHandSideAll);
    }

    public void printProductionRule () {
        System.out.print(this.leftHandSide + " -> ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String right: this.rightHandSide) {
            stringBuilder.append(" ").append(right).append(" |");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        System.out.println(stringBuilder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductionRule that = (ProductionRule) o;
        return Objects.equals(leftHandSide, that.leftHandSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftHandSide);
    }

    @Override
    public String toString() {
//        return "ProductionRule {" +
//                "leftHandSide='" + leftHandSide + '\'' +
//                ", rightHandSide=" + rightHandSide +
//                '}';
        return "ProductionRule: " + leftHandSide + " -> " + rightHandSide;
    }

    // for testing
    public static void main(String[] args) {
        ProductionRule productionRule = new ProductionRule("S");
        productionRule.addRightHandSide("Sa");
        productionRule.addRightHandSide("Sb");
        productionRule.addRightHandSide("c");
        productionRule.addRightHandSide("d");
        productionRule.printProductionRule();
    }
}
