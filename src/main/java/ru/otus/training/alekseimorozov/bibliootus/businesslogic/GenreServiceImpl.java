package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
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
        return checkAndReturnGenreIfExists(genreId);
    }

    @Override
    public void update(Long genreId, String name) {
        Genre genre = checkAndReturnGenreIfExists(genreId);
        genre.setName(name);
        genreDao.save(genre);
    }

    @Override
    public void delete(Long genreId) {
        try {
            genreDao.deleteById(genreId);
        } catch (DataIntegrityViolationException e) {
            throw new BiblioServiceException("GENRE WASN'T REMOVED DUE TO SOME BOOKS HAVE LINK TO THIS GENRE\n" +
                    "REMOVE THIS GENRE FROM THE BOOKS FIRST", e);
        } catch (EmptyResultDataAccessException e) {
            throw new BiblioServiceException(String.format("Genre with id %d do not found", genreId), e);
        }
    }

    private Genre checkAndReturnGenreIfExists(Long genreId) {
        return genreDao.findById(genreId).orElseThrow(() ->
                new BiblioServiceException(String.format("Genre with id: %d not exists", genreId)));
    }
}