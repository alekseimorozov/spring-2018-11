package ru.otus.training.alekseimorozov.quiz.dao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.quiz.entity.Answer;
import ru.otus.training.alekseimorozov.quiz.entity.Question;
import ru.otus.training.alekseimorozov.quiz.entity.QuizException;

import java.io.*;
import java.util.*;

@Repository
public class QuestionDaoImpl implements QuestionDao {
    @Override
    public List<Question> findAll(String fileName) {
        InputStream questionsStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (questionsStream == null) {
            throwQuizException("Quiz file " + fileName + " is not found.", new FileNotFoundException());
        }
        Reader questionReader = new InputStreamReader(questionsStream);
        List<Question> questions = new ArrayList<>();
        try {
            CSVParser parser = CSVFormat.DEFAULT.parse(questionReader);
            for (CSVRecord record : parser) {
                Question question = new Question();
                question.setId(Integer.parseInt(record.get(0)));
                question.setContent(record.get(1));
                List<Answer> answers = new ArrayList<>();
                answers.add(new Answer(record.get(2), record.get(3)));
                answers.add(new Answer(record.get(4), record.get(5)));
                answers.add(new Answer(record.get(6), record.get(7)));
                question.setAnswers(answers);
                question.setCorrectAnswerId(record.get(8));
                questions.add(question);
            }
        } catch (IOException e) {
            throwQuizException("IO exception during quiz file parsing", e);
        } catch (NumberFormatException e) {
            throwQuizException("wrong question id (mast be integer number)", e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throwQuizException("wrong format of source file: " + fileName, e);
        }
        return questions;
    }

    private void throwQuizException(String message, Throwable cause) {
        throw new QuizException(message, cause);
    }
}