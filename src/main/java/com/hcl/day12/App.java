package com.hcl.day12;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

	public static Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) {
		count1s2s();
		Invoice[] invoices = { new Invoice("83", "Electric sander", 7, 57.98),
				new Invoice("24", "Power saw", 18, 99.99), new Invoice("7", "Sledge hammer", 11, 21.50),
				new Invoice("77", "Hammer", 76, 11.99), new Invoice("39", "Lawn mower", 3, 79.50),
				new Invoice("68", "Screwdriver", 106, 6.99), new Invoice("56", "Jig saw", 21, 11.00),
				new Invoice("3", "Wrench", 34, 7.50) };
		handleInvoices(invoices);
		sortWordsAlphabetically("This is a sentence that I want to be sorted alphabetically");
		sortRandomCharacters();
		showLetterGrades();
		testMyInterface();
	}

	public static void count1s2s() {
		logger.trace(">> Print the count of 1s and 2s");
		new Random().ints(1000000, 1, 3).boxed()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
				.forEach((key, value) -> logger.info(String.format("%-6d%d", key, value)));
	}

	public static void handleInvoices(Invoice[] invoices) {
		logger.trace("========== Invoice Examples ==========");
		logger.trace(">> Sorted by part description");
		sortByPartDesc(invoices);

		logger.trace(">> Sorted by price per item");
		Arrays.stream(invoices).sorted((Invoice i1, Invoice i2) -> {
			double i = i1.getPricePerItem() - i2.getPricePerItem();
			return (int) (i > 0 ? Math.ceil(i) : Math.floor(i));
		}).forEachOrdered(logger::info);

		logger.trace(">> Map invoices to part description and quantity, then sort by quantity");
		// Key conflicts are handled on a "first wins" basis
		Map<String, Integer> map = Arrays.stream(invoices).collect(Collectors.toMap(Invoice::getPartDescription,
				Invoice::getQuantity, (existing, replacement) -> existing));
		map.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.forEach((entry) -> logger.info(String.format("%-20.20s %d", entry.getKey(), entry.getValue())));

		logger.trace(">> Map invoices to part description and value of invoice, then sort by value");
		Map<String, Double> map2 = Arrays.stream(invoices).collect(Collectors.toMap(Invoice::getPartDescription,
				(invoice) -> invoice.getPricePerItem() * invoice.getQuantity(), (existing, replacement) -> existing));
		map2.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.forEach((entry) -> logger.info(String.format("%-20.20s $%.2f", entry.getKey(), entry.getValue())));

		logger.trace(">> Same as above but only select values $200-$500");
		map2 = Arrays.stream(invoices).collect(Collectors.toMap(Invoice::getPartDescription,
				(invoice) -> invoice.getPricePerItem() * invoice.getQuantity(), (existing, replacement) -> existing));
		map2.entrySet().stream().filter((entry) -> entry.getValue() > 200 && entry.getValue() < 500)
				.sorted(Map.Entry.comparingByValue())
				.forEach((entry) -> logger.info(String.format("%-20.20s $%.2f", entry.getKey(), entry.getValue())));

		logger.trace(">> Find an invoice where part description contains \"saw\"");
		Optional<Invoice> sawInvoice = Arrays.stream(invoices)
				.filter((invoice) -> invoice.getPartDescription().contains("saw")).findAny();
		logger.info(sawInvoice.isPresent() ? sawInvoice.get()
				: "No invoice found containing \"saw\" in the part description");

		logger.trace("========== End of Invoice Examples ==========");
	}

	public static List<String> sortByPartDesc(Invoice[] invoices) {
		return Arrays.stream(invoices)
				.sorted((Invoice i1, Invoice i2) -> i1.getPartDescription().compareTo(i2.getPartDescription()))
				.peek(logger::info).map((invoice) -> invoice.getPartDescription()).collect(Collectors.toList());
	}

	public static void sortWordsAlphabetically(String string) {
		logger.trace(">> Print words alphabetically");
		logger.trace("String: " + string);
		Arrays.stream(string.split(" ")).sorted((s1, s2) -> s1.toLowerCase().compareTo(s2.toLowerCase()))
				.forEach(logger::info);
	}

	public static void sortRandomCharacters() {
		logger.trace(">> Sort random char array in ascending, descending, then ascending without duplicates");
		Random r = new Random();
		ArrayList<Character> list = new ArrayList<Character>();
		for (int i = 0; i < 30; i++) {
			list.add((char) (r.nextInt(26) + 'a'));
		}
		Collections.sort(list);
		logger.info(list);
		Collections.sort(list, (c1, c2) -> c2 - c1);
		logger.info(list);
		list = new ArrayList<>(new HashSet<>(list));
		Collections.sort(list);
		logger.info(list);
	}

	public static void showLetterGrades() {
		logger.trace(">> Read integer grades from file and display their letter grade");
		ArrayList<Integer> list = new ArrayList<Integer>();
		try (Scanner scanner = new Scanner(
				new File("C:\\Users\\colin_clarke\\eclipse-workspace\\day11\\grades.txt"));) {
			while (scanner.hasNextInt()) {
				list.add(scanner.nextInt());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		list.stream().forEach((grade) -> logger.info(String.format("%d %s", grade,
				grade >= 90 ? "A" : grade >= 80 ? "B" : grade >= 70 ? "C" : grade >= 60 ? "D" : "F")));
	}

	public static void testMyInterface() {
		logger.trace(">> Test the custom \"MyInterface\" interface");
		MyInterface myInterface1 = (var1, var2) -> (var1 + var2);
		logger.trace("Calling addTwoNumbers method from custom interface");
		int result1 = myInterface1.addTwoNumbers(10, 20);
		logger.info("Result 1: " + result1);

		logger.trace("Calling addTwoNumbers method from custom interface");
		MyInterface myInterface2 = (var1, var2) -> {
			return (var1 + var2);
		};
		int result2 = myInterface2.addTwoNumbers(30, 40);
		logger.info("Result 2: " + result2);

	}
}