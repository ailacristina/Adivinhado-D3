package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class tentarAvancado extends HttpServlet {

    public static final String ID_NUMERO_SECRETO = "numero_secreto";
    public static final String ID_TENTATIVA = "tentativa";
    public static final String ID_NUM_TENTATIVAS = "num_tentativas";
    public static final String ID_MSG_ERRO = "msg_erro";

    private int obterNumeroSecreto(HttpServletRequest request) {
        HttpSession sessao = request.getSession();
        if (sessao.getAttribute(ID_NUMERO_SECRETO) == null) {
            sessao.setAttribute(ID_NUMERO_SECRETO, "" + (int) (Math.random() * 100));
        }
        return Integer.parseInt((String) sessao.getAttribute(ID_NUMERO_SECRETO));
    }

    private void incrementarNumeroDeTentativas(HttpServletRequest request) {
        HttpSession sessao = request.getSession();
        if (sessao.getAttribute(ID_NUM_TENTATIVAS) == null) {
            sessao.setAttribute(ID_NUM_TENTATIVAS, "1");
        } else {
            int numTentativas = Integer.parseInt((String) sessao.getAttribute(ID_NUM_TENTATIVAS));
            numTentativas++;
            sessao.setAttribute(ID_NUM_TENTATIVAS, "" + numTentativas);
        }
    }

    private void alterarMensagemDeErro(HttpServletRequest request, String msg) {
        HttpSession sessao = request.getSession();
        sessao.setAttribute(ID_MSG_ERRO, msg);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        incrementarNumeroDeTentativas(request);
        int tentativa = Integer.parseInt(request.getParameter(ID_TENTATIVA));
        int numeroSecreto = obterNumeroSecreto(request);
        
        int maisdez = numeroSecreto + 10;
        int menosdez = numeroSecreto - 10;
        
        if (tentativa != numeroSecreto) {
            if ((tentativa > numeroSecreto - 5) && (tentativa < numeroSecreto + 5)) {
                alterarMensagemDeErro(request, "Você está quente...");
            } 
            else{
                if ((tentativa < numeroSecreto +10)&&(tentativa > numeroSecreto - 10)) {
                    alterarMensagemDeErro(request, "Você está morno...");
                } 
                else{
                        alterarMensagemDeErro(request, "Você está frio...");
                }                          
            }                      
            response.sendRedirect("errouAvancado.jsp");
        } else {
            response.sendRedirect("acertouAvancado.jsp");
        }
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
