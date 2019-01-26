package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.dao.AuthorDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private AuthorDao authorDao;

    @Autowired
    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public Author create(String name) {
        Author author = new Author();
        author.setFullName(name);
        authorDao.save(author);
        return author;
    }

    @Override
    public List<Author> readAll() {
        return (List<Author>) authorDao.findAll();
    }

    @Override
    public Author findById(Long id) {
        return checkAndReturnAuthorIfExists(id);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorDao.findByFullNameContainingIgnoreCase(name);
    }

    @Override
    public List<Author> findByBook(Long bookId) {
        return authorDao.findByBookId(bookId);
    }

    @Override
    public void update(Long id, String name) {
        Author author = checkAndReturnAuthorIfExists(id);
        author.setFullName(name);
        authorDao.save(author);
    }

    @Override
    public void delete(Long authorId) {
        try {
            authorDao.deleteById(authorId);
        } catch (DataIntegrityViolationException e) {
            throw new BiblioServiceException("AUTHOR WASN'T REMOVED DUE TO SOME BOOKS HAVE LINK TO THIS AUTHOR\n" +
                    "REMOVE THIS AUTHOR FROM THE BOOKS FIRST", e);
        } catch (EmptyResultDataAccessException e) {
            throw new BiblioServiceException(String.format("Author with id %d do not found", authorId), e);
        }
    }

    private Author checkAndReturnAuthorIfExists(Long id) {
        return authorDao.findById(id)
                .orElseThrow(() -> new BiblioServiceException(String.format("Author with id %d do not found", id)));
    }
}