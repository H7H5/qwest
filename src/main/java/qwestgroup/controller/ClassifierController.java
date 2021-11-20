package qwestgroup.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import qwestgroup.model.Purchare;
import qwestgroup.service.Services;

import java.util.List;


@ComponentScan(basePackages = "qwestgroup")
@PropertySource(value = "classpath:db.properties")
@Controller
public class ClassifierController {
    private Environment environment;
    private Services service;

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
        modelAndView.setViewName("basic");
        return modelAndView;
    }
    @RequestMapping(value = "/showDB", method = RequestMethod.GET)
    public ModelAndView allPurchareBack() {
        List<Purchare> purchares = service.allPurchareBySection();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("purchares");
        modelAndView.addObject("purchareslist", purchares);
        return modelAndView;
    }
    @RequestMapping(value = "/loadParts", method = RequestMethod.GET)
    public ModelAndView allPurchare() {
        service.parseJSON(environment.getRequiredProperty("dataSourceD"));
        List<Purchare> purchares = service.allPurchareBySection();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("purchares");
        modelAndView.addObject("purchareslist", purchares);
        return modelAndView;
    }
    @RequestMapping(value = "/group/{code}", method = RequestMethod.GET)
    public ModelAndView code(@PathVariable("code") String code) {
        Purchare currentPurchare = service.GetPurchareBySelection(code);
        List<Purchare> purchares = service.PurcharesByGroup(code);
        return CreateModel("group",currentPurchare,purchares);
    }
    @RequestMapping(value = "/class/{code}", method = RequestMethod.GET)
    public ModelAndView group(@PathVariable("code") String group) {
        Purchare currentPurchare = service.GetPurchareByGroup(group);
        List<Purchare> purchares = service.PurcharesByClass(group);
        return CreateModel("clas",currentPurchare,purchares);
    }
    @RequestMapping(value = "/category/{code}", method = RequestMethod.GET)
    public ModelAndView category(@PathVariable("code") String group) {
        Purchare currentPurchare = service.GetPurchareByClass(group);
        List<Purchare> purchares = service.PurcharesByCategory(group);
        return CreateModel("category",currentPurchare,purchares);
    }
    private ModelAndView CreateModel(String namePage, Purchare currentPurchare, List<Purchare> purchares){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(namePage);
        modelAndView.addObject("currentPurchare", currentPurchare);
        modelAndView.addObject("purchareslist", purchares);
        return modelAndView;
    }
}

