package evalutePack;


import interfaceEva.BaseEvaVal;


/**
 * @author Administrator
 *
 */
public class testEvaClass1 implements BaseEvaVal{

    
    /* (非 Javadoc)
     * @see interfaceEva.BaseEvaVal#execute(double)
     */
    public double execute(double[] gaParameter){
        
        double evaValue = (1 - Math.pow(gaParameter[0], 2))
                            + (1 - Math.pow(gaParameter[1], 2)); // [0, 0]で適応度が最大値となる

        return evaValue;
    }
    
    
}
