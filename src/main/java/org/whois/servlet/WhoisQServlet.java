package org.whois.servlet;

import java.io.IOException;
import java.net.SocketException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.whois.WhoisClient;
import org.whois.util.PropertiesUtil;
import org.whois.util.WhoisTest;

public class WhoisQServlet {

	private static Pattern pattern;
	private Matcher matcher;

	// regex whois parser
	private static final String WHOIS_SERVER_PATTERN = "Whois Server:\\s(.*)";
	static {
		pattern = Pattern.compile(WHOIS_SERVER_PATTERN);
	}

	public static void main(String[] args) {

		WhoisTest obj = new WhoisTest();
		System.out.println(obj.getWhois("google.com"));
		System.out.println("--------------------------------------------------------------------------------------------------");
		System.out.println(obj.getWhois("ename.com"));
		System.out.println("--------------------------------------------------------------------------------------------------");
		System.out.println(obj.getWhois("west.cc"));
		System.out.println("Done");

	}

	// example google.com
	public String getWhois(String domainName) {
		StringBuilder result = new StringBuilder("");
		WhoisClient whois = new WhoisClient();
		try {
			String domainSuffix = domainName.substring(domainName.lastIndexOf("."),domainName.length());
			Properties dbProp = PropertiesUtil.readProperties("whois.properties");
			String whoisConnect = dbProp.getProperty(domainSuffix);
			whois.connect(whoisConnect);
			String whoisData1 = whois.query("=" + domainName);
			result.append(whoisData1);
			whois.disconnect();
			String whoisServerUrl = getWhoisServer(whoisData1);
			if (!whoisServerUrl.equals("")) {
				String whoisData2 = queryWithWhoisServer(domainName,
						whoisServerUrl);
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
		while (matcher.find()) {
			result = matcher.group(1);
		}
		return result;
	}

}
