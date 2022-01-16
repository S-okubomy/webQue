package interfaceEva;


/**
 * 評価式のクラスに実装するインターフェースです。
 * 評価式のクラスに実装しないとエラーとなります。
 * @author Administrator
 * @param double[] gaParamet 評価式の引数（使用インデックスは任意）
 *
 */
public interface BaseEvaVal {
    
    void setNN(); // 個体数のセット
    void setCalSedai(); // 計算世代数
    void setRangeMin();  // 各変数の最小値
    void setRangeMax();  // 各変数の最大値

    double execute(double[] gaParamet); // 実行クラス
    
}
