package view.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.DecimalFormat;

public class PriceFormat extends TagSupport {

    private static final Logger logger = LogManager.getLogger(PriceFormat.class);
    private static final DecimalFormat numberFormat = new DecimalFormat("#.00");
    private long price;

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            double priceDouble = (double) price/100;
            out.write(numberFormat.format(priceDouble));
        } catch (IOException e) {
            logger.error("Could not parse price.", e);
        }
        return SKIP_BODY;
    }

}
