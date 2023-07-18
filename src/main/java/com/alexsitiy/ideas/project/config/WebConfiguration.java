package com.alexsitiy.ideas.project.config;

import com.alexsitiy.ideas.project.dto.ProjectSort;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ProjectSortMethodArgumentResolver());
    }

    static class ProjectSortMethodArgumentResolver implements HandlerMethodArgumentResolver {
        private static final Integer DEFAULT_PAGE = 0;
        private static final Integer DEFAULT_SIZE = 20;
        private static final String DEFAULT_SORT = "likes";

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameter().getType() == ProjectSort.class;
        }

        @Override
        public Object resolveArgument(MethodParameter parameter,
                                      ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest,
                                      WebDataBinderFactory binderFactory) throws Exception {
            String maybePage = webRequest.getParameter("page");
            String maybeSize = webRequest.getParameter("size");
            String mayBeSort = webRequest.getParameter("sort");

            int page = maybePage == null || maybePage.isBlank() ? DEFAULT_PAGE : Integer.parseInt(maybePage);
            int size = maybeSize == null || maybeSize.isBlank() ? DEFAULT_SIZE : Integer.parseInt(maybeSize);
            List<String> sortList = mayBeSort == null ? List.of(DEFAULT_SORT) : Arrays.asList(mayBeSort.split(","));

            return new ProjectSort(page, size, sortList);
        }
    }

}
