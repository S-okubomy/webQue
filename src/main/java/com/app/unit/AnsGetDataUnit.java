package com.app.unit;

import java.util.List;

import com.app.dto.AnsModelDto;
import com.app.util.GetNetInfoUtil;

/**
 * GAの学習結果より、ネットから情報を収集する
 * @author Administrator
 *
 */
public class AnsGetDataUnit {

    
    public List<AnsModelDto> getJitsuDate(String[] args, String queType) throws Exception {
        
        //データ取得先 URL指定
        String reqUrl = "http://www.google.com/search?q=";
        String splitSerchWord = args[0];
        
        String reqUrlAll = reqUrl + splitSerchWord + "&ie=UTF-8&oe=UTF-8&num=20";
        
        List<AnsModelDto> ansModelList = GetNetInfoUtil.getResultAns(args[0], queType
                , reqUrlAll, "Body div p", 5);

        return ansModelList;
    }
    
    public List<AnsModelDto> getJitsuDateForWiki(String[] args, String queType) throws Exception {
    
        //データ取得先 URL指定
        String reqUrl = "http://www.google.com/search?q=";
        String splitSerchWord = args[0];
        
        String reqUrlAll = reqUrl + splitSerchWord + "&ie=UTF-8&oe=UTF-8&num=20";
        
        List<AnsModelDto> ansModelList = GetNetInfoUtil.getResultAns(args[0], queType
                , reqUrlAll, "Body div p", 5);

        return ansModelList;
    }
    

}
