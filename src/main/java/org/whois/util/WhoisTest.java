package org.whois.util;

import java.io.IOException;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.whois.WhoisClient;

public class WhoisTest {

	private static Pattern pattern;
	private Matcher matcher;

	// regex whois parser
	private static final String WHOIS_SERVER_PATTERN = "Whois Server:\\s(.*)";
	static {
		pattern = Pattern.compile(WHOIS_SERVER_PATTERN);
	}

	public static void main(String[] args) {

		WhoisTest obj = new WhoisTest();
		System.out.println(obj.getWhois("west.cn"));
		System.out.println("--------------------------------------------------------------------------------------------------");
		System.out.println(obj.getWhois("qqqq.cn"));

		System.out.println("Done");

	}

	// example google.com
	public String getWhois(String domainName) {

		StringBuilder result = new StringBuilder("");

		WhoisClient whois = new WhoisClient();
		try {

//			  whois.connect(WhoisClient.DEFAULT_HOST);
//			  
//
//			  whois.connect(WhoisClient.NETASCII_EOL);
			  

			  whois.connect("whois.nic.cc");

		  // whois =google.com
		  String whoisData1 = whois.query("=" + domainName);

		  // append first result
		  result.append(whoisData1);
		  whois.disconnect();

		  // get the google.com whois server - whois.markmonitor.com
		  String whoisServerUrl = getWhoisServer(whoisData1);
		  if (!whoisServerUrl.equals("")) {

			// whois -h whois.markmonitor.com google.com
			String whoisData2 = 
                            queryWithWhoisServer(domainName, whoisServerUrl);

			// append 2nd result
			result.append(whoisData2);
		  }

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	}

	private String queryWithWhoisServer(String domainName, String whoisServer) {

		String result = "";
		WhoisClient whois = new WhoisClient();
		try {

			whois.connect(whoisServer);
			result = whois.query(domainName);
			whois.disconnect();

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	private String getWhoisServer(String whois) {

		String result = "";

		matcher = pattern.matcher(whois);

		// get last whois server
		while (matcher.find()) {
			result = matcher.group(1);
		}
		return result;
	}

}