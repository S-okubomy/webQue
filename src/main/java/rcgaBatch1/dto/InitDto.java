package rcgaBatch1.dto;

import java.io.Serializable;


/**
 * 初期遺伝子をつくる際に使用する
 * Javabeans
 * @author t_okubomy
 */
public class InitDto implements Serializable {
    
    // 変数の最小値
    public static double[] minA;
    // 変数の最大値
    public static double[] maxA;
	//染色体の数
	public static int nn;
	//個体数
	public static int NN;
	//計算世代数
	public static int calSedai; 

	public static final int calKabuNo = 50;                 //指定の株コードまで調整すること
	public static final int buyDay = 30;                    //売買期間 日                    
	public static final String keisanKabuCode = "3250";    //計算世代数
	// public static final int nn = minA.length;               //染色体の数
	

	/** x1[][]:染色体 */
	private static double[][] x1;  //共通変数にする

	/** ba1[][]:染色体のバックアップ */
	private static double[][] ba1; //共通変数にする

	/** ac[][]:目的関数の係数 */
	private static double[][] ac1; //共通変数にする

	/** 重み係数 ω(プロジェクト固有） **/
	private static int[] weightParam;  //共通変数にする

	public InitDto() {
	}

    public static void setRangeMinAndLen(double[] minA) {
		InitDto.nn = minA.length;
        InitDto.minA = minA;
    }

    public static double[] getRangeMin() {
        return InitDto.minA;
    }

    public static void setRangeMax(double[] maxA) {
        InitDto.maxA = maxA;
    }

    public static double[] getRangeMax() {
        return InitDto.maxA;
    }

    public static void setNN(int NN) {
        InitDto.NN = NN;
    }

	public static void setCalSedai(int calSedai) {
        InitDto.calSedai = calSedai;
    }

	public void setX1(double[][] x1){
		InitDto.x1 = x1;
    }

	public void setBa1(double[][] ba1){
		InitDto.ba1 = ba1;
    }

	public void setAc1(double[][] ac1){
		InitDto.ac1 = ac1;
    }

	public void setWeightParam(int[] weightParam){
        this.weightParam = weightParam;
    }


	/** 染色体を返すゲッター　*/
	public double[][] getX1() {
		return InitDto.x1;
	}

	/** 染色体のバックアップを返すゲッター　*/
	public double[][] getBa1() {
		return InitDto.ba1;
	}

	/** 目的関数の係数を返すゲッター　*/
	public double[][] getAc1() {
		return InitDto.ac1;
	}

	public int[] getWeightParam() {
		return this.weightParam;
	}




}
