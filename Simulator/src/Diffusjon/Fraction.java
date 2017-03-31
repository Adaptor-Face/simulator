/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Diffusjon;

import java.math.BigInteger;
import java.util.Objects;

/**
 *
 * @author Kristoffer
 */
public class Fraction {

    private BigInteger numerator;
    private BigInteger denominator;

    public Fraction(long numerator, long denominator) {
        this.numerator = new BigInteger("" + numerator);
        this.denominator = new BigInteger("" + denominator);
    }

    public Fraction(Fraction fract) {
        this.numerator = fract.numerator;
        this.denominator = fract.denominator;
    }

    public Fraction(String fract) throws NumberFormatException {
        if (fract.isEmpty()) {
            throw new NumberFormatException("Number String empty");
        }
        if (fract.equals("1")) {
            numerator = new BigInteger("" + 1);
            denominator = new BigInteger("" + 1);
        } else {
            String[] str = fract.split("\\n");
            numerator = new BigInteger("" + str[0]);
            denominator = new BigInteger("" + str[1]);
        }
    }

    public void add(Fraction fract) {
        if (denominator.equals(fract.denominator)) {
            numerator = numerator.add(fract.numerator);
        } else if (denominator.mod(fract.denominator).equals(new BigInteger("" + 0)) || fract.denominator.mod(denominator).equals(new BigInteger("" + 0))) {
            if (denominator.compareTo(fract.denominator) == 1) {
                numerator = numerator.add(fract.numerator.multiply(denominator.divide(fract.denominator)));
            } else {
                numerator = numerator.multiply(fract.denominator.divide(denominator)).add(fract.numerator);
                denominator = fract.denominator;
            }
        } else {
            BigInteger newDenominator = denominator.multiply(fract.denominator);
            numerator = numerator.multiply(fract.denominator);
            numerator = numerator.add(fract.numerator.multiply(denominator));
            denominator = newDenominator;
        }
    }

    public void multiply(Fraction fract) {
        numerator = numerator.multiply(fract.numerator);
        denominator = denominator.multiply(fract.denominator);
    }

    @Override
    public String toString() {
//        if (denominator.intValue() != 0 && numerator.intValue() != 0 && denominator.mod(numerator).equals( new BigInteger("" + 0))) {
//            return 1 + "\n" + (denominator.divide(numerator));
//        }
        return numerator + "\n" + denominator;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.numerator);
        hash = 29 * hash + Objects.hashCode(this.denominator);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fraction other = (Fraction) obj;
        if (numerator != other.numerator) {
            return false;
        }
        if (denominator != other.denominator) {
            return false;
        }
        return true;
    }

    public String toDecimal() {
        return numerator.divide(denominator).toString();
    }

    public Fraction factorize() {
        while (!numerator.gcd(denominator).equals(new BigInteger("1"))) {
            denominator = denominator.divide(numerator.gcd(denominator));
            numerator = numerator.divide(numerator.gcd(denominator));
        }
        return this;
    }

}
