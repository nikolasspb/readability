package readability;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String text = importText(args[0]);
        int vow = 0;
        String vowels = "aeiouy";
        int syllables = 0;
        int polysyllables = 0;
        System.out.println("The text is:\n" + text + "\n");
        text = text.toLowerCase();
        String textW = text.replaceAll("[.,?!]", "");
        String[] wordsAr = textW.split(" ");
        for (String w : wordsAr) {
            w = w.trim();
            int doubleW = 0;
            for (String ch : w.split("")) {
                if (vowels.contains(ch)) {
                    vow++;
                    doubleW++;
                    if (doubleW >= 2) {
                        vow--;
                    }
                } else {
                    doubleW = 0;
                }
            }
            if ("e".equals(w.substring(w.length() - 1))) {
                vow--;
            }
            if (vow == 0) {
                syllables++;
            } else {
                syllables += vow;
            }
            if (vow > 2) {
                polysyllables++;
            }
            vow = 0;
        }
        int words = wordsAr.length;
        int sentences = text.split("[.!?]").length;
        int characters = text.replace(" ", "").length();
        System.out.print("Words: " + words + "\nSentences: " + sentences + "\nCharacters: " + characters + "\nSyllables: " + syllables + "\nPolysyllables: " + polysyllables);
        System.out.print("\nEnter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner sc = new Scanner(System.in);
        String userChoice = sc.nextLine();
        System.out.println();
        switch (userChoice) {
            case "ARI":
                ari(characters, words, sentences);
                break;
            case "FK":
                fk(words, sentences, syllables);
                break;
            case "SMOG":
                smog(polysyllables, sentences);
                break;
            case "CL":
                cl(sentences, characters, words);
                break;
            case "all":
                Double aver = (Double.parseDouble(ari(characters, words, sentences)) + Double.parseDouble(fk(words, sentences, syllables)) +
                        Double.parseDouble(smog(polysyllables, sentences)) + Double.parseDouble(cl(sentences, characters, words))) / 4;
                final DecimalFormat df = new DecimalFormat("0.00");
                System.out.println("This text should be understood in average by " + df.format(aver) + "-year-olds.");
                break;
        }
    }

    private static String importText(String answ) {
        Scanner sc = new Scanner(System.in);
        File file = new File(answ);
        String text = "";
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                text = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return text;
    }

    private static String ari(int characters, int words, int sentences) {
        var score = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
        var res = outputString(score);
        System.out.println("Automated Readability Index: " + res[0]);
        return res[1];
    }

    private static String fk(int words, int sentences, int syllables) {
        var score = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
        var res = outputString(score);
        System.out.println("Flesch–Kincaid readability tests: " + res[0]);
        return res[1];
    }

    private static String smog(int polysyllables, int sentences) {
        var score = 1.043 * Math.sqrt((double) polysyllables * 30 / sentences) + 3.1291;
        var res = outputString(score);
        System.out.println("Simple Measure of Gobbledygook: " + res[0]);
        return res[1];
    }

    private static String cl(int sentences, int characters, int words) {
        double l = (double) characters / (double) words * 100;
        double s = (double) sentences / (double) words * 100;
        var score = 0.0588 * l - 0.296 * s - 15.8;
        var res = outputString(score);
        System.out.println("Coleman–Liau index: " + res[0]);
        return res[1];
    }

    private static String[] outputString(double score) {
        final DecimalFormat df = new DecimalFormat("0.00");
        int iScore = (int) Math.ceil(score);
        String[] ages = new String[]{"6", "7", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "24", "25"};
        String years = (iScore >= 14 ? ages[13] : ages[iScore - 1]);
        String[] str = new String[2];
        str[0] = df.format(score) + " (about " + years + "-year-old).";
        str[1] = years;
        return str;
    }
}
