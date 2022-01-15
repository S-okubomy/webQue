package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetNetInfoUtil {

	/**
	 * インスタンス化禁止のため、コンストラクタをprivateにしている。
	 */
	private GetNetInfoUtil() {}

	/**
	 * HTMLのURLリストを返す
	 * @param reqUrl
	 * @return
	 * @throws IOException
	 */
	public static List<String> getStudyHtmlList(String reqUrl) throws IOException {

		String beforeWord = "<p class=\"title fsL1\"><a href=\"";  //抽出単語の前部（空白含む）
		String afterWord = "\" target=";							 //抽出単語の後部（空白含む）

		List<String> studyHtmlList = new ArrayList<String>();
		try{

	    	Document document = Jsoup.connect(reqUrl).get();
	        String[] line = document.html().split("\n");

	        //受信したストリームから値を取り出す
	        for (int i = 0 ; i < line.length ; i++) {
				if(line[i].indexOf("<p class=\"title fsL1\"><a href") != -1){
					studyHtmlList.add(SelectWordUtil.selectWord(line[i], beforeWord, afterWord));
				}
			}
			//0.2秒待つ
			Thread.sleep( 200 ) ;

		} catch(Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}

		return studyHtmlList;
	}


}
