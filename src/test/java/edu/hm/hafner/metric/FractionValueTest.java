package edu.hm.hafner.metric;

import org.apache.commons.lang3.math.Fraction;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

import static edu.hm.hafner.metric.assertions.Assertions.*;

/**
 * Tests the class {@link FractionValue}.
 *
 * @author Ullrich Hafner
 */
class FractionValueTest {
    @Test
    void shouldCreateDelta() {
        var fifty = new FractionValue(Metric.LINE, Fraction.getFraction(50, 1));
        var hundred = new FractionValue(Metric.LINE, Fraction.getFraction(100, 1));

        assertThat(fifty.isBelowThreshold(50.1)).isTrue();
        assertThat(fifty.isBelowThreshold(50)).isFalse();

        assertThat(fifty.add(fifty)).isEqualTo(hundred);
        assertThat(fifty.max(hundred)).isEqualTo(hundred);
    }

    @Test
    void getFractionTest() {
        Metric metric = Metric.CONTAINER;
        Fraction fraction = Fraction.ZERO;
        FractionValue fractionValue = new FractionValue(metric, fraction);
        assertThat(fractionValue.getFraction()).isEqualTo(fraction);
    }

    @Test
    void addThrowsExceptionTest() {
        Metric metric = Metric.CONTAINER;
        Fraction fraction = Fraction.ZERO;
        FractionValue fractionValue = new FractionValue(metric, fraction);
        Value value = new FractionValue(Metric.LINE, Fraction.ZERO);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> fractionValue.add(value));
    }

    @Test
    void deltaTestThrowsExceptionTest() {
        FractionValue fractionValue = new FractionValue(Metric.CONTAINER, Fraction.ZERO);
        Value value = new FractionValue(Metric.LINE, Fraction.ZERO);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> fractionValue.delta(value));
    }

    @Test
    void deltaTest() {

        FractionValue fractionValue = new FractionValue(Metric.CONTAINER, Fraction.ONE);
        Value value = new FractionValue(Metric.CONTAINER, Fraction.ZERO);
        assertThat(fractionValue.delta(value)).isEqualTo(Fraction.ONE);
        assertThat(value.delta(fractionValue)).
                isEqualTo(new FractionValue(Metric.CONTAINER, -1, 1).getFraction());
    }

    @Test
    void maxThrowsExceptionTest() {

        FractionValue fractionValue = new FractionValue(Metric.CONTAINER, Fraction.ZERO);
        Value value = new FractionValue(Metric.LINE, Fraction.ZERO);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> fractionValue.max(value));
    }

    @Test
    void maxEqualFractionTest() {
        FractionValue fractionValue = new FractionValue(Metric.CONTAINER, Fraction.ZERO);
        Value value = new FractionValue(Metric.CONTAINER, Fraction.ZERO);
        //zeigt, dass da die beiden Werte gleich sind beide Tests durchlaufen
        assertThat(fractionValue.max(value)).isEqualTo(fractionValue);
        assertThat(fractionValue.max(value)).isEqualTo(value);
    }

    @Test
    void maxDifferentFractionTest() {
        FractionValue fractionValue = new FractionValue(Metric.CONTAINER, Fraction.ZERO);
        Value value = new FractionValue(Metric.CONTAINER, Fraction.ONE);
        assertThat(fractionValue.max(value)).isEqualTo(value);
    }

    @Test
    void equalsTest() {
        EqualsVerifier.forClass(FractionValue.class).withRedefinedSuperclass().verify();
    }

    @Test
    void serializeTest() {
        FractionValue fractionValue = new FractionValue(Metric.CONTAINER, Fraction.ZERO);
        String wanted = "CONTAINER: 0/1";
        assertThat(fractionValue.serialize()).isEqualTo(wanted);
    }
}
