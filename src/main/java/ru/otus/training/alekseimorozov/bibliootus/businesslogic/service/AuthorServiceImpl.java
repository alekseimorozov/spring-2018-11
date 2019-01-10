package ru.otus.training.alekseimorozov.bibliootus.businesslogic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        author.setName(name);
        authorDao.create(author);
        return author;
    }

    @Override
    public List<Author> readAll() {
        return authorDao.readAll();
    }

    @Override
    public Author findById(Long id) {
        return authorDao.findById(id);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorDao.findByName(name);
    }

    @Override
    public List<Author> findByBook(Long bookId) {
        return authorDao.findByBookId(bookId);
    }

    @Override
    public void update(Long id, String name) {
        authorDao.update(id, name);
    }

    @Override
    public void delete(Long authorId) {
        authorDao.delete(authorId);
    }
}