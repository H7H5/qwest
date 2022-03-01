package qwestgroup.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import qwestgroup.model.Purchase;
import qwestgroup.service.Attack2;
import qwestgroup.service.Services;

import java.util.List;
import java.util.Optional;


@ComponentScan(basePackages = "qwestgroup")
@PropertySource(value = "classpath:db.properties")
@Controller
public class ClassifierController {
    private Environment environment;
    private Services service;
    Attack2 atack = new Attack2();

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    @Autowired
    public void setService(Services service) {
        this.service = service;
    }





    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView Vocabulary() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @PostMapping("/select")
    public String blogPostAdd(@RequestParam String title,
                              Model model) throws Exception {
        atack.main1(title);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("select");
        //modelAndView.addObject("text",text);
        return "select";
    }


    @RequestMapping(value = "/showDB", method = RequestMethod.GET)
    public ModelAndView allPurchareBack() {
        List<Purchase> purchases = service.allPurchareBySection();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("purchares");
        modelAndView.addObject("purchareslist", purchases);
        return modelAndView;
    }
    @RequestMapping(value = "/loadParts", method = RequestMethod.GET)
    public ModelAndView allPurchare() {
        service.parseJSON(environment.getRequiredProperty("dataSourceD"));
        List<Purchase> purchases = service.allPurchareBySection();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("purchares");
        modelAndView.addObject("purchareslist", purchases);
        return modelAndView;
    }
    @RequestMapping(value = "/group/{code}", method = RequestMethod.GET)
    public ModelAndView code(@PathVariable("code") String code) {
        Optional<Purchase> currentPurchare = service.GetPurchareBySelection(code);
        List<Purchase> purchases = service.PurchasesByGroup(code);
        return CreateModel("group",currentPurchare, purchases);
    }
    @RequestMapping(value = "/class/{code}", method = RequestMethod.GET)
    public ModelAndView group(@PathVariable("code") String group) {
        Optional<Purchase> currentPurchare = service.GetPurchaseByGroup(group);
        //Purchase purchare = currentPurchare.get().getGroup();
        List<Purchase> purchases = service.PurchasesByClass(group);
        return CreateModel("clas",currentPurchare, purchases);
    }
    @RequestMapping(value = "/category/{code}", method = RequestMethod.GET)
    public ModelAndView category(@PathVariable("code") String group) {
        Optional<Purchase> currentPurchare = service.GetPurchaseByClass(group);
        List<Purchase> purchases = service.PurcharesByCategory(group);
        return CreateModel("category",currentPurchare, purchases);
    }
    private ModelAndView CreateModel(String namePage, Optional<Purchase> currentPurchare, List<Purchase> purchases){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(namePage);
        modelAndView.addObject("currentPurchare", currentPurchare);
        modelAndView.addObject("purchareslist", purchases);
        return modelAndView;
    }
}

