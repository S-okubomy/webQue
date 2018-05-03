package com.app.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.app.util.GetHinshiUtil.HINSHI_NOTUSE_NETINFO;

import net.java.sen.SenFactory;
import net.java.sen.StringTagger;
import net.java.sen.dictionary.Token;

public class SelectWordUtil {

	/**
	 * インスタンス化禁止のため、コンストラクタをprivateにしている。
	 */
	private SelectWordUtil() {}


	/**
	 * 文字列から指定の文字間を抜き出す
	 * @param line
	 * @param beforeWord
	 * @param afterWord
	 * @return　抜き出した文字列 指定の文字列がない場合は、そのまま返す
	 */
	public static String selectWord(String line, String beforeWord, String afterWord ) {

		if (!SelectWordUtil.isNullOrEmpty(line)) {
			int indexBefore = line.indexOf(beforeWord);
			if (indexBefore == -1) {
				return line;
			}

			indexBefore += beforeWord.length();
			int indexAfter = line.indexOf(afterWord);
			if (indexAfter == -1) {
				return line;
			}

			return line.substring(indexBefore,indexAfter) ;
		} else {
			return "";
		}

	}

	/**
	 * 文字列から指定の文字間を抜き出す
	 * @param line
	 * @param beforeWord
	 * @param afterWord
	 * @return　抜き出した文字列 指定の文字列がない場合は、空文字を返す
	 */
	public static String selectWordWithEmp(String line, String beforeWord, String afterWord ) {

		if (!SelectWordUtil.isNullOrEmpty(line)) {
			int indexBefore = line.indexOf(beforeWord);
			if (indexBefore == -1) {
				return "";
			}

			indexBefore += beforeWord.length();
			int indexAfter = line.indexOf(afterWord);
			if (indexAfter == -1) {
				return "";
			}

			return line.substring(indexBefore,indexAfter) ;
		} else {
			return "";
		}

	}

	/**
	 * 文字列から指定の文字から後ろを取り出す
	 * @param line
	 * @param beforeWord
	 * @param afterWord
	 * @return　抜き出した文字列 指定の文字列がない場合は、空文字を返す
	 */
	public static String selectWordKoubuWithEmp(String line, String beforeWord) {

		if (!SelectWordUtil.isNullOrEmpty(line)) {
			int indexBefore = line.indexOf(beforeWord);
			if (indexBefore == -1) {
				return "";
			}

			indexBefore += beforeWord.length();

			return line.substring(indexBefore);
		} else {
			return "";
		}

	}
	
	public static String getSplitWord(String str) throws IOException {
	    
       StringTagger tagger = SenFactory.getStringTagger(null);
       List<Token> tokens = new ArrayList<Token>();
       tagger.analyze(str, tokens);
       
       StringBuilder splitWord = new StringBuilder();
       for (Token token : tokens) {
           // 該当品詞の場合は、スペース区切りで文字列を結合する。
           if (!isNotUseWord(token.getMorpheme().getPartOfSpeech())) {
               splitWord.append(token.getSurface() + " ");
           }
       }
       
       return splitWord.toString();
	}
	
	public static boolean isNotUseWord(String wordType) {
	    for (HINSHI_NOTUSE_NETINFO hinshi : HINSHI_NOTUSE_NETINFO.values()) {
	        // 使わない品詞の場合（文字列が存在する場合）は、Trueを設定してReturn
	        if (-1 != wordType.indexOf(hinshi.name())) {
	            return true;
	        }
	    }
	    
	    return false;
	}
	
	


	/**
	 * strがnullもしくは空文字であればtrueを返す
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {

	    return (str == null || str.length() == 0);
	}
}
