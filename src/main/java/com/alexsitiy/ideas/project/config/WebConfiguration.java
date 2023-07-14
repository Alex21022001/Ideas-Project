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
        private static final Integer DEFAULT_PAGE = 0;
        private static final Integer DEFAULT_SIZE = 20;
        private static final String DEFAULT_SORT = "likes";

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

            int page = maybePage == null ? DEFAULT_PAGE : Integer.parseInt(maybePage);
            int size = maybeSize == null ? DEFAULT_SIZE : Integer.parseInt(maybeSize);
            List<String> sortList = mayBeSortList == null ? List.of(DEFAULT_SORT) : Arrays.asList(mayBeSortList);

            return new SortRequest(page, size, sortList);
        }
    }

}
