package rcgaBatch1.batch;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import net.java.sen.SenFactory;
import net.java.sen.StringTagger;
import net.java.sen.dictionary.Token;
import util.ReadFileUtil;




public class TestCountBunkai {

	public static void main(String[] args) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

		// この3行で解析できる
		StringTagger tagger = SenFactory.getStringTagger(null);
		List<Token> tokens = new ArrayList<Token>();
		//tagger.analyze("君が買った時が天上。売った時が底値・・・だろ？株ってそんなもんだよ。", tokens);

//		String str = "澤口澤口澤口さんは、日常と違ったことをするのも情動体験のひとつで。スリルや爽快感を体験できるバイクもおすすめだという日常";
//		String[] line = str.split("。");

		//GA学習結果の読み取り
		String csvStudyInput = "C:\\pleiades\\workspace\\getStudyHosoku\\src\\main\\java\\rcgaBatch1\\batch\\studyInput.txt";
		LinkedHashMap<String,String[]> studyMap = ReadFileUtil.readCsvCom(csvStudyInput);

		//ネット情報を分割して配列に
		Pattern p = Pattern.compile("[。.]+");


		LinkedHashMap<String,String[]> mapCountWord = new LinkedHashMap<String, String[]>();

		String[] wordInfo;
		for (String key : studyMap.keySet()) {
			String studyLine = studyMap.get(key)[3];
				tagger.analyze(studyLine, tokens);
				// 初期化
				for (Token token : tokens) {
					wordInfo = new String[4];
					wordInfo[0] = "SEIKAI";
					wordInfo[2] = "0";  //正解データ（学習データ）の単語数
					wordInfo[3] = "0";  //不正解データの単語数
					if ("*".equals(token.getMorpheme().getBasicForm())) {
						wordInfo[1] = token.getSurface();
					} else {
						wordInfo[1] = token.getMorpheme().getBasicForm();
					}
					mapCountWord.put(wordInfo[1], wordInfo);
				}
		}

		//回数カウント
		for (String key : studyMap.keySet()) {
			String studyLine = studyMap.get(key)[3];
			if ("T".equals(studyMap.get(key)[1])) {
				tagger.analyze(studyLine, tokens);
				for (Token token : tokens) {
					wordInfo = new String[4];
					if ("*".equals(token.getMorpheme().getBasicForm())) {
						wordInfo[1] = token.getSurface();
					} else {
						wordInfo[1] = token.getMorpheme().getBasicForm();
					}
					wordInfo[0] = "SEIKAI";
					wordInfo[2] = Integer.toString(Integer.valueOf(mapCountWord.get(wordInfo[1])[2]) + 1);
					wordInfo[3] = Integer.toString(Integer.valueOf(mapCountWord.get(wordInfo[1])[3]) + 0); //不正解データには加算しない
					mapCountWord.put(wordInfo[1], wordInfo);
				}
			} else {
				tagger.analyze(studyLine, tokens);
				for (Token token : tokens) {
					wordInfo = new String[4];
					if ("*".equals(token.getMorpheme().getBasicForm())) {
						wordInfo[1] = token.getSurface();
					} else {
						wordInfo[1] = token.getMorpheme().getBasicForm();
					}
					wordInfo[0] = "SEIKAI";
					wordInfo[2] = Integer.toString(Integer.valueOf(mapCountWord.get(wordInfo[1])[2]) + 0);
					wordInfo[3] = Integer.toString(Integer.valueOf(mapCountWord.get(wordInfo[1])[3]) + 1);
					mapCountWord.put(wordInfo[1], wordInfo);
				}

			}


		}


		for (String key : mapCountWord.keySet()) {
			System.out.println("単語 : " + mapCountWord.get(key)[1] + "　回数（正解データ） : " + mapCountWord.get(key)[2]
					+ "　回数（不正解データ） : " + mapCountWord.get(key)[3]);
		}

	}



}
