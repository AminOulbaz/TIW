package api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.ExamResultsGsonFactory;
import model.ExamStatus;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/examStatus")
public class GetExamStatus extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExamStatus> examStatus = ExamStatus.getExamsStatus();
        String json = ExamResultsGsonFactory.getGson().toJson(examStatus);
        resp.setContentType("application/json");
        resp.getWriter().println(json);
    }
}
