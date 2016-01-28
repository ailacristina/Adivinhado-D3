package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TentarAvancado extends HttpServlet {

    public static final String ID_NUMERO_SECRETO = "numero_secreto";
    public static final String ID_TENTATIVA = "tentativa";
    public static final String ID_NUM_TENTATIVAS = "num_tentativas";
    public static final String ID_MSG_ERRO = "msg_erro";

    private int obterNumeroSecreto(HttpServletRequest request, HttpServletResponse response) {
        HttpSession sessao = request.getSession();
        if (sessao.getAttribute(ID_NUMERO_SECRETO) == null) {
            sessao.setAttribute(ID_NUMERO_SECRETO, "" + (int) (Math.random() * 100));
        }
        return Integer.parseInt((String) sessao.getAttribute(ID_NUMERO_SECRETO));
    }

    private int incrementarNumeroDeTentativas(HttpServletRequest request, HttpServletResponse response) {
        HttpSession sessao = request.getSession();
        if (sessao.getAttribute(ID_NUM_TENTATIVAS) == null) {
            sessao.setAttribute(ID_NUM_TENTATIVAS, "1");
            return 1;
        }
        int numTentativas = Integer.parseInt((String) sessao.getAttribute(ID_NUM_TENTATIVAS));
        numTentativas++;
        sessao.setAttribute(ID_NUM_TENTATIVAS, "" + numTentativas);
        return numTentativas;
    }

    private void alterarMensagemDeErro(HttpServletRequest request, HttpServletResponse response, String msg) {
        HttpSession sessao = request.getSession();
        sessao.setAttribute(ID_MSG_ERRO, msg);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        incrementarNumeroDeTentativas(request, response);
        int tentativa = Integer.parseInt(request.getParameter(ID_TENTATIVA));
        int numeroSecreto = obterNumeroSecreto(request, response);
        RequestDispatcher rd;

        if (tentativa != numeroSecreto) {
            if (Math.abs(tentativa - numeroSecreto) >= 50) {
                alterarMensagemDeErro(request, response, "Você está FRIO...");
            } else if (Math.abs(tentativa - numeroSecreto) >= 15) {
                alterarMensagemDeErro(request, response, "Você está MORNO...");
            } else if (Math.abs(tentativa - numeroSecreto) >= 5) {
                alterarMensagemDeErro(request, response, "Você está QUENTE...");
            } else {
                alterarMensagemDeErro(request, response, "Você está MUITO QUENTE...");
            }
            rd = request.getRequestDispatcher("errou.jsp");
        } else {
            rd = request.getRequestDispatcher("acertou.jsp");
        }
        rd.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
