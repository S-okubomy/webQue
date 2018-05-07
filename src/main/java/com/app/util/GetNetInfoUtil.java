package com.app.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

		String beforeWord = "<p class=\"title fsL1\"> <a href=\"";  //抽出単語の前部（空白含む）
		String afterWord = "\" target=";							 //抽出単語の後部（空白含む）

		List<String> studyHtmlList = new ArrayList<String>();
		try{

	    	Document document = Jsoup.connect(reqUrl).get();
	        String[] line = document.html().split("\n");

	        //受信したストリームから値を取り出す
	        for (int i = 0 ; i < line.length ; i++) {
				if(line[i].indexOf("<p class=\"title fsL1\"> <a href") != -1){
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

    /**
     * (google検索用)HTMLのURLリストを返す
     * @param reqUrl
     * @return
     * @throws IOException
     */
    public static List<String> getHtmlListForGoogle(String reqUrl) throws IOException {
    
        List<String> studyHtmlList = new ArrayList<String>();
        try{
            Document document = Jsoup.connect(reqUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")
                    .get();
            Elements elementPs = document.select(".r a");  // <h3 class="r">中の<a>要素を取得
            
            //受信したストリームから値を取り出す
            for (Element elementp : elementPs) {
                if (!elementp.attr("href").contains("youtube")){
                    studyHtmlList.add("http://google.com" + elementp.attr("href"));
                }
            }
            
            //0.2秒待つ
            Thread.sleep( 200 ) ;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error Unsupported");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error IOException");
            e.printStackTrace();
        } catch(Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return studyHtmlList;
    }
    
    
    /**
     * 翻訳用（英語）
     * @param reqUrl
     * @return
     * @throws IOException
     */
    public static List<String> getEnglishWord(String reqUrl) throws IOException {
    
        List<String> studyHtmlList = new ArrayList<String>();
        try{
            Document document = Jsoup.connect(reqUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")
                    .get();
            Element elementp = document.select("td.content-explanation").first();  // tdタグのcontent-explanation
            
            //受信したストリームから値を取り出す
            studyHtmlList.add(elementp.text());
            //0.2秒待つ
            Thread.sleep( 200 ) ;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error Unsupported");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error IOException");
            e.printStackTrace();
        } catch(Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return studyHtmlList;
    }
    
    /**
     * 画像のURLリストを返す
     * @param reqUrl
     * @return
     * @throws IOException
     */
    public static List<String> getUrlListForImg(String reqUrl) throws IOException {
    
        List<String> studyHtmlList = new ArrayList<String>();
        try{
            Document document = Jsoup.connect(reqUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")
                    .get();
            Elements elementPs = document.select("body img");  // <h3 class="r">中の<a>要素を取得
            String preUrl = reqUrl.replace("http://google.com/url?q=", "");
            String urlPreHead = SelectWordUtil.selectWord(preUrl, "", "://"); 
            String urlHead = urlPreHead +  "://" + SelectWordUtil.selectWord(preUrl
                    .replace(urlPreHead  + "://", ""), "", "/"); 
            
            //受信したストリームから値を取り出す
            for (Element elementp : elementPs) {
                if (elementp.attr("src") != null && (elementp.attr("src").contains(".jpg")
                        || elementp.attr("src").contains(".JPG")) && !"".equals(elementp.attr("alt"))) {
                    if (elementp.attr("src").contains(urlPreHead)) {
                        studyHtmlList.add(elementp.attr("src"));
                    } else if (elementp.attr("src").contains("//")) {
                        studyHtmlList.add(urlPreHead + ":" + elementp.attr("src"));
                    } else if (elementp.attr("src").contains("/")) {
                        studyHtmlList.add(urlHead + elementp.attr("src"));
                    }
                }
            }
            
            //0.2秒待つ
            Thread.sleep( 200 ) ;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error Unsupported");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error IOException");
            e.printStackTrace();
        } catch(Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return studyHtmlList;
    }
}
