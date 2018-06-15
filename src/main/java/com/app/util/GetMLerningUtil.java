package com.app.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.app.dto.AnsModelDto;
import com.app.dto.StudyModelDto;

import net.java.sen.SenFactory;
import net.java.sen.StringTagger;
import net.java.sen.dictionary.Token;

public class GetMLerningUtil {

    /**
     * インスタンス化禁止のため、コンストラクタをprivateにしている。
     */
    private GetMLerningUtil() {}


    public static final String SEIKAI = "T";
    public static final String FUSEIKAI = "F"; 
    
    /**
     * 素性ベクトルを返す
     * @param soseiVecterSakuseiMap
     * @param strNetInfo
     * @return
     * @throws Exception
     */
    public static String[] getSujoVector(LinkedHashMap<String,String[]> soseiVecterSakuseiMap
            , String strNetInfo) throws Exception {
        // この3行で解析できる
        StringTagger tagger = SenFactory.getStringTagger(null);
        List<Token> tokens = new ArrayList<Token>();

        StringBuilder oneGramTitle = new StringBuilder();
        StringBuilder twoGramTitle = new StringBuilder();
        StringBuilder tangoHinshi = new StringBuilder();
        for (String key : soseiVecterSakuseiMap.keySet()) {
            String studyLine = soseiVecterSakuseiMap.get(key)[3];
            tagger.analyze(studyLine, tokens);
            for (int i = 0; i < tokens.size(); i++) {
                // 1-gram
                oneGramTitle.append(tokens.get(i).getSurface() + ",");
                // 2-gram
                if (i < tokens.size() -1) {
                    // 2-gram（単語）
                    twoGramTitle.append(tokens.get(i).getSurface()); // 1単語目の出力
                    twoGramTitle.append(tokens.get(i + 1).getSurface() + ","); // 連結 2単語目の出力
                    // 2-gram（単語/品詞）
                    tangoHinshi.append(tokens.get(i).getSurface());  // 1単語目の出力
                    tangoHinshi.append("/");
                    tangoHinshi.append(SelectWordUtil.selectWord(tokens.get(i+1).getMorpheme().getPartOfSpeech(), "", "-") + ",");  // 連結 2単語目の出力
                }
            }
        }

        // 以下、素性ベクトル作成
        String[] tmpOneGramTitle = oneGramTitle.toString().split(",");
        String[] tmpTwoGramTitle = twoGramTitle.toString().split(",");
        String[] tmpTangoHinshiGramTitle = tangoHinshi.toString().split(",");
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
                if (twoGram.equals(tmpRenketsu.toString())) {
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
                if (TangoHinshiGram.equals(tmpRenketsu.toString())) {
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
        allSujoVector.append(tangoHinshiSujoVector);

        return allSujoVector.toString().split(",");
    }

    /**
     * 正解データかどうか振り分ける
     * @param gaParameterKotai（GAが文章を述語と振り分けるために算出した係数）
     * @param getHinshiTmp
     * @return　述語に振り分けたらTrue（正解データと判定）, それ以外はFalse
     */
    public static StudyModelDto isFuriwake(String[] allSujoVector, String[] weightParam, double gaParameter) {

        int[] intSujoVector = new int[allSujoVector.length];
        int[] intWeightParam = new int[allSujoVector.length];

        //配列をint型へ変換
        for(int ii = 0; ii < allSujoVector.length -1; ii++) {
            intSujoVector[ii] = Integer.valueOf(allSujoVector[ii]);
            intWeightParam[ii] = Integer.valueOf(weightParam[ii]);
        }

        double fxValue = (double)getNaiseki(intWeightParam, intSujoVector)
                            + gaParameter;
        
        StudyModelDto studyModelDto = new StudyModelDto();
        if (fxValue >= 0) {
            studyModelDto.setFxValue(fxValue);
            studyModelDto.setHanteiJoho(SEIKAI);
        } else { // fx が負の場合
           studyModelDto.setFxValue(fxValue);
           studyModelDto.setHanteiJoho(FUSEIKAI);
        }
        
        return studyModelDto;
    }
    
    
    /**
     * 振り分け結果を出力する。
     * @param sujoVector
     * @param weightValueMap
     * @param gaResultArray
     * @param rsltSentence
     * @param newFileStream
     * @throws IOException
     */
    public static void outFuriwakeResult(String[] sujoVector
            , LinkedHashMap<String,String[]> weightValueMap, String[] gaResultArray
            , String rsltSentence
            , List<AnsModelDto> ansModelList, String htmlPath, String queType) throws IOException {
        // 振り分け結果を出力
        for (String key : weightValueMap.keySet()) {
            // タイトルラベルは読み飛ばす
            if ("質問分類".equals(weightValueMap.get(key)[0]) 
                    || !queType.equals(weightValueMap.get(key)[0])) {
                continue;
            }
            
             //配列を作りなおし
           String[] weightValueMapArray = new String[weightValueMap.get(key).length -1];
           for(int ii = 0; ii < weightValueMap.get(key).length -1; ii++) {
               weightValueMapArray[ii] = weightValueMap.get(key)[ii + 1];
           }
           
           StudyModelDto studyModelDto = isFuriwake(sujoVector, weightValueMapArray
                                           , Double.parseDouble(gaResultArray[3]));
           
           if (SEIKAI.equals(studyModelDto.getHanteiJoho()) 
                   && studyModelDto.getFxValue() > 5.0) {
               // 回答結果をListに格納
               AnsModelDto ansModelDto = new AnsModelDto();
               ansModelDto.setHanteiJoho(studyModelDto.getHanteiJoho());
               ansModelDto.setAnsBunrui(weightValueMap.get(key)[0]);
               ansModelDto.setFxValue(studyModelDto.getFxValue());
               ansModelDto.setAnsSentence(rsltSentence);
               ansModelDto.setHtmlPath(htmlPath);
               ansModelList.add(ansModelDto);
           }
       }
    }

    /**
     * 内積を計算する
     * @param vectorX1
     * @param vectorX2
     * @return 内積
     */
    public static int getNaiseki(int[] vectorX1, int[] vectorX2) {
        //内積
        int naisekiValue = 0;
        for(int ii = 0; ii < vectorX1.length; ii++) {
            naisekiValue = naisekiValue + vectorX1[ii] * vectorX2[ii];
        }

        return naisekiValue;
    }
    
}
