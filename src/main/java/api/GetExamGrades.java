package api;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.ExamResultsGsonFactory;
import model.ExamGrade;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/examGrades")
public class GetExamGrades extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExamGrade> examGrades = ExamGrade.getExamGrades();
        String json = ExamResultsGsonFactory.getGson().toJson(examGrades);
        resp.setContentType("application/json");
        resp.getWriter().println(json);
    }
}
