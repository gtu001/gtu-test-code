package com.example.gtu001.qrcodemaker.services;

import android.content.Context;

import com.example.gtu001.qrcodemaker.dao.YoutubeVideoDAO;

import java.util.List;

public class YoutubeVideoService {
    private YoutubeVideoDAO dao;

    public YoutubeVideoService(Context context) {
        this.dao = new YoutubeVideoDAO(context);
    }

    public List<YoutubeVideoDAO.YoutubeVideo> queryAll() {
        return this.dao.queryAll();
    }

    public boolean insertData(YoutubeVideoDAO.YoutubeVideo vo) {
        return this.dao.insert(vo) > 0;
    }

    public YoutubeVideoDAO.YoutubeVideo findByPk(String videoId) {
        return this.dao.findByPk(videoId);
    }

    public boolean deleteId(String videoId) {
        return this.dao.deleteByPk(videoId) > 0;
    }

    public boolean update(YoutubeVideoDAO.YoutubeVideo vo) {
        return this.dao.updateByVO(vo) > 0;
    }
}
