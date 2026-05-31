package service;

import database.BDAnime;
import model.Anime;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnimeService {

    public void addToFavorites(int userId, int animeId) throws SQLException {
        BDAnime.manageFavorite(userId, animeId, true);
    }

    public void removeFromFavorites(int userId, int animeId) throws SQLException {
        BDAnime.manageFavorite(userId, animeId, false);
    }

    public void markAsWatched(int userId, int animeId) throws SQLException {
        BDAnime.markAsWatched(userId, animeId);
    }

    public ArrayList<Anime> getAllAnime() throws SQLException {
        return BDAnime.getAllAnime();
    }

    public Anime getAnimeById(int animeId) throws SQLException {
        return BDAnime.getAnimeById(animeId);
    }

    public ArrayList<Anime> getUserFavorites(int userId) throws SQLException {
        return BDAnime.getFavorites(userId);
    }

    public ArrayList<Anime> getRecommendationsForUser(int userId) throws SQLException {
        return BDAnime.getRecommendations(userId);
    }

    public void initDatabase() throws ClassNotFoundException, SQLException, IOException, InterruptedException {
        BDAnime.connectionDB();
        BDAnime.initSchema();

        if (BDAnime.isDatabaseEmpty()) {
            System.out.println("База данных пуста, загружаю данные...");
            BDAnime.seedData();
            BDAnime.syncWithAPI();
        } else {
            System.out.println("Данные уже есть в базе");
        }
    }
}