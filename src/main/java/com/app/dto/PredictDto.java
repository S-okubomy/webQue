package com.app.dto;

import java.io.Serializable;

import com.winkelmeyer.salesforce.einsteinvision.model.PredictionResult;

public class PredictDto implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PredictDto() {
	}

	/** 予測 */
	private PredictionResult predictionResult;

	/** url */
	private String urlForImg;

	public void setPredictionResult(PredictionResult predictionResult){
        this.predictionResult = predictionResult;
    }

	public void setUrlForImg(String urlForImg){
        this.urlForImg = urlForImg;
    }

	public PredictionResult getPredictionResult() {
		return this.predictionResult;
	}

	public String getUrlForImg() {
		return this.urlForImg;
	}

}
