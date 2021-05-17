package command;

import database.DBManager;
import entities.Category;
import entities.Dish;
import service.CategoryService;
import service.DishService;
import service.impl.CategoryServiceImpl;
import service.impl.DishServiceImpl;
import util.WebPages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.UUID;

public class AddDishCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryService categoryService = new CategoryServiceImpl(DBManager.getInstance());
        DishService dishService = new DishServiceImpl(DBManager.getInstance());
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceDouble = request.getParameter("price");
        String priceInt = priceDouble.replace(".", "");
        int price = Integer.parseInt(priceInt);
        int categoryId = Integer.parseInt(request.getParameter("category"));
        Part filePart = request.getPart("image");
        String fileName = filePart.getSubmittedFileName() + UUID.randomUUID();
        for (Part part : request.getParts()) {
            part.write("/webapp/images/" + fileName);
        }
        Category category = categoryService.findById(categoryId).get();
        Dish dish = new Dish(name, price, description, fileName, category);
        dishService.add(dish);
        return WebPages.DISH_COMMAND + dish.getId();
    }
}
