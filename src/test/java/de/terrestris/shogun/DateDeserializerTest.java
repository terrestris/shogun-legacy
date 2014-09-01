/* Copyright (c) 2012-2014, terrestris GmbH & Co. KG
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * (This is the BSD 3-Clause, sometimes called 'BSD New' or 'BSD Simplified',
 * see http://opensource.org/licenses/BSD-3-Clause)
 *
 * The client-side code of SHOGun partly depends on the ExtJS-framework (see
 * http://www.sencha.com/products/extjs, available separately under various
 * licensing options: http://www.sencha.com/products/extjs/licensing); it is
 * released in accordance with Sencha's Exception for Development Version 1.04
 * from January 18, 2013 (see http://www.sencha.com/legal/open-source-faq/
 * open-source-license-exception-for-development/).
 */
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
