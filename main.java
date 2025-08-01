// Java version of the Python script using the same logic

import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static int baseStrToInt(String value, int base) {
        return Integer.parseInt(value, base);
    }

    public static BigInteger lagrangeInterpolationAtZero(List<int[]> points) {
        BigInteger total = BigInteger.ZERO;
        int k = points.size();

        for (int i = 0; i < k; i++) {
            int xi = points.get(i)[0];
            BigInteger yi = BigInteger.valueOf(points.get(i)[1]);
            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                int xj = points.get(j)[0];
                num = num.multiply(BigInteger.valueOf(-xj));
                den = den.multiply(BigInteger.valueOf(xi - xj));
            }
            total = total.add(yi.multiply(num.divide(den)));
        }

        return total;
    }

    public static void processTestCase(String filename) {
        System.out.println("\n\uD83D\uDD0D Processing: " + filename);
        String content;
        try {
            content = Files.readString(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("\u274C Error loading " + filename + ": " + e.getMessage());
            return;
        }

        Map<String, Object> data;
        try {
            data = new ObjectMapper().readValue(content, Map.class);
        } catch (IOException e) {
            System.out.println("\u274C Error parsing JSON in " + filename + ": " + e.getMessage());
            return;
        }

        int k = (int) ((Map<?, ?>) data.get("keys")).get("k");
        List<int[]> entries = new ArrayList<>();

        for (String key : data.keySet()) {
            if (!key.equals("keys")) {
                Map<?, ?> point = (Map<?, ?>) data.get(key);
                int x = Integer.parseInt(key);
                int base = Integer.parseInt((String) point.get("base"));
                String value = (String) point.get("value");
                int y;
                try {
                    y = baseStrToInt(value, base);
                } catch (Exception e) {
                    System.out.println("\u274C Error decoding y at x = " + x + ": " + e.getMessage());
                    return;
                }
                entries.add(new int[]{x, y});
            }
        }

        entries.sort(Comparator.comparingInt(a -> a[0]));
        List<int[]> points = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            points.add(entries.get(i));
        }

        try {
            BigInteger secret = lagrangeInterpolationAtZero(points);
            System.out.println("\u2705 The secret (constant term c) is: " + secret);
        } catch (Exception e) {
            System.out.println("\u274C Error computing Lagrange interpolation: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String[] testFiles = {"input1.json", "input2.json"};
        for (String file : testFiles) {
            processTestCase(file);
        }
    }
}
