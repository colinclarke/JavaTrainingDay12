package com.hcl.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("<= App Specification =>")
public class AppSpec {

	private final Invoice[] invoices = { new Invoice("83", "Electric sander", 7, 57.98),
			new Invoice("24", "Power saw", 18, 99.99), new Invoice("7", "Sledge hammer", 11, 21.50),
			new Invoice("77", "Hammer", 76, 11.99), new Invoice("39", "Lawn mower", 3, 79.50),
			new Invoice("68", "Screwdriver", 106, 6.99), new Invoice("56", "Jig saw", 21, 11.00),
			new Invoice("3", "Wrench", 34, 7.50) };

	@Test
	@DisplayName("Invoices are sorted based on part descritpion")
	void testInvoicesSortedByPartDescription() {
		List<String> list = App.sortByPartDesc(invoices);
		List<String> sortedList = new LinkedList<String>(list);
		Collections.sort(sortedList);
		assertEquals(list, sortedList, "Returned invoice descriptions should be sorted");
	}
}
