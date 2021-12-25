package br.edu.ifpb.pweb2.boletimDigital.filter;

import br.edu.ifpb.pweb2.boletimDigital.model.Admin;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class LoginFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpRequest.getSession(false);

        String uri = httpRequest.getRequestURI();

        if (httpSession != null) {
            Admin loginUser = (Admin) httpSession.getAttribute("admin");
            if (loginUser == null) {
                redirectLogin(httpRequest, httpResponse, uri);
                return;
            } else {
                chain.doFilter(request, response);
            }
        } else {
            redirectLogin(httpRequest, httpResponse, uri);
        }

    }

    private void redirectLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String uri) throws IOException {
        String baseUrl = httpRequest.getContextPath();
        String paginaLogin = baseUrl + "/login";
        httpResponse.sendRedirect(httpResponse.encodeRedirectURL(paginaLogin));
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}

