package krasa;

import ro.isdc.wro.model.resource.processor.support.JSMin;

import java.io.UnsupportedEncodingException;

public class Shading {
    public static void main(String[] args) throws UnsupportedEncodingException {
        org.springframework.web.util.UriUtils.encode("ffoo", "UTF-8");
        Class<JSMin> jsMinClass = JSMin.class;
    }
}
