package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.models.Copy;

import java.util.List;

public interface CopyDao extends CommonDao<Copy, Long> {

    List<Copy> findCopy(Integer filmId, String type, String status, Integer price);
    List<Copy> getAllCopyByRentPrice(Long from, Long to);
}