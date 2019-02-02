package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.dao.AuthorDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private AuthorDao authorDao;
    private BookDao bookDao;

    @Autowired
    public AuthorServiceImpl(AuthorDao authorDao, BookDao bookDao) {
        this.authorDao = authorDao;
        this.bookDao = bookDao;
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
        return authorDao.findAll();
    }

    @Override
    public Author findById(String id) {
        return checkAndReturnAuthorIfExists(id);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorDao.findByFullNameIgnoreCase(name);
    }

    @Override
    public void update(String id, String name) {
        Author author = checkAndReturnAuthorIfExists(id);
        author.setFullName(name);
        authorDao.save(author);
    }

    @Override
    public void delete(String authorId) {
        if (bookDao.countBookByAuthors(checkAndReturnAuthorIfExists(authorId)) > 0) {
            throw new BiblioServiceException("AUTHOR WASN'T REMOVED DUE TO SOME BOOKS HAVE LINK TO THIS AUTHOR\n" +
                    "REMOVE THIS AUTHOR FROM THE BOOKS FIRST");
        }
        authorDao.deleteById(authorId);
    }

    private Author checkAndReturnAuthorIfExists(String id) {
        return authorDao.findById(id)
                .orElseThrow(() -> new BiblioServiceException(String.format("Author with id %s do not found", id)));
    }
}