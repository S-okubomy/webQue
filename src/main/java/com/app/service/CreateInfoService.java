package com.app.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.assertj.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.dto.AnsModelDto;
import com.app.dto.ResultQAModelDto;
import com.app.util.ReadFileUtil;
import com.app.util.SelectWordUtil;

@Component
public class CreateInfoService {

    @Autowired
    WebQAExeService webQAExeService;
    
    /**
     * @param question
     * @param infoFileName ファイルの拡張子込み（例：「unchiku.txt」）
     * @return
     * @throws Exception
     */
    public List<AnsModelDto> getCreateInfo(String[] question, String infoFileName) throws Exception {

        // Projectのトップディレクトリパス取得
        String folderName = System.getProperty("user.dir");
        // トップディレクトリパス以降を設定
        String infoFileFolderName = folderName + "/src/main/resources/infoFile/";
        
        // INFOファイル読み込み
        String infoFilePath = infoFileFolderName + infoFileName;
        LinkedHashMap<String,String[]> infoFileMap = ReadFileUtil.readCsvCom(infoFilePath);
        
        // ファイルの更新日取得
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy'/'MM'/'dd");
        
        // ファイルの更新日が古い場合は再書き込みし、読み込み。
        if (isNeedReWriting(sdf1, infoFileMap)) {
            reWriteInfo(sdf1, question, infoFilePath);
            infoFileMap = ReadFileUtil.readCsvCom(infoFilePath);
        }
        
        List<AnsModelDto> resultAnsList = new ArrayList<AnsModelDto>();
        for (String key : infoFileMap.keySet()) {
            // 更新日以外をセット
            if (!"1".equals(key)) {
                AnsModelDto ans = new AnsModelDto();
                ans.setAnsNo(Integer.toString(Integer.valueOf(key) -1));
                ans.setAnsSentence(infoFileMap.get(key)[0]);
                ans.setHtmlPath(infoFileMap.get(key)[1]);
                resultAnsList.add(ans);
            }
        }

        return resultAnsList;
    }
    
    /**
     * @param question
     * @param infoFileName ファイルの拡張子込み（例：「unchiku.txt」）
     * @return * @throws Exception
     */
    public List<AnsModelDto> getPyCreateInfo(String[] question, String infoFileName, String pyFileName) throws Exception {

        // Projectのトップディレクトリパス取得
        String folderName = System.getProperty("user.dir");
        // トップディレクトリパス以降を設定
        String infoFileFolderName = folderName + "/src/main/resources/infoFile/";
        // python ファイルのパス
        String pyFolderName = folderName + "/src/main/resources/py1/";
        String pyPath = pyFolderName + pyFileName;
        
        // INFOファイル読み込み
        String infoFilePath = infoFileFolderName + infoFileName;
        LinkedHashMap<String,String[]> infoFileMap = ReadFileUtil.readCsvCom(infoFilePath);
        
        // ファイルの更新日取得
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy'/'MM'/'dd");
        
        // ファイルの更新日が古い場合は再書き込みし、読み込み。
        if (isNeedReWriting(sdf1, infoFileMap)) {
            reWriteInfoByPy(sdf1, question, infoFilePath, pyPath);
            infoFileMap = ReadFileUtil.readCsvCom(infoFilePath);
        }
        
        List<AnsModelDto> resultAnsList = new ArrayList<AnsModelDto>();
        for (String key : infoFileMap.keySet()) {
            // 更新日以外をセット
            if (!"1".equals(key)) {
                AnsModelDto ans = new AnsModelDto();
                ans.setAnsNo(Integer.toString(Integer.valueOf(key) -1));
                ans.setAnsSentence(infoFileMap.get(key)[0]);
                ans.setHtmlPath(infoFileMap.get(key)[1]);
                resultAnsList.add(ans);
            }
        }

        return resultAnsList;
    }
    
    private boolean isNeedReWriting(SimpleDateFormat sdf1, LinkedHashMap<String,String[]> infoFileMap) throws Exception {
        
        int mapSize = infoFileMap.size();
        // マップサイズが0の場合は再書き込み
        if (0 == mapSize) {
            return true;
        }
        
        // 更新日付がNULLの場合は、再書き込みする。
        if (SelectWordUtil.isNullOrEmpty(infoFileMap.get("1")[0])) {
            return true;
        }
        
        // 日本時間に変換
        ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        Date dateJp = Date.from(nowZonedDt.toInstant());
        
        // 実行日取得（日付のみ）
        Date today = DateUtil.truncateTime(dateJp);
        
        // infoファイルより取得した更新日をDate型に変換
        Date updateDate = sdf1.parse(infoFileMap.get("1")[0]);
        
        // 実行日より更新日が古い場合又は要素数が1以下の場合（更新日しか入っていない場合）、再書き込み
        if (updateDate.compareTo(today) < 0 || mapSize <= 1) {
            return true;
        } else {
            return false;
        }
    }
    
    private void reWriteInfo(SimpleDateFormat sdf1, String[] question, String infoFilePath) throws Exception {
        
        ResultQAModelDto resultQADto = webQAExeService.getWebQA(question);
        
        // ファイル書き込み準備
        FileOutputStream fosUse = new FileOutputStream(infoFilePath);
        OutputStreamWriter oswUse = new OutputStreamWriter(fosUse , "UTF-8");
        BufferedWriter bwUse = new BufferedWriter(oswUse);
        
        try{
            // 日本時間に変換
            ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
            Date dateJp = Date.from(nowZonedDt.toInstant());
            // 更新日書き込み
            String todayWrite = sdf1.format(dateJp);
            String writeSentence = todayWrite + "\r\n";
            bwUse.write(writeSentence);
            
            // 回答結果を書き込み
            for (AnsModelDto ans : resultQADto.getResultAnsList()) {
                writeSentence = ans.getAnsSentence() + "," + ans.getHtmlPath() + "\r\n";
                bwUse.write(writeSentence);
            }
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("ファイル書き込み失敗");
        } finally {
            try {
                // ストリームは必ず finally で close
                if (bwUse != null) {
                    bwUse.close();
                    //0.5秒待つ
                    Thread.sleep(500) ;
                }
            } catch (IOException e) {
            }
        }
    }
    
    private void reWriteInfoByPy(SimpleDateFormat sdf1, String[] question, String infoFilePath
            , String pyPath) throws Exception {
        
        // python実行
        ProcessBuilder processBuilder = new ProcessBuilder("python", pyPath);
        Process process = processBuilder.start();
        BufferedReader reader = 
                new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
        
        // Pythonの出力結果を格納
        List<AnsModelDto> ansModelList = new ArrayList<AnsModelDto>();
        String strMoji;
        while ((strMoji = reader.readLine()) != null ) {
            if (!"".equals(strMoji)) {
                System.out.println(strMoji);
                 AnsModelDto ansModelDto = new AnsModelDto();
                 ansModelDto.setAnsSentence(strMoji);
                 ansModelDto.setHtmlPath("http://openweathermap.org/city/1850147");
                 ansModelList.add(ansModelDto);
            }
        }
        
        ResultQAModelDto resultQADto = new ResultQAModelDto();
        resultQADto.setResultAnsList(ansModelList);
        
        // ファイル書き込み準備
        FileOutputStream fosUse = new FileOutputStream(infoFilePath);
        OutputStreamWriter oswUse = new OutputStreamWriter(fosUse , "UTF-8");
        BufferedWriter bwUse = new BufferedWriter(oswUse);
        
        try{
            // 日本時間に変換
            ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
            Date dateJp = Date.from(nowZonedDt.toInstant());
            // 更新日書き込み
            String todayWrite = sdf1.format(dateJp);
            String writeSentence = todayWrite + "\r\n";
            bwUse.write(writeSentence);
            
            // 回答結果を書き込み
            for (AnsModelDto ans : resultQADto.getResultAnsList()) {
                writeSentence = ans.getAnsSentence() + "," + ans.getHtmlPath() + "\r\n";
                bwUse.write(writeSentence);
            }
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("ファイル書き込み失敗");
        } finally {
            try {
                // ストリームは必ず finally で close
                if (bwUse != null) {
                    bwUse.close();
                    //0.5秒待つ
                    Thread.sleep(500) ;
                }
            } catch (IOException e) {
            }
        }
    }

}
