package ru.msu.cmc.webprac.models;

public interface CommonEntity<ID> {
    ID getId();
    void setId(ID id);
}