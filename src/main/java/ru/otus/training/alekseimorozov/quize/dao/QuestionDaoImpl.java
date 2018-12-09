package ru.otus.training.alekseimorozov.quize.dao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.otus.training.alekseimorozov.quize.entity.Answer;
import ru.otus.training.alekseimorozov.quize.entity.Question;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class QuestionDaoImpl implements QuestionDao {
    private String fileName;

    public QuestionDaoImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Question> findAll() throws IOException {
        InputStream questionsStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (questionsStream == null) {
            throw new IOException("File " + fileName + " is not found.");
        }
        Reader questionReader = new InputStreamReader(questionsStream);
        CSVParser parser = CSVFormat.DEFAULT.parse(questionReader);
        List<Question> questions = new ArrayList<>();
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
        return questions;
    }
}