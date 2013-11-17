package sph;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 
 * UrlUtils.java
 * 
 * <p>
 * This class has some methods for URL handling.
 * </p>
 * <p>
 * Original source from 
 * <a href="http://www.rgagnon.com/javadetails/java-0059.html">Real's Java How-to</a>.
 * </p>
 * 
 * @author Sami Holck
 * @author Juha Mäkilä
 * 
 */
public class UrlUtils {
	
	/**
	 * Tests if the given HTTP URL is working.
	 * 
	 * @param url the tested HTTP URL
	 * @return true if the given URL exists and false otherwise.
	 */
	public static boolean exists(String url) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			// HttpURLConnection.setInstanceFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			// System.out.println("HTTP url: '" + url + "' is not valid");
			// e.printStackTrace();
			return false;
		}
	}
}
