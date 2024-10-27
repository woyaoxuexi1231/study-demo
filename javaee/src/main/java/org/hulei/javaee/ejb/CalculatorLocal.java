package org.hulei.javaee.ejb;

import jakarta.ejb.Local;

@Local
public interface CalculatorLocal {
    int add(int a, int b);
    int subtract(int a, int b);
}
