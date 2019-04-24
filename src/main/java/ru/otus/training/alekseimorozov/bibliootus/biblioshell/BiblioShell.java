package ru.otus.training.alekseimorozov.bibliootus.biblioshell;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class BiblioShell {
    private Job job;
    private JobLauncher launcher;

    public BiblioShell(Job job, JobLauncher launcher) {
        this.job = job;
        this.launcher = launcher;
    }

    @ShellMethod("Start job")
    public void startJob() throws Exception {
        launcher.run(job, new JobParameters());
    }
}