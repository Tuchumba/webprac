package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.models.Copy;

import java.util.List;

public interface CopyDao extends CommonDao<Copy, Integer> {

    List<Copy> findCopy(Integer filmId, Copy.CopyType type, Copy.RentStatus status, Integer price);
    List<Copy> getAllCopyByRentPrice(Long from, Long to);
}