package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

@WebServlet("/check")
public class CheckServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String txtStr = request.getParameter("txtStr");
        String option = request.getParameter("ddlist");

        String result = "";

        if ("opPass".equals(option)) {

            if (txtStr == null || txtStr.trim().isEmpty()) {
                result = "Password is invalid!";
            } else {

                boolean hasLetter =
                        Pattern.compile("[a-zA-Z]").matcher(txtStr).find();

                boolean hasDigit =
                        Pattern.compile("\\d").matcher(txtStr).find();

                boolean hasUpper =
                        Pattern.compile("[A-Z]").matcher(txtStr).find();

                boolean hasSpecial =
                        Pattern.compile("[!@#$%^&*]")
                                .matcher(txtStr).find();

                if (txtStr.length() < 6) {
                    result = "Weak";
                } else if (txtStr.length() <= 10
                        && hasLetter && hasDigit) {
                    result = "Medium";
                } else if (txtStr.length() > 10
                        && hasUpper
                        && hasDigit
                        && hasSpecial) {
                    result = "Strong";
                } else {
                    result = "Weak";
                }
            }

        } else if ("opCount".equals(option)) {

            String text = txtStr.trim();

            int words = text.isEmpty()
                    ? 0
                    : text.split("\\s+").length;

            int sentences = 0;

            for (char c : text.toCharArray()) {
                if (c == '.' || c == '!' || c == '?') {
                    sentences++;
                }
            }

            result = words + " " + sentences;

        } else if ("opFill".equals(option)) {

            String[] badWords = {
                "badword1",
                "badword2",
                "badword3"
            };

            result = txtStr;

            for (String w : badWords) {
                result = result.replaceAll("(?i)" + w, "***");
            }
        }

        try (PrintWriter out = response.getWriter()) {

            out.println("<html><body>");
            out.println("<div id='txtOutput'>");
            out.println(result);
            out.println("</div>");
            out.println("</body></html>");
        }
    }
}