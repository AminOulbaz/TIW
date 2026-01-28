package api;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.ExamResultsGsonFactory;
import model.ExamStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/examStatus")
public class GetExamStatus extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> examStatusLabels = new ArrayList<>();
        ExamStatus.getExamsStatus().forEach(e -> examStatusLabels.add(e.getLabel()));
        String json = new Gson().toJson(examStatusLabels);
        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }
}
