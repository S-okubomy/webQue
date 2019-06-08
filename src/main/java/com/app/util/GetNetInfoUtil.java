package com.app.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.app.dto.AnsModelDto;

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
        String afterWord = "\" target=";                             //抽出単語の後部（空白含む）

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
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .get();
            Elements elementPs = document.select(".rc .r a");  // <h3 class="r">中の<a>要素を取得
            
            //受信したストリームから値を取り出す
            for (Element elementp : elementPs) {
                if (!"".equals(elementp.attr("href")) && !"#".equals(elementp.attr("href"))
                		&& elementp.attr("href").startsWith("http")
                		&& !elementp.attr("href").contains("webcache")
                		&& !elementp.attr("href").contains("youtube")){
                    studyHtmlList.add(elementp.attr("href"));
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
    
    
    public static List<AnsModelDto> getResultAns(String splitSerchWord, String queType, String reqUrlAll
            , String jpSelecter, int maxHtmlListSize) throws Exception {
        
        long startAns = System.currentTimeMillis();
        
        //学習先のHTMLリスト
        List<String> studyHtmlList = new ArrayList<String>();
        long startHtml = System.currentTimeMillis();
        studyHtmlList = GetNetInfoUtil.getHtmlListForGoogle(reqUrlAll);
        System.out.println("以下、検索元URL");
        System.out.println(reqUrlAll);
        System.out.println("以下、URLリスト（検索結果）");
        for (String htmlUrl : studyHtmlList) {
            System.out.println(htmlUrl);
        }
        
        // Projectのトップディレクトリパス取得
        String folderName = System.getProperty("user.dir");
        
        // トップディレクトリパス以降を設定
        String inputFolderName = folderName + "/src/main/resources/inputFile/";

        //重み係数の読み込み
        String csvWeightValueFileInput = inputFolderName + "ans_outWeightValue.csv";
        LinkedHashMap<String,String[]> weightValueMap = ReadFileUtil.readCsvCom(csvWeightValueFileInput);

        //GA学習結果の読み取り（getStudyManModelTestHist.csv → ans_SVMParam.csv）
        String csvStudyResultInput = inputFolderName + "ans_SVMParam.csv";
        LinkedHashMap<String,String[]> studyResultMap = ReadFileUtil.readCsvCom(csvStudyResultInput);
        String[] gaResultArray = studyResultMap.get("2").clone();

        //素性ベクトル作成用
        String soseiVecterSakusei = inputFolderName + "ans_studyInput.txt";
        LinkedHashMap<String,String[]> soseiVecterSakuseiMap = ReadFileUtil.readCsvCom(soseiVecterSakusei);
        
        long endHtml = System.currentTimeMillis();
        long intervalHtml = endHtml - startHtml;
        System.out.println(intervalHtml + "ミリ秒  html");

        Pattern pUrl = Pattern.compile("(.pdf)");
        Matcher matcherUrl;
        String[] sujoVector;
        
        // 回答結果のList格納用
        List<AnsModelDto> ansModelList = new ArrayList<AnsModelDto>();
        try {
            int cntHtmlList = 0;
            Pattern p = Pattern.compile("[。.]+");
            for(String studyHtml : studyHtmlList) {
                try{
                    matcherUrl = pUrl.matcher(studyHtml);
                    if (!matcherUrl.find()) {
                        //URLにアクセス
                        
                        long start = System.currentTimeMillis();
                        Document document = Jsoup.connect(studyHtml)
                        		.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        		.get();
                        Elements elementP = document.select(jpSelecter);
                        long end = System.currentTimeMillis();
                        long interval = end - start;
                        System.out.println(interval + "ミリ秒  Jsop");
                        //ネット情報を分割して配列に
                        
                        long start2 = System.currentTimeMillis();
                        String[] rsltNetInfo = p.split(elementP.text());
                        for (int iCount =0; iCount < rsltNetInfo.length; iCount++) {
                            // 正規表現でフィルター（文章の前後にスペースを含む行を除く    "^\\x01-\\x7E"で1バイト文字以外を探す）
                            // 5文字以上、上記以外の文章を対象にする。
                            if (15 <= rsltNetInfo[iCount].length() && rsltNetInfo[iCount].length() <= 150 
                                    && !rsltNetInfo[iCount].matches(".*([a-zA-Z0-9]|[^\\x01-\\x7E]).*\\ ([a-zA-Z0-9]|[^\\x01-\\x7E]).*")
                                    && !rsltNetInfo[iCount].matches(".*([a-zA-Z0-9/%\\-_:=?]{10,}).*")
                                    && !rsltNetInfo[iCount].matches(".*「追加する」ボタンを押してください.*")) {
                                sujoVector = GetMLerningUtil.getSujoVector(soseiVecterSakuseiMap
                                        , rsltNetInfo[iCount]);
                                // 振り分け結果を出力
                                GetMLerningUtil.outFuriwakeResult(sujoVector, weightValueMap, gaResultArray
                                        , rsltNetInfo[iCount], ansModelList, studyHtml, queType);
                            }
// TODO 時間短縮 検討
                            if (500 <= iCount -1) {
                                break;
                            }
                        }
                        cntHtmlList++;
                        
                        long end2 = System.currentTimeMillis();
                        long interval2 = end2 - start2;
                        System.out.println(interval2 + "ミリ秒  振り分け");
                    }
                    if (maxHtmlListSize <= cntHtmlList) {
                        break; // 最大検索サイト数以上になったらブレーク
                    }
                } catch (HttpStatusException e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                } catch (IOException e){
                    e.printStackTrace();
                    System.out.println("1検索 失敗: " + e.getMessage());
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("その他: " + e.getMessage());
                } 
            }
            Collections.sort(ansModelList, new SortAnsModelList());
            System.out.println("回答抽出完了");
            long endAns = System.currentTimeMillis();
            long intervalAns = endAns - startAns;
            System.out.println(intervalAns + "ミリ秒  Ans");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("全件 失敗: " + e.getMessage());
        } finally {
        }

        return ansModelList;
        
        
    }
    
    /**
     * ソート用クラス
     * @author Administrator
     *
     */
    private static class SortAnsModelList implements Comparator<AnsModelDto> {
        public static final int ASC = 1;   //昇順 (1.2.3....)
        public static final int DESC = -1; //降順 (3.2.1....)
        
        @Override
        public int compare(AnsModelDto ansModel1, AnsModelDto ansModel2) {
            // compareメソッド : 引数1=引数2→0、引数1<引数2→-1、引数1>引数2→1
            // 降順
            return DESC * Double.compare(ansModel1.getFxValue(), ansModel2.getFxValue());
        }
    }
    
    
}
