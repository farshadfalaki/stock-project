package com.farshad.stock.controller;

import com.farshad.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import static com.farshad.stock.constants.URlConstants.*;

@Controller
@RequestMapping(path = DASHBOARD_CONTROLLER_BASE_PATH)
public class DashboardController {
    @Autowired
    private StockService stockService;

    @RequestMapping(value = DASHBOARD_CONTROLLER_ALL_STOCKS,method = RequestMethod.GET)
    public ModelAndView showAll(Model model) {
        ModelAndView modelAndView = new ModelAndView("stocks");
        model.addAttribute("stocks", stockService.retrieveAll());
        return modelAndView;
    }


}
