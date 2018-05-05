package com.app.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jose4j.lang.JoseException;

import com.app.dto.PredictDto;
import com.app.util.GetNetInfoUtil;
import com.app.util.token.AccessTokenProvider;
import com.app.util.token.representations.AccessToken;
import com.winkelmeyer.salesforce.einsteinvision.PredictionService;
import com.winkelmeyer.salesforce.einsteinvision.model.PredictionResult;

public class EinsVisonUnit {
    
    private String getToken(){
        // For Heroku users
        Optional<String> accountIdOpt = Optional
            .ofNullable(System.getenv("EINSTEIN_VISION_ACCOUNT_ID"));
        Optional<String> privateKeyContentOpt = Optional
            .ofNullable(System.getenv("EINSTEIN_VISION_PRIVATE_KEY"));

        String email = accountIdOpt.get();
        String privateKey = privateKeyContentOpt.get();
        long durationInSeconds = 60 * 15;
        // Create a AccessToken provider
        AccessTokenProvider tokenProvider = AccessTokenProvider
            .getProvider(email, privateKey, durationInSeconds);

        // Use this if you want the refresh the token automatically
        // Schedule Token refresher to refresh
        // AccessTokenRefresher.schedule(tokenProvider, 60 * 14);
        AccessToken accessToken = tokenProvider.getAccessToken();
        
        return accessToken.getToken();
    }
    
    public List<PredictDto> getPredictList(String htmlUrl) throws Exception {
        
        String tokenEins = getToken();
        PredictionService predictionService = new PredictionService(tokenEins);
        List<String> urlListForImg = GetNetInfoUtil.getUrlListForImg(htmlUrl);
        List<PredictDto> imgInfoList = new ArrayList<PredictDto>();
        if (0 < urlListForImg.size()) {
            for (int i = 0; i < 10; i++) {
                try {
                    PredictionResult resultImg = predictionService.predictUrl(
                            "SceneClassifier", urlListForImg.get(i), "");
                    PredictDto predictDto = new PredictDto();
                    predictDto.setPredictionResult(resultImg);
                    predictDto.setUrlForImg(urlListForImg.get(i));
                    imgInfoList.add(predictDto);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }
        
        return imgInfoList;
    }
    
    public static List<String> countWord(List<PredictDto> predictDtoList){
        Map<String, Integer> map = new HashMap<>();
        for (PredictDto predictDto : predictDtoList) {
            String word = predictDto.getPredictionResult().getProbabilities().get(0).getLabel();
            if (!word.isEmpty()) {
                if (map.containsKey(word)) {
                    int count = map.get(word) + 1;
                    map.put(word, count);
                } else {
                    map.put(word, 1);
                }
            }
        }
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            list.add(key);
        }
        Collections.sort(list, (o1, o2) -> {
            return - map.get(o1) + map.get(o2);
        });
 
        System.out.println("出現回数");
        String format = "%-" + 10 + "s: %3d";
        for (String word : list) {
            int count = map.get(word);
            if (1 <= count) {
                System.out.printf(format, word, count);
                System.out.println();
            }
        }
        
        return list;
    }
    
    public static List<PredictDto> getSelectedImgList(List<PredictDto> imgInfoList) {
        
        List<String> selectedLabel = countWord(imgInfoList);
        List<PredictDto> selectedPredictList = new ArrayList<PredictDto>();
        for (PredictDto imgInfo : imgInfoList) {
            String label = imgInfo.getPredictionResult().getProbabilities().get(0).getLabel();
            float probability = imgInfo.getPredictionResult().getProbabilities().get(0).getProbability();
            String url = imgInfo.getUrlForImg();
            if (selectedLabel.contains(label)) {
                selectedPredictList.add(imgInfo);
                System.out.println(label + " : " + probability + " : " + url);
            }
        }
        return selectedPredictList;
    }
    
}
