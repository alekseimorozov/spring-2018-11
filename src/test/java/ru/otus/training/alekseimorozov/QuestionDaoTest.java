package ru.otus.training.alekseimorozov;

import org.junit.Before;
import org.junit.Test;
import ru.otus.training.alekseimorozov.quize.dao.QuestionDao;
import ru.otus.training.alekseimorozov.quize.dao.QuestionDaoImpl;
import ru.otus.training.alekseimorozov.quize.entity.Answer;
import ru.otus.training.alekseimorozov.quize.entity.Question;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class QuestionDaoTest {
    @Test
    public void findAllTest() throws IOException {
        QuestionDao questionDao = new QuestionDaoImpl("questions.csv");
        List<Question> actualQuestions = questionDao.findAll();
        assertEquals(actualQuestions.size(), 2);
        Question actualOne = actualQuestions.get(0);
        assertEquals(Integer.valueOf(1), actualOne.getId());
        assertEquals("Вопрос первый", actualOne.getContent());
        assertEquals("a", actualOne.getCorrectAnswerId());
        List<Answer> actualAnswersOne = actualOne.getAnswers();
        assertEquals(3, actualAnswersOne.size());
        assertEquals("a", actualAnswersOne.get(0).getId());
        assertEquals("вариант 1", actualAnswersOne.get(0).getContent());
        assertEquals("b", actualAnswersOne.get(1).getId());
        assertEquals("вариант 2", actualAnswersOne.get(1).getContent());
        assertEquals("c", actualAnswersOne.get(2).getId());
        assertEquals("вариант 3", actualAnswersOne.get(2).getContent());
        Question actualTwo = actualQuestions.get(1);
        assertEquals(Integer.valueOf(2), actualTwo.getId());
        assertEquals("Вопрос второй", actualTwo.getContent());
        assertEquals("b", actualTwo.getCorrectAnswerId());
        List<Answer> actualAnswersTwo = actualTwo.getAnswers();
        assertEquals(3, actualAnswersTwo.size());
        assertEquals("a", actualAnswersTwo.get(0).getId());
        assertEquals("ответ 1", actualAnswersTwo.get(0).getContent());
        assertEquals("b", actualAnswersTwo.get(1).getId());
        assertEquals("ответ 2", actualAnswersTwo.get(1).getContent());
        assertEquals("c", actualAnswersTwo.get(2).getId());
        assertEquals("ответ 3", actualAnswersTwo.get(2).getContent());
    }

    @Test(expected = IOException.class)
    public void fileNotFoundTest() throws IOException {
        QuestionDao questionDao = new QuestionDaoImpl("notexist.csv");
        questionDao.findAll();
    }
}
