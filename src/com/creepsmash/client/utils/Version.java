package com.creepsmash.client.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.creepsmash.client.Core;



public class Version {
	/**
	 * @return the version
	 */
	public static String getVersion() {
		String version = null;

		InputStream inStream = Version.class.getResourceAsStream("version");
		try {
			if (inStream.available() > 0) {
				InputStreamReader inStreamReader = null;

				try {
					inStreamReader = new InputStreamReader(inStream);
				} catch (Exception e) {
					Core.logger.info("IOException: " + e);
					inStream.close();
				}

				try {
					BufferedReader reader = new BufferedReader(inStreamReader);
					version = reader.readLine();
					reader.close();
				} catch (IOException e) {
					Core.logger.info("IOException: " + e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (version == null) {
			version = "-unknown-";
		}
		return version;
	}
}
