package gtu.lineapp.ex1;

import org.apache.commons.lang.Validate;

/**
 * https://notify-bot.line.me/my/
 * https://notify-bot.line.me/my/services/
 * 
 * https://poychang.github.io/line-notify-1-basic/
 * 
 * @author gtu001
 *
 */
public class LineAppNotifiyServiceApplyer {

    final static int MAX_SIZE = Integer.MAX_VALUE;

    private static final String URL = "https://notify-bot.line.me/oauth/authorize";

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";

    public static void main(String[] args) {
        __LineAuthBean bean = new __LineAuthBean();

        bean.setClient_id("sr4HhZhAtaYUkSemExzn1V");
        bean.setRedirect_uri("http://localhost:8080");
        bean.setState("1");

        String result = LineAppNotifiyServiceApplyer.newInstance().lineAuthBean(bean).getURL();

        System.out.println(result);
        System.out.println("done...v2");
    }

    private __LineAuthBean lineAuthBean;

    public LineAppNotifiyServiceApplyer lineAuthBean(__LineAuthBean lineAuthBean) {
        this.lineAuthBean = lineAuthBean;
        return this;
    }

    public static class __LineAuthBean {
        private String response_type = "code";
        private String client_id;
        private String redirect_uri;
        private String scope = "notify";
        private String state = "1";
        private String response_mode = "GET";

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public String getRedirect_uri() {
            return redirect_uri;
        }

        public void setRedirect_uri(String redirect_uri) {
            this.redirect_uri = redirect_uri;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getResponse_mode() {
            return response_mode;
        }

        public void setResponse_mode(String response_mode) {
            this.response_mode = response_mode;
        }
    }

    public String getURL() {
        try {
            Validate.notNull(lineAuthBean, "必須輸入 lineAuthBean");

            String postForm = BeanToHttpForm.newInstance(lineAuthBean).getParameterString();
            System.out.println("#postForm = " + postForm);

            String finalUrl = URL + "?" + postForm;
            System.out.println(finalUrl);
            return finalUrl;
        } catch (Exception ex) {
            throw new RuntimeException("send ERR : " + ex.getMessage(), ex);
        }
    }

    public static LineAppNotifiyServiceApplyer newInstance() {
        return new LineAppNotifiyServiceApplyer();
    }
}
