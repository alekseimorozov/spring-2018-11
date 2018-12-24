package ru.otus.training.alekseimorozov.quiz.configuration;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class QuizShellPromptProvider implements PromptProvider {
    @Override
    public AttributedString getPrompt() {
        return new AttributedString("quiz:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
    }
}