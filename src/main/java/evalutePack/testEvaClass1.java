package evalutePack;


import rcgaBatch1.dto.InitDto;

import interfaceEva.BaseEvaVal;


/**
 * @author Administrator
 *
 */
public class testEvaClass1 implements BaseEvaVal {

    @Override
    public void setNN() {
        InitDto.setNN(100);
    }

    @Override
    public void setCalSedai() {
        InitDto.setCalSedai(7777);
    }

    @Override
    public void setRangeMin(){
        InitDto.setRangeMinAndLen(new double[]{-5, -2}); // 各変数の最小値
    }

    @Override
    public void setRangeMax() {
        InitDto.setRangeMax(new double[]{10, 2}); // 各変数の最大値
    }

    /* (非 Javadoc)
     * @see interfaceEva.BaseEvaVal#execute(double)
     */
    @Override
    public double execute(double[] gaParameter){
        double evaValue = (1 - Math.pow(gaParameter[0], 2))
                            + (1 - Math.pow(gaParameter[1], 2)); // [0, 0]で適応度が最大値2となる

        return evaValue;
    }
}
