package com.onemore.karungguniapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;
import android.util.Log;

public class SHA1 {

	// SHA1 variables
	private static String SHAHash;
	private static int NO_OPTIONS=0;
	
	/**
	* Used by computeSHAHash
	*/	
	private static String convertToHex(byte[] data) throws java.io.IOException 
	{         
		StringBuffer sb = new StringBuffer();
		String hex=null;
		 
		hex=Base64.encodeToString(data, 0, data.length, NO_OPTIONS);
		 
		sb.append(hex);
		             
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
		}
		catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		byte[] data = mdSha1.digest();
		try {
			SHAHash=convertToHex(data);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return SHAHash;
	}

}