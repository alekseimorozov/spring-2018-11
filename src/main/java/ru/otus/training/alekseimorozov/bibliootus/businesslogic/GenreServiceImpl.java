package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

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
        return genreDao.save(new Genre(genreName));
    }

    @Override
    public List<Genre> readAll() {
        return (List<Genre>) genreDao.findAll();
    }

    @Override
    public Genre readById(Long genreId) {
        return genreDao.findById(genreId).get();
    }

    @Override
    public void update(Long genreId, String name) {
        Genre genre = genreDao.findById(genreId).get();
        genre.setName(name);
        genreDao.save(genre);
    }

    @Override
    public void delete(Long genreId) {
        genreDao.deleteById(genreId);
    }
}