package evalutePack;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

import interfaceEva.BaseEvaVal;
import util.ReadFileUtil;


/**
 * @author Administrator
 *
 */
public class SvmEvaHapi1 implements BaseEvaVal{

    public static final String SEIKAI = "T";
    public static final String FUSEIKAI = "F";
    
    /* (非 Javadoc)
     * @see interfaceEva.BaseEvaVal#execute(double)
     */
    public double execute(double[] gaParameter){
        
        // Projectのトップディレクトリパス取得
        String folderName = System.getProperty("user.dir");
        // トップディレクトリパス以降を設定
        folderName = folderName + "\\src\\main\\java\\evalutePack\\";
        
        //学習データの読み込み
        String csvFileInput = folderName + "testHap1.csv";
        LinkedHashMap<String,String[]> studyMap = null;
        try {
            studyMap = ReadFileUtil.readCsvFile(csvFileInput);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初期化
        int vectorSu = studyMap.get("データNo").length - 2; //ベクトル数
        int[] vectorX1 = new int[vectorSu];
        int[] weightParam = new int[vectorSu];
        for(int i = 0; i < vectorSu; i++) { //ベクトルの数
            weightParam[i] = 1;
        }
        
        double evaValue = 0;
        
        for (String key : studyMap.keySet()) {
            if (!"データNo".equals(studyMap.get(key)[0])) {
                //マップの配列を入れ替え
                for(int ii = 0; ii < vectorSu; ii++) {
                    vectorX1[ii] = Integer.valueOf(studyMap.get(key)[ii + 2]);
                }

                double fxValue = (double)getNaiseki(weightParam, vectorX1)
                        + gaParameter[0];

                if (fxValue >= 0) {
                    System.out.println("正解No" + studyMap.get(key)[0] + "fx " + fxValue);
                    //間違えた場合は加える
                    if (FUSEIKAI.equals(studyMap.get(key)[1])) {
                        for(int ii = 0; ii < vectorSu; ii++) { //固体の数
                            weightParam[ii] = weightParam[ii] + ((-1) * Integer.valueOf(studyMap.get(key)[ii + 2]));
                        }
                    } else {
                        evaValue = evaValue + 1;
                    }
                } else { // fx が負の場合
                    System.out.println("不正解No" + studyMap.get(key)[0] + "fx " + fxValue);
                    //間違えた場合は加える
                    if (SEIKAI.equals(studyMap.get(key)[1])) {
                        for(int ii = 0; ii < vectorSu; ii++) { //固体の数
                            weightParam[ii] = weightParam[ii] + ((+1) * Integer.valueOf(studyMap.get(key)[ii + 2]));
                        }
                    } else {
                        evaValue = evaValue + 1;
                    }
                }
            }

        }
        // 重み係数をCSVに出力する。
        outPutWeightValue(folderName, weightParam);

        return evaValue;
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

    /**
     * 絶対値を計算する
     * @param vectorX
     * @return
     */
    private double getZetaichi(int[] vectorX) {
        //絶対値計算
        double sowaValue = 0;
        for(int ii = 0; ii < vectorX.length; ii++) {
            sowaValue = sowaValue + Math.pow((double)vectorX[ii], 2);
        }

        return Math.sqrt(sowaValue);
    }
    
    private void outPutWeightValue(String folderName, int[] weightParam) {
        
        //重み係数をCSVへ書き込み
        String fnameWeight ="hap1outWeightValue";
        StringBuilder weightParamOut = new StringBuilder();
        for (int weightParamString : weightParam) {
            weightParamOut.append(Integer.toString(weightParamString) +",");
        }
        try {
            FileOutputStream fosWeight = new FileOutputStream( folderName + fnameWeight + ".csv");
            OutputStreamWriter oswWeight = new OutputStreamWriter(fosWeight , "UTF-8");
            BufferedWriter bwWeight = new BufferedWriter(oswWeight);
            bwWeight.write("重み係数," + new String(weightParamOut));
            //ファイルクローズ
            bwWeight.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
