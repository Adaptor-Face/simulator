/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Diffusjon;

/**
 *
 * @author Kristoffer
 */
public class Fraction {

    private long numerator;
    private long denominator;

    public Fraction(long numerator, long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
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
            numerator = 1;
            denominator = 1;
        } else {
            String[] str = fract.split("/");
            numerator = Integer.parseInt(str[0]);
            denominator = Integer.parseInt(str[1]);
        }
    }

    public void add(Fraction fract) {
        if (denominator == fract.denominator) {
            numerator += fract.numerator;
        }
        else {
            long newDenominator = denominator * fract.denominator;
            numerator = numerator * fract.denominator;
            numerator = numerator + (fract.numerator * denominator);
            denominator = newDenominator;
        }
    }

    public void multiply(Fraction fract) {
        numerator = numerator * fract.numerator;
        denominator = denominator * fract.denominator;
    }

    @Override
    public String toString() {
        if (numerator == denominator) {
            return "" + 1;
        }
        return numerator + "/" + denominator;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.numerator ^ (this.numerator >>> 32));
        hash = 79 * hash + (int) (this.denominator ^ (this.denominator >>> 32));
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

}
