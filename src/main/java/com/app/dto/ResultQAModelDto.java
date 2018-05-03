package com.app.dto;

import java.io.Serializable;
import java.util.List;

public class ResultQAModelDto implements Serializable {

	public ResultQAModelDto() {
	}

	/** 質問タイプ結果リスト */
	private List<AnsModelDto> resultQueTypeList;

	/** 回答結果リスト */
	private List<AnsModelDto> resultAnsList;

	public void setResultQueTypeList(List<AnsModelDto> resultQueTypeList){
        this.resultQueTypeList = resultQueTypeList;
    }

	public void setResultAnsList(List<AnsModelDto> resultAnsList){
        this.resultAnsList = resultAnsList;
    }

	public List<AnsModelDto> getResultQueTypeList() {
		return this.resultQueTypeList;
	}

    public List<AnsModelDto> getResultAnsList() {
        return this.resultAnsList;
    }

}
