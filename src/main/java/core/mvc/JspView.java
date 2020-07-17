package core.mvc;

import core.mvc.exception.ViewRenderException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

@Slf4j
public class JspView implements View {
    private static final Logger logger = LoggerFactory.getLogger(JspView.class);
    public static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private String viewName;

    public JspView(String viewName) {
        if (viewName == null) {
            throw new NullPointerException("viewName is null. 이동할 URL을 입력하세요.");
        }
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws ViewRenderException {
        logger.debug("ViewName : {}", viewName);

        try {
            if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
                response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
                return;
            }

            Set<String> keys = model.keySet();
            for (String key : keys) {
                logger.debug("attribute name : {}, value : {}", key, model.get(key));
                request.setAttribute(key, model.get(key));
            }

            RequestDispatcher rd = request.getRequestDispatcher(viewName);
            rd.forward(request, response);
        }
        catch (Exception e) {
            throw new ViewRenderException(e, viewName);
        }
    }
}
