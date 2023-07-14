package com.alexsitiy.ideas.project.config;

import com.alexsitiy.ideas.project.dto.SortRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SortRequestMethodArgumentResolver());
    }


    static class SortRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameter().getType() == SortRequest.class;
        }

        @Override
        public Object resolveArgument(MethodParameter parameter,
                                      ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest,
                                      WebDataBinderFactory binderFactory) throws Exception {
            String maybePage = webRequest.getParameter("page");
            String maybeSize = webRequest.getParameter("size");
            String[] mayBeSortList = webRequest.getParameterValues("sort");

            int page = maybePage == null ? 0 : Integer.parseInt(maybePage);
            int size = maybeSize == null ? 20 : Integer.parseInt(maybeSize);
            List<String> sortList = mayBeSortList == null ? List.of("likes") : Arrays.asList(mayBeSortList);

            return new SortRequest(page, size, sortList);
        }
    }

}
