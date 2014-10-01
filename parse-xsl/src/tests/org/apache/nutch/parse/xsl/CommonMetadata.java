package org.apache.nutch.parse.xsl;

/**
 * Metadata that are fetched.
 */
public class CommonMetadata {

	/* Metadata related to wanted people */

	/** description of the wanted people */
	public static final String META_DESCRIPTION = "description";
	/** the people that is searching */
	public static final String META_SEARCHER_PEOPLE = "searcherPeople";
	/** the people that is searched */
	public static final String META_WANTED_PEOPLE = "wantedPeople";

	/* Metadata related */

	/** birth date metadata */
	public static final String META_PEOPLE_BIRTH_DATE = "birthDate";
	/** country metadata */
	public static final String META_PEOPLE_COUNTRY = "country";
	/** city metadata */
	public static final String META_PEOPLE_CITY = "city";
	/** gender metadata */
	public static final String META_PEOPLE_GENDER = "gender";
	/** last name metadata */
	public static final String META_PEOPLE_LAST_NAME = "lastName";
	/** first name metadata */
	public static final String META_PEOPLE_FIRST_NAME = "firstName";
	/** age metadata */
	public static final String META_PEOPLE_AGE = "age";
}
