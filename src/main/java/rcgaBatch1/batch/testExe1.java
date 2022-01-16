package rcgaBatch1.batch;

import rcgaBatch1.dto.InitDto;
import rcgaBatch1.dto.ResultGeneDto;

import static util.FmtUtil.dblToStr;

import evalutePack.SvmEvaClass;
import evalutePack.prod1Eva1;
import evalutePack.testEvaClass1;

public class testExe1 {

    public static void main(String[] args) {

        MainRcga mainRcga = new MainRcga();
    //    ResultGeneDto result = mainRcga.calGene("evalutePack.testEvaClass1", new testEvaClass1()); // 評価クラス変える場合はInitDtoのminAとminBの配列数を数式の変数の数にする。
        // ResultGeneDto result = mainRcga.calGene("evalutePack.SvmEvaClass", new SvmEvaClass());
        ResultGeneDto result = mainRcga.calGene("evalutePack.prod1Eva1", new prod1Eva1());

        System.out.println("---------------------最適化結果--------------------");
        String strPrMaxAll = "実値: " + dblToStr(result.getTrueVal1()) +  "  係数： ";
        for (int i = 0; i < result.getAc1().length; i++) {
            strPrMaxAll = strPrMaxAll + dblToStr(result.getAc1()[i])  + "  ";
        }
        System.out.println(strPrMaxAll);
        
        System.out.println("完了!!!!");
    }

}
