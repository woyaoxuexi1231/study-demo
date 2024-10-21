package org.hulei.javaee8.ejb;

import javax.ejb.Local;

@Local
public interface CalculatorLocal {
    int add(int a, int b);
    int subtract(int a, int b);
}
