/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Diffusjon;

/**
 *
 * @author Face
 */
class DoubleWrapper {
    private double value;

    public DoubleWrapper(double value) {
        this.value = value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    public void add(double value) {
        this.value += value;
    }
    public void add(DoubleWrapper value) {
        this.value += value.value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
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
        final DoubleWrapper other = (DoubleWrapper) obj;
        if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        return true;
    }
    
    
    
}
