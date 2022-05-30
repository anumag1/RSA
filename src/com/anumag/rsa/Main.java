package com.anumag.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class Main {
    static int numOfBits = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String message;
        BigInteger P = BigInteger.probablePrime(numOfBits, new SecureRandom());
        BigInteger Q = BigInteger.probablePrime(numOfBits, new SecureRandom());
        BigInteger N = P.multiply(Q);
        BigInteger Phi = euler(P, Q);
        BigInteger Ka = findOpenK(Phi);
        BigInteger Kb = findClosedK(Ka, Phi);

        System.out.println("Введіть повідомлення: ");
        message = scanner.nextLine();

        System.out.println("P = " + P + "\nQ = " + Q + "\nN = " + N + "\nPhi = " + Phi + "\nKa = " + Ka + "\nKb = " + Kb);

        String encryptedMessage = encrypt(message, Ka, N);
        System.out.println("\nЗашифроване повідомлення: " + encryptedMessage);
        System.out.println("\nРозшифроване повідомлення: " + decrypt(encryptedMessage, Kb, N));
    }

    public static String encrypt(String message, BigInteger k, BigInteger n) {
        int[] mesID = new int[message.length()];
        BigInteger[] temp = new BigInteger[mesID.length];
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            mesID[i] = message.charAt(i);
            System.out.print(mesID[i] + " ");
        }

        for (int i = 0; i < temp.length; i++) {
            temp[i] = BigInteger.valueOf(mesID[i]).modPow(k, n);
            encrypted.append((char) temp[i].intValue());
        }


        return encrypted.toString();
    }

    public static String decrypt(String message, BigInteger k, BigInteger n) {
        StringBuilder decrypted = new StringBuilder();
        int[] mesID = new int[message.length()];
        BigInteger[] temp = new BigInteger[mesID.length];

        for (int i = 0; i < message.length(); i++) {
            mesID[i] = message.charAt(i);
            System.out.print(mesID[i] + " ");
        }

        for (int i = 0; i < temp.length; i++) {
            temp[i] = BigInteger.valueOf(mesID[i]).modPow(k, n);
            decrypted.append((char) temp[i].intValue());
        }

        return decrypted.toString();
    }

    public static BigInteger findClosedK(BigInteger k, BigInteger phi) { // Kb*Ka=1(mod(Phi))
        return k.modInverse(phi);
    }

    public static BigInteger findOpenK(BigInteger phi) {
        SecureRandom rand = new SecureRandom();
        BigInteger temp = new BigInteger(numOfBits, rand);
        while (true) {
            if (temp.compareTo(phi) < 0 && temp.compareTo(BigInteger.ONE) >= 0 && temp.gcd(phi).equals(BigInteger.ONE)) {
                return temp;
            } else {
                temp = new BigInteger(numOfBits, rand);
            }
        }
    }

    public static BigInteger euler(BigInteger p, BigInteger q) {
        return (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
    }
}
