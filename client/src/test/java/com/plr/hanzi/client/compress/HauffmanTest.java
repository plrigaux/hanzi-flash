package com.plr.hanzi.client.compress;

import java.util.HashMap;

import org.junit.Test;

import com.plr.hanzi.client.compress.HuffmanDecoder;
import com.plr.hanzi.client.compress.HuffmanEncoder;

public class HauffmanTest {
	@Test
	public void test1() {
		
		
	String 	source = "asdfasdfasdfasdfasdfasdfasdfsarfewrfasdfasdfasdfasdfasdf";
	
	HashMap<Character, String> encodingData = new HashMap<Character, String>();

        HuffmanEncoder ENC = new HuffmanEncoder();
        HuffmanDecoder DEC = new HuffmanDecoder();

//            byte[] data = StringUtil.toUtf16ByteArray(source);
//            data.length;
        byte[] data = ENC.encode(source, encodingData);
      
        String 	res =  DEC.decode(data, encodingData, source.length());
	
        System.out.println(source);
        System.out.println(encodingData);
        System.out.println(source.length());
        System.out.println(data.length);
        System.out.println(res);
	}
}
