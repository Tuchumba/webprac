package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.models.Copy;
import ru.msu.cmc.webprac.models.Film;

import java.util.List;

public interface CopyDao extends CommonDao<Copy, Integer> {

    List<Copy> findCopy(Film film, Copy.CopyType type);
    List<Copy> getAllCopyByRentPrice(Long from, Long to);
}