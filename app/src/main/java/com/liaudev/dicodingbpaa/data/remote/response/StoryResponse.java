package com.liaudev.dicodingbpaa.data.remote.response;

import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;

import java.util.List;

/**
 * Created by Budiliauw87 on 2022-06-02.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class StoryResponse extends BaseResponse {

    private List<StoryEntity> listStory;

    public StoryResponse() { }

    public List<StoryEntity> getListStory() {
        return listStory;
    }

    public void setListStory(List<StoryEntity> listStory) {
        this.listStory = listStory;
    }
}
