package command;

import database.DBManager;
import entities.Category;
import entities.Dish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.CategoryService;
import service.DishService;
import service.impl.CategoryServiceImpl;
import service.impl.DishServiceImpl;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * creates a new dish.
 * available to manager only.
 */
public class AddDishCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddDishCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing add dish command-----");
        CategoryService categoryService = new CategoryServiceImpl(DBManager.getInstance());
        DishService dishService = new DishServiceImpl(DBManager.getInstance());
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceDouble = request.getParameter("price");
        String priceInt = priceDouble.replace(".", "");
        int price = Integer.parseInt(priceInt);
        int categoryId = Integer.parseInt(request.getParameter("category"));
        Category category = categoryService.findById(categoryId).get();
        String imagePath = request.getParameter("imagePath");

        Dish dish = new Dish(name, price, description, imagePath, category);
        dishService.save(dish);
        logger.debug("-----successfully executed add dish command-----");
        return WebPages.DISH_COMMAND + dish.getId();
    }

}
