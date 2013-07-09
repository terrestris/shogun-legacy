package de.terrestris.shogun;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import de.terrestris.shogun.deserializer.DateDeserializer;


/**
 * Unit tests for the DateDeserializer class.
 */
public class DateDeserializerTest {

	String aNumber = "1293836400";
	String correctlyFormattedDate = "30.04.2013 13:43:55";
	String correctlyFormattedDateUnparseable = "75.15.7017 47:61:31";
	String incorrectlyFormattedDate = "2013-04-30 13:43:55";
	String nonsense = "Humpty.Dumpty:12345";

	SimpleDateFormat compareFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	/**
	 * Check whether strings passed to the static looksLikeFormattedDateString
	 * method are detected as dates when expected.
	 */
	@Test
	public void testFormattedDateDetection() {
		Assert.assertFalse(
			"'" + aNumber + "' is not detected as formatted date.",
			DateDeserializer.looksLikeFormattedDateString(aNumber)
		);

		Assert.assertTrue(
			"'" + correctlyFormattedDate + "' is detected as formatted date.",
			DateDeserializer.looksLikeFormattedDateString(correctlyFormattedDate)
		);

		Assert.assertTrue(
			"'" + correctlyFormattedDateUnparseable + "' is detected as formatted date.",
			DateDeserializer.looksLikeFormattedDateString(correctlyFormattedDateUnparseable)
		);

		Assert.assertFalse(
			"'" + incorrectlyFormattedDate + "' is not detected as formatted date.",
			DateDeserializer.looksLikeFormattedDateString(incorrectlyFormattedDate)
		);

		Assert.assertTrue(
			"'" + nonsense + "' is not detected as formatted date.",
			DateDeserializer.looksLikeFormattedDateString(nonsense)
		);
	}

	/**
	 * Check whether parsing works correctly when given correctly formatted
	 * string with sane content and returns null otherwise.
	 */
	@Test
	public void testDeserialisation() {
		Date dateFromNumber = DateDeserializer.parseCommonlyFormattedDate(aNumber);
		Date correctDate = DateDeserializer.parseCommonlyFormattedDate(correctlyFormattedDate);
		Date correctlyFormattedUnparseable = DateDeserializer.parseCommonlyFormattedDate(correctlyFormattedDateUnparseable);
		Date incorrectlyFormatted = DateDeserializer.parseCommonlyFormattedDate(incorrectlyFormattedDate);
		Date nonsenseDate = DateDeserializer.parseCommonlyFormattedDate(nonsense);

		Assert.assertNull(
			"Parsing '" + aNumber + "' as formatted date does return null.",
			dateFromNumber
		);
		Assert.assertNull(
			"Parsing '" + correctlyFormattedDateUnparseable + "' as formatted date does return null.",
			correctlyFormattedUnparseable
		);
		Assert.assertNull(
			"Parsing '" + incorrectlyFormattedDate + "' as formatted date does return null.",
			incorrectlyFormatted
		);
		Assert.assertNull(
			"Parsing '" + nonsense + "' as formatted date does return null.",
			nonsenseDate
		);

		// this one should work
		Assert.assertNotNull(
			"Parsing '" + correctlyFormattedDate + "' as formatted date does not return null.",
			correctDate
		);
		Assert.assertEquals(
			"Parsing '" + correctlyFormattedDate + "' as formatted date returns correct date.",
			compareFormat.format(correctDate),
			correctlyFormattedDate
		);

	}
}
