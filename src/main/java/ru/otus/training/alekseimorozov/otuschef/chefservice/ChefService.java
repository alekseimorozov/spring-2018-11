package ru.otus.training.alekseimorozov.otuschef.chefservice;

import ru.otus.training.alekseimorozov.otuschef.domen.Meal;
import ru.otus.training.alekseimorozov.otuschef.domen.Product;

public interface ChefService {
    Meal cook(Product product);
}
