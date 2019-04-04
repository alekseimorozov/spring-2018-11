package ru.otus.training.alekseimorozov.todolist.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.todolist.repo.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository repo;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return repo.findByUsername(s)
                .blockOptional()
                .orElseThrow(() -> new UsernameNotFoundException("Username or password is not correct"));
    }
}