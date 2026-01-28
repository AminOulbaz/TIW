package web;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import service.ProfessorService;
import service.ProfessorServiceFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/verbalize")
public class VerbalizeExamSessionController extends HttpServlet {
    ProfessorService professorService;
    @Override
    public void init() throws ServletException {
        try {
            professorService = ProfessorServiceFactory.getProfessorService(getServletContext());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int examSessionId = Integer.parseInt(req.getParameter("examSessionId"));
        List<ExamResultWithStudent> examResults = (List<ExamResultWithStudent>) req.getSession().getAttribute("infos");
        for(ExamResultWithStudent examResult : examResults) {
            if(examResult.getExamResult().getStatus().equals(ExamStatus.REFUSED))
                examResult.getExamResult().setGrade(ExamGrade.DEFERRED);
            if(examResult.getExamResult().getStatus().equals(ExamStatus.PUBLISHED) ||
                    examResult.getExamResult().getStatus().equals(ExamStatus.REFUSED))
                examResult.getExamResult().setStatus(ExamStatus.VERBALIZED);
            professorService.updateExamResult(examResult.getExamResult());
        }

        Verbal verbal = professorService.makeVerbal(
                professorService.getProfessorByUsername(user.getUsername()),
                examSessionId
        );
        System.out.println("Debug examVerbalId: "+verbal.getExamVerbalId());
        if(professorService.getAllVerbalsByExamSessionId(examSessionId).isEmpty())
            professorService.createVerbal(verbal);
        else
            professorService.updateVerbal(verbal.getCreationTimestamp(),verbal.getProfessorId(),examSessionId);
        req.setAttribute("verbal", verbal);
        req.getRequestDispatcher("/WEB-INF/jsp/professor/verbale.jsp")
                .forward(req, resp);
    }
}
