package ar.com.juani.melishows.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormattingConstants {
	
	
	public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(Locale.forLanguageTag("es")));
	
	private FormattingConstants() {
	}
}
