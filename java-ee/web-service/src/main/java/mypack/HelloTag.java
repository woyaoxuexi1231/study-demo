package mypack;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspTagException;
import jakarta.servlet.jsp.tagext.TagSupport;

public class HelloTag extends TagSupport {

    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print("Hello");
        } catch (Exception e) {
            throw new JspTagException(e.getMessage());
        }
        return EVAL_PAGE;
    }
}