package mypack;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

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