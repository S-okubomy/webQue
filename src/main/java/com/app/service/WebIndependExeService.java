package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.app.dto.AnsModelDto;
import com.app.dto.ResultQAModelDto;
import com.app.unit.AnsGetDataUnit;
import com.app.unit.IndependAnsGetDataUnit;


/**
 * 単独の応答解析結果より、ネット上を検索する。
 */
@Component
public class WebIndependExeService {

    /**
     * 上限の件数
     */
    public static final int resultKensuQue = 5;
    public static final int resultKensuAns = 60;
    
    
    public ResultQAModelDto getWebQA(String[] question, String independAnsKey) throws Exception {
        
        long startQAsys = System.currentTimeMillis();
        
        // 最終結果格納用Dto
        ResultQAModelDto resultQAModelDto = new ResultQAModelDto();

        // ================= ネット検索して回答作成  =================
        IndependAnsGetDataUnit ansGetJitsuDate = new IndependAnsGetDataUnit();
        // 回答結果を取得（判定結果の降順に格納）
        List<AnsModelDto> ansList = ansGetJitsuDate.getJitsuDate(question, independAnsKey);
        // 回答結果を上限件数分、格納
        List<AnsModelDto> resultAnsList = getResultAnsList(ansList);
        // 最終結果（回答結果リスト）を格納する。
        resultQAModelDto.setResultAnsList(resultAnsList);

        // ================= 画面に表示 =================
        
        System.out.println("============ 回答並び替え後 ============");
        // 正規表現でフィルター（文章の前後にスペースを含む行を除く    "^\\x01-\\x7E"で1バイト文字以外を探す）
        resultAnsList.stream()
                    .forEach(ansModel -> System.out.println("回答分類: " + ansModel.getAnsBunrui() 
                                + " fx= " + ansModel.getFxValue() 
                                + " 文章: " + ansModel.getAnsSentence()));
        System.out.println("出力完了");
        
        long endQAsys = System.currentTimeMillis();
        long intervalQAsys = endQAsys - startQAsys;
        System.out.println(intervalQAsys + "ミリ秒  QAsys処理時間");
        
        return resultQAModelDto;
    }
    
    /**
     * 回答結果を上限件数分、格納
     * @param resultQueTypeList
     * @param ansList
     * @return
     */
    private List<AnsModelDto> getResultAnsList(List<AnsModelDto> ansList) {
        List<AnsModelDto> resultAnsList = new ArrayList<AnsModelDto>();
        int cntAns = 0;
        for (AnsModelDto ansModel : ansList) {
            resultAnsList.add(ansModel);
            cntAns++;
            // 上限件数 以上 格納したらbreak
            if (resultKensuAns <= cntAns) {
                break;
            }
        }
        return resultAnsList;
    }

}
