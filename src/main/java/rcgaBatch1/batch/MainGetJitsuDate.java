package rcgaBatch1.batch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.java.sen.SenFactory;
import net.java.sen.StringTagger;
import net.java.sen.dictionary.Token;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import rcgaBatch1.dto.InitDto;
import util.ReadFileUtil;
import util.SelectWordUtil;

/**
 * GAの学習結果より、ネットから情報を収集する
 * @author Administrator
 *
 */
public class MainGetJitsuDate {

	public static final String SEIKAI = "T";
	public static final String FUSEIKAI = "F";

	public static void main(String[] args) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

		MainGetJitsuDate mainGetJitsuDate = new MainGetJitsuDate();

		//
		String[] args1 = new String[2];
		mainGetJitsuDate.getJitsuDate(args1);
	}

	public void getJitsuDate(String[] args) throws Exception {

		//学習データ取得先 URL指定
//		String reqUrl = "http://dic.nicovideo.jp/a/";
		String reqUrl = "https://ja.wikipedia.org/wiki/";
		//String reqUrl = "http://search.goo.ne.jp/web.jsp?MT=掲示板 ADワークス&mode=0&sbd=goo001&IE=UTF-8&OE=UTF-8";

		//学習先のHTMLリスト
		List<String> studyHtmlList = new ArrayList<String>();

		//TODO テスト用　後消す
//		args[0] = "Raspberry Piとは";
//		args[0] = "チョコレートとは";
		args[0] = "ギター";
		studyHtmlList.add(reqUrl + args[0]);
		System.out.println("以下、検索元URL");
		System.out.println(reqUrl + args[0]);
		System.out.println("以下、URLリスト（検索結果）");
		for (String htmlUrl : studyHtmlList) {
			System.out.println(htmlUrl);
		}
		
        // Projectのトップディレクトリパス取得
        String folderName = System.getProperty("user.dir");
        // トップディレクトリパス以降を設定
        folderName = folderName + "\\src\\main\\java\\rcgaBatch1\\batch\\";

		//重み係数の読み込み
		String csvWeightValueFileInput = folderName + "outWeightValue.csv";
		LinkedHashMap<String,String[]> weightValueMap = ReadFileUtil.readCsvCom(csvWeightValueFileInput);

		//配列を作りなおし
		String[] weightValueMapArray = new String[weightValueMap.get("1").length -1];
		for(int ii = 0; ii < weightValueMap.get("1").length -1; ii++) {
			weightValueMapArray[ii] = weightValueMap.get("1")[ii + 1];
    	}

		//GA学習結果の読み取り
		String csvStudyResultInput = folderName + "getStudyManModelTestHist.csv";
		LinkedHashMap<String,String[]> studyResultMap = ReadFileUtil.readCsvCom(csvStudyResultInput);
		String[] gaResultArray = studyResultMap.get("2").clone();

		//素性ベクトル作成用
		String soseiVecterSakusei = folderName + "studyInput.txt";
		LinkedHashMap<String,String[]> soseiVecterSakuseiMap = ReadFileUtil.readCsvCom(soseiVecterSakusei);

		//出力ファイル
		String strOutputFile = folderName + "outputJitsu.txt";
		BufferedWriter newFileStream = new BufferedWriter(new FileWriter(strOutputFile));

		Pattern pUrl = Pattern.compile("(.pdf)");
		Matcher matcherUrl;
		String[] sujoVector;

		try{
			for(String studyHtml : studyHtmlList) {
				matcherUrl = pUrl.matcher(studyHtml);
				if (!matcherUrl.find()) {
					//URLにアクセス
					Document document = Jsoup.connect(studyHtml).get();
					//ネット情報を分割して配列に
					Pattern p = Pattern.compile("[。.]+");
					String[] rsltNetInfo = p.split(document.text());
						for (int iCount =0; iCount < rsltNetInfo.length; iCount++) {
							sujoVector = getSujoVector(soseiVecterSakuseiMap, rsltNetInfo[iCount]);
							if (SEIKAI.equals(isFuriwake(sujoVector, weightValueMapArray, Double.parseDouble(gaResultArray[3])))) {
								//ファイルへの書き込み
								newFileStream.write(rsltNetInfo[iCount]);
								newFileStream.newLine();
								newFileStream.flush();
							}
						}
				}
			}
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("ファイル書き込み失敗");
		} finally {
			try {
				// ストリームは必ず finally で close
				if (newFileStream != null) {
					newFileStream.close();
					System.out.println("出力完了");
				}
			} catch (IOException e) {
			}
		}

	}

	/**
	 * 素性ベクトルを返す
	 * @param soseiVecterSakuseiMap
	 * @param strNetInfo
	 * @return
	 * @throws Exception
	 */
	private String[] getSujoVector(LinkedHashMap<String,String[]> soseiVecterSakuseiMap, String strNetInfo) throws Exception {

		// この3行で解析できる
		StringTagger tagger = SenFactory.getStringTagger(null);
		List<Token> tokens = new ArrayList<Token>();

		// 1-gram
		StringBuilder oneGramTitle = new StringBuilder();
		for (String key : soseiVecterSakuseiMap.keySet()) {
			String studyLine = soseiVecterSakuseiMap.get(key)[3];
			tagger.analyze(studyLine, tokens);
			for (Token token : tokens) {
				if (!"名詞".equals(SelectWordUtil.selectWord(token.getMorpheme().getPartOfSpeech(), "", "-"))) {
					oneGramTitle.append(token.getSurface() + ",");
				}
			}
		}

		// 2-gram（単語）
		StringBuilder twoGramTitle = new StringBuilder();
		for (String key : soseiVecterSakuseiMap.keySet()) {
			String studyLine = soseiVecterSakuseiMap.get(key)[3];
			tagger.analyze(studyLine, tokens);
			for (int i = 0; i < tokens.size() -1; i++) {
				if (!"名詞".equals(SelectWordUtil.selectWord(tokens.get(i).getMorpheme().getPartOfSpeech(), "", "-"))
						&& !"名詞".equals(SelectWordUtil.selectWord(tokens.get(i+1).getMorpheme().getPartOfSpeech(), "", "-"))) {
					twoGramTitle.append(tokens.get(i).getSurface()); // 1単語目の出力
					twoGramTitle.append(tokens.get(i + 1).getSurface() + ","); // 連結 2単語目の出力
				}
			}
		}

		// 2-gram（単語/品詞）
		StringBuilder tangoHinshi = new StringBuilder();
		for (String key : soseiVecterSakuseiMap.keySet()) {
			String studyLine = soseiVecterSakuseiMap.get(key)[3];
			tagger.analyze(studyLine, tokens);
			for (int i = 0; i < tokens.size() -1; i++) {
				if (!"名詞".equals(SelectWordUtil.selectWord(tokens.get(i).getMorpheme().getPartOfSpeech(), "", "-"))
						&& !"名詞".equals(SelectWordUtil.selectWord(tokens.get(i+1).getMorpheme().getPartOfSpeech(), "", "-"))) {
					tangoHinshi.append(tokens.get(i).getSurface());  // 1単語目の出力
					tangoHinshi.append("/");
					tangoHinshi.append(SelectWordUtil.selectWord(tokens.get(i+1).getMorpheme().getPartOfSpeech(), "", "-") + ",");  // 連結 2単語目の出力
				}

			}
		}

		// 以下、素性ベクトル作成
		String[] tmpOneGramTitle = new String(oneGramTitle).split(",");
		String[] tmpTwoGramTitle = new String(twoGramTitle).split(",");
		String[] tmpTangoHinshiGramTitle = new String(tangoHinshi).split(",");
		StringBuilder tmpRenketsu;

		String studyLine = strNetInfo;
		tagger.analyze(studyLine, tokens);

		boolean isVectorFlag;
		//1gram チェック
		StringBuilder oneGramSujoVector = new StringBuilder();
		for (String oneGram : tmpOneGramTitle) {
			isVectorFlag = false;
			for (Token token : tokens) {
				if (oneGram.equals(token.getSurface())) {
					oneGramSujoVector.append("1,");
					isVectorFlag = true;
					break;
				}
			}
			if (!isVectorFlag) {
				oneGramSujoVector.append("0,");
			}
		}

		//2gram チェック
		StringBuilder twoGramSujoVector = new StringBuilder();
		for (String twoGram : tmpTwoGramTitle) {
			isVectorFlag = false;
			for (int i = 0; i < tokens.size() -1; i++) {
				tmpRenketsu = new StringBuilder();
				tmpRenketsu.append(tokens.get(i).getSurface()); // 1単語目の出力
				tmpRenketsu.append(tokens.get(i + 1).getSurface()); // 連結 2単語目の出力
				if (twoGram.equals(new String(tmpRenketsu))) {
					twoGramSujoVector.append("1,");
					isVectorFlag = true;
					break;
				}
			}
			if (!isVectorFlag) {
				twoGramSujoVector.append("0,");
			}
		}

		//2-gram（単語/品詞）
		StringBuilder tangoHinshiSujoVector = new StringBuilder();
		for (String TangoHinshiGram : tmpTangoHinshiGramTitle) {
			isVectorFlag = false;
			for (int i = 0; i < tokens.size() -1; i++) {
				tmpRenketsu = new StringBuilder();
				tmpRenketsu.append(tokens.get(i).getSurface()); // 1単語目の出力
				tmpRenketsu.append("/");
				tmpRenketsu.append(SelectWordUtil.selectWord(tokens.get(i+1).getMorpheme().getPartOfSpeech(), "", "-"));  // 連結 2単語目の出力
				if (TangoHinshiGram.equals(new String(tmpRenketsu))) {
					tangoHinshiSujoVector.append("1,");
					isVectorFlag = true;
					break;
				}
			}
			if (!isVectorFlag) {
				tangoHinshiSujoVector.append("0,");
			}
		}

		// ベクトルを全てまとめる
		StringBuilder allSujoVector = new StringBuilder();
		allSujoVector.append(oneGramSujoVector);
		allSujoVector.append(twoGramSujoVector);
		allSujoVector.append(twoGramSujoVector);

		return new String(allSujoVector).split(",");
	}


	/**
	 * 正解データかどうか振り分ける
	 * @param gaParameterKotai（GAが文章を述語と振り分けるために算出した係数）
	 * @param getHinshiTmp
	 * @return　述語に振り分けたらTrue（正解データと判定）, それ以外はFalse
	 */
	private String isFuriwake(String[] allSujoVector, String[] weightParam, double gaParameter) {

		int[] intSujoVector = new int[allSujoVector.length];
		int[] intWeightParam = new int[allSujoVector.length];

		//配列をint型へ変換
		for(int ii = 0; ii < allSujoVector.length -1; ii++) {
			intSujoVector[ii] = Integer.valueOf(allSujoVector[ii]);
    	}

		//配列をint型へ変換
		for(int ii = 0; ii < allSujoVector.length -1; ii++) {
			intWeightParam[ii] = Integer.valueOf(weightParam[ii]);
    	}

		double fxValue = (double)getNaiseki(intWeightParam, intSujoVector)
							+ gaParameter;

		if (fxValue >= 0) {
			return SEIKAI;
		} else { // fx が負の場合
			return FUSEIKAI;
		}

	}

	/**
	 * 内積を計算する
	 * @param vectorX1
	 * @param vectorX2
	 * @return 内積
	 */
	private int getNaiseki(int[] vectorX1, int[] vectorX2) {
		//内積
		int naisekiValue = 0;
		for(int ii = 0; ii < vectorX1.length; ii++) {
			naisekiValue = naisekiValue + vectorX1[ii] * vectorX2[ii];
    	}

		return naisekiValue;
	}



}
