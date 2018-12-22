package ru.otus.training.alekseimorozov.quiz.dao;

import org.junit.Test;
import ru.otus.training.alekseimorozov.quiz.entity.Answer;
import ru.otus.training.alekseimorozov.quiz.entity.Question;
import ru.otus.training.alekseimorozov.quiz.entity.QuizException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class QuestionDaoTest {
    private static QuestionDao questionDao = new QuestionDaoImpl();

    @Test
    public void findAllTest() {
        List<Question> actualQuestions = questionDao.findAll("questions.csv");
        assertEquals(2, actualQuestions.size());
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

    @Test(expected = QuizException.class)
    public void fileNotFoundTest() {
        questionDao.findAll("notexist.csv");
    }

    @Test(expected = QuizException.class)
    public void incorrectQuestionIdTest() {
        questionDao.findAll("incorrectQuestionId.csv");
    }

    @Test(expected = QuizException.class)
    public void incorrectQuestionContentTest() {
        questionDao.findAll("incorrectQuestionContent.csv");
    }

    @Test(expected = QuizException.class)
    public void incorrectFileFormatTest() {
        questionDao.findAll("incorrectFileFormat.csv");
    }

    @Test(expected = QuizException.class)
    public void incorrectAnswerIdTest() {
        questionDao.findAll("incorrectAnswerId.csv");
    }

    @Test(expected = QuizException.class)
    public void incorrectAnswerContentTest() {
        questionDao.findAll("incorrectAnswerContent.csv");
    }
}