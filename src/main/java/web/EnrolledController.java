package web;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExamResultWithStudent;
import model.ExamStatus;
import service.ProfessorService;
import service.ProfessorServiceFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/enrolled")
public class EnrolledController extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int examSessionId = Integer.parseInt(req.getParameter("examSessionId"));
        req.setAttribute("examSession", professorService.getExamSessionByExamSessionId(examSessionId));
        List<ExamResultWithStudent> examResultWithStudents =
                req.getParameter("sort") == null ?
                professorService.getExamResultWithStudentsByExamSessionId(examSessionId) :
                        professorService.getExamResultWithStudentsByExamSessionId(examSessionId
                                , req.getParameter("sort")
                                , req.getParameter("ord") == null ?
                                    "ASC" : req.getParameter("ord"));
        req.setAttribute("infos", examResultWithStudents);
        req.setAttribute("hasInserted", examResultWithStudents
                .stream().anyMatch(e -> e.getExamResult().getStatus().equals(ExamStatus.INSERTED))
        );
        req.setAttribute("hasVerbalizable", examResultWithStudents
                .stream().anyMatch(e -> e.getExamResult().getStatus().equals(ExamStatus.PUBLISHED) ||
                        e.getExamResult().getStatus().equals(ExamStatus.REFUSED))
        );
        if(req.getParameter("ord") != null) {
            req.setAttribute("ord", req.getParameter("ord"));
        }
        req.getSession().setAttribute("infos", examResultWithStudents);
        req.getRequestDispatcher("/WEB-INF/jsp/professor/iscritti.jsp")
                .forward(req, resp);
    }
}
