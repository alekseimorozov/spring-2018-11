package ru.otus.training.alekseimorozov.otuschef.chefservice;

import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.otuschef.domen.Meal;
import ru.otus.training.alekseimorozov.otuschef.domen.Product;

@Service
public class ChefServiceImpl implements ChefService {
    @Override
    public Meal cook(Product product) {
        String result = null;
        switch (product.getName()) {
            case "meat":
                result = "stake";
                break;
            case "eggs":
                result = "omelet";
                break;
            case "fish":
                result = "fried fish";
                break;
            case "tomato":
                result = "ketchup";
                break;
            case "potato":
                result = "french fries";
                break;
            default:
                result = "chef's surprise";
        }
        return new Meal(result);
    }
}