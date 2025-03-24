package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.models.Rent;

import java.sql.Timestamp;
import java.util.List;

public interface RentDao extends CommonDao<Rent, Long> {

    List<Rent> getAllRentByPeriod(Timestamp from, Timestamp to);
}