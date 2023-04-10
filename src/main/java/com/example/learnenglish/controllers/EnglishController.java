package com.example.learnenglish.controllers;

import com.example.learnenglish.service.RandomTranslationPairService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class EnglishController {
private final RandomTranslationPairService randomTranslationPairService;

    public EnglishController(RandomTranslationPairService randomTranslationPairService) {
        this.randomTranslationPairService = randomTranslationPairService;
    }


    @GetMapping("/english")
    public String greeting(Model model) {
        model.addAttribute("title", "English");
        return "english";
    }

    @GetMapping(path = "/englishJSON")
    public void response2(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.println(new ObjectMapper().writeValueAsString(randomTranslationPairService.translationPairRandom()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            printWriter.close();
        }
    }


//    @GetMapping(value ="/eng", params = "name")
//    @ResponseStatus(HttpStatus.CONFLICT)
//    @ResponseBody
//    public String response2(@RequestParam(value = "name", required = false)String name){
//        String jsonString = "";
//        English english = new English();
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            jsonString = mapper.writeValueAsString(english);
//            System.out.println(jsonString);
//            return jsonString;
//        } catch (Exception e) {
//            jsonString = "Json Error";
//        }
//        return jsonString;
//    }
//    @GetMapping(value ="/english2", params = "name")
//    @ResponseStatus(HttpStatus.CONFLICT)
//    @ResponseBody
//    public void response2(HttpServletRequest request,HttpServletResponse response) throws IOException {
//        PrintWriter printWriter = response.getWriter();
//
//        String jsonString = "";
//        English english = new English();
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            jsonString = mapper.writeValueAsString(english);
//            System.out.println(jsonString);
////            return jsonString;
//        } catch (Exception e) {
//            jsonString = "Json Error";
//        }
//        printWriter.print(jsonString);
//        printWriter.close();
////        return "english";
//    }

    //    @RequestMapping(path = "/englishGSON", method = RequestMethod.GET)
//    public String resp(@RequestParam(value = "name", required = false) String name) {
////        PrintWriter printWriter = response.getWriter();
////        response.setContentType("text/html");
////        String jsonString = "";
////        English english = new English();
////        ObjectMapper mapper = new ObjectMapper();
////        try {
////            jsonString = mapper.writeValueAsString(english);
//////            System.out.println(jsonString);
//////            return jsonString;
////        } catch (Exception e) {
////            jsonString = "Json Error";
////        }
////        printWriter.println(jsonString);
////        System.out.println(jsonString);
////        printWriter.close();
//        return "english222";
//    }


}
