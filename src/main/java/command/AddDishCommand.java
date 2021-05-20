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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * creates a new dish.
 * available to manager only.
 */
public class AddDishCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddDishCommand.class);

    // price only can be entered by user like '800.00' (group 1) or '800' (group 2);
    private static final String PRICE_REGEX = "([0-9]+\\.[0-9]{2})|([0-9]+)";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing add dish command-----");
        CategoryService categoryService = new CategoryServiceImpl(DBManager.getInstance());
        DishService dishService = new DishServiceImpl(DBManager.getInstance());
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceString = request.getParameter("price");
        String imagePath = request.getParameter("imagePath");
        int categoryId = Integer.parseInt(request.getParameter("category"));
        Category category = categoryService.findById(categoryId).get();
        int price = 0;

        if (!isValid(name, priceString, imagePath, request)) {
            request.setAttribute("categories", categoryService.findAll());
            return WebPages.ADD_DISH_PAGE;
        }

        Matcher m = Pattern.compile(PRICE_REGEX).matcher(priceString);
        if (m.find()) {
            if (m.group(1) != null) { // <-- price is entered as number with floating point
                String priceInt = priceString.replace(".", "");
                price = Integer.parseInt(priceInt);
            } else { // <-- price is entered as whole number
                // since all prices are stored in database in coins, given price
                // must be multiplied by 100
                price = 100 * Integer.parseInt(priceString);
            }
        }

        Dish dish = new Dish(name, price, description, imagePath, category);
        dishService.save(dish);
        logger.debug("-----successfully executed add dish command-----");
        return WebPages.DISH_COMMAND + dish.getId();
    }

    private boolean isValid(String name, String priceDouble, String imagePath, HttpServletRequest request) {
        boolean hasErrors = false;
        if (name == null || name.isEmpty()) {
            hasErrors = true;
            request.setAttribute("nameMsg", "name can't be empty");
        }
        if (!Pattern.compile(PRICE_REGEX).matcher(priceDouble).find()) {
            hasErrors = true;
            request.setAttribute("priceMsg", "price is incorrect");
        }
        if (imagePath == null || imagePath.isEmpty()) {
            hasErrors = true;
            request.setAttribute("imageMsg", "image url can't be empty");
        }
        return !hasErrors;
    }

}
