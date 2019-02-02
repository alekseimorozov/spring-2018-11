package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.GenreDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private GenreDao genreDao;
    private BookDao bookDao;

    @Autowired
    public GenreServiceImpl(GenreDao genreDao, BookDao bookDao) {
        this.genreDao = genreDao;
        this.bookDao = bookDao;
    }

    @Override
    public Genre create(String genreName) {
        return genreDao.save(new Genre(genreName));
    }

    @Override
    public List<Genre> readAll() {
        return genreDao.findAll();
    }

    @Override
    public Genre readById(String genreId) {
        return checkAndReturnGenreIfExists(genreId);
    }

    @Override
    public void update(String genreId, String name) {
        Genre genre = checkAndReturnGenreIfExists(genreId);
        genre.setName(name);
        genreDao.save(genre);
    }

    @Override
    public void delete(String genreId) throws BiblioServiceException {
        Genre genre = checkAndReturnGenreIfExists(genreId);
        if (bookDao.countBookByGenre(genre) > 0) {
            throw new BiblioServiceException("GENRE WASN'T REMOVED DUE TO SOME BOOKS HAVE LINK TO THIS GENRE\n" +
                    "REMOVE THIS GENRE FROM THE BOOKS FIRST");
        }
        genreDao.deleteById(genreId);
    }

    private Genre checkAndReturnGenreIfExists(String genreId) {
        return genreDao.findById(genreId).orElseThrow(() ->
                new BiblioServiceException(String.format("Genre with id: %s not exists", genreId)));
    }
}