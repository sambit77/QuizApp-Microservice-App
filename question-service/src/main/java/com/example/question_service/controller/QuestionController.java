package com.example.question_service.controller;

import com.example.question_service.model.Question;
import com.example.question_service.model.QuestionWrapper;
import com.example.question_service.model.Response;
import com.example.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//http://localhost:8080/question/allQuestions
//
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    Environment environment;
    @GetMapping("/allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions()
    {
        return questionService.getAllQuestions();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Question>>  getQuestionByCategory(@PathVariable String category)
    {
        return  questionService.getQuestionsByCategory(category);
    }

    @PostMapping("/add")
    public ResponseEntity<String>addQuestion(@RequestBody Question question)
    {
        return questionService.addQuestion(question);
    }

    // generate

    @GetMapping("/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category,
                                                             @RequestParam Integer numQuestions)
    {
        return questionService.getQuestionsForQuiz(category,numQuestions);
    }
    // getQuestions(questionId)
    //http://localhost:8080/question/getQuestions
    @PostMapping("/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds)
    {
        System.out.println( "Port is " + environment.getProperty("local.server.port"));
        return  questionService.getQuestionsFromId(questionIds);
    }
    // getScore
    //http://localhost:8080/question/getScore
    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses)
    {
        return questionService.getScore(responses);
    }
}
