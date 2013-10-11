package com.onemore.karungguniapp;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public class SHA1 {

	// SHA1 variables
	private static String SHAHash;
	private static final String HEX_DIGITS = "0123456789abcdef";
	private static final int BYTE_MSK = 0xFF;
	private static final int HEX_DIGIT_MASK = 0xF;
    private static final int HEX_DIGIT_BITS = 4;
	
	/**
	* Used by computeSHAHash
	*/	
	private static String convertToHex(byte[] data)
	{   
		StringBuilder sb = new StringBuilder(data.length * 2);
        for (int i = 0; i < data.length; i++) {
                int b = data[i] & BYTE_MSK;
                sb.append(HEX_DIGITS.charAt(b >>> HEX_DIGIT_BITS)).append(
                                HEX_DIGITS.charAt(b & HEX_DIGIT_MASK));
        }
        return sb.toString();
	}
	 
	/**
	* Used to generate a hashed string
	*/	 
	public static String computeHash(String password)
	{
		MessageDigest mdSha1 = null;
		try {
			mdSha1 = MessageDigest.getInstance("SHA-1");
		}
		catch (NoSuchAlgorithmException e1) {
			Log.e("myapp", "Error initializing SHA1 message digest");
		}
		try {
			mdSha1.update(password.getBytes("ASCII"));
			byte[] data = mdSha1.digest();
			SHAHash=convertToHex(data);
		}
		catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}

		return SHAHash;
	}
}
