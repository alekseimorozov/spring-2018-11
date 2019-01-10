package ru.otus.training.alekseimorozov.bibliootus.businesslogic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.bibliootus.dao.GenreDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private GenreDao genreDao;

    @Autowired
    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Genre create(String genreName) {
        return genreDao.create(Genre.getGenre(null, genreName));
    }

    @Override
    public List<Genre> readAll() {
        return genreDao.readAll();
    }

    @Override
    public Genre readById(Long genreId) {
        return genreDao.readById(genreId);
    }

    @Override
    public void update(Long genreId, String name) {
        genreDao.update(genreId, name);
    }

    @Override
    public void delete(Long genreId) {
        genreDao.delete(genreId);
    }
}