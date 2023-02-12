/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmsprimeserver;

/**
 *
 * @author Punnarat Rattanapawan 62050191
 */
public class PrimeNumberCounter {
    public static int countPrimes(int start, int end) {
        int count = 0;
        for (int i = start; i <= end; i++) {
            if (isPrime(i))
                count++;
        }
        
        return count;
    }
    
    private static boolean isPrime(int number) {
        if (number <= 1)
            return false;
        
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0)
                return false;
        }
        
        return true;
    }
}
